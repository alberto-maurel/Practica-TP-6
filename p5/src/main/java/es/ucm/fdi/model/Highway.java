package es.ucm.fdi.model;

import java.util.Map;

public class Highway extends Road {
  
	protected int lanes;
	
	public Highway(String id, int longitud, int maxVel, Junction src, Junction dest, int lanes) {
		super(id, longitud, maxVel, src, dest);
		this.lanes = lanes;
	}
	
	protected int calcularVelocidadBase(int velocidadMaxima) {
		int aux1 = Math.max(1 , Math.toIntExact(situacionCarretera.sizeOfValues()));
		int aux2 = (velocidadMaxima * lanes)/aux1;
		return Math.min(velocidadMaxima, aux2 + 1);
	}
	
	protected int calcularFactorReduccion(int nVehiculosAveriados) {
		if(lanes > nVehiculosAveriados) return 1;
		else return 2;
	}
	
	protected void fillReportDetails(Map<String, String> out) {
		out.put("type", "lanes");
		super.fillReportDetails(out);
	}
}
