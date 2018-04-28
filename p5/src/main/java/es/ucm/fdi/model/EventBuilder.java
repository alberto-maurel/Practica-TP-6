package es.ucm.fdi.model;

import java.util.ArrayList;
import java.util.Arrays;

import es.ucm.fdi.ini.IniSection;

public interface EventBuilder {

	public Event parse(IniSection sec) throws SimulationException;
	
	public default boolean isValidId(String id) {
		return id.matches("[a-zA-Z0-9_]+");
	}
	
	public default boolean parseIdList(IniSection sec, String key) {
		if(sec.getValue(key) == null) {
			return false;
		}
		return true;
	}
	
	public default boolean parseInt(IniSection sec, String key, int def) {
		if (parseIdList(sec, key)) {
			if (Integer.parseInt(sec.getValue(key)) >= def) return true;
		}
		return false;
	}
	
	public default boolean parseLong(IniSection sec, String key, long def) {
		if (parseIdList(sec, key)) {
			if (Long.parseLong(sec.getValue(key)) >= def) return true;
		}
		return false;
	}
	
	public default boolean parseDouble(IniSection sec, String key, double def) {
		if (parseIdList(sec, key)) {
			if (Double.parseDouble(sec.getValue(key)) >= def) return true;
		}
		return false;
	}
	
	
	//Este es un método que se necesita para los tres vehículos. Para evitar ponerlo en los 3, se pone aquí, aunque no sea útil para
	//el buider del resto de los objetos. Quizá sería útil crear otra interfaz llamada evenBuilderVehicles, y que los builders de los 
	//vehículos implementasen ambas
	public default ArrayList<String> parsearItinerario(IniSection sec) throws SimulationException{
		//Creamos el itinerario
		String[] itinerarioString = sec.getValue("itinerary").split("[ ,]");
		ArrayList<String> itinerario = new ArrayList<>(Arrays.asList(itinerarioString));
		
		for(String juntName: itinerario) {
			if(!isValidId(juntName)) {
				throw new SimulationException("El nombre de una junction del itinerario no es válido");
			}
		}
		return itinerario;
	}
}
