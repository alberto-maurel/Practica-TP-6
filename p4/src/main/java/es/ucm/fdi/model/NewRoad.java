package es.ucm.fdi.model;

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
}