package es.ucm.fdi.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class RoadMap {
	
	//Map que contiene como claves los id del objeto y como valor el objeto
	private HashMap<String, SimulatedObject> simObjects; //Permite buscar los elementos
	
	//Listados de cada una de las clases de elementos ordenados por orden de llegada
	private List<Junction> junctions = new ArrayList<>();
	private List<Road> roads = new ArrayList<>();
	private List<Vehicle> vehicles = new ArrayList<>();
	

	public RoadMap() {
		simObjects = new HashMap<>();		
		junctions = new ArrayList<>();
		roads = new ArrayList<>();
		vehicles = new ArrayList<>();
	}
	
	
	public List<Junction> getConstantJunctions() {
		return Collections.unmodifiableList(junctions);
	}
	
	public List<Road> getConstantRoads() {
		return Collections.unmodifiableList(roads);
	}
	
	public List<Vehicle> getConstantVehicles() {
		return Collections.unmodifiableList(vehicles);
	}
	
	public Map<String, SimulatedObject> getConstantSimObjects() {
		return Collections.unmodifiableMap(simObjects);
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
