package es.ucm.fdi.model;

import java.util.ArrayList;

public class TrafficSimulator {

	private int indiceActualEventos;
	private ArrayList<Event> eventos;
	
	private int tick;
	
	private RoadMap mapaTrafico;
	
	public void run(){	
		//En primer lugar carga los eventos correspondientes a dicho tick
		while(eventos.get(indiceActualEventos).time == tick){
			eventos.get(indiceActualEventos).execute();
			++indiceActualEventos;
		}
		
		
	}
	
	
	
	//Posteriormente hace avanzar a los objetos
	
	
	/**
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
