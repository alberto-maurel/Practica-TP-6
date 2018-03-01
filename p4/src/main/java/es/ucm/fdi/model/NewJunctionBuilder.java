package es.ucm.fdi.model;

import es.ucm.fdi.ini.*;

public class NewJunctionBuilder implements EventBuilder{
	public Event parse(IniSection sec) {
		if (!sec.getTag().equals("new_junction")) return null;
		return new NewJunction(Integer.parseInt(sec.getValue("time")), sec.getValue("id"));
	}
}
