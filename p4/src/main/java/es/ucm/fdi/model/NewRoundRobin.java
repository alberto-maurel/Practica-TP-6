package es.ucm.fdi.model;

public class NewRoundRobin extends NewJunction {
	
	protected int max_time_slice;
	protected int min_time_slice;
	
	public NewRoundRobin(int time, String id, int max, int min) {
		super(time, id);
		max_time_slice = max;
		min_time_slice = min;
	}
}
