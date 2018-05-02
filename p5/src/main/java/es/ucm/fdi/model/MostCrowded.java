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
			}
			else if(colasCoches.get(carreteraAct).isEmpty() &&
 					colasCoches.get(carreteraAct).size() > maxActual && !id.equals(carreteraAct)) {
				idAct = carreteraAct;
				maxActual = 0;
			} else if (carreterasEntrantesOrdenadas.size() == 1) idAct = id;
		}
		return idAct;
	}

	private String buscarCarreteraAtascadaIni() {
		int maxActual = -1;
		String idAct = "";
	
		for (String carreteraAct: carreterasEntrantesOrdenadas) {
			if(!colasCoches.get(carreteraAct).isEmpty() &&
					colasCoches.get(carreteraAct).size() > maxActual) {
				idAct = carreteraAct;
				maxActual = colasCoches.get(carreteraAct).size();
			}
			else if(colasCoches.get(carreteraAct).isEmpty() &&
						colasCoches.get(carreteraAct).size() > maxActual) {
				idAct = carreteraAct;
				maxActual = 0;
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
			carreteraAtascada = buscarCarreteraAtascadaIni();
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
			if(colasCoches.size() > 0 && colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)) != null && 
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
	
	protected String toStringRoad(int index) {
		StringBuilder sb = new StringBuilder();
		if(index < carreterasEntrantesOrdenadas.size()) {
			sb.append('(');
			//En primer lugar añadimos el identificador de la carretera
			sb.append(carreterasEntrantesOrdenadas.get(index));
			sb.append(',');
			//Ahora chequeamos de qué color está el semáforo
			if (index == semaforoVerde) {
				sb.append("green:"+ Integer.toString(intervaloDeTiempo - unidadesDeTiempoUsadas));
			} else {
				sb.append("red");
			}
			sb.append(",[");
			//And now we add all the cars
			for(Vehicle v: colasCoches.get(carreterasEntrantesOrdenadas.get(index))) {
				sb.append(v.identificador + ',');
			}
			if(colasCoches.get(carreterasEntrantesOrdenadas.get(index)).size() > 0) {			
				sb.setLength(sb.length()-1);
			}
			if(colasCoches.get(carreterasEntrantesOrdenadas.get(index)).size() != 0) {
				carreterasEntrantesOrdenadas.get(index);
			}
			sb.append("])");
		}
		return sb.toString();
	}
	
	//TODO: preguntar a Freire como podríamos eliminar esto
	public void describe(Map<String,String> out) {
		out.put("ID", identificador);
		
		StringBuilder greenOutput = new StringBuilder();
		greenOutput.append('[');
		greenOutput.append(toStringRoad(semaforoVerde));
		greenOutput.append(']');
		out.put("Green", greenOutput.toString());
		
		StringBuilder redOutput = new StringBuilder();
		redOutput.append('[');
		for(int i = 0; i < carreterasEntrantesOrdenadas.size(); ++i) {
			if(i != semaforoVerde) {
				redOutput.append(toStringRoad(i));
				redOutput.append(',');
			}
		}
		if (redOutput.length() > 1) redOutput.deleteCharAt(redOutput.length() - 1);
		redOutput.append(']');
		out.put("Red", redOutput.toString());
	}
}
