package es.ucm.fdi.model;

import es.ucm.fdi.ini.IniSection;

public interface EventBuilder {

	public abstract Event parse(IniSection sec);
	/*
	– isValidId(String id)
	– parseInt(IniSection sec, String key, int default)
	– parseIdList(IniSection sec, String key)
	*/
}
