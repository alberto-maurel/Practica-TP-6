package es.ucm.fdi.model;

import java.util.Map;

import es.ucm.fdi.ini.IniSection;

public class NewRoad extends Event{
	protected String src;
	protected String dest;
	protected int max_speed;
	protected int length;	
	
	public NewRoad(int time, String id, int max_speed, int length, String src, String dest) {
		super(time, id);
		this.max_speed = max_speed;
		this.length = length;
		this.src = src;
		this.dest = dest;
	}
	
	
	public static class Builder implements EventBuilder {
		
		public Event parse(IniSection sec) throws SimulationException {
			if (!sec.getTag().equals("new_road")) {
				return null;
			} 
			if(sec.getValue("type") == null) {
				if (parseInt(sec, "time", 0) && parseIdList(sec, "id") && 
						isValidId(sec.getValue("id")) && parseInt(sec, "max_speed", 0) && 
					parseInt(sec, "length", 0) && parseIdList(sec, "src") &&
					isValidId(sec.getValue("src")) && parseIdList(sec, "dest") &&
					isValidId(sec.getValue("dest"))) {
					
					return new NewRoad(Integer.parseInt(sec.getValue("time")), sec.getValue("id"), 
							Integer.parseInt(sec.getValue("max_speed")), 
						Integer.parseInt(sec.getValue("length")), sec.getValue("src"), sec.getValue("dest"));
				} else {
					throw new SimulationException("Algún parámetro no existe o es inválido");
				} 	
			}
			return null;
		}
		
    }
	
	public void execute(RoadMap roadMap) throws SimulationException {		
		Junction J1, J2;
		if(roadMap.getConstantSimObjects().get(id) == null) { //Comprobamos que no haya otra carretera con el mismo id
			//Comprobamos que existan ambos cruces
			
			J1 = parsearCruce(roadMap, src);
			J2 = parsearCruce(roadMap, dest);
			
			Road nuevaCarretera = new Road(id, length, max_speed, J1, J2); //Cast feillo, revisar

			roadMap.getSimObjects().put(id, nuevaCarretera);
			roadMap.getRoads().add(nuevaCarretera);
		} else {
			throw new SimulationException("El identificador está duplicado");
		}
	}
	
	protected Junction parsearCruce(RoadMap roadMap, String identificadorCruce) {
		if(roadMap.getConstantSimObjects().get(identificadorCruce) == null) {
			throw new SimulationException("El cruce no existe");
		}
		Junction J1 = (Junction) roadMap.getSimObjects().get(identificadorCruce);
		return J1;
	}
	
	public void describe(Map<String,String> out) {
		out.put("#", id);
		out.put("Time", "" + time);
		out.put("Type", "New Road");
	}
	
}