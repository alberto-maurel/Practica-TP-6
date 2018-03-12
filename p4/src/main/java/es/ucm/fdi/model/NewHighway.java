package es.ucm.fdi.model;

import es.ucm.fdi.ini.IniSection;

public class NewHighway extends NewRoad {
	
	protected int lanes;
	
	public NewHighway(int time, String id, int max_speed, int length, String src, String dest, int l) {
		super(time, id, max_speed, length, src, dest);
		lanes = l;
	}
	
	public static class Builder implements EventBuilder{
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
}
