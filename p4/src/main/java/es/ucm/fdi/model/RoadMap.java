package es.ucm.fdi.model;

import es.ucm.fdi.util.*;

import java.util.List;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.HashMap;

import es.ucm.fdi.model.*;

public class RoadMap {
	//public SimObject getSimObject(string id)

	//Permite buscar los elementos
	protected HashMap<String, SimulatedObject> simObjects; //Map que contiene como claves los id del objeto y como valor el objeto
	
	//Listados de cada una de las clases de elementos ordenados por orden de llegada
	protected List<Junction> junctions = new ArrayList<>();
	protected List<Road> roads = new ArrayList<>();
	protected List<Vehicle> vehicles = new ArrayList<>();
}
