package es.ucm.fdi.model;

import es.ucm.fdi.ini.IniSection;

public class NewMostCrowded extends NewJunction {
	 
	public NewMostCrowded(int t, String id) {
		super(t, id);
	}
	
	public static class Builder implements EventBuilder {
		public Event parse(IniSection sec) throws SimulationException {
			if (!sec.getTag().equals("new_junction")) {
				return null;
			}
			if ("mc".equals(sec.getValue("type"))) {
				if (parseInt(sec, "time", 0) && parseIdList(sec, "id") && isValidId(sec.getValue("id"))) {
					return new NewMostCrowded(Integer.parseInt(sec.getValue("time")), sec.getValue("id"));
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
			MostCrowded jActual = new MostCrowded(id);
			//Y en caso de no existir la añadimos
			roadMap.getSimObjects().put(id, jActual);
			roadMap.getJunctions().add(jActual);
		} else {
			throw new SimulationException("Ya existe un cruce con el mismo identificador");
		}
	}
}
