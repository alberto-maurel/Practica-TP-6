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
		 
		
		 		return new NewVehicle(Integer.parseInt(sec.getValue("time")), sec.getValue("id"),
				Integer.parseInt(sec.getValue("max_value")), itinerario);
		}
		
		
	}
}
