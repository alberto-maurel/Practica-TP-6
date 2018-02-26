package es.ucm.fdi.model;

import java.util.Map;

public abstract class SimulatedObject {
	protected String identificador;
	
	void avanza() {}
	void generarInforme(int time, Map<String, String> out) {
		out.put("", getReportHeader());
		out.put("id", identificador);
		out.put("time", String.valueOf(time));
		fillReportDetails(out);
	}
	
	protected abstract void fillReportDetails(Map<String, String> out);
	protected abstract String getReportHeader();
}
