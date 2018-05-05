package es.ucm.fdi.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.*;
import es.ucm.fdi.model.TrafficSimulator.Listener;

public class Controller {
	private int nPasos;
	private TrafficSimulator simulador;
	private InputStream input;
	private OutputStream output;
	private EventBuilder[] eventosDisponibles = { new NewJunction.Builder(),
			new NewVehicle.Builder(), 	new NewRoad.Builder(), 
		new NewFaultyVehicle.Builder(), new NewCar.Builder(), new NewBike.Builder(),
			new NewHighway.Builder(), new NewPath.Builder(), 
		new NewMostCrowded.Builder(), new NewRoundRobin.Builder()};
	
	private ArrayList<Event> eventosIntroducidos;
	
	
	public Controller(int nPasos, InputStream input, OutputStream output) {
		this.nPasos = nPasos;
		this.input = input;
		simulador = new TrafficSimulator(output);
		eventosIntroducidos = new ArrayList<>();
		this.output = output;
	}
	
	public void modifyInputStream(InputStream is) {
		input = is;
	}
	
	public InputStream getInput() {
		return input;
	}
	
	public void modifyOutputStream(OutputStream os) {
		output = os;
		simulador.modifyOutputStream(os);
	}
	
	public int getPasosAEjecutar() {
		return nPasos;
	}
	
	public int getPasos() {
		return simulador.getTime();
	}
	
	public void run() {
		cargarEventos();
		cargarEventosEnElSimulador(/*nPasos*/);
		try {
			simulador.run(nPasos);
		} catch (SimulationException e) {
			e.printMessage();
			e.printStackTrace();
		}
	}
	
	
	public void cargarEventos() {
		Ini eventosPorProcesar = input();
		eventosIntroducidos.clear();
		for(IniSection is: eventosPorProcesar.getSections()) {
			this.parseEvent(is);
		}
	}
	
	public void run(int nPaso) throws SimulationException {
		simulador.run(nPaso);
	}
	
	public void parseEvent(IniSection ini) {
		Event eventoActual = null;
		try {
			for(int i = 0; i < eventosDisponibles.length; ++i) {
				if(eventosDisponibles[i].parse(ini) != null) {
					eventoActual = eventosDisponibles[i].parse(ini);
					break;
				}
			}
			eventosIntroducidos.add(eventoActual);
		}
		catch(SimulationException e){
			System.out.println("Se ha producido un error durante la creación de los eventos");
			e.printMessage();
			e.printStackTrace();
		}
	}

	
	public Ini input() {
		try{
			Ini ini = new Ini(input);
			return ini;
		}
		catch (IOException io){
			System.out.println("Ha ocurrido un error durante la operación de lectura");
			return null;
		}
	}
	
	public Ini input(InputStream in) {
		try{
			Ini ini = new Ini(in);
			return ini;
		}
		catch (IOException io){
			System.out.println("Ha ocurrido un error durante la operación de lectura");
			return null;
		}
	}

	public void output(IniSection ini) {
		try{		
			OutputStream s = output;
			ini.store(s);
		}
		catch (IOException io){
			System.out.println("Ha ocurrido un error durante la operación de escritura");
		}
	}
	
	public void generarInformes(OutputStream out) {
		try {
			simulador.generarInformes(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void generarInformes(OutputStream out, Set<String> junctions, 
			Set<String> roads, Set<String> vehicles) {
		try {
			simulador.generarInformes(out, junctions, roads, vehicles);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reset() {
		eventosIntroducidos = new ArrayList<>();
		nPasos = 0;
		simulador.reset();
	}
	
	public void addSimulatorListener(Listener l) {
		simulador.addSimulatorListener(l);
	}
  
	public int tiempoActual() {
		return simulador.getTime();
	}
	
	public void cargarEventosEnElSimulador() {
		simulador.cargarEventos(eventosIntroducidos);
	}
	
	public Boolean hayEventosCargados() {
		return simulador.hayEventosCargados() ? true : false;
	}
	
	public List<Junction> getJunctions() {
		return simulador.getJunctions();
	}
	
	public List<Road> getRoads() {
		return simulador.getRoads();
	}
	
	public List<Vehicle> getVehicles() {
		return simulador.getVehicles();
	}
}
