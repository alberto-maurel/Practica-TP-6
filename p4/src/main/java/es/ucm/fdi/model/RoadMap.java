package es.ucm.fdi.model;

import es.ucm.fdi.util.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.HashMap;

import es.ucm.fdi.model.*;

public class RoadMap {
	//Permite buscar los elementos
	private HashMap<String, SimulatedObject> simObjects; //Map que contiene como claves los id del objeto y como valor el objeto
		
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
