package es.ucm.fdi.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class RoadMap {
	
	//Map que contiene como claves los id del objeto y como valor el objeto
	private HashMap<String, SimulatedObject> simObjects = new HashMap<>(); //Permite buscar los elementos
	
	//Listados de cada una de las clases de elementos ordenados por orden de llegada
	private List<Junction> junctions = new ArrayList<>();
	private List<Road> roads = new ArrayList<>();
	private List<Vehicle> vehicles = new ArrayList<>();
	
	
	private Map<String, SimulatedObject> unmodifiableSimObjects = 
			Collections.unmodifiableMap(simObjects);
	private List<Junction> unmodifiableJunctions = Collections.unmodifiableList(junctions);
	private List<Road> unmodifiableRoads = Collections.unmodifiableList(roads);
	private List<Vehicle> unmodifiableVehicles = Collections.unmodifiableList(vehicles);

	public RoadMap() {}
	
	
	public List<Junction> getConstantJunctions() {
		return unmodifiableJunctions;
	}
	
	public List<Road> getConstantRoads() {
		return unmodifiableRoads;
	}
	
	public List<Vehicle> getConstantVehicles() {
		return unmodifiableVehicles;
	}
	
	public Map<String, SimulatedObject> getConstantSimObjects() {
		return unmodifiableSimObjects;
	}
	
	//Puesto que tenemos que actualizar las listas constantes tras modificar las listas,
	//incluimos unas funciones a las que se llama tras realizar modificaciones
	
	public void cacheRoads() {
		unmodifiableRoads = Collections.unmodifiableList(roads);
	}
	
	public void cacheJunctions() {
		unmodifiableJunctions = Collections.unmodifiableList(junctions);
	}
	
	public void cacheVehicles() {
		unmodifiableVehicles = Collections.unmodifiableList(vehicles);
	}
	
	public void cacheSimObjects() {
		unmodifiableSimObjects = Collections.unmodifiableMap(simObjects);
	}
	
	public List<Junction> getJunctions() {
		return junctions;
	}
	
	public List<Road> getRoads() {
		return roads;
	}
	
	public List<Vehicle> getVehicles() {
		return vehicles;
	}
	
	public Map<String, SimulatedObject> getSimObjects() {
		return simObjects;
	}
}
