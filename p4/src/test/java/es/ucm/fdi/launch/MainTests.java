package es.ucm.fdi.launch;

import org.junit.Test;

import es.ucm.fdi.launcher.Main;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MainTests {
	//Testeo de funcionamiento de la práctica, con los ejemplos de entrada y salida

	@Test
	public void basicTest() {
		Main mainBasico = new Main();
		try {
			assertTrue("Tests basicos pasan", mainBasico.test("C:\\Users\\Alberto\\Documents\\_Academico\\_2º Carrera\\TP\\Practica 5\\Practica-TP-5\\p4\\examples\\basic"));
			assertTrue( "Tests avanzados pasan", mainBasico.test("examples/advanced"));
			mainBasico.test("examples/err");
			fail("no da error con una entrada mala");
		}
		catch(Exception e) {
			System.out.println("Se ha producido un error durante la ejecución");
			e.printStackTrace();
		}
	}
}
