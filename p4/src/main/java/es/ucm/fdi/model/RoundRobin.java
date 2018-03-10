package es.ucm.fdi.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/* - Creo que no actualiza el semáforo según el orden de adición de las carreteras
 * - fillReportDetails
 * 
 */

public class RoundRobin extends Junction {
	
	private int maxValorIntervalo;
	private int minValorIntervalo;
	private int intervaloDeTiempo;
	private int unidadesDeTiempoUsadas;
	private ArrayList<Integer> pasados; // Indica el numero de vehículos que han pasado por la carretera 
										// entrante durante el intervalo
	
	public RoundRobin(String id, int max, int min) {
		super(id);
		maxValorIntervalo = max;
		minValorIntervalo = min;
		intervaloDeTiempo = max;
		unidadesDeTiempoUsadas = 0;
		pasados.ensureCapacity(colasCoches.size());
		Collections.fill(pasados, 0);
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
					pasados.set(semaforoVerde, pasados.get(semaforoVerde) + 1);
		}
		actualizarSemaforo();
	}
	
	public void actualizarSemaforo() {
		if (unidadesDeTiempoUsadas == intervaloDeTiempo) {
			//1. Pone semáforo verde a rojo
			//2. Actualizar intervalo de tiempo
			if (pasados.get(semaforoVerde) == intervaloDeTiempo) { //Si en cada paso ha cruzado un coche
				intervaloDeTiempo = Math.min(intervaloDeTiempo + 1, maxValorIntervalo);
			} else if (pasados.get(semaforoVerde) == 0) { // Si no ha pasado ningún coche durante el intervalo
				intervaloDeTiempo = Math.max(intervaloDeTiempo - 1, minValorIntervalo);
			} // En caso contrario, el intervalo no se modifica.
			
			unidadesDeTiempoUsadas = 0; // Volvemos a poner las unidades de tiempo usadas a 0
			pasados.set(semaforoVerde, 0); //Y también el contador de coches de esa carretera entrante
			// Y ahora ponemos en verde el semáforo de la siguiente carretera entrante
			if(colasCoches.size() > 0) semaforoVerde = (semaforoVerde + 1) % colasCoches.size();
		}
		else ++unidadesDeTiempoUsadas;
	}
	
	protected void fillReportDetails(Map<String, String> out) {
		
	}
	
}
