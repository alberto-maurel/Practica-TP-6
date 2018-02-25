package es.ucm.fdi.model;

public class NewRoad extends Event{
			//src = <JUNC-ID>
			//dest = <JUNC-ID>
	int max_speed;
	int length;
	public NewRoad(int time, String id, int max_Speed, int Length) {
		super(time, id);
		max_speed = max_Speed;
		length = Length;
	}
}
