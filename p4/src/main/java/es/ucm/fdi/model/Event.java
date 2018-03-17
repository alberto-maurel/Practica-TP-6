package es.ucm.fdi.model;

public abstract class Event {
	
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
	
}
