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
	private OutputStream out;
	
	public TrafficSimulator(OutputStream out) {
		indiceActualEventos = 0;
		this.listaEventos = new ArrayList<>();
		tick = 0;
		mapaTrafico = new RoadMap();
		this.out = out;
	}
	
	
	//Métodos
	public void run(int time){
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
			
			//Hacer refactoring de estas 3 cosas
			for(Junction j: mapaTrafico.getConstantJunctions()) {
				Map<String, String> reporte = new HashMap<>();
				j.generarInforme(tick, reporte);
				writeReport(reporte, out); //Añadir el outputStream
			}
			
			for(Road j:mapaTrafico.getConstantRoads()) {
				Map<String, String> reporte = new HashMap<>();
				j.generarInforme(tick, reporte);
				writeReport(reporte, out); //Añadir el outputStream
			}
			
			for(Vehicle j:mapaTrafico.getConstantVehicles()) {
				Map<String, String> reporte = new HashMap<>();
				j.generarInforme(tick, reporte);
				writeReport(reporte, out); //Añadir el outputStream
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		++tick;
	}
		
	}
	
	public void writeReport(Map<String, String> report, OutputStream out) {
		for(Map.Entry<String,String> campo: report.entrySet()) {
			if(campo.getKey().equals("")) {
				System.out.println(campo.getValue());
			} else {
				System.out.println(campo.getKey() + " = " + campo.getValue());
			}
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
