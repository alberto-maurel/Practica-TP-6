package es.ucm.fdi.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Junction extends SimulatedObject {
	
	protected int semaforoVerde; //Indica el número del semáforo que se encuentra en verde
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
	
		
	/**
	 * Entra un vehículo en el cruce
	 * @param vehiculo
	 */
	void entraVehiculo(Vehicle vehiculo) {
		colasCoches.get(vehiculo.carreteraActual.identificador).offer(vehiculo);
	}
	
	/**
	 * Avanzamos un tick ell cruce
	 */
	public void avanza() {
		//Actualizamos los semáforos
		actualizarSemaforo();
		/* NOTA: en el guión de la práctica se especificaba que en primer lugar tenían que actualizarse los vehículos del cruce y después
				 cambiar el semáforo. Sin embargo, nos parece que el comportamiento simulado se asemeja más al de actualizar de antemano 
				 el cruce y posteriormente mover los coches. */
		
		//Vemos si la carretera con el semaforo en verde tiene algún coche esperando para pasar
		if (colasCoches.size() > 0 && colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)) != null && 
				colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)).size() > 0) {
					//En dicho caso sacamos el coche
					Vehicle v = (Vehicle) colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)).poll();
					//Y lo movemos a su siguiente carretera
					v.moverASiguienteCarretera();		
		}
	}
	
	public void actualizarSemaforo() {
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
		String aux = "";
		for (int i = 0; i < carreterasEntrantesOrdenadas.size(); ++i) {
			aux += "(" + carreterasEntrantesOrdenadas.get(i) + ",";
			
			if (i == carreterasEntrantesOrdenadas.size() - 1) {
				if(semaforoVerde == 0) {
					aux += "green,";
				} else {
					aux += "red,";
				}	
			} else {
				if(semaforoVerde == i + 1) {
					aux += "green,";
				} else {
					aux += "red,";
				}	
			}
			
			aux += '[';
			//And now we add all the cars
			for(Vehicle v: colasCoches.get(carreterasEntrantesOrdenadas.get(i))) {
				aux += v.identificador + ',';
			}
			if(colasCoches.get(carreterasEntrantesOrdenadas.get(i)).size() > 0) {			
				aux = aux.substring(0, aux.length() - 1);
			}
			if(colasCoches.get(carreterasEntrantesOrdenadas.get(i)).size() != 0) {
				carreterasEntrantesOrdenadas.get(i);
			}
			aux += ']';
			aux += ')';
			if(i != carreterasEntrantesOrdenadas.size() - 1) aux += ',';
		}
		out.put("queues", aux);
	}
	
	
}
