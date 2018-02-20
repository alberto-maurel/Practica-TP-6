package es.ucm.fdi.model;

import es.ucm.fdi.util.MultiTreeMap;

public class Road extends SimulatedObjects{
	int longitud;
	int maxVel;
	MultiTreeMap<Integer, Vehicle> situacionCarretera;
	
	void entraVehiculo(Vehicle v) {
		situacionCarretera.putValue(0, v);
	}
	
	void saleVehiculo() {
		
	}
	
	void avanza() {
		//for(MultiTreeMap.Inner<Integer,Vehicle> v: situacionCarretera.entrySet()) {		
			//1. Calculamos la velocidad base de cada elemento
			
			
		}
	}
}
