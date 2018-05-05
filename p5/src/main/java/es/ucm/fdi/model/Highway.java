package es.ucm.fdi.model;

import java.util.Map;

public class Highway extends Road {
  
	protected int lanes;
	
	public Highway(String id, int longitud, int maxVel, Junction src, Junction dest, int lanes) {
		super(id, longitud, maxVel, src, dest);
		this.lanes = lanes;
	}
	
	protected int calcularVelocidadBase(int velocidadMaxima) {
		int totalCochesEnCarretera = (int)situacionCarretera.sizeOfValues();
		return Math.min(velocidadMaxima,
			1 + (velocidadMaxima * lanes) / Math.max(1, totalCochesEnCarretera));
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
