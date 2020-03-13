package eu.spring;

import java.io.Serializable;

/**
 * Implementation of SPRING algorithms, as described in: 
 * 'Stream Monitoring under the TimeWarping Distance', Sakurai et al, 2007
 * 
 * @author Christian van Rooij
 */
public class SpringAlgorithm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final boolean debug;
	private final double sequenceLengthMinimum;	
	private final double sequenceEnergyMinimum;
	private final String outputIdentifier;
	private final Profile profile;
	private final double threshold;
	
	private SpringObject[] memory; 
	private SpringObject[] current; 
	private SpringObject foundState;
	private long foundStateEndPosition;
	private long cycle;
	
	private boolean patternIsPossiblyFound;
	private int countPattern;
	
	public SpringAlgorithm(Profile profile, String outputIdentifier, double threshold, boolean debug) {
		this(profile, outputIdentifier, threshold, debug, 0, 0);
	}
	
	public SpringAlgorithm(Profile osp, String outputIdentifier, double threshold, boolean debug,
			double sequenceLengthMinimum, double sequenceEnergyMinimum) {
		this.profile = osp;
		this.outputIdentifier = outputIdentifier;
		this.threshold = threshold;
		this.debug = debug;
		this.sequenceLengthMinimum = sequenceLengthMinimum;
		this.sequenceEnergyMinimum = sequenceEnergyMinimum;
		
		initialiseSpring();
	}
	
	public void initialiseSpring() {
		this.cycle = 0;
        this.foundState = new SpringObject(Double.MAX_VALUE, Long.MAX_VALUE, Long.toString(Long.MAX_VALUE));
        this.memory = new SpringObject[profile.getProfileLength() + 1];
        this.current = new SpringObject[profile.getProfileLength() + 1];
		
        for (int i = 0; i <= profile.getProfileLength(); i++) {
			memory[i] = new SpringObject(Double.MAX_VALUE, Long.MAX_VALUE, Long.toString(Long.MAX_VALUE));
		}
	}

	public ObservedState executeCycle(double input, String timestamp) {
		cycle++;
    	patternIsPossiblyFound = true;
    	countPattern = 0;
    	current[0] = new SpringObject(0, cycle, timestamp);		
		ObservedState result = null;
		
		if(debug) {log("New cycle. Tick: " + cycle + ". input: " + input);}
		
    	for(int patternIndex = 1; patternIndex <= profile.getProfileLength(); patternIndex++) {    		
    		calculateMatrix(patternIndex, input);
    		if(isOptimalPatternFound(patternIndex)) {
    			result = reportMostOptimalState();
	    		resetMemory();
    		}
    	}
    	updateFoundState();
    	substituteMemory();
    	
    	return result;
	}
	
	/**
     * Update the startingPoints (s(i)) and dtwDistance (d(i)) values
     */
	private void calculateMatrix(int patternIndex, double input) {
		double absoluteDistance = Math.abs((input - profile.getValues()[patternIndex]));
		
		// First look at the previous saved value
		SpringObject minimum = current[patternIndex-1];
		
		// Then look at value of the same patternIndex, but the previous input
		if(minimum.getDtwDistance() > memory[patternIndex].getDtwDistance())
			minimum = memory[patternIndex];

		// Then look at value of the previous saved value for the patternIndex, with the previous input
		if(minimum.getDtwDistance() > memory[patternIndex-1].getDtwDistance()) 
			minimum = memory[patternIndex-1]; 
		
		current[patternIndex] = new SpringObject(absoluteDistance, input, minimum);
		
		if(debug) {log("Updated matrix on position " + patternIndex + ": " + current[patternIndex].toString());}
	}
	
	private boolean isOptimalPatternFound(int patternIndex) {
		if((foundState.getDtwDistance() <= this.threshold) && patternIsPossiblyFound) {
			
			if(current[patternIndex].getDtwDistance() >= foundState.getDtwDistance()
					|| current[patternIndex].getStartPosition() > foundStateEndPosition) {
    			countPattern++;
			}
			else {  
				patternIsPossiblyFound = false;
			}
			
			if(countPattern == (profile.getProfileLength())
				&& (foundStateEndPosition - foundState.getStartPosition() + 1) 
					>= (sequenceLengthMinimum * profile.getProfileLength()) 
				&& (foundState.getTotalEnergy() >= sequenceEnergyMinimum * profile.getTotalEnergy())) {
					return true;
			}
		}
		return false;
	}
	
	private ObservedState reportMostOptimalState() {
		if(debug) {log("Operating state found! dMin: " + foundState.getDtwDistance() + ", start: " 
							+ foundState.getStartPosition() + ", end: " + foundStateEndPosition + System.lineSeparator() 
							+ "	Observed State: " + foundState);}

		return new ObservedState(
				foundState.getStartTime(), foundState.getStartPosition(), foundStateEndPosition, cycle,
				foundState.getDtwDistance(), foundState.getMaxPower(), foundState.getMinPower(), 
				foundState.getTotalEnergy());
	}
	
	/**
     * Reset foundPattern values, remove values of startPositions and distanceValues
     */
    private void resetMemory() {
		 foundState = new SpringObject(Double.MAX_VALUE, Long.MAX_VALUE, Long.toString(Long.MAX_VALUE));
		 
		 for(int i = 1; i <= profile.getProfileLength(); i++) {
			 
			 if(current[i].getStartPosition() <= foundStateEndPosition)
				 current[i].setDtwDistance(Double.MAX_VALUE);
		 }
	}
	
	private void updateFoundState() {
		if(current[profile.getProfileLength()].getDtwDistance() <= this.threshold 
    			&& current[profile.getProfileLength()].getDtwDistance() < foundState.getDtwDistance()) {
    		
    		foundState = current[profile.getProfileLength()];
    		foundStateEndPosition = cycle;
    		
    		if(debug) {log("Updated dMin: " + foundState.getDtwDistance() + ", start: " 
    							+ foundState.getStartPosition() + ", end: " + foundStateEndPosition);}	 
    	}
	}
	
	private void substituteMemory() {
		memory = current.clone();
	}

	public void reportSPRINGInitialisation() {
		log("SPRING algorithm initialised" + System.lineSeparator() +
			"     Operating state profile length: " + profile.getProfileLength() + System.lineSeparator() +
			"     Operating state profile SPRING threshold: " + this.threshold + System.lineSeparator() +
			"     Operating state profile total energy: " + profile.getTotalEnergy() + System.lineSeparator());
	}
	
	private void log(String output) {
		System.out.println(outputIdentifier + "" + output);
	}
}
