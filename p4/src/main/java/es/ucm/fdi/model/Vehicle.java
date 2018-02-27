package es.ucm.fdi.model;

import java.util.ArrayList;
import java.util.Map;

public class Vehicle extends SimulatedObject {
	private int velMaxima;
	private int velActual;
	protected Road carreteraActual;
	protected int localizacionCarretera;
	private ArrayList<Junction> itinerario; 
	private int indItinerario;   		  //y un contador que nos indica en cual nos encontramos
	private int tiempoAveria;
	private int kilometrage;
	
	//Constructores
	public Vehicle() {}
	
	public Vehicle(String id, int velMaxima, ArrayList<Junction> itinerario) {
		super(id);
		this.velMaxima = velMaxima;
		this.velActual = 0;
		this.itinerario = itinerario;
		//carreteraActual = itinerario.get(0);
		localizacionCarretera = 0;
		indItinerario = 0;
		tiempoAveria = 0;
		kilometrage = 0;
	}
	
	
	//Métodos
	/**
	 * Avanza el estado actual del coche
	 */
	void avanza() {
		if(tiempoAveria > 0) {
			--tiempoAveria;
		} else {
			if(localizacionCarretera + velActual < carreteraActual.longitud) {
				localizacionCarretera += velActual;
				kilometrage += velActual;
			} else {
				kilometrage += (carreteraActual.longitud - localizacionCarretera);
				localizacionCarretera = carreteraActual.longitud;				
				carreteraActual.cruceFin.entraVehiculo(this);
			}
		}	
	}
	
	/**
	 * Pasamos el vehículo desde la carretera actual a la siguiente en el itinerario
	 */
	void moverASiguienteCarretera() {
		//Informamos a la carretera de que la hemos dejado
		carreteraActual.saleVehiculo(this);
		
		//Cambiamos de carretera
		Junction cruceActual = itinerario.get(indItinerario);
		++indItinerario;
		//Para ello buscamos que carretera va de un cruce al otro
		Junction siguienteCruce = itinerario.get(indItinerario);
		carreteraActual = cruceActual.buscarCarretera(siguienteCruce);
		
		//Y lo colocamos al principio
		localizacionCarretera = 0;
	}
	
	/**
	 * Incrementa el tiempo que queda para que el coche deje de estar averiado
	 * @param averia Tiempo que tarda en repararse la avería que acaba de suceder
	 */
	public void setTiempoAveria(int averia) {
		tiempoAveria += averia;
	}
	
	/**
	 * Modifica la velocidad actual del coche
	 * @param av Velocidad actual del coche
	 */
	public void setVelocidadActual(int velocidad) {
		if(velocidad < velMaxima) {
			velActual = velocidad;
		}
		else {
			velActual = velMaxima;
		}
	}

	/**
	 * Indica si un coche está averiado o no
	 * @return True si el coche está averiado, false si no
	 */
	public boolean isCocheAveriado() {
		return tiempoAveria > 0;
	}
	
	protected String getReportHeader() {
		return "[vehicle_report]";
	}
	
	protected void fillReportDetails(Map<String, String> out) {
		out.put("speed", String.valueOf(velActual));
		out.put("kilometrage", String.valueOf(kilometrage));
		out.put("faulty", String.valueOf(tiempoAveria));
		out.put("location", "(" + carreteraActual.identificador + ", " + localizacionCarretera + ")");
	}
}
