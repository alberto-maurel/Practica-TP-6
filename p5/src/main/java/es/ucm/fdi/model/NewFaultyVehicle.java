package es.ucm.fdi.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import es.ucm.fdi.ini.IniSection;

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
	
	
	public static class Builder implements EventBuilder {
		public Event parse(IniSection sec) {
			if (!sec.getTag().equals("make_vehicle_faulty")) return null;
			String[] faultyVehicles = sec.getValue("vehicles").split("[ ,]");
			ArrayList<String> vehicles = new ArrayList<>(Arrays.asList(faultyVehicles));
			return new NewFaultyVehicle(Integer.parseInt(sec.getValue("time")), "", 
					Integer.parseInt(sec.getValue("duration")), vehicles);
		}
	}
	
	
	public void execute(RoadMap roadMap) {
		for (String s: idVehiclesInvolved) {
			//Para cada vehículo que exista, ponemos su tiempo de avería al tiempo indicado.
			if (roadMap.getConstantSimObjects().get(s) != null) {
				//En primer lugar encontramos el coche averiado dentro del arrayList
				int cont = 0;
				for(Vehicle v: roadMap.getVehicles()) {
					if(s.equals(v.identificador)) {
						roadMap.getVehicles().get(cont).setTiempoAveria(duration);
						break;
					}
					++cont;
				}
			}
		}
	}
	
	public void describe(Map<String,String> out) {
		StringBuilder sb = new StringBuilder();
		for (String s: idVehiclesInvolved) sb.append(s + ",");
		sb.delete(sb.length() - 1, sb.length());
		out.put("#", sb.toString());
		out.put("Time", "" + time);
		out.put("Type", "New Faulty Vehicle");
	}	
	
}
