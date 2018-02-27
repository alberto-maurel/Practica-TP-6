package es.ucm.fdi.model;
import java.util.HashMap;

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
	
	public void execute(RoadMap roadMap) {
		//Comprobamos que no haya otra carretera con el mismo id
		if(roadMap.simObjects.get(id) != null) {
			SimulatedObject J1 = roadMap.simObjects.get(src); 
			SimulatedObject J2 = roadMap.simObjects.get(dest);
			
			Road nuevaCarretera = new Road(id, length, max_speed, (Junction) J1, (Junction) J2); //Cast feillo, revisar
			
			roadMap.simObjects.put(id, nuevaCarretera);
			roadMap.roads.add(nuevaCarretera);
		}
	}
}