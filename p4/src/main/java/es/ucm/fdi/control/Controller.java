package es.ucm.fdi.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.*;

public class Controller {
	private int nPasos;
	private TrafficSimulator simulador;
	private InputStream input;
	private OutputStream output;
	private EventBuilder[] eventosDisponibles = { new NewJunction.Builder(), new NewVehicle.Builder(), new NewRoad.Builder(), new NewFaultyVehicle.Builder(), new NewCar.Builder(), new NewBike.Builder()};
		
	public Controller(int nPasos, InputStream input, OutputStream output) {
		this.nPasos = nPasos;
		simulador = new TrafficSimulator(output);
		this.input = input;
		this.output = output;
	}
	
	public void run() {
		Ini eventosPorProcesar = input();
		for(IniSection is: eventosPorProcesar.getSections()) {
			this.parseEvent(is);
		}
		simulador.run(nPasos);
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
		catch(Exception e){
			
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
			File file = new File("C:/Users/Alberto/git/Practica-TP-4/p4/output1.ini");
			OutputStream s = new FileOutputStream(file);
			ini.store(s);
		}
		catch (IOException io){
			System.out.println("Ha ocurrido un error durante la operación de escritura");
		}
	}
  
}
