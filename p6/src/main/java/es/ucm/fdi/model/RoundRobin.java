package es.ucm.fdi.model;

import java.util.Map;

public class RoundRobin extends Junction {
	
	protected int maxValorIntervalo;
	protected int minValorIntervalo;
	protected int intervaloDeTiempo; 
	protected int unidadesDeTiempoUsadas;
	protected int pasados; // Indica el numero de vehículos que han pasado por la carretera 
										// entrante durante el intervalo

	
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
			if(colasCoches.size() > 0 && 
					colasCoches.get(carreterasEntrantesOrdenadas.get(semaforoVerde)) != null && 
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
				pasados = 0;
			} else {
				++unidadesDeTiempoUsadas;
			}
		} else { // Si es el primer cambio de semáforo
			super.actualizarSemaforo();
			unidadesDeTiempoUsadas = 0;
			pasados = 0;
		} 		
			
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
		out.put("type", "rr");
	}
	
	protected String toStringGreen() {
		return "green:"+ Integer.toString(intervaloDeTiempo - unidadesDeTiempoUsadas);
	}
	
}