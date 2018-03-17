package es.ucm.fdi.model;

import java.util.ArrayList;
import java.util.Map;

public class Bike extends Vehicle {
	
	public Bike(String id, int velMaxima, ArrayList<Junction> itinerario) {
		super(id, velMaxima, itinerario);
	}
	
	
	public void setTiempoAveria(int averia) {
		if(velActual >= velMaxima/2) {		
			tiempoAveria += averia;
		}
	}
	
	protected void fillReportDetails(Map<String, String> out) {
		out.put("type", "bike");
		super.fillReportDetails(out);
	}
	
}
