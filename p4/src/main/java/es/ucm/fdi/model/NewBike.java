package es.ucm.fdi.model;

import java.util.ArrayList;
import es.ucm.fdi.ini.IniSection;

public class NewBike extends NewVehicle {
	
	public NewBike(int time, String id, int max_speed, ArrayList<String> itinerario) {
		super(time, id, max_speed, itinerario);
	}
	
	
	public static class Builder implements EventBuilder {
		
		public Event parse(IniSection sec) throws SimulationException {
			if (!sec.getTag().equals("new_vehicle")) {
				return null;
			}
			if ("bike".equals(sec.getValue("type"))) {
				if(parseInt(sec, "time", 0) && parseIdList(sec, "id") && 
						isValidId(sec.getValue("id")) && parseInt(sec, "max_speed", 0)) {
					
					//Creamos el itinerario
					ArrayList<String> itinerario = parsearItinerario(sec);
					
					return new NewBike(Integer.parseInt(sec.getValue("time")), sec.getValue("id"),
						Integer.parseInt(sec.getValue("max_speed")), itinerario);
				
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
			Bike nuevoVehiculo = new Bike(id, max_speed, itinerarioVehiculoJunctions);
			
			//Y lo insertamos en el roadMap
			roadMap.getVehicles().add(nuevoVehiculo);
			roadMap.getSimObjects().put(id, nuevoVehiculo);
		} else {
			throw new SimulationException("Identificador de objeto duplicado");
		}
	}
	
	
}
