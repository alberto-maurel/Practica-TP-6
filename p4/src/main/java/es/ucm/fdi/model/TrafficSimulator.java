package es.ucm.fdi.model;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TrafficSimulator {

	private int indiceActualEventos;
	private ArrayList<Event> listaEventos;
	private int tick;
	private RoadMap mapaTrafico;
	private OutputStream out;
	

	public TrafficSimulator(OutputStream out) {
		indiceActualEventos = 0;
		this.listaEventos = new ArrayList<>();
		tick = 0;
		mapaTrafico = new RoadMap();
		this.out = out;
	}
	
	public class SortbyTime implements Comparator<Event>{
	    public int compare(Event a, Event b) {
	        return a.time - b.time;
	    }
	}
	
	//Métodos
	public void run(int time){
		//Before running the simulation we ensure that all the events are sorted by it's starting time
		
		Collections.sort(listaEventos, new SortbyTime());
		
		while (tick < time) {
		try {
			//En primer lugar carga los eventos correspondientes a dicho tick
			while(indiceActualEventos < listaEventos.size() && listaEventos.get(indiceActualEventos).time == tick){
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
			
			++tick;
			
			//Hacer refactoring de estas 3 cosas
			for(Junction j: mapaTrafico.getConstantJunctions()) {
				LinkedHashMap<String, String> reporte = new LinkedHashMap<>();
				j.generarInforme(tick, reporte);
				writeReport(reporte, out);
			}
			
			for(Road j:mapaTrafico.getConstantRoads()) {
				LinkedHashMap<String, String> reporte = new LinkedHashMap<>();
				j.generarInforme(tick, reporte);
				writeReport(reporte, out);
			}
			
			for(Vehicle j:mapaTrafico.getConstantVehicles()) {
				LinkedHashMap<String, String> reporte = new LinkedHashMap<>();
				j.generarInforme(tick, reporte);
				writeReport(reporte, out);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
		
	}
	
	public void writeReport(Map<String, String> report, OutputStream out) throws IOException{
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
	public void insertaEvento(Event evento) throws SimulationException{
		//Comprobamos si el evento se ejecuta en un ciclo que no haya ocurrido ya
		if(evento.time >= tick) {
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
	
	/**
	 * Para tests jUnit
	 * @return nº de eventos en la simulación
	 */
	public int numberOfEvents() {
		return listaEventos.size();
	}

	
	/**
	 * Ayudita
	 * en el simulador principal, en Run,
	 * 
	 * para cada tick a simular
	 *    - ejecuto eventos del tick
	 *    - avanzo carreteras
	 *       - y cada una avanza sus coches, tras darles velocidad
	 *    - avanzo cruces
	 *    - escribo informes
	 * 
	 * 
	 * 
	 * 
	 */
	
}
