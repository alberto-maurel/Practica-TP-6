package es.ucm.fdi.model;

import es.ucm.fdi.ini.*;

public class NewJunctionBuilder implements EventBuilder{
	public Event parse(IniSection sec) {
		if (!sec.getTag().equals("new_junction")) {
			return null;
		} else if (sec.getValue("type").equals("rr")) {
			return new NewRoundRobin(Integer.parseInt(sec.getValue("time")), sec.getValue("id"),
					Integer.parseInt(sec.getValue("max_time_slice")), Integer.parseInt(sec.getValue("min_time_slice")));
		} else if (sec.getValue("type").equals("mc")) {
			return new NewMostCrowded(Integer.parseInt(sec.getValue("time")), sec.getValue("id"));
		} else {
			return new NewJunction(Integer.parseInt(sec.getValue("time")), sec.getValue("id"));
		}		
	}
}
