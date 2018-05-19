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
import es.ucm.fdi.model.TrafficSimulator.UpdateEvent;

public class Controller implements Listener {
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
		/* Añadimos controlador como un listener, necesario para
		 * el control de errores del modo batch. En caso de no ser
		 * necesario, ya que los errores se mostrarán en la interfaz gráfica
		 * (modo GUI), se elimina posteriormente).
		 */
		addSimulatorListener(this);
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
	
	public void run() throws Exception {
		cargarEventos();
		cargarEventosEnElSimulador();
		simulador.run(nPasos);
	}
	
	public void run(int nPaso) {
		simulador.run(nPaso);
	}
	
	public void cargarEventos() throws Exception {
		Ini eventosPorProcesar = input();
		eventosIntroducidos.clear();
		for(IniSection is: eventosPorProcesar.getSections()) {
			this.parseEvent(is);
		}
	}	
	
	public void cargarEventosEnElSimulador() {
		simulador.cargarEventos(eventosIntroducidos);
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
		catch(Exception e) {throw e;}
	}

	public Ini input() throws Exception {
		try{
			Ini ini = new Ini(input);
			return ini;
		}
		catch (Exception io) { throw io;
			/*error("Ha ocurrido un error durante la operación de lectura.\n" +
					"Error: " + io.getMessage() + "\n" +
					"Clase: " + io.getStackTrace()[0].getClassName() + "\n" +
					"Método: " + io.getStackTrace()[0].getMethodName() + "\n" +
					"Línea: " + io.getStackTrace()[0].getLineNumber());*/
			//return null;
		}
	}
	
	public Ini input(InputStream in) {
		try{
			Ini ini = new Ini(in);
			return ini;
		}
		catch (IOException io) {
			error("Ha ocurrido un error durante la operación de lectura.\n" +
					"Error: " + io.getMessage() + "\n" +
					"Clase: " + io.getStackTrace()[0].getClassName() + "\n" +
					"Método: " + io.getStackTrace()[0].getMethodName() + "\n" +
					"Línea: " + io.getStackTrace()[0].getLineNumber());
			return null;
		}
	}

	public void output(IniSection ini) {
		try{		
			OutputStream s = output;
			ini.store(s);
		}
		catch (IOException io){
			error("Ha ocurrido un error durante la operación de escritura.\n" +
					"Error: " + io.getMessage() + "\n" +
					"Clase: " + io.getStackTrace()[0].getClassName() + "\n" +
					"Método: " + io.getStackTrace()[0].getMethodName() + "\n" +
					"Línea: " + io.getStackTrace()[0].getLineNumber());
		}
	}
	
	public void generarInformes(OutputStream out) {
		try {
			simulador.generarInformes(out, null, null, null);
		} catch (IOException e) {
			error("Ha ocurrido un error durante la generación de los informes.\n" +
					"Error: " + e.getMessage() + "\n" +
					"Clase: " + e.getStackTrace()[0].getClassName() + "\n" +
					"Método: " + e.getStackTrace()[0].getMethodName() + "\n" +
					"Línea: " + e.getStackTrace()[0].getLineNumber());
		}
	}
	
	public void generarInformes(OutputStream out, Set<String> junctions, 
			Set<String> roads, Set<String> vehicles) {
		try {
			simulador.generarInformes(out, junctions, roads, vehicles);
		} catch (IOException e) {
			error("Ha ocurrido un error durante la generación de los informes.\n" +
					"Error: " + e.getMessage() + "\n" +
					"Clase: " + e.getStackTrace()[0].getClassName() + "\n" +
					"Método: " + e.getStackTrace()[0].getMethodName() + "\n" +
					"Línea: " + e.getStackTrace()[0].getLineNumber());
		}
	}
	
	public void reset() {
		eventosIntroducidos = new ArrayList<>();
		nPasos = 0;
		simulador.reset();
	}
  
	public int tiempoActual() {
		return simulador.getTime();
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
	
	public void addSimulatorListener(Listener l) {
		simulador.addSimulatorListener(l);
	}
	
	public void removeFirstListener() {
		simulador.removeFirstListener();
	}
	
	public int getListenersSize() {
		return simulador.getListenersSize();
	}

	public void registered(UpdateEvent ue) {
	}
	
	public void reset(UpdateEvent ue) {
	}

	public void newEvent(UpdateEvent ue) {
	}
	
	public void advanced(UpdateEvent ue) {		
	}
	
	public void error(UpdateEvent ue, String error) {
		System.out.println(error);		
	}
	
	public void error(String error) {
		System.out.println(error);		
	}
}
