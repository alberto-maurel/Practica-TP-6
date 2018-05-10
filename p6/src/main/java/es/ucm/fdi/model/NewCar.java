package es.ucm.fdi.model;

import java.util.ArrayList;
import java.util.Map;

import es.ucm.fdi.ini.IniSection;

public class NewCar extends NewVehicle {
	
	int resistance;
	double fault_prob;
	int max_fault_duration;
	long seed;
	
	
	public NewCar(int time, String id, int max_speed, ArrayList<String> itinerario, 
			int r, double f_p, int mfd, long s) {
		super(time, id, max_speed, itinerario);
		resistance = r;
		fault_prob = f_p;
		max_fault_duration = mfd;
		seed = s;
	}
	
	
	public static class Builder implements EventBuilder {
		
		public Event parse(IniSection sec) throws SimulationException {
			if (!sec.getTag().equals("new_vehicle")) {
				return null;
			}			
			if ("car".equals(sec.getValue("type"))) {
				if(parseInt(sec, "time", 0) && parseIdList(sec, "id") && 
						isValidId(sec.getValue("id")) && parseInt(sec, "max_speed", 0) &&
					parseInt(sec, "resistance", 0) && parseDouble(sec, "fault_probability", 0) &&
						parseInt(sec, "max_fault_duration", 0) && parseLong(sec, "seed", 0)) {
					
					//Creamos el itinerario
					ArrayList<String> itinerario = parsearItinerario(sec);
				
					return new NewCar(Integer.parseInt(sec.getValue("time")), 
							sec.getValue("id"), Integer.parseInt(sec.getValue("max_speed")), itinerario, 
						Integer.parseInt(sec.getValue("resistance")), 
							Double.parseDouble(sec.getValue("fault_probability")), 
						Integer.parseInt(sec.getValue("max_fault_duration")), 
							Long.parseLong(sec.getValue("seed")));
				
				}
				throw new SimulationException("Algún parámetro no existe o es inválido");
			} 
			return null;
		}
	}
	
	
	public void execute(RoadMap roadMap) throws SimulationException {
		//Comprobamos que no existiera previamente el vehículo
		if(roadMap.getConstantSimObjects().get(id) == null) {
			//Creamos el itinerario
			ArrayList<Junction> itinerarioVehiculoJunctions = crearItinerario(roadMap);
			
			//Llamamos al constructor del vehículo
			Vehicle nuevoVehiculo = new Car(id, max_speed, itinerarioVehiculoJunctions, 
					resistance, fault_prob, max_fault_duration, seed);
			
			//Y lo insertamos en el roadMap
			roadMap.getVehicles().add(nuevoVehiculo);
			roadMap.getSimObjects().put(id, nuevoVehiculo);
		} else {
			throw new SimulationException("Identificador de objeto duplicado");
		}
	}
	
	public void describe(Map<String,String> out, String rowIndex) {
		out.put("#", rowIndex);
		out.put("Time", "" + time);
		out.put("Type", "New Car " + id);
	}
}
