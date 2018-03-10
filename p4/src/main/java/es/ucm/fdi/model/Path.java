package es.ucm.fdi.model;

public class Path extends Road {	
	
	protected int calcularFactorReduccion(int nVehiculosAveriados) {
		return 1 + nVehiculosAveriados;
	}
	
}
