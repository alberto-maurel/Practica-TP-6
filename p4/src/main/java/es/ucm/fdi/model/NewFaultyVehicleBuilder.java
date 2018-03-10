package es.ucm.fdi.model;

import java.util.ArrayList;
import java.util.Arrays;

import es.ucm.fdi.ini.IniSection;

public class NewFaultyVehicleBuilder implements EventBuilder {
	
	public Event parse(IniSection sec) {
		if (!sec.getTag().equals("make_vehicle_faulty")) return null;
		String[] faultyVehicles = sec.getValue("vehicles").split("[ ,]");
		ArrayList<String> vehicles = new ArrayList<>(Arrays.asList(faultyVehicles));
		return new NewFaultyVehicle(Integer.parseInt(sec.getValue("time")), "", 
				Integer.parseInt(sec.getValue("duration")), vehicles);
	}
}
