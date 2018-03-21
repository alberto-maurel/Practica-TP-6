package es.ucm.fdi.model;

import java.util.ArrayList;

import org.junit.Test;

public class SimObjectTest {
	@Test
	public void test1() {
		Junction cruce1 = new Junction("J1");
		Junction cruce2 = new Junction("J2");
		ArrayList<Junction> itinerario = new ArrayList<>();
		itinerario.add(cruce1); itinerario.add(cruce2);
		Road carretera1 = new Road("ROUTE 49", 100, 40, cruce1,cruce2);
		Vehicle coche1 = new Vehicle("CH1", 40, itinerario);

		cruce2.entraVehiculo(coche1);

		cruce2.avanza();
		carretera1.avanza();
	}
}
