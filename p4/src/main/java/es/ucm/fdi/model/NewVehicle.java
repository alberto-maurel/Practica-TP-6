package es.ucm.fdi.model;

import java.util.ArrayList;

public class NewVehicle extends Event{
	int max_speed;
	private ArrayList<String> itinerario; //Guardamos los cruces en forma de ID
	
	public NewVehicle(){
		super();
	}
	
	public NewVehicle(int time, String id, int max_speed, ArrayList<String> itinerario) {
		super(time, id);
		this.max_speed = max_speed;
		this.itinerario = itinerario;
	}
	
	public void execute(RoadMap roadMap) throws Exception {
		//Comprobamos que no existiera previamente el vehículo
		if(roadMap.simObjects.get(id) == null) {
			ArrayList<Junction> itinerarioVehiculoJunctions = new ArrayList<Junction> ();
			//Para cada cruce que pertenezca al itinerario del vehículo
			for(String idJunction: itinerario) {
				//Añadimos el cruce al arrayList de Junctions que representa el itinerario del vehículo
				if(roadMap.simObjects.get(idJunction) == null) {
					throw new Exception("El cruce no existe");
					/*//Creamos las junction que aún no existiesen 
					Junction cruceFaltante = new Junction(id);
					roadMap.junctions.add(cruceFaltante);
					roadMap.simObjects.put(idJunction, cruceFaltante);
					itinerarioVehiculoJunctions.add(cruceFaltante);*/
				}
				else {
					itinerarioVehiculoJunctions.add((Junction) roadMap.simObjects.get(idJunction));
				}	
			}
			
			//Llamamos al constructor del vehículo
			Vehicle nuevoVehiculo = new Vehicle(id, max_speed, itinerarioVehiculoJunctions);
			
			//Y lo insertamos en el roadMap
			roadMap.vehicles.add(nuevoVehiculo);
			roadMap.simObjects.put(id, nuevoVehiculo);
		} else {
			throw new Exception("Identificador de objeto duplicado");
		}
	}
}
