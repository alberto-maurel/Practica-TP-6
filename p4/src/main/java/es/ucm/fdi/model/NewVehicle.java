 package es.ucm.fdi.model;

import java.util.ArrayList;
import java.util.Arrays;

import es.ucm.fdi.ini.IniSection;

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
	
	public static class Builder implements EventBuilder{
		public Event parse(IniSection sec) {
			if (!sec.getTag().equals("new_vehicle")) {
				return null;
			} else {
				//Creamos el itinerario
				//Creo que viene todo en la misma sección, así que haremos el split a ver si funciona
				String[] itinerarioString = sec.getValue("itinerary").split("[ ,]");
				ArrayList<String> itinerario = new ArrayList<>(Arrays.asList(itinerarioString));
				
				//Y ahora dependiendo del vehículo que tenemoos que crear llamamos a uno u otro
				if(sec.getValue("type") == null) {
					return new NewVehicle(Integer.parseInt(sec.getValue("time")), sec.getValue("id"),
							Integer.parseInt(sec.getValue("max_speed")), itinerario);
				} else if (sec.getValue("type").equals("car")) {
					return new NewCar(Integer.parseInt(sec.getValue("time")), sec.getValue("id"), Integer.parseInt(sec.getValue("max_speed")), itinerario, 
							Integer.parseInt(sec.getValue("resistance")), Double.parseDouble(sec.getValue("fault_probability")), 
							Integer.parseInt(sec.getValue("max_fault_duration")), Long.parseLong(sec.getValue("seed")));
				} 
				else if (sec.getValue("type").equals("bike")) {
					return new NewBike(Integer.parseInt(sec.getValue("time")), sec.getValue("id"), Integer.parseInt(sec.getValue("max_speed")), itinerario);
				} else {
					return null;
				}
				
			}
		}
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
