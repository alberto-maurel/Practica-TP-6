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
	
	private boolean cambioDeSemaforoEsteTurno = false;
	
	public MostCrowded(String id) {
		super(id);
		intervaloDeTiempo = 0;
		unidadesDeTiempoUsadas = 0;
	}
	
	private String buscarCarreteraAtascada(String id) {
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
	
	@SuppressWarnings("unlikely-arg-type")
	public void actualizarSemaforo() {
		String semaforoIndex;
		if (unidadesDeTiempoUsadas == intervaloDeTiempo) {
			String carreteraAtascada = buscarCarreteraAtascada(carreterasEntrantesOrdenadas.get(semaforoVerde));
			semaforoVerde = carreterasEntrantesOrdenadas.indexOf(carreteraAtascada);
			if(!carreterasEntrantesOrdenadas.get(semaforoVerde).isEmpty()) 
				intervaloDeTiempo = Math.max(colasCoches.get(carreteraAtascada).size() / 2, 1);
			else intervaloDeTiempo = 1;
			unidadesDeTiempoUsadas = 0;
			cambioDeSemaforoEsteTurno = true;
		}
		else {
			++unidadesDeTiempoUsadas;
			cambioDeSemaforoEsteTurno = false;
		}
	}
	
	public void avanza() {
		//En primer lugar vemos si la carretera con el semaforo en verde tiene algún coche esperando para pasar			
		if(colasCoches.size() > 0 && colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)) != null && 
				colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)).size() > 0) {
					//En dicho caso sacamos el coche
					Vehicle v = (Vehicle) colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)).poll();
					//Y lo movemos a su siguiente carretera
					v.moverASiguienteCarretera();
		}
		actualizarSemaforo();
	}
	
	protected void fillReportDetails(Map<String, String> out) {
		String aux = "";
		for (int i = 0; i < carreterasEntrantesOrdenadas.size(); ++i) {
			aux += "(" + carreterasEntrantesOrdenadas.get(i) + ",";
			
			//El semáforo está correctamente colocado
			if(!cambioDeSemaforoEsteTurno) {
				if(semaforoVerde == i) {
					if(intervaloDeTiempo - unidadesDeTiempoUsadas == 0) {
						aux += "green:1,";
					} else {
						aux += "green:"+ Integer.toString(intervaloDeTiempo - unidadesDeTiempoUsadas) +",";
					}	
				}
				else {
					aux += "red,";
				}
			} else {
				if(i == 0) {
					if(semaforoVerde == carreterasEntrantesOrdenadas.size() - 1) {
						aux += "green:1,";	
					} else {
						aux += "red,";
					}
				}
				else {
					if(semaforoVerde == i + 1) {
						aux += "green:1,";	
					} else {
						aux += "red,";
					}
				}
			}
			
			aux += '[';
			//And now we add all the cars
			for(Vehicle v: colasCoches.get(carreterasEntrantesOrdenadas.get(i))) {
				aux += v.identificador + ',';
			}
			if(colasCoches.get(carreterasEntrantesOrdenadas.get(i)).size() > 0) {			
				aux = aux.substring(0, aux.length() - 1);
			}
			if(colasCoches.get(carreterasEntrantesOrdenadas.get(i)).size() != 0) {
				carreterasEntrantesOrdenadas.get(i);
			}
			aux += ']';
			aux += ')';
			if(i != carreterasEntrantesOrdenadas.size() - 1) aux += ',';
		}
		out.put("queues", aux);
		out.put("type", "mc");
	}
	
}
