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
		SimulatedObject J1, J2;
		if(roadMap.simObjects.get(id) != null) {
			//Comprobamos que los cruces existen y si no existen los creamos
			if(roadMap.simObjects.get(src) == null) {
				J1 = new Junction(src);
				//Y la incluimos en el roadMap
				roadMap.simObjects.put(src, J1);
				roadMap.junctions.add((Junction)J1);
			} else {
				J1 = roadMap.simObjects.get(src); 
			}
			
			if(roadMap.simObjects.get(dest) == null) {
				J2 = new Junction(dest);
			} else {
				J2 = roadMap.simObjects.get(dest); 
				//Y la incluimos en el roadMap
				roadMap.simObjects.put(src, J2);
				roadMap.junctions.add((Junction)J2);
			}
			
			Road nuevaCarretera = new Road(id, length, max_speed, (Junction) J1, (Junction) J2); //Cast feillo, revisar

			roadMap.simObjects.put(id, nuevaCarretera);
			roadMap.roads.add(nuevaCarretera);
		}
	}
}