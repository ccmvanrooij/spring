package eu.spring;

import java.io.Serializable;

/**
 * Keep track of necessary SPRING structures
 * In addition, continuously update certain values for SPRING's report action
 * 		Minimum
 * 		Maximum
 * 		Total energy (sum of all consumed energy over time)
 * @author Christian van Rooij
 */
public class SpringObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final String startTime;
	private final long startPosition;	
	private double dtwDistance;
	private final double maxPower;
	private final double minPower;
	private final double totalEnergy; 

	public SpringObject(double dtwDistance, long startPosition, String timeStamp) {
		this.startPosition = startPosition;
		this.dtwDistance = dtwDistance;	
		this.startTime = timeStamp;
		this.maxPower = Double.MIN_VALUE;
		this.minPower = Double.MAX_VALUE;
		this.totalEnergy = 0;
	}
	
	public SpringObject(double absoluteDistance, double input, SpringObject minimum) {
		this.dtwDistance = absoluteDistance + minimum.getDtwDistance();
		this.startPosition = minimum.getStartPosition();
		this.startTime = minimum.getStartTime();
		this.maxPower = Math.max(minimum.getMaxPower(), input);
		this.minPower = Math.min(minimum.getMinPower(), input);
		this.totalEnergy = minimum.getTotalEnergy() + input;
	}

	public void setDtwDistance(double dtw) {
		this.dtwDistance = dtw;
	}
	
	public double getDtwDistance() {
		return dtwDistance;
	}
	
	public long getStartPosition() {
		return startPosition;
	}
	
	public String getStartTime() {
		return startTime;
	}

	public double getMaxPower() {
		return maxPower;
	}

	public double getMinPower() {
		return minPower;
	}

	public double getTotalEnergy() {
		return totalEnergy;
	}
	
    @Override
	public String toString() {
		return "SPRINGObject [startTime=" + startTime + ", startPosition=" + startPosition + 
				", dtwDistance=" + dtwDistance + ", maxPower=" + maxPower + ", minPower=" + minPower + 
				", totalEnergy=" + totalEnergy + "]";
	}
}
