package es.ucm.fdi.model;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class SimulatedObject {
	
	protected String identificador;
	
	
	public SimulatedObject() {
		identificador = "UnknownId";
	}
	
	public SimulatedObject(String id){
		identificador = id;
	}
	
	public String getId() {
		return identificador;
	}
	
	void avanza() {}
	
	void generarInforme(int time, LinkedHashMap<String, String> out) {
		out.put("", getReportHeader());
		out.put("id", identificador);
		out.put("time", String.valueOf(time));
		fillReportDetails(out);
	}
	
	protected abstract String getReportHeader();
	
	protected abstract void fillReportDetails(Map<String, String> out);
	
	
}
