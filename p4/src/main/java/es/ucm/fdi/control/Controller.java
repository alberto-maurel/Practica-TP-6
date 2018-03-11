package es.ucm.fdi.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.*;

public class Controller {
	private int nPasos;
	private TrafficSimulator simulador;
	private EventBuilder[] eventosDisponibles = { new NewJunction.Builder(), new NewVehicle.Builder(), new NewRoad.Builder(), new NewFaultyVehicle.Builder()};
	
	public Controller() {
		nPasos = 0;
		simulador = new TrafficSimulator();
	}
	
	public void parseEvent(IniSection ini) {
		Event eventoActual = null;
		for(int i = 0; i < eventosDisponibles.length; ++i) {
			if(eventosDisponibles[i].parse(ini) != null) {
				eventoActual = eventosDisponibles[i].parse(ini);
			}
		}
		try {
			simulador.insertaEvento(eventoActual);
		}
		catch(Exception e){
			
		}
	}
	
	public Ini input() {
		try{		
			File file = new File("C:/Users/Alberto/git/Practica-TP-4/p4/input1.ini");
			InputStream s = new FileInputStream(file);
			Ini ini = new Ini(s);
			return ini;
		}
		catch (IOException io){
			System.out.println("Ha ocurrido un error durante la operación de lectura");
			return null;
		}
	}
}
