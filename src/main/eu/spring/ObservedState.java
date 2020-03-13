package eu.spring;

import java.io.Serializable;

/**
 * 
 * @author Christian van Rooij
 *
 */
public class ObservedState implements Serializable {
		
	private static final long serialVersionUID = 1L;

	private final String timeStart;
	private final long startPosition;
	private final long endPosition;
	private final long reportingPosition;
	private final double finalDtwDistance;
	private final double powerMax;
	private final double powerMin;
	private final double energy;
	
	public ObservedState(String startTime, long startPosition, long endPosition, long reportingPosition, 
			double dtwDistance, double maxPower, double minPower, double totalEnergy) {
		this.timeStart = startTime;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.reportingPosition = reportingPosition;
		this.finalDtwDistance = dtwDistance;
		this.powerMax = maxPower;
		this.powerMin = minPower;
		this.energy = totalEnergy;
	}

	public long getStartPosition() {
		return startPosition;
	}

	public long getEndPosition() {
		return endPosition;
	}

	public long getReportingPosition() {
		return reportingPosition;
	}

	public String getTimeStart() {
		return timeStart;
	}

	public long getDurationInCycles() {
		return endPosition - startPosition + 1;		
	}
	
	public long getLatencyInCycles() {
		return this.reportingPosition - this.endPosition;
	}
	
	public double getFinalDtwDistance() {
		return finalDtwDistance;
	}

	public double getPowerMax() {
		return powerMax;
	}

	public double getPowerMin() {
		return powerMin;
	}

	public double getEnergy() {
		return energy;
	}

	public boolean isEqual(ObservedState oos) {
		return this.startPosition == oos.getStartPosition()
			&& this.endPosition == oos.getEndPosition()
			&& this.reportingPosition == oos.getReportingPosition() 
			&& this.timeStart.equals(oos.getTimeStart())			
			&& this.finalDtwDistance == oos.getFinalDtwDistance()
			&& this.powerMax == oos.getPowerMax()
			&& this.powerMin == oos.getPowerMin()
			&& this.energy == oos.getEnergy();	
	}
	
	public static String generateCsvTableHeader() {
		return "timeStart,startPosition,endPosition,durationInCycles,reportingPosition,latencyInCycles,"
				+ "finalDtwDistance,powerMax,powerMin,energy";
	}
	
	public String toCsvFormat() {
		return timeStart + "," + startPosition + "," + endPosition + "," + this.getDurationInCycles() + "," +
			    reportingPosition + "," + this.getLatencyInCycles() + "," + finalDtwDistance + "," + 
				powerMax + "," + powerMin + "," + energy;		
	}
	
	@Override
	public String toString() {
		return "ObservedState [" +
				"timeStart=" + timeStart + 
				", startPosition=" + startPosition + 
				", endPosition=" + endPosition + 
				", durationInCycles=" + this.getDurationInCycles() + 
				", reportingPosition=" + reportingPosition +
				", latencyInCycles=" + this.getLatencyInCycles() +
				", finalDtwDistance=" + finalDtwDistance +
				", powerMax=" + powerMax + 
				", powerMin=" + powerMin + 
				", energy=" + energy + "]";
	}
}
