package es.ucm.fdi.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class TrafficSimulatorTest {
	
	@Test
	public void test1() {
		ArrayList<Event> listaEventosAux = new ArrayList<Event>();  
		Event cruce1 = new NewJunction(0,"j1");
		Event cruce2 = new NewJunction(0,"j2");
		Event carretera1 = new NewRoad(0,"r1", 30, 20, "j1", "j2");
		
		ArrayList<String> itin = new ArrayList<String>(); itin.add("j1"); itin.add("j2");
		Event coche1 = new NewVehicle(0,"v1", 20, itin);
		
		
		listaEventosAux.add(cruce1);
		listaEventosAux.add(cruce2);
		listaEventosAux.add(carretera1);
		listaEventosAux.add(coche1);
			
		TrafficSimulator mock = new TrafficSimulator(listaEventosAux);
		
		mock.run(2);
		assertTrue(true);
	}	
}
