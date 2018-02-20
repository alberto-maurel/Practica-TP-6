package es.ucm.fdi.model;

import java.util.ArrayList;

public class Vehicle extends SimulatedObjects{
	int velMaxima;
	int velActual;
	Road carreteraActual;
	int localizacionCarretera;
	ArrayList<String> itinerario; //Itinerario - Representado como un vector con los identificadores de las carreteras que hay que recorrer 
	int indItinerario;   		  //y un contador que nos indica en cual nos encontramos
	int averia;
	
	public Vehicle() {
		
	}
	
	void moverASiguienteCarretera() {
		
	}
	
	void setTiempoAveria(int av) {
		averia += av;
	}
	
	void setVelocidadActual(int vel) {
		velActual = vel;
	}
}
