package es.ucm.fdi.model;

import java.util.List;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

public class TrafficSimulator {

	private int indiceActualEventos;
	//La lista de eventos solo contendrá los eventos que se hayan cargado y que estén dentro de los ticks de la simulación
	//que se van a ejecutar
	private ArrayList<Event> listaEventos; 
	private int tickActual;
	private RoadMap mapaTrafico;
	private OutputStream out;
	
	public TrafficSimulator(OutputStream out) {
		indiceActualEventos = 0;
		this.listaEventos = new ArrayList<>();
		tickActual = 0;
		mapaTrafico = new RoadMap();
		this.out = out;
		fireUpdateEvent(EventType.RESET, "Ha ocurrido un error durante la construcción del simulador");
	}
	
	public class SortbyTime implements Comparator<Event> {
	    public int compare(Event a, Event b) {
	        return a.time - b.time;
	    }
	}
		
	//Métodos
	public void run(int time) throws SimulationException {
		int tick = 0;
		//Before running the simulation we ensure that all the events are sorted by it's starting time		
		Collections.sort(listaEventos, new SortbyTime());
		
		try {
			while (tick < time) {
				//En primer lugar carga los eventos correspondientes a dicho tick
				while(indiceActualEventos < listaEventos.size() && listaEventos.get(indiceActualEventos).time == tickActual) {
					listaEventos.get(indiceActualEventos).execute(mapaTrafico);
					++indiceActualEventos;
				}
				
				//Ahora avanzo cada una de las carreteras (y ellas a su vez hacen avanzar a los coches)
				for(Road r: mapaTrafico.getRoads()) {
					r.avanza();
				}
				
				//Avanzamos los cruces
				for(Junction j: mapaTrafico.getJunctions()) {
					j.avanza();
				}
				
				//Y por último escribimos los informes en el orden indicado
				fireUpdateEvent(EventType.ADVANCED, "Ha ocurrido un error al ejecutar la simulación");
				++tick;
				++tickActual;				
				generarInformes(out);
				
			}		
		} catch (Exception e) {
			throw new SimulationException(/*e.getMessage()*/);
			//System.out.println("Error producido en traffic simulator");
		}		
	}
	
	public void generarInformes(OutputStream out) throws IOException {
		for(Junction j: mapaTrafico.getConstantJunctions()) {
			LinkedHashMap<String, String> reporte = new LinkedHashMap<>();
			j.generarInforme(tickActual, reporte);
			writeReport(reporte, out);
		}
		
		for(Road j:mapaTrafico.getConstantRoads()) {
			LinkedHashMap<String, String> reporte = new LinkedHashMap<>();
			j.generarInforme(tickActual, reporte);
			writeReport(reporte, out);
		}
		
		for(Vehicle j:mapaTrafico.getConstantVehicles()) {
			LinkedHashMap<String, String> reporte = new LinkedHashMap<>();
			j.generarInforme(tickActual, reporte);
			writeReport(reporte, out);
		}
	}
	
	public void writeReport(Map<String, String> report, OutputStream out) throws IOException {
		try {
			for(Map.Entry<String,String> campo: report.entrySet()) {
				if(campo.getKey().equals("")) {
					String aux = campo.getValue()  + "\n";
					out.write(aux.getBytes());
					
				} else {
					String aux = campo.getKey() + " = " + campo.getValue() + "\n";
					out.write(aux.getBytes());
				}
			}
		}
		catch(IOException io) {
			throw io;
		}
		out.write("\n".getBytes());
		out.flush();
	}
	
	/**
	 * Inserta el evento de forma ordenada según el momento en el que se produce
	 * @param evento
	 */
	public void insertaEvento(Event evento) throws SimulationException {
		//Comprobamos si el evento se ejecuta en un ciclo que no haya ocurrido ya
		if(evento.time >= tickActual) {
			//Recorremos el array buscando donde insertarlo y hacemos lo propio
			int posInsertar = 0;
			while(posInsertar < listaEventos.size() && listaEventos.get(posInsertar).time <= evento.time) {
				++posInsertar;
			}
			listaEventos.add(posInsertar, evento);
		}
		else {
			throw new SimulationException("Se ha añadido un evento en un momento posterior a su ejecución");
		}
	}
	
	public void modifyOutputStream(OutputStream os) {
		out = os;
	}
	
	public void reset() {
		indiceActualEventos = 0;
		tickActual = 0;
		mapaTrafico = new RoadMap();
		fireUpdateEvent(EventType.RESET, "Ha ocurrido un error durante la ejecución del reset");
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
	
	//Uso interno, evita tener que escribir el mismo bucle muchas veces
	private void fireUpdateEvent(EventType type, String error) {
		UpdateEvent ue = new UpdateEvent(type);
		for(Listener l: listeners) {
			if(type == EventType.REGISTERED) {
				l.registered(ue);
			}
			else if (type == EventType.RESET) {
				l.reset(ue);
			}
			else if (type == EventType.NEW_EVENT) {
				l.newEvent(ue);
			}
			else if (type == EventType.ADVANCED) {
				l.advanced(ue);
			}
			else if (type == EventType.ERROR) {
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
		
		public List<Vehicle> getVehicles(){
			return mapaTrafico.getVehicles();
		}
		
		public List<Junction> getJunctions(){
			return mapaTrafico.getJunctions();
		}
		
		public List<Road> getRoads(){
			return mapaTrafico.getRoads();
		}
		
		// Actualizamos la tabla eliminando los eventos ya procesados
		public List<Event> getEventQueue() {
			ArrayList<Event> listaEventosPorProcesar = new ArrayList<Event>();
			for (int i = indiceActualEventos; i < listaEventos.size(); ++i) {
				listaEventosPorProcesar.add(listaEventos.get(i));
			}
			return listaEventosPorProcesar;
		}
		
		public int getCurrentTime() {
			return tickActual;
		}
	}
	
	public int getTime() {
		return tickActual;
	}
	
	/**
	 * Función que carga los eventos en el simulador antes de ejecutarlos
	 * @param nTicksAEjecutar - Número de ticks a partir del actual de los que se cargarán eventos
	 * @param eventosCargados - Array con los eventos que había introducido el usuario
	 */
	public void cargarEventos(int nTicksAEjecutar, ArrayList<Event> eventosCargados) {
		//En primer lugar limpiamos la lista de eventos
		listaEventos = new ArrayList<>();
		indiceActualEventos = 0;
		//Ahora añadimos los eventos del fichero que deberían ocurrir en los ticks que pensamos simular
		//(los ticks que marca el spinner en este momento)
		//Como no tenemos garantizado que los eventos estén ordenados, hacemos un sort del array de eventos,
		//puesto que posteriormente necesitaremos tenerlos ordenados para usarlos en el run
		
		eventosCargados.sort((Event e1, Event e2) -> e1.time - e2.time);
		
		for(Event e: eventosCargados) {
			if(e.time >= tickActual && e.time < tickActual + nTicksAEjecutar) {
				listaEventos.add(e);
			} else if (e.time > tickActual + nTicksAEjecutar) {
				//Como están ordenados, si el tick de dicho elemento es mayor a el último tick que vamos 
				//a ejecutar nos podemos salir
				break;
			}
			
			//TODO: revisar si está bien usado aquí el listener
			fireUpdateEvent(EventType.NEW_EVENT, "Ha ocurrido un error al insertar un evento");
		}
	}
	
	public Boolean hayEventosCargados() {
		return listaEventos.size() > 0 ? true : false;
	}
	
}
