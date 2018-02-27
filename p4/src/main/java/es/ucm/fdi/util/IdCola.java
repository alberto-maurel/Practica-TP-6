package es.ucm.fdi.util;

import java.util.Queue;
import java.util.ArrayDeque;
import es.ucm.fdi.model.Vehicle;

/**
 * Pares de id y colas que representan el final de las distintas carreteras que entran en un cruce
 * @author Alberto
 *
 */
public class IdCola {
	//Atributos
	private String id;
	private Queue<Vehicle> colaCoches;
	
	//Constructores
	public IdCola(String id){
		this.id = id;
		colaCoches = new ArrayDeque<Vehicle>();
	}
	
	public IdCola(String id, Vehicle v) {
		this.id = id;
		colaCoches = new ArrayDeque<Vehicle>();
		colaCoches.offer(v);
	}
	
	//Métodos
	/*
	public Queue<Vehicle> getQueue() {
		return colaCoches;
	}
	*/
	
	public String getId() {
		return this.id;
	}
	
	/**
	 * Nos indica si hay algún coche esperando en el cruce
	 * @return True si el cruce está vacío, false si no
	 */
	public boolean cruceVacio() {
		return colaCoches.isEmpty();
	}
	
	public Vehicle sacarCoche() {
		return colaCoches.poll();
	}
	
	/**
	 * Coloca el coche al final de la cola de espera para cruzar el semáforo
	 * @param Vehículo
	 */
	public void meterCoche(Vehicle v) {
		colaCoches.offer(v);
	}
}
