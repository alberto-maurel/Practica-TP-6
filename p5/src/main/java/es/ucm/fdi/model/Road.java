package es.ucm.fdi.model;

import es.ucm.fdi.util.MultiTreeMap;
import java.util.Map;

public class Road extends SimulatedObject implements Describable{
	
	protected int longitud;
	protected int maxVel;
	protected MultiTreeMap<Integer, Vehicle> situacionCarretera;
	protected Junction cruceIni;
	protected Junction cruceFin;
	
	public Road(String id, int longitud, int maxVel, Junction src, Junction dest) {
		super(id);
		this.longitud = longitud;
		this.maxVel = maxVel;
		this.situacionCarretera = new MultiTreeMap<>((a,b) -> b - a);
		this.cruceIni = src;
		this.cruceFin = dest;
		src.nuevaCarreteraSaliente(this);
		dest.nuevaCarreteraEntrante(this);
	}

	//Getters para representar el grafo
	public String idSourceJunction() {
		return cruceIni.identificador;
	}
	
	public String idTargetJunction() {
		return cruceFin.identificador;
	}
	
	public int getLength() {
		return longitud;
	}
	
	/**
	 * Para poder pintar a carretera
	 * @return true si el semáforo del final de la carretera está en verde y false si no
	 */
	public boolean estaEnVerde() {
		if(cruceFin.carreterasEntrantesOrdenadas.get(cruceFin.semaforoVerde).equals(this.identificador)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	//Funcionalidad
	//
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
		int velocidadBase = calcularVelocidadBase(maxVel, Math.toIntExact(situacionCarretera.sizeOfValues()));
		
		//Hacemos una copia de la carretera para poder iterar por ella
		
		MultiTreeMap<Integer, Vehicle> situacionCarreteraAuxiliar = new MultiTreeMap<>((a,b) -> b - a);
		for(Vehicle v: situacionCarretera.innerValues()) {
			situacionCarreteraAuxiliar.putValue(v.localizacionCarretera, v);
		}
		
		int factorReduccion;
		int nVehiculosAveriadosHastaElMomento = 0;
		for(Vehicle v: situacionCarreteraAuxiliar.innerValues()) {
			if(v.isCocheAveriado()) {
				++nVehiculosAveriadosHastaElMomento;
				v.setVelocidadActual(0);
			} else {
				factorReduccion = calcularFactorReduccion(nVehiculosAveriadosHastaElMomento);
				v.setVelocidadActual(velocidadBase/factorReduccion);
			}
			//Y hacemos que el coche avance
			v.avanza();
		}	
	}	
	
	protected int calcularVelocidadBase(int velocidadMaxima, int nVehiculosCarretera) {
		int totalCochesEnCarretera = (int)situacionCarretera.sizeOfValues();
		return Math.min(velocidadMaxima,
				1 + velocidadMaxima / Math.max(1, totalCochesEnCarretera));
	}

	protected int calcularFactorReduccion(int nVehiculosAveriados) {
		if(nVehiculosAveriados == 0) {
			return 1;
		} else {
			return 2;
		}
	}
	
	//Para imprimir Road
	//
	protected String getReportHeader() {
		return "[road_report]";
	}
	
	protected void fillReportDetails(Map<String, String> out) {
		StringBuilder sb = new StringBuilder();
		for (Vehicle v: situacionCarretera.innerValues()) {
			//if(v.localizacionCarretera != v.carreteraActual.longitud) 
			sb.append("(" + v.identificador + "," + v.localizacionCarretera + "),");
		}
		//Los metemos todos en un string y quitamos la ultima coma
		if(sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1); 
		}
		out.put("state", sb.toString());
	}
	
	public void describe(Map<String,String> out, String rowIndex) {
		out.put("ID", identificador);
		out.put("Source", cruceIni.identificador);
		out.put("Target", cruceFin.identificador);
		out.put("Length", "" + longitud);
		out.put("Max Speed", "" + maxVel);
		out.put("Vehicles", toStringVehicles());
	}	
	
	public String toStringVehicles() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(Vehicle v: situacionCarretera.innerValues()) {
			sb.append(v.identificador + ", ");
		}
		//En caso de que nuestra carretera tuviese algún vehículo, tenemos que quitar los últimos
		//espacio en blanco y coma que añadimos
		if(sb.length() > 1) {
			sb.delete(sb.length() - 2, sb.length() - 1);
		}
		sb.append("]");
		return sb.toString();
	}
}
