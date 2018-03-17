package es.ucm.fdi.model;

import es.ucm.fdi.ini.IniSection;

public class NewHighway extends NewRoad {
	
	protected int lanes;
	
	public NewHighway(int time, String id, int max_speed, int length, String src, String dest, int l) {
		super(time, id, max_speed, length, src, dest);
		lanes = l;
	}
	
	public static class Builder implements EventBuilder {
		public Event parse(IniSection sec) throws SimulationException {
			if (!sec.getTag().equals("new_road")) {
				return null;
			}
			
			if ("lanes".equals(sec.getValue("type"))) {
				if (parseInt(sec, "time", 0) && parseIdList(sec, "id") && 
						isValidId(sec.getValue("id")) && parseInt(sec, "max_speed", 0) && 
					parseInt(sec, "length", 0) && parseIdList(sec, "src") &&
						isValidId(sec.getValue("src")) && parseIdList(sec, "dest") &&
						isValidId(sec.getValue("dest")) &&parseInt(sec, "lanes" , 1)) {
					
					return new NewHighway(Integer.parseInt(sec.getValue("time")), sec.getValue("id"), Integer.parseInt(sec.getValue("max_speed")), 
							Integer.parseInt(sec.getValue("length")), sec.getValue("src"), sec.getValue("dest"), Integer.parseInt(sec.getValue("lanes")));
				} else {
					throw new SimulationException("Algún parámetro no existe o es inválido");
				} 	
			} 			
			return null;	
		}
	}
	
	public void execute(RoadMap roadMap) throws SimulationException {
		//Comprobamos que no haya otra carretera con el mismo id
		SimulatedObject J1, J2;
		if(roadMap.getConstantSimObjects().get(id) == null) {
			//Comprobamos que existan ambos cruces
			if(roadMap.getConstantSimObjects().get(src) == null) {
				throw new SimulationException("El cruce no existe");
			}
			J1 = roadMap.getSimObjects().get(src); 

			if(roadMap.getSimObjects().get(dest) == null) {
				throw new SimulationException("El cruce no existe");
			} 
			J2 = roadMap.getSimObjects().get(dest); 
			
			Highway nuevaCarretera = new Highway(id, length, max_speed, (Junction) J1, (Junction) J2, lanes);

			roadMap.getSimObjects().put(id, nuevaCarretera);
			roadMap.getRoads().add(nuevaCarretera);
		} else {
			throw new SimulationException("El identificador está duplicado");
		}
	}
}
