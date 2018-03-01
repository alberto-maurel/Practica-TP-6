package es.ucm.fdi.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class TrafficSimulatorTest {
	
	@Test
	public void test1() {
		ArrayList<Event> listaEventosAux = new ArrayList<Event>();  
		Event cruce1 = new NewJunction(0,"Junct1");
		listaEventosAux.add(cruce1);
		
		TrafficSimulator mock = new TrafficSimulator(listaEventosAux);
		
		mock.run();
		boolean b1 = true;
		boolean b2 = true;
		
		assertTrue("FOO",b2);
	}
	
	
	
	
}
