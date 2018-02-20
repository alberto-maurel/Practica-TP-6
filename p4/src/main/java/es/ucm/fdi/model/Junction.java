package es.ucm.fdi.model;

import java.util.ArrayList;
import es.ucm.fdi.util.*;

public class Junction extends SimulatedObjects{
	int semaforoVerde;
	ArrayList<IdCola> colasCoches; //Array con pares de id de las carreteras y colas de vehículos que proceden de dicha carretera
	
	void entraVehiculo() {
		
	}
	
	void avanza() {
		//En primer lugar vemos si la carretera con el semaforo en verde tiene algún coche esperando para pasar
		if(!colasCoches.get(semaforoVerde).getQueue().isEmpty()) {
			//En dicho caso sacamos el coche
			colasCoches.get(semaforoVerde).sacarCoche();
		}
		//Y encendemos el semaforo de la siguiente interseccion
		semaforoVerde = (semaforoVerde + 1) % colasCoches.size();
	}
}
