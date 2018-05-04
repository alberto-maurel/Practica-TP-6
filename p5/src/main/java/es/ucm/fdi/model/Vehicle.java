package es.ucm.fdi.model;

import java.util.ArrayList;
import java.util.Map;

public class Vehicle extends SimulatedObject implements Describable {
	
	protected final int arrived = -1;	
	protected int velMaxima;
	protected int velActual;
	protected Road carreteraActual;
	protected int localizacionCarretera;
	protected ArrayList<Junction> itinerario; 
	protected int indItinerario;  //Contador que nos indica en qué junction nos encontramos
	protected int tiempoAveria;
	protected int kilometrage;
	
	public Vehicle(String id, int velMaxima, ArrayList<Junction> itinerario) {
		super(id);
		this.velMaxima = velMaxima;
		this.velActual = 0;
		this.itinerario = itinerario;
		//Indicamos al coche cual es su carretera
		carreteraActual = itinerario.get(0).buscarCarretera(itinerario.get(1));
		//E introducimos el coche en la carretera
		carreteraActual.entraVehiculo(this);
		localizacionCarretera = 0;
		indItinerario = 1;
		tiempoAveria = 0;
		kilometrage = 0;
	}
	
	public int getPosicionActual() {
		return localizacionCarretera;
	}
	
	public String getIdCarreteraAct() {
		return carreteraActual.identificador;
	}
	
	/**
	 * Avanza el estado actual del coche
	 */
	public void avanza() {
		//Si está averiado
		if(tiempoAveria > 0) {
			--tiempoAveria;
		} else { //Si no está averiado
			//Sacamos el coche de la carretera
			carreteraActual.situacionCarretera.removeValue(localizacionCarretera, this);
			
			//Procesamos el vehículo
			//Si el vehículo no llega en este tick al final de la carretera
			if(localizacionCarretera + velActual < carreteraActual.longitud) {
				avanzarCocheSinLlegarAlFinal();
				
				//El coche entra en la intersección (pero aún no estaba)
			} else if (localizacionCarretera != carreteraActual.longitud) { 
				avanzarCocheLlegandoAlFinal();
				
				//El coche ya estaba en la intersección
			} else { 
				velActual = 0;
				
			}
			//Y volvemos a meter el coche en la posición en la que debería ir
			carreteraActual.situacionCarretera.putValue(localizacionCarretera, this);
		}	
	}
	
	protected void avanzarCocheSinLlegarAlFinal() {
		localizacionCarretera += velActual;
		kilometrage += velActual;
	}
	
	protected void avanzarCocheLlegandoAlFinal() {
		kilometrage += (carreteraActual.longitud - localizacionCarretera);
		localizacionCarretera = carreteraActual.longitud;
		velActual = 0;
		carreteraActual.cruceFin.entraVehiculo(this);
	}
	
	/**
	 * Pasamos el vehículo desde la carretera actual a la siguiente en el itinerario
	 */
	public void moverASiguienteCarretera() {
		//Informamos a la carretera de que la hemos dejado
		carreteraActual.saleVehiculo(this);
		
		//Cambiamos de carretera
		//Buscamos cuál es el siguiente cruce
		Junction cruceActual = itinerario.get(indItinerario);
		++indItinerario;
		//Si ya hemos llegado al final marcamos como llegado
		if(indItinerario == itinerario.size()){
			localizacionCarretera = arrived;
			
		//Si no hemos llegado aún a la última intersección
		} else { 
			
			//Buscamos que carretera va de un cruce al otro
			Junction siguienteCruce = itinerario.get(indItinerario);
			carreteraActual = cruceActual.buscarCarretera(siguienteCruce);
			
			//Y lo colocamos al principio
			localizacionCarretera = 0;
			carreteraActual.entraVehiculo(this);
		}
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
		if(localizacionCarretera == arrived) {
			out.put("location", "arrived");
		} else {
			out.put("location", "(" + carreteraActual.identificador + "," + localizacionCarretera + ")");
		}
	}	
	
	public void describe(Map<String,String> out, String rowIndex) {
		out.put("ID", identificador);
		out.put("Road", carreteraActual.identificador);
		if(localizacionCarretera == arrived) out.put("Location", "Llegado");
		else out.put("Location", "" + localizacionCarretera);
		out.put("Speed", "" + velActual);
		out.put("Km", "" + kilometrage);
		out.put("Faulty units", "" + tiempoAveria);
		out.put("Itinerary", toStringItinerary());
	}	
	
	public String toStringItinerary() {
		String s = "[";
		for(int i = 0; i < itinerario.size(); ++i) {
			if(i == itinerario.size() - 1) {
				s += itinerario.get(i).identificador + "]";
			} else {
				s += itinerario.get(i).identificador + ", ";
			}
		}
		return s;
	}
}
