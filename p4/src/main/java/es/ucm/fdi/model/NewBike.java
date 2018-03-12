package es.ucm.fdi.model;

import java.util.ArrayList;
import java.util.Arrays;

import es.ucm.fdi.ini.IniSection;

public class NewBike extends NewVehicle {
	
	public NewBike(int time, String id, int max_speed, ArrayList<String> itinerario) {
		super(time, id, max_speed, itinerario);
	}
	
	public static class Builder implements EventBuilder{
		public Event parse(IniSection sec) throws SimulationException {
			if (!sec.getTag().equals("new_vehicle")) {
				return null;
			}
			if ("bike".equals(sec.getValue("type"))) {
				if(parseInt(sec, "time", 0) && parseIdList(sec, "id") && 
						isValidId(sec.getValue("id")) && parseInt(sec, "max_speed", 0)){
					
					//Creamos el itinerario
					String[] itinerarioString = sec.getValue("itinerary").split("[ ,]");
					ArrayList<String> itinerario = new ArrayList<>(Arrays.asList(itinerarioString));
					
					for(String juntName: itinerario){
						if(!isValidId(juntName)) throw new SimulationException("El nombre de una junction del itinerario es incorrecto");
					}
				
					return new NewBike(Integer.parseInt(sec.getValue("time")), sec.getValue("id"),
						Integer.parseInt(sec.getValue("max_speed")), itinerario);
				
				}
				throw new SimulationException("Algún parámetro no existe o es inválido");
			} 
			return null;
		}
	}
	
}
