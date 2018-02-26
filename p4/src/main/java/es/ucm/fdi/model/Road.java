package es.ucm.fdi.model;

import es.ucm.fdi.util.MultiTreeMap;
import es.ucm.fdi.model.*;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class Road extends SimulatedObjects{
	int longitud;
	int maxVel;
	MultiTreeMap<Integer, Vehicle> situacionCarretera;
	
	/**
	 * Introduce al vehículo en la carretera
	 * @param Vehículo
	 */
	void entraVehiculo(Vehicle v) {
		situacionCarretera.putValue(0, v);
	}
	
	/**
	 * Saca al vehículo de la carretera
	 * @param Vehículo
	 */
	void saleVehiculo(Vehicle v) {
		//Sabemos que el vehículo que tenemos que retirar está al final de la carretera
		situacionCarretera.removeValue(longitud, v);
	}
	
	/**
	 * Función que mueve a todos los coches de la carretera
	 */
	void avanza() {
		//En primer lugar calculamos la velocidad base de la carretera
		int aux1 = Math.max(1 , Math.toIntExact(situacionCarretera.sizeOfValues() - 1));
		int aux2 = maxVel/aux1;
		int velocidadBase = Math.min(maxVel, aux2);
		
		//Recorremos la carretera.
		//En una primera pasada cogemos cada coche de la carretera y lo metemos en un array, que posteriormente se ordena por posición
		//de los coches. En la segunda pasada se modifica cada coche
		ArrayList<Vehicle> vehiculosColocadosPorPosicion = new ArrayList<>();

		for(Vehicle v: situacionCarretera.innerValues()) {		
			vehiculosColocadosPorPosicion.add(v);		
		}
		
		Collections.sort(vehiculosColocadosPorPosicion, new Comparator<Vehicle>(){
			public int compare(Vehicle v1, Vehicle v2) {
				return v1.localizacionCarretera - v2.localizacionCarretera;
			}
		});
		
		//Y ahora vamos contabilizando cuantos vehículos están averiados y les vamos cambiando la velocidad
		int factorReduccion = 1;
		for(Vehicle v: vehiculosColocadosPorPosicion) {
			if(v.cocheAveriado()) {
				factorReduccion = 2;
			}
			else {
				v.setVelocidadActual(velocidadBase/factorReduccion);
			}
			//Y hacemos que el coche avance
			v.avanza();
		}	
	}
}
