package es.ucm.fdi.model;

import es.ucm.fdi.util.IdCola;
import es.ucm.fdi.util.MultiTreeMap;
import es.ucm.fdi.model.*;

import java.lang.Math;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

public class Road extends SimulatedObject{
	protected int longitud;
	protected int maxVel;
	protected MultiTreeMap<Integer, Vehicle> situacionCarretera;
	protected Junction cruceIni;
	protected Junction cruceFin;
	
	//Constructores
	public Road(){}
	
	public Road(String id, int longitud, int maxVel, Junction src, Junction dest){
		super(id);
		this.longitud = longitud;
		this.maxVel = maxVel;
		situacionCarretera = new MultiTreeMap<>();
		cruceIni = src;
		cruceFin = dest;
		src.nuevaCarreteraSaliente(this);
		dest.nuevaCarreteraEntrante(this);
	}

	
	/**
	 * Introduce al vehículo en la carretera
	 * @param Vehículo
	 */
	public void entraVehiculo(Vehicle v) {
		situacionCarretera.putValue(0, v);
	}
	
	/**
	 * Saca al vehículo de la carretera
	 * @param Vehículo
	 */
	void saleVehiculo(Vehicle v) {
		//Sabemos que el vehículo que tenemos que retirar está al final de la carretera
		situacionCarretera.removeValue(longitud, v);
	}
	
	/**
	 * Función que mueve a todos los coches de la carretera
	 */
	void avanza() {
		//En primer lugar calculamos la velocidad base de la carretera
		/*
		int aux1 = Math.max(1 , Math.toIntExact(situacionCarretera.sizeOfValues()));
		int aux2 = maxVel/aux1;
		int velocidadBase = Math.min(maxVel, aux2 + 1);
		*/
		int velocidadBase = calcularVelocidadBase(maxVel, Math.toIntExact(situacionCarretera.sizeOfValues()));
			
		//Y ahora vamos contabilizando cuantos vehículos están averiados y les vamos cambiando la velocidad
		int factorReduccion;
		int nVehiculosAveriadosHastaElMomento = 0;
		
		//Hacemos una copia de la carretera para poder iterar por ella
		MultiTreeMap<Integer, Vehicle> situacionCarreteraAuxiliar = (MultiTreeMap<Integer, Vehicle>) situacionCarretera.clone();
		
		for(Vehicle v: situacionCarreteraAuxiliar.innerValues()) {
			//Si está al final de la carretera lo metemos en el cruce
			if(v.localizacionCarretera == v.carreteraActual.longitud){
				v.carreteraActual.cruceFin.entraVehiculo(v);
			}
			
			else if(v.isCocheAveriado()) {
				//Si está averiado no es necesario actualizar la información porque en este tick no se va a mover
				++nVehiculosAveriadosHastaElMomento;
			}
			else {
				factorReduccion = calcularFactorReduccion(nVehiculosAveriadosHastaElMomento);
				v.setVelocidadActual(velocidadBase/factorReduccion);
			}
			//Y hacemos que el coche avance
			v.avanza();
		}	
	}
	
	protected int calcularVelocidadBase(int velocidadMaxima, int nVehiculosCarretera) {
		int aux1 = Math.max(1 , Math.toIntExact(situacionCarretera.sizeOfValues()));
		int aux2 = velocidadMaxima/aux1;
		return Math.min(velocidadMaxima, aux2 + 1);
	}

	protected int calcularFactorReduccion(int nVehiculosAveriados) {
		if(nVehiculosAveriados == 0) return 1;
		else return 2;
	}
	
	protected String getReportHeader() {
		return "[road_report]";
	}
	
	protected void fillReportDetails(Map<String, String> out) {
		String aux = "";
		for (Vehicle v: situacionCarretera.innerValues()) {
			aux += "(" + v.identificador + ", " + v.localizacionCarretera + "),";
		}
		//Los metemos todos en un string y quitamos la ultima coma
		if(aux.length() > 0) {
			aux = aux.substring(0, aux.length() - 1); // Cutre jeje
		} else {
			//Carretera vacía
			//aux = "empty";
		}
		out.put("state", aux);
	}
}
