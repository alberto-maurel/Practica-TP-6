package es.ucm.fdi.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/* - Creo que no actualiza el semáforo según el orden de adición de las carreteras
 * - fillReportDetails
 * 
 */

public class RoundRobin extends Junction {
	
	protected int maxValorIntervalo;
	protected int minValorIntervalo;
	protected ArrayList<Integer> intervaloDeTiempo; 
	protected int unidadesDeTiempoUsadas;
	protected int pasados; // Indica el numero de vehículos que han pasado por la carretera 
										// entrante durante el intervalo
	private boolean cambioDeSemaforoEsteTurno = false; //booleano para facilitar el writeReport

	
	public RoundRobin(String id, int max, int min) {
		super(id);
		maxValorIntervalo = max;
		minValorIntervalo = min;
		intervaloDeTiempo = new ArrayList<>(carreterasEntrantesOrdenadas.size());
		Collections.fill(intervaloDeTiempo, maxValorIntervalo);
		pasados = 0;
		unidadesDeTiempoUsadas = 0;
	}
	
	public void nuevaCarreteraEntrante(Road road) {
		super.nuevaCarreteraEntrante(road);
		intervaloDeTiempo.add(maxValorIntervalo);
	}
	
	
	public void avanza() {
		//En primer lugar vemos si la carretera con el semaforo en verde tiene algún coche esperando para pasar			
		if(colasCoches.size() > 0 && colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)) != null && 
				colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)).size() > 0) {
					//En dicho caso sacamos el coche
					Vehicle v = (Vehicle) colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)).poll();
					//Y lo movemos a su siguiente carretera
					v.moverASiguienteCarretera();
					//Añadimos uno al numero de coches que han pasado en el intervalo
					//pasados.set(semaforoVerde, pasados.get(semaforoVerde) + 1);
					++pasados;
		}
		actualizarSemaforo();
	}
	
	public void actualizarSemaforo() {
		//En primer lugar vemos si hemos agotado el tiempo del semáforo actual
		if (intervaloDeTiempo.get(semaforoVerde) == unidadesDeTiempoUsadas) {
			//2. Actualizar intervalo de tiempo
			if (pasados == intervaloDeTiempo.get(semaforoVerde)) { //Si en cada paso ha cruzado un coche
				intervaloDeTiempo.set(semaforoVerde, Math.min(intervaloDeTiempo.get(semaforoVerde) + 1, maxValorIntervalo));
			} else if (pasados == 0) { // Si no ha pasado ningún coche durante el intervalo
				intervaloDeTiempo.set(semaforoVerde, Math.max(intervaloDeTiempo.get(semaforoVerde) - 1, minValorIntervalo));
			} // En caso contrario, el intervalo no se modifica.
			
			//1. Pone semáforo verde a rojo
			super.actualizarSemaforo();
			cambioDeSemaforoEsteTurno = true;
			
			unidadesDeTiempoUsadas = 1; // Volvemos a poner las unidades de tiempo usadas a 0
			pasados = 0; //Y también el contador de coches de esa carretera entrante
		} else {
			cambioDeSemaforoEsteTurno = false;
			++unidadesDeTiempoUsadas;
		}
	}
	
	protected void fillReportDetails(Map<String, String> out) {
		String aux = "";
		for (int i = 0; i < carreterasEntrantesOrdenadas.size(); ++i) {
			aux += "(" + carreterasEntrantesOrdenadas.get(i) + ",";
			
			//El semáforo está correctamente colocado
			if(!cambioDeSemaforoEsteTurno) {
				if(semaforoVerde == i) {
					aux += "green:" + Integer.toString(intervaloDeTiempo.get(i) - unidadesDeTiempoUsadas + 1) + ",";
				}
				else {
					aux += "red,";
				}
			} else { //Hemos cambiado de semáforo
				if(semaforoVerde == i) {
					aux += "green:" + Integer.toString(intervaloDeTiempo.get(i) - unidadesDeTiempoUsadas + 1) + ",";
				}
				else {
					aux += "red,";
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
		out.put("type", "rr");
	}
	
}
