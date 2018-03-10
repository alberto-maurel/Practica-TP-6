package es.ucm.fdi.model;

import es.ucm.fdi.ini.IniSection;

public class NewRoadBuilder implements EventBuilder{
	public Event parse(IniSection sec) {
		if (!sec.getTag().equals("new_road")) {
			return null;
		} else {
			if (sec.getValue("type").equals("lanes")) {
				return new NewHighway(Integer.parseInt(sec.getValue("time")), sec.getValue("id"), Integer.parseInt(sec.getValue("max_speed")), 
				Integer.parseInt(sec.getValue("length")), sec.getValue("src"), sec.getValue("dest"), Integer.parseInt(sec.getValue("lanes")));
			} else if (sec.getValue("type").equals("dirt")) {
				return new NewPath(Integer.parseInt(sec.getValue("time")), sec.getValue("id"), Integer.parseInt(sec.getValue("max_speed")), 
						Integer.parseInt(sec.getValue("length")), sec.getValue("src"), sec.getValue("dest"));
			} else {
				return new NewRoad(Integer.parseInt(sec.getValue("time")), sec.getValue("id"), Integer.parseInt(sec.getValue("max_speed")), 
						Integer.parseInt(sec.getValue("length")), sec.getValue("src"), sec.getValue("dest"));
			}
		}		
	}
}
