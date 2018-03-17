 package es.ucm.fdi.model;

import java.util.ArrayList;
import java.util.Arrays;
import es.ucm.fdi.ini.IniSection;

public class NewVehicle extends Event {
	protected int max_speed;
	protected ArrayList<String> itinerario; //Guardamos los cruces en forma de ID
	
	
	public NewVehicle(int time, String id, int max_speed, ArrayList<String> itinerario) {
		super(time, id);
		this.max_speed = max_speed;
		this.itinerario = itinerario;
	}
	
	
	public static class Builder implements EventBuilder {
		
		public Event parse(IniSection sec) throws SimulationException {
			if (!sec.getTag().equals("new_vehicle")) {
				return null;
			}		
			if(sec.getValue("type") == null) {
				if(parseInt(sec, "time", 0) && parseIdList(sec, "id") && 
						isValidId(sec.getValue("id")) && parseInt(sec, "max_speed", 0)) {
					
					ArrayList<String> itinerario = parsearItinerario(sec);
				
					//Tenemos garantizado que los argumentos que nos han introducido son válidos
					return new NewVehicle(Integer.parseInt(sec.getValue("time")), sec.getValue("id"),
						Integer.parseInt(sec.getValue("max_speed")), itinerario);
				
				}
				throw new SimulationException("Algún parámetro no existe o es inválido");
			}				
			//Devolvemos null si es un vehículo sin tipo
			return null;
		}
		
	}
	
	
	
	public void execute(RoadMap roadMap) throws SimulationException {
		//Comprobamos que no existiera previamente el vehículo
		if(roadMap.getConstantSimObjects().get(id) == null) {	
			ArrayList<Junction> itinerarioVehiculoJunctions = crearItinerario(roadMap);
			
			//Llamamos al constructor del vehículo
			Vehicle nuevoVehiculo = new Vehicle(id, max_speed, itinerarioVehiculoJunctions);
			
			//Y lo insertamos en el roadMap
			roadMap.getVehicles().add(nuevoVehiculo);
			roadMap.getSimObjects().put(id, nuevoVehiculo);
		} else {
			throw new SimulationException("Identificador de objeto duplicado");
		}
	}
	
	protected ArrayList<Junction> crearItinerario(RoadMap roadMap) throws SimulationException{
		//Creamos el itinerario
		ArrayList<Junction> itinerarioVehiculoJunctions = new ArrayList<Junction> ();
		
		//Para cada cruce que pertenezca al itinerario del vehículo
		for(String idJunction: itinerario) {
			//Añadimos el cruce al arrayList de Junctions que representa el itinerario del vehículo
			if(roadMap.getConstantSimObjects().get(idJunction) == null) {
				throw new SimulationException("El cruce no existe");
			}
			itinerarioVehiculoJunctions.add((Junction) roadMap.getSimObjects().get(idJunction));	
		}
		return itinerarioVehiculoJunctions;
	}
}

