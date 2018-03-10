package es.ucm.fdi.model;

public class NewHighway extends NewRoad {
	
	protected int lanes;
	
	public NewHighway(int time, String id, int max_speed, int length, String src, String dest, int l) {
		super(time, id, max_speed, length, src, dest);
		lanes = l;
	}
}
