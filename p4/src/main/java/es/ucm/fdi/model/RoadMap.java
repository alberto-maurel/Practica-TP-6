package es.ucm.fdi.model;

import es.ucm.fdi.util.*;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.model.*;

public class RoadMap {
	//public SimObject getSimObject(string id)
	
	
	//Permite buscar los elementos
	private MultiTreeMap<String, SimulatedObject> simObjects; //Map que contiene como claves los id del objeto y como valor el objeto
	
	//Listados de cada una de las clases de elementos ordenados por orden de llegada
	private List<Junction> junctions = new ArrayList<>();
	private List<Road> roads = new ArrayList<>();
	private List<Vehicle> vehicles = new ArrayList<>();
}
