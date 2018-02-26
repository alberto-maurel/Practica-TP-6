package es.ucm.fdi.model;

import java.util.ArrayList;

public class Vehicle extends SimulatedObjects{
	private int velMaxima;
	private int velActual;
	private Road carreteraActual;
	protected int localizacionCarretera;
	private ArrayList<String> itinerario; //Itinerario - Representado como un vector con los identificadores de las carreteras que hay que recorrer 
	private int indItinerario;   		  //y un contador que nos indica en cual nos encontramos
	private int tiempoAveria;
	
	
	public Vehicle() {
		
	}
	
	//Métodos
	void avanza() {
		if(tiempoAveria > 0) --tiempoAveria;
		else {
			
		}
		
	}
	
	
	
	void moverASiguienteCarretera() {
		
		
		//Informamos a la carretera de que la hemos dejado
		carreteraActual.saleVehiculo();
		
		//Cambiamos de carretera
		
		//Y lo colocamos al principio
		localizacionCarretera = 0;
	}
	
	/**
	 * Incrementa el tiempo que queda para que el coche deje de estar averiado
	 * @param av Tiempo que tarda en repararse la avería que acaba de suceder
	 */
	public void setTiempoAveria(int av) {
		tiempoAveria += av;
	}
	
	/**
	 * Modifica la velocidad actual del coche
	 * @param av Velocidad actual del coche
	 */
	public void setVelocidadActual(int vel) {
		if(vel < velMaxima) {
			velActual = vel;
		}
		else {
			velActual = velMaxima;
		}
	}

	/**
	 * Indica si un coche está averiado o no
	 * @return True si el coche está averiado, false si no
	 */
	public boolean cocheAveriado() {
		if(tiempoAveria == 0) return false;
		else return true;
	}
}
