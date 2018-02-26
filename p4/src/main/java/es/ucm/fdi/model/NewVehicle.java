package es.ucm.fdi.model;

import java.util.ArrayList;

public class NewVehicle extends Event{
	int max_speed;
	private ArrayList<Road> itinerario;
	
	public NewVehicle(){
		super();
	}
	
	public NewVehicle(int time, String id, int max_speed, ArrayList<Road> itinerario) {
		super(time, id);
		this.max_speed = max_speed;
		this.itinerario = itinerario;
	}
}
