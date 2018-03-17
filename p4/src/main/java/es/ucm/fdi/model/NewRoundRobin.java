package es.ucm.fdi.model;

import es.ucm.fdi.ini.IniSection;

public class NewRoundRobin extends NewJunction {
	
	protected int max_time_slice;
	protected int min_time_slice;
	
	
	public NewRoundRobin(int time, String id, int max, int min) {
		super(time, id);
		this.max_time_slice = max;
		this.min_time_slice = min;
	}
	
	
	public static class Builder implements EventBuilder {
		
		public Event parse(IniSection sec) throws SimulationException {
			if (!sec.getTag().equals("new_junction")) {
				return null;
			}
			if ("rr".equals(sec.getValue("type"))) {
				if (parseInt(sec, "time", 0) && parseIdList(sec, "id") && isValidId(sec.getValue("id")) &&
						parseInt(sec, "max_time_slice", 0) && parseInt(sec, "min_time_slice", 0)) {
					return new NewRoundRobin(Integer.parseInt(sec.getValue("time")), sec.getValue("id"),
							Integer.parseInt(sec.getValue("max_time_slice")), Integer.parseInt(sec.getValue("min_time_slice")));
				} else {
					throw new SimulationException("Algún parámetro no existe o es inválido");
				}
			}
			return null;
		}
    
	}	
	
	public void execute(RoadMap roadMap) throws SimulationException{
		//Comprobamos que la intersección no exista previamente
		if(roadMap.getConstantSimObjects().get(id) == null) {
			RoundRobin jActual = new RoundRobin(id, max_time_slice, min_time_slice);
			//Y en caso de no existir la añadimos
			roadMap.getSimObjects().put(id, jActual);
			roadMap.getJunctions().add(jActual);
		} else {
			throw new SimulationException("Ya existe un cruce con el mismo identificador");
		}
	}

	
}
