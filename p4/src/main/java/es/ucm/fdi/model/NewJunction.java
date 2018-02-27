package es.ucm.fdi.model;

public class NewJunction extends Event{
	
	/**
	 * Constructor
	 * @param time momento en el que ocurre el evento
	 * @param id id del evento
	 */
	public NewJunction(int time, String id) {
		super(time, id);
	}
	
	public void execute(RoadMap roadMap) {
		//Comprobamos que la intersección no exista previamente
		if(roadMap.simObjects.get(id) == null) {
			//Y en caso de no existir la añadimos
			roadMap.simObjects.put(id, new Junction(id));
			roadMap.junctions.add(new Junction(id));
		}
	}
}
