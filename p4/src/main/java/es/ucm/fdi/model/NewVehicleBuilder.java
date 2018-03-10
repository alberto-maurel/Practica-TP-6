package es.ucm.fdi.model;

import java.util.ArrayList;
import java.util.Arrays;

import es.ucm.fdi.ini.IniSection;

public class NewVehicleBuilder implements EventBuilder{
	public Event parse(IniSection sec) {
		if (!sec.getTag().equals("new_vehicle")) {
			return null;
		} else {
			//Creamos el itinerario
			//Creo que viene todo en la misma sección, así que haremos el split a ver si funciona
			String[] itinerarioString = sec.getValue("itinerary").split("[ ,]");
			ArrayList<String> itinerario = new ArrayList<>(Arrays.asList(itinerarioString));
		
			//Y ahora dependiendo del vehículo que tenemoos que crear llamamos a uno u otro
			// No sé qué pasa si no encuentra el campo "type"
			if (sec.getValue("type").equals("car")) {
				return new NewCar(Integer.parseInt(sec.getValue("time")), sec.getValue("id"), Integer.parseInt(sec.getValue("max_speed")), itinerario, 
						Integer.parseInt(sec.getValue("resistance")), Double.parseDouble(sec.getValue("fault_probability")), 
						Integer.parseInt(sec.getValue("max_fault_duration")), Long.parseLong(sec.getValue("seed")));
			} else if (sec.getValue("type").equals("bike")) {
				return new NewBike(Integer.parseInt(sec.getValue("time")), sec.getValue("id"), Integer.parseInt(sec.getValue("max_speed")), itinerario);
			} else {
				return new NewVehicle(Integer.parseInt(sec.getValue("time")), sec.getValue("id"),
						Integer.parseInt(sec.getValue("max_value")), itinerario);
			}
			
		}
		
		
	}
}
