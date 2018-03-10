package es.ucm.fdi.model;

import java.util.ArrayList;

import es.ucm.fdi.util.MultiTreeMap;

public class Path extends Road{
	public Path(){}
	
	public Path(String id, int longitud, int maxVel, Junction src, Junction dest){
		super(id, longitud, maxVel, src, dest);
	}
		
	protected int calcularVelocidadBase(int velocidadMaxima) {
		return velocidadMaxima;
	}
	
	protected int calcularFactorReduccion(int nVehiculosAveriados) {
		return 1 + nVehiculosAveriados;
	}
}
