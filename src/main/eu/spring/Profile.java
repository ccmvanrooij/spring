package eu.spring;

import java.io.Serializable;
import java.util.Arrays;

public class Profile implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private final int operatingStateProfileId;
	private final double[] values;
	private final double totalEnergy;
	private final int profileLength;
	
	public Profile(int osp_id, double[] values) {
		
		this.operatingStateProfileId = osp_id;
		this.values = values;
		this.totalEnergy = calculateTotalEnergy();
		this.profileLength = values.length - 1;
	}
	
	private double calculateTotalEnergy() {

		double total = 0;
		for (int i = 1; i < values.length; i++) {
			total += values[i];
		}
		return total;
	}

	public int getOperatingStateProfileId() {
		return operatingStateProfileId;
	}

	/**
	 * values has format {-1, tick1, tick2, tick3, ...} in order to synchronize it with the SPRING memory matrix	 
	 * @return reference profile for a identified operating state pattern
	 */
	public double[] getValues() {
		return values;
	}
	
	public Double getTotalEnergy() {
		return totalEnergy;
	}
	
	/**
	 * profileLength does not depict the length of var values, but of the saved profile in the database
	 * @return length of the identified operating state profile
	 */
	public int getProfileLength() {
		return profileLength;
	}

	@Override
	public String toString() {
		return "Profile [operatingStateProfileId=" + operatingStateProfileId + ", values="
				+ Arrays.toString(values) + ", totalEnergy=" + totalEnergy + ", profileLength=" + profileLength + "]";
	}
}
