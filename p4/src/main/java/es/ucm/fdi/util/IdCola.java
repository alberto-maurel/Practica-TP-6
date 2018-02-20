package es.ucm.fdi.util;

import java.util.Queue;
import java.util.ArrayDeque;
import es.ucm.fdi.model.Vehicle;

public class IdCola {
	private String id;
	private Queue<Vehicle> colaCoches;
	
	public IdCola(){}
	
	public IdCola(String id, Vehicle v) {
		this.id = id;
		colaCoches = new ArrayDeque<Vehicle>();
		colaCoches.offer(v);
	}
	
	//Accedentes
	public Queue<Vehicle> getQueue() {
		return colaCoches;
	}
	
	public void sacarCoche() {
		colaCoches.poll();
	}
	
	public void meterCoche(Vehicle v) {
		colaCoches.offer(v);
	}
}
