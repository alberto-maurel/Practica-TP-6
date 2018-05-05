package es.ucm.fdi.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Junction extends SimulatedObject implements Describable {
	
	protected int semaforoVerde; //Indica el número del semáforo que se encuentra en verde
	//Array con pares de id de las carreteras y colas de vehículos que proceden de dicha carretera
	protected HashMap<String, Queue<Vehicle>> colasCoches; 
	protected HashMap<String, Road> carreterasSalientes;
	protected ArrayList<String> carreterasEntrantesOrdenadas;
	
	
	public Junction() {
		semaforoVerde = -1;
		this.colasCoches = new HashMap<>();
		this.carreterasSalientes = new HashMap<>(); 
		this.carreterasEntrantesOrdenadas = new ArrayList<>();
	}
	
	public Junction(String id) {
		super(id);
		semaforoVerde = -1;
		this.colasCoches = new HashMap<>();
		this.carreterasSalientes = new HashMap<>(); 
		this.carreterasEntrantesOrdenadas = new ArrayList<>();
	}
	
	
	//Funcionalidad
	//
	/**
	 * Entra un vehículo en el cruce
	 * @param vehiculo
	 */
	void entraVehiculo(Vehicle vehiculo) {
		colasCoches.get(vehiculo.carreteraActual.identificador).offer(vehiculo);
	}
	
	/**
	 * Avanzamos un tick el cruce
	 */
	public void avanza() {
		if (semaforoVerde != -1) { 
			//Vemos si la carretera con el semaforo en verde tiene algún coche esperando para pasar
			if (colasCoches.size() > 0 &&
					colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)) != null &&
				colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)).size() > 0) {
					//En dicho caso sacamos el coche
					Vehicle v = (Vehicle) colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)).poll();
					//Y lo movemos a su siguiente carretera
					v.moverASiguienteCarretera();	
			}					
		}
		//Actualizamos los semáforos
		actualizarSemaforo();
	}
	
	public void actualizarSemaforo() {
		if (semaforoVerde == -1) {
			semaforoVerde = 0;
		} else if (colasCoches.size() > 0) {
			semaforoVerde = (semaforoVerde + 1) % colasCoches.size();
		}
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
	
	
	//Para imprimir la junction
	//
	protected String getReportHeader() {
		return "[junction_report]";
	}

	protected void fillReportDetails(Map<String, String> out) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < carreterasEntrantesOrdenadas.size(); ++i) {
			sb.append(toStringRoad(i));
			sb.append(',');
		}
		if(sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
		out.put("queues", sb.toString());
	}
	
	/**
	 * @return String que contiene la descripción de la carretera con semáforo verde 
	 * en ese cruce (en caso de haberla)
	 */
	private String toStringRoad(int index) {
		StringBuilder sb = new StringBuilder();
		if(index < carreterasEntrantesOrdenadas.size()) {
			sb.append('(');
			//En primer lugar añadimos el identificador de la carretera
			sb.append(carreterasEntrantesOrdenadas.get(index));
			sb.append(',');
			//Ahora chequeamos de qué color está el semáforo
			if (index == semaforoVerde) {
				sb.append("green");
			} else {
				sb.append("red");
			}
			sb.append(",[");
			//And now we add all the cars
			for(Vehicle v: colasCoches.get(carreterasEntrantesOrdenadas.get(index))) {
				sb.append(v.identificador + ',');
			}
			if(colasCoches.get(carreterasEntrantesOrdenadas.get(index)).size() > 0) {			
				sb.setLength(sb.length()-1);
			}
			if(colasCoches.get(carreterasEntrantesOrdenadas.get(index)).size() != 0) {
				carreterasEntrantesOrdenadas.get(index);
			}
			sb.append("])");
		}
		return sb.toString();
	}
	
	public void describe(Map<String,String> out, String rowIndex) {
		out.put("ID", identificador);
		
		StringBuilder greenOutput = new StringBuilder();
		greenOutput.append('[');
		greenOutput.append(toStringRoad(semaforoVerde));
		greenOutput.append(']');
		out.put("Green", greenOutput.toString());
		
		StringBuilder redOutput = new StringBuilder();
		redOutput.append('[');
		for(int i = 0; i < carreterasEntrantesOrdenadas.size(); ++i) {
			if(i != semaforoVerde) {
				redOutput.append(toStringRoad(i));
				redOutput.append(',');
			}
		}
		if (redOutput.length() > 1) redOutput.deleteCharAt(redOutput.length() - 1);
		redOutput.append(']');
		out.put("Red", redOutput.toString());
	}		
}
