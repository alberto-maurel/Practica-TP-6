package es.ucm.fdi.model;

import es.ucm.fdi.ini.IniSection;

public class NewRoad extends Event{
	private String src;
	private String dest;
	int max_speed;
	int length;
	
	public NewRoad(int time, String id, int max_speed, int length, String src, String dest) {
		super(time, id);
		this.max_speed = max_speed;
		this.length = length;
		this.src = src;
		this.dest = dest;
	}
	
	public static class Builder implements EventBuilder  {
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
			
			Road nuevaCarretera = new Road(id, length, max_speed, (Junction) J1, (Junction) J2); //Cast feillo, revisar

			roadMap.getSimObjects().put(id, nuevaCarretera);
			roadMap.getRoads().add(nuevaCarretera);
		} else {
			throw new SimulationException("El identificador está duplicado");
		}
	}
}