package es.ucm.fdi.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.layout.SimulatorLayout;
import es.ucm.fdi.model.*;
import es.ucm.fdi.model.TrafficSimulator.EventType;
import es.ucm.fdi.model.TrafficSimulator.Listener;
import es.ucm.fdi.model.TrafficSimulator.UpdateEvent;

public class Controller {
	private int nPasos;
	private TrafficSimulator simulador;
	private InputStream input;
	private OutputStream output;
	private EventBuilder[] eventosDisponibles = { new NewJunction.Builder(), new NewVehicle.Builder(), 
			new NewRoad.Builder(), new NewFaultyVehicle.Builder(), new NewCar.Builder(),
		new NewBike.Builder(), new NewHighway.Builder(), new NewPath.Builder(), 
			new NewMostCrowded.Builder(), new NewRoundRobin.Builder()};
	
	public Controller(int nPasos, InputStream input, OutputStream output) {
		this.nPasos = nPasos;
		this.input = input;
		simulador = new TrafficSimulator(output);
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
	
	public int getPasos() {
		return nPasos;
	}
	
	public void run() {
		cargarEventos();
		try {
			simulador.run(nPasos);
		} catch (SimulationException e) {
			e.printMessage();
			e.printStackTrace();
		}
	}
	
	public void cargarEventos() {
		Ini eventosPorProcesar = input();
		for(IniSection is: eventosPorProcesar.getSections()) {
			this.parseEvent(is);
		}
	}
	
	public void run(int nPaso) {
		try {
			simulador.run(nPaso);
		} catch (SimulationException e) {
			e.printMessage();
			e.printStackTrace();
		}
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
			simulador.insertaEvento(eventoActual);
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
	
	public void reset() {
		nPasos = 0;
		simulador.reset();
	}
	
	public void addSimulatorListener(Listener l) {
		simulador.addSimulatorListener(l);
	}
  
	public int tiempoActual() {
		return simulador.getTime();
	}
	
}
