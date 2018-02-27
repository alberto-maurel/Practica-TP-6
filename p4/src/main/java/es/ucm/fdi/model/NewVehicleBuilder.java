package es.ucm.fdi.model;

import es.ucm.fdi.ini.IniSection;

public class NewVehicleBuilder extends EventBuilder{
	public Event parse(IniSection sec) {
		if (!sec.getTag().equals("new_vehicle")) return null;
		
		//Creamos el itinerario
		
		
		
		return new NewVehicle(Integer.parseInt(sec.getValue("time")), sec.getValue("id"), sec.getValue("max_value"), itinerario);
	}
}
