package es.ucm.fdi.model;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TrafficSimulator {

	private int indiceActualEventos;
	private ArrayList<Event> listaEventos;
	private int tick;
	private RoadMap mapaTrafico;
	
	
	//Métodos
	public void run(){	
		//En primer lugar carga los eventos correspondientes a dicho tick
		while(listaEventos.get(indiceActualEventos).time == tick){
			listaEventos.get(indiceActualEventos).execute();
			++indiceActualEventos;
		}
		
		//Ahora avanzo cada una de las carreteras (y ellas a su vez hacen avanzar a los coches)
		for(Road r: mapaTrafico.roads) {
			r.avanza();
		}
		
		//Avanzamos los cruces
		for(Junction j: mapaTrafico.junctions) {
			j.avanza();
		}
		
		//Y por último escribimos los informes en el orden indicado
		//OutputStream out = 
		for(Junction j:mapaTrafico.junctions) {
			Map<String, String> reporte = new HashMap<>();
			j.generarInforme(tick, reporte);
			writeReport(reporte, out); //Añadir el outputStream
		}
	}
	
	public void writeReport(Map<String, String> report, OutputStream out) {
		for(Map.Entry<String,String> campo: report.entrySet()) {
			System.out.println(campo.getKey() + " = " + campo.getValue());
		}
	}
	
	/**
	 * Inserta el evento de forma ordenada según el momento en el que se produce
	 * @param evento
	 */
	public void insertaEvento(Event evento) {
		//Comprobamos si el evento se ejecuta en un ciclo que no haya ocurrido ya
		if(evento.time >= tick) {
			//Recorremos el array buscando donde insertarlo y hacemos lo propio
			int posInsertar = 0;
			while(posInsertar < listaEventos.size() && listaEventos.get(posInsertar).time <= evento.time) {
				++posInsertar;
			}
			listaEventos.add(posInsertar, evento);
		}
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
