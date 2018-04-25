package es.ucm.fdi.model;

import java.util.Map;

public abstract class Event implements Describable{
	
	protected int time;
	protected String id;
	
	
	public Event(){}
	
	/**
	 * Constructor
	 * @param time
	 * @param id
	 */
	public Event(int time, String id){
		this.time = time;
		this.id = id;
	}
	
	public abstract void execute(RoadMap roadMap) throws Exception;

	public void describe(Map<String,String> out){
		out.put("#", id);
		out.put("Time", "" + time);
		out.put("Type", "Not implemented yet");
	}
}
