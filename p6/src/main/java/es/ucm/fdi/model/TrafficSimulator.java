package es.ucm.fdi.model;

import java.util.List;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.SwingUtilities;

import es.ucm.fdi.util.MultiTreeMap;

public class TrafficSimulator {

	/*La lista de eventos solo contendrá los eventos que se hayan cargado y que estén dentro de los ticks de la simulación
	/que se van a ejecutar*/
	private MultiTreeMap<Integer, Event> listaEventos; 
	private int tickActual;
	private RoadMap mapaTrafico;
	private OutputStream out;
	//Para distinguir entre antes de ejecutar el primer tick y después
	//private Boolean primerTick;
	
	public TrafficSimulator(OutputStream out) {
		this.listaEventos = new MultiTreeMap<>();
		tickActual = 0;
		mapaTrafico = new RoadMap();
		//primerTick = true;
		this.out = out;
		fireUpdateEvent(EventType.RESET, "Ha ocurrido un error durante la construcción del simulador");
	}

	public void run(int time) {
		int tick = 0;
		try {
			while (tick < time) {
				ArrayList<Event> eventosAEliminar = new ArrayList<>();
				for(Event ev: listaEventos.innerValues()) {
					//Si el evento se debiera ejecutar en el tick actual lo añadimos a la lista de eventos
					//y lo guardamos en el array para deletearlo despues
					if(ev.time != tickActual) {
						break;
					} else {
						eventosAEliminar.add(ev);
						ev.execute(mapaTrafico);
					}
				}
				
				//Eliminamos los elementos que ya hemos ejecutado (no queremos eliminar elementos de una 
				//estructura mientras la iteramos)
				for(Event ev: eventosAEliminar) {
					listaEventos.removeValue(ev.time, ev);
				}
				
				//Ahora avanzo cada una de las carreteras (y ellas a su vez hacen avanzar a los coches)
				for(Road r: mapaTrafico.getRoads()) {
					r.avanza();
				}
				
				
				//Avanzamos los cruces
				for(Junction j: mapaTrafico.getJunctions()) {
					j.avanza();
				}
				
				//primerTick = false;
				//Y por último escribimos los informes en el orden indicado
				fireUpdateEvent(EventType.ADVANCED, "Ha ocurrido un error al ejecutar la simulación");
				++tick;
				++tickActual;
				generarInformes(out, null, null, null);
				
			}		
		} catch (Exception e) {			
			fireUpdateEvent(EventType.ERROR,
					"Ha ocurrido un error al ejecutar la simulación.\n" +
					"Error: " + e.getMessage() + "\n" +
					"Clase: " + e.getStackTrace()[0].getClassName() + "\n" +
					"Método: " + e.getStackTrace()[0].getMethodName() + "\n" +
					"Línea: " + e.getStackTrace()[0].getLineNumber());
		}
	}
	
	public void generarInformes(OutputStream out, Set<String> junctions, 
			Set<String> roads, Set<String> vehicles) throws IOException {
		for(Junction j: mapaTrafico.getConstantJunctions()) {
			if(junctions == null || junctions != null && junctions.contains(j.identificador)) {
				LinkedHashMap<String, String> reporte = new LinkedHashMap<>();
				j.generarInforme(tickActual, reporte);
				writeReport(reporte, out);
			}
		}
		
		for(Road j: mapaTrafico.getConstantRoads()) {
			if(roads == null || roads != null && roads.contains(j.identificador)) {
				LinkedHashMap<String, String> reporte = new LinkedHashMap<>();
				j.generarInforme(tickActual, reporte);
				writeReport(reporte, out);
			}
		}
		
		for(Vehicle j: mapaTrafico.getConstantVehicles()) {
			if(vehicles == null || vehicles != null && vehicles.contains(j.identificador)) {		
				LinkedHashMap<String, String> reporte = new LinkedHashMap<>();
				j.generarInforme(tickActual, reporte);
				writeReport(reporte, out);
			}
		}
	}
	
	public void writeReport(Map<String, String> report, OutputStream out) throws IOException {
		for(Map.Entry<String,String> campo: report.entrySet()) {
			if(campo.getKey().equals("")) {
				String aux = campo.getValue()  + "\n";
				out.write(aux.getBytes());
				
			} else {
				String aux = campo.getKey() + " = " + campo.getValue() + "\n";
				out.write(aux.getBytes());
			}
		}			
		out.write("\n".getBytes());
		out.flush();
	}
	
