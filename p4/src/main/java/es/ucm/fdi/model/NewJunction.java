package es.ucm.fdi.model;

import es.ucm.fdi.ini.IniSection;

public class NewJunction extends Event{
	
	/**
	 * Constructor
	 * @param time momento en el que ocurre el evento
	 * @param id id del evento
	 */
	public NewJunction(int time, String id) {
		super(time, id);
	}
	
	public static class Builder implements EventBuilder{
		public Event parse(IniSection sec) {
			if (!sec.getTag().equals("new_junction")) {
				return null;
			} else if (sec.getValue("type") == null) {
				return new NewJunction(Integer.parseInt(sec.getValue("time")), sec.getValue("id"));
			} else if (sec.getValue("type").equals("rr")) {
				return new NewRoundRobin(Integer.parseInt(sec.getValue("time")), sec.getValue("id"),
						Integer.parseInt(sec.getValue("max_time_slice")), Integer.parseInt(sec.getValue("min_time_slice")));
			} else if (sec.getValue("type").equals("mc")) {
				return new NewMostCrowded(Integer.parseInt(sec.getValue("time")), sec.getValue("id"));
			} else {
				return null;
			}		
		}
	}
	
	public void execute(RoadMap roadMap) {
		//Comprobamos que la intersección no exista previamente
		if(roadMap.getConstantSimObjects().get(id) == null) {
			
			Junction jActual = new Junction(id);
			//Y en caso de no existir la añadimos
			roadMap.getSimObjects().put(id, jActual);
			roadMap.getJunctions().add(jActual);
		}
	}
}
