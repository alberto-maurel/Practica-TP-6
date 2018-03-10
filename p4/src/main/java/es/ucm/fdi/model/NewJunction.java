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
			if (!sec.getTag().equals("new_junction")) return null;
			return new NewJunction(Integer.parseInt(sec.getValue("time")), sec.getValue("id"));
		}
	}
		
	public void execute(RoadMap roadMap) {
		//Comprobamos que la intersección no exista previamente
		if(roadMap.simObjects.get(id) == null) {
			
			Junction jActual = new Junction(id);
			//Y en caso de no existir la añadimos
			roadMap.simObjects.put(id, jActual);
			roadMap.junctions.add(jActual);
		}
	}
}