	/**
	 * Función que carga los eventos en el simulador antes de ejecutarlos
	 * @param nTicksAEjecutar - Número de ticks a partir del actual de los que se cargarán eventos
	 * @param eventosCargados - Array con los eventos que había introducido el usuario
	 */
	public void cargarEventos(ArrayList<Event> eventosCargados) {
		//En primer lugar limpiamos la lista de eventos
		listaEventos = new MultiTreeMap<>();

		for(Event e: eventosCargados) {	
			if(e.time >= tickActual) {
				listaEventos.putValue(e.time,e);
			}
			fireUpdateEvent(EventType.NEW_EVENT, "Ha ocurrido un error al insertar un evento");
		}
	}
	
	public void modifyOutputStream(OutputStream os) {
		out = os;
	}
	
	public void reset() {
		//primerTick = true;
		listaEventos = new MultiTreeMap<>();
		tickActual = 0;
		mapaTrafico = new RoadMap();
		fireUpdateEvent(EventType.RESET, "Ha ocurrido un error durante la ejecución del reset");
	}
	
	public int getTime() {
		return tickActual;
	}
	
	public List<Junction> getJunctions() {
		return mapaTrafico.getJunctions();
	}
	
	public List<Road> getRoads() {
		return mapaTrafico.getRoads();
	}
	
	public List<Vehicle> getVehicles() {
		return mapaTrafico.getVehicles();
	}
	
	public Boolean hayEventosCargados() {
		return listaEventos.size() > 0 ? true : false;
	}
	
	public interface Listener {
		void registered(UpdateEvent ue);
		void reset(UpdateEvent ue);
		void newEvent(UpdateEvent ue);
		void advanced(UpdateEvent ue);
		void error(UpdateEvent ue, String error);
	}
	
	private List<Listener> listeners = new ArrayList<>();
	
	public void addSimulatorListener(Listener l) {
		listeners.add(l);
		UpdateEvent ue = new UpdateEvent(EventType.REGISTERED);
		// evita pseudo-recursividad
		SwingUtilities.invokeLater(()->l.registered(ue));
	}
	
	public void removeListener(Listener l) {
		listeners.remove(l);
	}
	
	public void removeFirstListener() {
		listeners.remove(listeners.get(0));
	}
	
	public int getListenersSize() {
		return listeners.size();
	}
	
	//Uso interno, evita tener que escribir el mismo bucle muchas veces
	private void fireUpdateEvent(EventType type, String error) {
		UpdateEvent ue = new UpdateEvent(type);
		for(Listener l: listeners) {
			if(type == EventType.REGISTERED) {
				l.registered(ue);
			} else if (type == EventType.RESET) {
				l.reset(ue);
			} else if (type == EventType.NEW_EVENT) {
				l.newEvent(ue);
			} else if (type == EventType.ADVANCED) {
				l.advanced(ue);
			} else if (type == EventType.ERROR) {
				l.error(ue, error);
			}
		}
	}
	
	public enum EventType {
		REGISTERED,
		RESET,
		NEW_EVENT,
		ADVANCED,
		ERROR
	}
	
	public class UpdateEvent {
		private EventType tipoEvento;
		
		public UpdateEvent(EventType e){
			tipoEvento = e;
		}
		
		public EventType getEvent(){
			return tipoEvento;
		}
		
		public List<Vehicle> getVehicles() {
			return mapaTrafico.getVehicles();
		}
		
		public List<Junction> getJunctions() {
			return mapaTrafico.getJunctions();
		}
		
		public List<Road> getRoads() {
			return mapaTrafico.getRoads();
		}
		
		// Actualizamos la tabla eliminando los eventos ya procesados
		public List<Event> getEventQueue() {
			ArrayList<Event> listaEventosPorProcesar = new ArrayList<Event>();
			for(Event ev: listaEventos.innerValues()) {
				listaEventosPorProcesar.add(ev);
			}
			return listaEventosPorProcesar;
		}
		
		public int getCurrentTime() {
			return tickActual;
		}
	}
}
