package es.ucm.fdi.model;

public class SimulationException extends Exception{
	private String mensaje;
	
	public SimulationException(){}
	
	public SimulationException(String s) {
		this.mensaje = s;
	}

	public String getMessage() {
		return this.mensaje;
	}
	
	public void setMessage(String msg) {
		this.mensaje = msg;
	}
	
	public void printMessage() {
		System.out.println(mensaje);
	}
}
