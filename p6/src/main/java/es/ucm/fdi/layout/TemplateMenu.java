package es.ucm.fdi.layout;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import es.ucm.fdi.control.SimulatorAction;

@SuppressWarnings("serial")
public class TemplateMenu extends JPopupMenu {
	
	public TemplateMenu(JTextArea fichero, SimulatorAction loadEventsFile, 
			SimulatorAction guardar, SimulatorAction borrar) {
		addPopupMenu(fichero, loadEventsFile, guardar, borrar);
	}
	
	/**
	 * Implementación del menú para poder añadir plantillas de eventos
	 */
	private void addPopupMenu(JTextArea fichero, SimulatorAction loadEventsFile, 
			SimulatorAction guardar, SimulatorAction borrar) {
		
		JMenu subMenu = new JMenu("Add Template");

		String[] templateNames = { "New RR Junction", "New MC Junction", "New Junction",
				"New Dirt Road", "New Lanes Road", "New Road", 
				"New Bike", "New Car", "New Vehicle",
				"Make Vehicle Faulty" };
		
		Map<String, String> templates = generateTemplates();
		
		for (String s : templateNames) {
			JMenuItem menuItem = new JMenuItem(s);
			menuItem.addActionListener(e -> fichero.append(templates.get(s) + '\n'));
			subMenu.add(menuItem);
		}
		
		JMenuItem loadOption = new JMenuItem(loadEventsFile);		
		JMenuItem saveOption = new JMenuItem(guardar);
		JMenuItem clearOption = new JMenuItem(borrar);
		
		add(subMenu);
		addSeparator();
		add(loadOption);
		add(saveOption);
		add(clearOption);
		
	}
	
	private Map<String, String> generateTemplates() {
		Map<String, String> templates = new HashMap<>();
		templates.put("New RR Junction", "[new_junction]\n" + 
				"time = \n" + 
				"id = \n" + 
				"max_time_slice = \n" + 
				"min_time_slice = \n" + 
				"type = rr\n");
		templates.put("New MC Junction", "[new_junction]\n" + 
				"time = \n" + 
				"id = \n" + 
				"type = mc\n");
		templates.put("New Junction", "[new_junction]\n" + 
				"time = \n" + 
				"id = \n");
		templates.put("New Dirt Road", "[new_road]\n" + 
				"time = \n" + 
				"id = \n" + 
				"src = \n" + 
				"dest = \n" + 
				"max_speed = \n" + 
				"length = \n" + 
				"type = dirt\n");
		templates.put("New Lanes Road", "[new_road]\n" + 
				"time = \n" + 
				"id = \n" + 
				"src = \n" + 
				"dest = \n" + 
				"max_speed = \n" + 
				"length = \n" + 
				"lanes = \n" + 
				"type = lanes\n");
		templates.put("New Road", "[new_road]\n" + 
				"time = \n" + 
				"id = \n" + 
				"src = \n" + 
				"dest = \n" + 
				"max_speed = \n" + 
				"length = \n");
		templates.put("New Bike", "[new_vehicle]\n" + 
				"time = \n" + 
				"id = \n" + 
				"itinerary = \n" + 
				"max_speed = \n" + 
				"type = bike\n");
		templates.put("New Car", "[new_vehicle]\n" + 
				"time = \n" + 
				"id = \n" + 
				"itinerary = \n" + 
				"max_speed = \n" + 
				"type = car\n" + 
				"resistance = \n" + 
				"fault_probability = \n" + 
				"max_fault_duration = \n" + 
				"seed = \n");
		templates.put("New Vehicle", "[new_vehicle]\n" + 
				"time = \n" + 
				"id = \n" + 
				"itinerary = \n" + 
				"max_speed = \n");
		templates.put("Make Vehicle Faulty", "[make_vehicle_faulty]\n" + 
				"time = \n" + 
				"vehicles = \n" + 
				"duration = \n");
		return templates;
	}

}
