package es.ucm.fdi.launch;

import org.junit.Test;

import es.ucm.fdi.launcher.ExampleMain;

public class MainTests {
	//Testeo de funcionamiento de la práctica, con los ejemplos de entrada y salida
	
	//Pasa todos los tests. Los archivos están en la ruta C:\\Users\\Alberto\\git\\Practica-TP-4\\p4\\src\\test\\java\\es\\ucm\\fdi\\launch\\
	//y en las carpetas\\basic, \\advanced, \\err
	@Test
	public void basicTest() {
		ExampleMain mainBasico = new ExampleMain();
		try {
			mainBasico.test("C:\\Users\\Alberto\\git\\Practica-TP-4\\p4\\src\\test\\java\\es\\ucm\\fdi\\launch\\basic");
			mainBasico.test("C:\\Users\\Alberto\\git\\Practica-TP-4\\p4\\src\\test\\java\\es\\ucm\\fdi\\launch\\advanced");
			mainBasico.test("C:\\Users\\Alberto\\git\\Practica-TP-4\\p4\\src\\test\\java\\es\\ucm\\fdi\\launch\\err");
		}
		catch(Exception e) {
			System.out.println("Se ha producido un error durante la ejecución");
			e.printStackTrace();
		}
	}
	
	
}
