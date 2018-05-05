package es.ucm.fdi.model;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Car extends Vehicle {
	
	protected int resistance;
	protected double faultProbability;
	protected int maxFaultDuration;
	protected long seed;
	protected int kmSinceLastFailure;
	private Random numAleatorio;
	

	//Constructor sin semilla
	public Car(String id, int velMaxima, ArrayList<Junction> itinerario, int resistance, 
			double faultProbability, int maxFaultDuration) {
		this(id, velMaxima, itinerario, resistance, faultProbability, 
				maxFaultDuration, System.currentTimeMillis());
	}
	
	//Constructor con semilla
	public Car(String id, int velMaxima, ArrayList<Junction> itinerario, int resistance,
			double faultProbability, int maxFaultDuration, long seed) {
		super(id, velMaxima, itinerario);
		this.resistance = resistance;
		this.faultProbability = faultProbability;
		this.maxFaultDuration = maxFaultDuration;
		this.seed = seed;
		this.kmSinceLastFailure = 0;
		this.numAleatorio = new Random(seed);
	}
	
	public void avanza() {
		if(tiempoAveria > 0) {
			--tiempoAveria;
		} else { //Vemos primero si se va a averiar en este paso antes de avanzar	
			boolean isCarOk = true;
			if (kmSinceLastFailure > resistance) {
				if (numAleatorio.nextDouble() < faultProbability) { 
					//En ese caso, hemos averiado el vehículo
					this.setTiempoAveria(numAleatorio.nextInt(maxFaultDuration) + 1); 
					isCarOk = false;
					kmSinceLastFailure = 0;
					velActual = 0;
				}
			}
			if(isCarOk) { // Si el coche está bien, se mueve
				//Sacamos el coche de la carretera
				carreteraActual.situacionCarretera.removeValue(localizacionCarretera, this); 		 		
				//Procesamos el vehículo				
				if(localizacionCarretera + velActual < carreteraActual.longitud) { 
					//Si el vehículo no llega en este tick al final de la carretera
					avanzarCocheSinLlegarAlFinal();
					kmSinceLastFailure += velActual;
				} else if (localizacionCarretera != carreteraActual.longitud) { 
					//El coche entra en la intersección (pero aún no estaba)
					avanzarCocheLlegandoAlFinal();
				} else { //El coche ya estaba en la intersección
					velActual = 0;					
				}
				//Volvemos a meter el coche en la posición en la que debería ir
				carreteraActual.situacionCarretera.putValue(localizacionCarretera, this);
			} else { //Si el coche está averiado
				--tiempoAveria;
			}
		}
	}
	
	protected void fillReportDetails(Map<String, String> out) {
		out.put("type", "car");
		super.fillReportDetails(out);
	}	
}
