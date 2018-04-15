package es.ucm.fdi.control;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Prueba extends JFrame {
	public Prueba() {
		super("Traffic Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addBars();		
		
		
		String[] columnNames = {"Nombre", "Apellido1", "Apellido2"};
		Object[][] data = {
				{"Laura", "Castilla", "Castellano"},
				{"Alberto", "Maurel", "Serrano"}
				}; 
		
		JTable table1 = new JTable(data, columnNames);
		table1.setSize(100,100);
		//JTable#setFillsViewportHeight(true);
		JTable table2 = new JTable(data, columnNames);
		table2.setSize(100,100);
		JTable table3 = new JTable(data, columnNames);
		table3.setSize(100,100);
		JTable table4 = new JTable(data, columnNames);
		table4.setSize(100,100);
		JTable table5 = new JTable(data, columnNames);
		table5.setSize(100,100);
		JTable table6 = new JTable(data, columnNames);
		table6.setSize(100,100);

		
		//Creación de uno de los paneles que tiene las tablas dentro
		JTextArea fichero = new JTextArea();
		fichero.setEditable(true);
		
		JTextArea reports = new JTextArea();
		reports.setEditable(true);
		
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
		upperPanel.add(createTextAreaPanel("Fichero", fichero, new Dimension(200,200)));
		upperPanel.add(createTablePanel("Events Queue", table4, new Dimension(200,200)));
		upperPanel.add(createTextAreaPanel("Reports", reports, new Dimension(200,200)));	
		
		JPanel leftLowerPanel = new JPanel();
		leftLowerPanel.setLayout(new BoxLayout(leftLowerPanel, BoxLayout.Y_AXIS));
		leftLowerPanel.add(createTablePanel("Vehicles", table1, new Dimension(200,200)));
		leftLowerPanel.add(createTablePanel("Roads", table2, new Dimension(200,200)));
		leftLowerPanel.add(createTablePanel("Junctions", table3, new Dimension(200,200)));

		leftLowerPanel.setSize(100, 100);
		leftLowerPanel.setBackground(Color.WHITE);
		
		JPanel rightLowerPanel = new JPanel();
		rightLowerPanel.setSize(100, 100);
		rightLowerPanel.setBackground(Color.WHITE);
		
	
		JSplitPane lowerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftLowerPanel, rightLowerPanel);
		JSplitPane middleSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperPanel, lowerSplit);			
		
		add(middleSplit, BorderLayout.CENTER);
		
		setSize(1000, 1000);
		setVisible(true);
		
		middleSplit.setDividerLocation(.35);
		lowerSplit.setDividerLocation(.5);
	}

	
	private void addBars(){
		SimulatorAction salir = new SimulatorAction(
			"Salir", "exit.png", "Salir de la aplicacion", KeyEvent.VK_S, "control shift S", ()->System.exit(0));
		
		SimulatorAction guardar = new SimulatorAction(
			"Guardar", "save.png", "Guardar cosas", KeyEvent.VK_S, "control S", ()->System.err.println("guardando... "));
				
		SimulatorAction loadEventsFile = new SimulatorAction(
				"Cargar fichero de eventos", "open.png", "Carga un fichero de eventos", KeyEvent.VK_A, "control A", 
				()->System.err.println("abriendo archivo..."));
		
		SimulatorAction borrar = new SimulatorAction(
				"Borrar", "clear.png", "Borra la lista de eventos", KeyEvent.
				VK_C, "control C", ()->System.err.println("Borrando cola de eventos..."));
		
		SimulatorAction events = new SimulatorAction(
				"Eventos", "events.png", "???", KeyEvent.
				VK_E, "control E", ()->System.err.println("???..."));
		
		SimulatorAction play = new SimulatorAction(
				"Ejecutar", "play.png", "Ejecutar la simulación", KeyEvent.
				VK_P, "control P", ()->System.err.println("Ejecutando..."));
		
		SimulatorAction reset = new SimulatorAction(
				"Resetear", "reset.png", "Resetear la simulación", KeyEvent.
				VK_R, "control R", ()->System.err.println("Reseteando..."));
		
		SimulatorAction generateReport = new SimulatorAction(
				"Generar Reporte", "report.png", "Generar el reporte de la simulación", KeyEvent.
				VK_G, "control G", ()->System.err.println("Generando..."));
		
		SimulatorAction deleteReport = new SimulatorAction(
				"Borrar Reporte", "delete_report.png", "Borrar el reporte de la simulación", KeyEvent.
				VK_B, "control B", ()->System.err.println("Borrando..."));
		
		SimulatorAction saveReport = new SimulatorAction(
				"Guardar Reporte", "save_report.png", "Guardar el reporte de la simulación", KeyEvent.
				VK_G, "control shift G", ()->System.err.println("Guardando..."));
		
		JToolBar bar = new JToolBar();
		bar.add(loadEventsFile);
		bar.add(guardar);
		bar.add(borrar);
		bar.add(events);
		bar.add(play);
		bar.add(reset);
		
		
		bar.add(generateReport);
		bar.add(deleteReport);
		bar.add(saveReport);
		bar.add(salir);
		
		
		add(bar, BorderLayout.NORTH);
	
		JMenu file = new JMenu("File");
		file.add(loadEventsFile);
		file.add(guardar);
		file.add(saveReport);
		file.add(salir);
		
		JMenu simulator = new JMenu("Simulator");
		simulator.add(play);
		simulator.add(reset);
				
		//file.add(borrar);
		//file.add(events);
		
		JMenu generate = new JMenu("Generate");
		generate.add(generateReport);
		generate.add(deleteReport);
		
		
		
		JMenuBar menu = new JMenuBar();
		menu.add(file);
		menu.add(simulator);
		menu.add(generate);
		setJMenuBar(menu);
	}
	
	private JScrollPane createTablePanel(String title, JTable table, Dimension d){
		Border b = BorderFactory.createLineBorder(Color.black, 2);
		JScrollPane p = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		p.setPreferredSize(d);
		p.setBorder(BorderFactory.createTitledBorder(b, title));
		return p;
	}
	
	private JScrollPane createTextAreaPanel(String title, JTextArea tArea, Dimension d){
		Border b = BorderFactory.createLineBorder(Color.black, 2);
		JScrollPane p = new JScrollPane(tArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		p.setPreferredSize(d);
		p.setBorder(BorderFactory.createTitledBorder(b, title));
		return p;
	}
	
	public static void main(String ... args) {
		SwingUtilities.invokeLater(()-> new Prueba());
	}
}



/*
 * String t = new String(Files.readAllBytes(f.toPath()). "UTF-8"); // "ISO-8859-1"
 * 
 *  Files.write(f.toPath(), t.getBytes("UTF-8"));* 
 * 
 * 
 * */
 