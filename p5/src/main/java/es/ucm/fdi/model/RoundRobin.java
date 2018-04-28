package es.ucm.fdi.model;

import java.util.Map;

public class RoundRobin extends Junction {
	
	protected int maxValorIntervalo;
	protected int minValorIntervalo;
	protected int intervaloDeTiempo; 
	protected int unidadesDeTiempoUsadas;
	protected int pasados; // Indica el numero de vehículos que han pasado por la carretera 
										// entrante durante el intervalo
	private boolean cambioDeSemaforoEsteTurno = false; //booleano para facilitar el writeReport

	
	public RoundRobin(String id, int max, int min) {
		super(id);
		maxValorIntervalo = max;
		minValorIntervalo = min;
		intervaloDeTiempo = maxValorIntervalo;
		pasados = 0;
		unidadesDeTiempoUsadas = 0;
	}
	
	public void nuevaCarreteraEntrante(Road road) {
		super.nuevaCarreteraEntrante(road);
		intervaloDeTiempo = maxValorIntervalo;
	}
	
	
	public void avanza() {
		if (semaforoVerde != -1) {
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
		}		
		actualizarSemaforo();
	}
	
	public void actualizarSemaforo() {
		if (semaforoVerde != -1) {
			//En primer lugar vemos si hemos agotado el tiempo del semáforo actual
			if (intervaloDeTiempo - unidadesDeTiempoUsadas == 1) {
				//2. Actualizar intervalo de tiempo
				if (pasados == intervaloDeTiempo) { //Si en cada paso ha cruzado un coche
					intervaloDeTiempo = Math.min(intervaloDeTiempo + 1, maxValorIntervalo);
				} else if (pasados == 0) { // Si no ha pasado ningún coche durante el intervalo
					intervaloDeTiempo = Math.max(intervaloDeTiempo - 1, minValorIntervalo);
				} // En caso contrario, el intervalo no se modifica.
				unidadesDeTiempoUsadas = 0;
				super.actualizarSemaforo();
				cambioDeSemaforoEsteTurno = true;				
				pasados = 0;
			} else {
				cambioDeSemaforoEsteTurno = false;
				++unidadesDeTiempoUsadas;
			}
		} else { // Si es el primer cambio de semáforo
			super.actualizarSemaforo();
			cambioDeSemaforoEsteTurno = true;
			unidadesDeTiempoUsadas = 0;
			pasados = 0;
		} 		
			
	}
	
	protected void fillReportDetails(Map<String, String> out) {
		String aux = "";
		for (int i = 0; i < carreterasEntrantesOrdenadas.size(); ++i) {
			aux += "(" + carreterasEntrantesOrdenadas.get(i) + ",";

			if (semaforoVerde == i) {
				aux += "green:" + Integer.toString(intervaloDeTiempo - unidadesDeTiempoUsadas) + ",";
			}
			else {
				aux += "red,";
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
