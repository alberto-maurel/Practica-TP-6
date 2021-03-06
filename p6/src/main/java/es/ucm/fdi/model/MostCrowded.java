package es.ucm.fdi.model;

import java.util.Map;

public class MostCrowded extends Junction implements Describable{
	private int intervaloDeTiempo;
	private int unidadesDeTiempoUsadas;
	private boolean cambioDeSemaforoEsteTurno = false;
  
  
	public MostCrowded(String id) {
		super(id);
		intervaloDeTiempo = 0;
		unidadesDeTiempoUsadas = 0;
	}
	
	private String buscarCarreteraAtascada(String id){
		int maxActual = -1;
		String idAct = "";
		
		for (String carreteraAct: carreterasEntrantesOrdenadas) {
			if(!colasCoches.get(carreteraAct).isEmpty() &&
 					colasCoches.get(carreteraAct).size() > maxActual && !id.equals(carreteraAct)) {
				idAct = carreteraAct;
				maxActual = colasCoches.get(carreteraAct).size();
			} else if (colasCoches.get(carreteraAct).isEmpty() &&
 					colasCoches.get(carreteraAct).size() > maxActual && !id.equals(carreteraAct)) {
				idAct = carreteraAct;
				maxActual = 0;
			} else if (carreterasEntrantesOrdenadas.size() == 1) {
				idAct = id;
			}
		}
		return idAct;
	}

	public void actualizarSemaforo() {
		String carreteraAtascada;		
		if (semaforoVerde != -1) {
			if (intervaloDeTiempo - unidadesDeTiempoUsadas == 1) {
				carreteraAtascada = buscarCarreteraAtascada(carreterasEntrantesOrdenadas.get(semaforoVerde));
				semaforoVerde = carreterasEntrantesOrdenadas.indexOf(carreteraAtascada);
				if(!carreterasEntrantesOrdenadas.get(semaforoVerde).isEmpty()) {
					intervaloDeTiempo = Math.max(colasCoches.get(carreteraAtascada).size() / 2, 1);
				} else {
					intervaloDeTiempo = 0;
					unidadesDeTiempoUsadas = 0;					
				}
				cambioDeSemaforoEsteTurno = true;
			} else {
				++unidadesDeTiempoUsadas;
				cambioDeSemaforoEsteTurno = false;
			}
		} else {
			carreteraAtascada = buscarCarreteraAtascada("");
			semaforoVerde = carreterasEntrantesOrdenadas.indexOf(carreteraAtascada);
			if(!carreterasEntrantesOrdenadas.get(semaforoVerde).isEmpty()) {
				intervaloDeTiempo = Math.max(colasCoches.get(carreteraAtascada).size() / 2, 1);
			} else {
				intervaloDeTiempo = 0;
				unidadesDeTiempoUsadas = 0;
			}
			cambioDeSemaforoEsteTurno = true;
		}
						
	}
	
	public void avanza() {
		//En primer lugar vemos si la carretera con el semaforo en verde tiene algún coche esperando para pasar
		if (semaforoVerde != -1) {
			if(colasCoches.size() > 0 && 
					colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)) != null && 
				colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)).size() > 0) {
					//En dicho caso sacamos el coche
					Vehicle v = (Vehicle) colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)).poll();
					//Y lo movemos a su siguiente carretera
					v.moverASiguienteCarretera();
			}
		}
		actualizarSemaforo();
	}
	
	protected void fillReportDetails(Map<String, String> out) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < carreterasEntrantesOrdenadas.size(); ++i) {
			sb.append(toStringRoad(i));
			sb.append(',');
		}
		if(sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
		out.put("queues", sb.toString());
		out.put("type", "mc");
	}
	
	protected String toStringGreen() {
		return "green:"+ Integer.toString(intervaloDeTiempo - unidadesDeTiempoUsadas);
	}
	
}