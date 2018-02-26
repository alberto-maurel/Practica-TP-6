package es.ucm.fdi.model;

import java.util.ArrayList;

import es.ucm.fdi.util.*;

public class Junction extends SimulatedObject{
	private int semaforoVerde; //Indica el número del semáforo que se encuentra en verde
	private ArrayList<IdCola> colasCoches; //Array con pares de id de las carreteras y colas de vehículos que proceden de dicha carretera
	
	
	
	
	void entraVehiculo(Vehicle vehiculo) {
		String idCarretera = vehiculo.carreteraActual.identificador;
		int nCruce;
		int pos = 0;
		while(pos < colasCoches.size()){			
			if (colasCoches.get(pos).getId().equals(idCarretera)) {
				colasCoches.get(pos).meterCoche(vehiculo);
			}
		}
	}
	
	public void avanza() {
		//En primer lugar vemos si la carretera con el semaforo en verde tiene algún coche esperando para pasar
		if(!colasCoches.get(semaforoVerde).cruceVacio()) {
			//En dicho caso sacamos el coche
			Vehicle v = colasCoches.get(semaforoVerde).sacarCoche();
			//Y lo movemos a su siguiente carretera
			v.moverASiguienteCarretera();
		}
		//Y encendemos el semaforo de la siguiente interseccion independientemente de si hemos movido algún coche o no
		semaforoVerde = (semaforoVerde + 1) % colasCoches.size();
	}
	
	protected String getReportHeader() {
		return "[junction_report]";
	}
}
