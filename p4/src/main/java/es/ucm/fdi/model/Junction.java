package es.ucm.fdi.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import es.ucm.fdi.util.*;

public class Junction extends SimulatedObject{
	private int semaforoVerde; //Indica el número del semáforo que se encuentra en verde
	protected HashMap<String, Queue<Vehicle>> colasCoches; //Array con pares de id de las carreteras y colas de vehículos que proceden de dicha carretera
	protected HashMap<String, Road> carreterasSalientes;
	protected ArrayList<String> carreterasEntrantesOrdenadas;
	
	public Junction() {
		super();
		semaforoVerde = 0;
		this.colasCoches = new HashMap<>();
		this.carreterasSalientes = new HashMap<>(); 
		this.carreterasEntrantesOrdenadas = new ArrayList<>();
	}
	
	public Junction(String id) {
		super(id);
		semaforoVerde = 0;
		this.colasCoches = new HashMap<>();
		this.carreterasSalientes = new HashMap<>(); 
		this.carreterasEntrantesOrdenadas = new ArrayList<>();
	}
	
	public Junction(String id, HashMap<String, Queue<Vehicle>> colasCoches) {
		super(id);
		semaforoVerde = 0;
		this.colasCoches = colasCoches;
		this.carreterasSalientes = new HashMap<>(); 
		this.carreterasEntrantesOrdenadas = new ArrayList<>();
	}
	
	void entraVehiculo(Vehicle vehiculo) {
		colasCoches.get(vehiculo.carreteraActual.identificador).offer(vehiculo);
	}
	
	public void avanza() {
		//En primer lugar vemos si la carretera con el semaforo en verde tiene algún coche esperando para pasar
		//if(colasCoches.size() > 0 && colasCoches.get(semaforoVerde) != null && colasCoches.get(semaforoVerde).size() > 0) {
			
		if(colasCoches.size() > 0 && colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)) != null && 
				colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)).size() > 0) {
					//En dicho caso sacamos el coche
					Vehicle v = (Vehicle) colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)).poll();
					//Y lo movemos a su siguiente carretera
					v.moverASiguienteCarretera();		
		}
		//Y encendemos el semaforo de la siguiente interseccion independientemente de si hemos movido algún coche o no
		if(colasCoches.size() > 0) semaforoVerde = (semaforoVerde + 1) % colasCoches.size();
	}
	
	public Road buscarCarretera(Junction sigCruce) {
		for(Map.Entry<String, Road> r: carreterasSalientes.entrySet()) {
			if(r.getValue().cruceFin == sigCruce) return r.getValue();
		}
		return null;
	}
	
	/**
	 * Añade una nueva carretera saliente al cruce
	 */
	public void nuevaCarreteraSaliente(Road road) {
		carreterasSalientes.put(road.identificador, road);
	}

	public void nuevaCarreteraEntrante(Road road) {
		colasCoches.put(road.identificador, new ArrayDeque<Vehicle>());
		carreterasEntrantesOrdenadas.add(road.identificador);
	}
	
	protected String getReportHeader() {
		return "[junction_report]";
	}

	protected void fillReportDetails(Map<String, String> out) {
		
	}
}
