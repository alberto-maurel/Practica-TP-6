package es.ucm.fdi.control;

import java.util.ArrayList;

import es.ucm.fdi.model.*;

public class Controller {
	int nPasos;
	TrafficSimulator simulador;
	ArrayList<EventBuilder> eventosDisponibles;
	
	public Controller() {
		eventosDisponibles = new ArrayList<>();
		eventosDisponibles.add(new NewJunction.Builder());
		eventosDisponibles.add(new NewVehicle.Builder());
		eventosDisponibles.add(new NewRoad.Builder());
		eventosDisponibles.add(new NewFaultyVehicle.Builder());
	}

	
	
	
	
	
	
}
