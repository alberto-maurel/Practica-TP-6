package es.ucm.fdi.model;

import es.ucm.fdi.ini.IniSection;

public class NewJunction extends Event {
	
	/**
	 * Constructor
	 * @param time momento en el que ocurre el evento
	 * @param id id del evento
	 */
	public NewJunction(int time, String id) {
		super(time, id);
	}
	
	
	public static class Builder implements EventBuilder {
		
		public Event parse(IniSection sec) throws SimulationException {
			if (!sec.getTag().equals("new_junction")) {
				return null;
			}
			if (sec.getValue("type") == null) {
				if (parseInt(sec, "time", 0) && parseIdList(sec, "id") && isValidId(sec.getValue("id"))) {
					return new NewJunction(Integer.parseInt(sec.getValue("time")), sec.getValue("id"));
				} 
				throw new SimulationException("Algún parámetro no existe o es inválido");
			}
			return null;
		}
		
	}		

	public void execute(RoadMap roadMap) throws SimulationException {
		//Comprobamos que la intersección no exista previamente
		if(roadMap.getConstantSimObjects().get(id) == null) {
			Junction jActual = new Junction(id);
			//Y en caso de no existir la añadimos
			roadMap.getSimObjects().put(id, jActual);
			roadMap.getJunctions().add(jActual);
		} else {
			throw new SimulationException("Ya existe un cruce con el mismo identificador");
		}
	}
	
	
}
