package es.ucm.fdi.model;

import java.util.Map;
import java.util.Queue;

/* - Creo que no actualiza el semáforo según el orden de adición de las carreteras
 * - fillReportDetails
 * 
 */

public class MostCrowded extends Junction {
	
	private int intervaloDeTiempo;
	private int unidadesDeTiempoUsadas;
	
	
	public MostCrowded() {
		intervaloDeTiempo = 0;
		unidadesDeTiempoUsadas = 0;
	}
	
	
	// No sé si hay una forma más fácil de hacer esto xD
	private String buscarCarreteraAtascada(String id) {
		int maxActual = 0;
		String idAct = "";
		for (Map.Entry<String, Queue<Vehicle>> entry : colasCoches.entrySet()) {
		   if (entry.getValue().size() > maxActual && entry.getKey() != id) {
			   idAct = entry.getKey();
		   }
		}
		return idAct;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public void actualizarSemaforo() {
		if (unidadesDeTiempoUsadas == intervaloDeTiempo) {
			String carreteraAtascada = buscarCarreteraAtascada(carreterasEntrantesOrdenadas.get(semaforoVerde));
			semaforoVerde = carreterasEntrantesOrdenadas.indexOf(carreteraAtascada);
			intervaloDeTiempo = Math.max(colasCoches.get(semaforoVerde).size() / 2, 1);
			unidadesDeTiempoUsadas = 0;
		}
	}
	
	protected void fillReportDetails(Map<String, String> out) {
		
	}
	
	
}
