package es.ucm.fdi.model;

import java.util.ArrayList;

public class NewCar extends NewVehicle {
	
	int resistance;
	double fault_prob;
	int max_fault_duration;
	long seed;
	
	public NewCar(int time, String id, int max_speed, ArrayList<String> itinerario, int r, double f_p, int mfd, long s) {
		super(time, id, max_speed, itinerario);
		resistance = r;
		fault_prob = f_p;
		max_fault_duration = mfd;
		seed = s;
	}

}
