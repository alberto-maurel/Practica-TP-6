package es.ucm.fdi.model;

import java.util.ArrayList;

public class NewFaultyVehicle extends Event {
	int duration;
	ArrayList<String> idVehiclesInvolved;
	
	
	public NewFaultyVehicle() {
		super();
	}
	
	public NewFaultyVehicle(int time, String id, int dur, ArrayList<String> vehicles) {
		super(time, id);
		this.duration = dur;
		this.idVehiclesInvolved = vehicles;
	}
	
	
	public void execute(RoadMap roadMap) {
		for (String s: idVehiclesInvolved) {
			//Para cada vehículo que exista, ponemos su tiempo de avería al tiempo indicado.
			if (roadMap.simObjects.get(s) != null) {
				roadMap.vehicles.get(roadMap.vehicles.indexOf(roadMap.simObjects.get(s))).setTiempoAveria(duration);
			}
		}
	}
	
	
}
