package es.ucm.fdi.model;

@SuppressWarnings("serial")
public class SimulationException extends RuntimeException {
	
	private String mensaje;
	private Throwable cause;
	
	
	public SimulationException() {}
	
	public SimulationException(String s) {
		mensaje = s;
	}
	
	public SimulationException(Throwable c) {
		cause = c;
	}
	
	public SimulationException(String s,Throwable c) {
		mensaje = s;
		cause = c;
	}

	public String getMessage() {
		return mensaje;
	}
	
	public Throwable getCause() {
		return cause;
	}
	
	public void setMessage(String msg) {
		mensaje = msg;
	}
	
	public void printMessage() {
		System.out.println(mensaje);
	}
	
	
}
