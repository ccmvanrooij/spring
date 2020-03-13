package eu.spring;

import java.util.ArrayList;

/**
 * Test program for checking the output of the SPRING algorithm.
 * 
 * @author Christian van Rooij
 */
public class TestProgram {
	
	public static void main(String[] args) {
    	
		System.out.println("Starting SPRING algorithm test");

		ArrayList<ObservedState> expectedResults = new ArrayList<ObservedState>();
		expectedResults.add(new ObservedState("2", 2, 5, 7, 4.0, 12.0, 6.0, 34.0));
		
    	double[] incomingData = readIncomingData();
    	Profile ospv = new Profile(1, new double[] {-1, 11, 6, 9, 4});
    	ArrayList<ObservedState> results = new ArrayList<ObservedState>();
    	
        SpringAlgorithm SPRING = new SpringAlgorithm(ospv, "", 10, false);
        SPRING.reportSPRINGInitialisation();
        
        for(int i=0; i<incomingData.length; i++) {
        	ObservedState result = SPRING.executeCycle(incomingData[i], i + 1 + "");
        	if(result != null)
        		results.add(result);
        }
        
        if(results.size() == 1 && results.get(0).isEqual(expectedResults.get(0)))
        	System.out.println("Test successful");
        else
        	System.out.println("Test failed, resulting states do not match");
    }

	private static double[] readIncomingData() {
    	return new double[] {5, 12, 6, 10, 6, 5, 13};
	}
}
