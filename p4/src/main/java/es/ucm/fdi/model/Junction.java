package es.ucm.fdi.model;

import java.util.ArrayList;
import java.util.Map;

import es.ucm.fdi.util.*;

public class Junction extends SimulatedObject{
	private int semaforoVerde; //Indica el número del semáforo que se encuentra en verde
	private ArrayList<IdCola> colasCoches; //Array con pares de id de las carreteras y colas de vehículos que proceden de dicha carretera
	private ArrayList<Road> carreterasSalientes;
	
	public Junction() {
		super();
		semaforoVerde = 0;
		this.colasCoches = null;
	}
	
	public Junction(String id) {
		super(id);
		semaforoVerde = 0;
		this.colasCoches = null;
	}
	
	public Junction(String id, ArrayList<IdCola> colasCoches) {
		super(id);
		semaforoVerde = 0;
		this.colasCoches = colasCoches;
	}
	
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
	
	public Road buscarCarretera(Junction sigCruce) {
		for(Road r: carreterasSalientes) {
			if(r.cruceFin == sigCruce) return r;
		}
		return null;
	}
	
	/**
	 * Añade una nueva carretera saliente al cruce
	 */
	public void nuevaCarreteraSaliente(Road road) {
		carreterasSalientes.add(road);
	}

	public void nuevaCarreteraEntrante(Road road) {
		IdCola nuevaCarretera = new IdCola(road.identificador);
		colasCoches.add(nuevaCarretera);
	}
	
	protected String getReportHeader() {
		return "[junction_report]";
	}

	protected void fillReportDetails(Map<String, String> out) {
		
	}
}
