package es.ucm.fdi.layout;

import javax.swing.*;
import javax.swing.border.Border;

import es.ucm.fdi.control.SimulatorAction;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class SimulatorLayout extends JFrame {
	public SimulatorLayout() {
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
	
	private void addBars(){
		SimulatorAction salir = new SimulatorAction(
			"Salir", "exit.png", "Salir de la aplicacion", KeyEvent.VK_S, "control shift S", ()->System.exit(0));
		
		JMenu file = new JMenu("File");
		JMenu simulator = new JMenu("Simulator");
		JMenu generate = new JMenu("Generate");
		
		JToolBar bar = new JToolBar();
		
		bar.add(createComponents1(file));
		bar.add(createComponents2(simulator));
		bar.add(createComponents5(file, generate));
		
		bar.add(salir);
		file.add(salir);
		
		JMenuBar menu = new JMenuBar();
		menu.add(file);
		menu.add(simulator);
		menu.add(generate);
		setJMenuBar(menu);
		add(bar, BorderLayout.NORTH);
	}
	
	private JToolBar createComponents1(JMenu file) {
		SimulatorAction guardar = new SimulatorAction(
				"Guardar", "save.png", "Guardar cosas", KeyEvent.VK_S, "control S", ()->System.err.println("guardando... "));
					
		SimulatorAction loadEventsFile = new SimulatorAction(
				"Cargar fichero de eventos", "open.png", "Carga un fichero de eventos", KeyEvent.VK_A, "control A", 
				()->System.err.println("abriendo archivo..."));
			
		SimulatorAction borrar = new SimulatorAction(
				"Borrar", "clear.png", "Borra la lista de eventos", KeyEvent.
				VK_C, "control C", ()->System.err.println("Borrando cola de eventos..."));
		
		//Agregamos las acciones a la barra
		JToolBar bar = new JToolBar();
		bar.add(loadEventsFile);
		bar.add(guardar);
		bar.add(borrar);
		
		//Y al menú
		file.add(loadEventsFile);
		file.add(guardar);
		return bar;
	}
	
	private JToolBar createComponents2(JMenu simulator) {
		SimulatorAction events = new SimulatorAction(
				"Eventos", "events.png", "???", KeyEvent.
				VK_E, "control E", ()->System.err.println("???..."));
		
		SimulatorAction play = new SimulatorAction(
				"Ejecutar", "play.png", "Ejecutar la simulación", KeyEvent.
				VK_P, "control P", ()->System.err.println("Ejecutando..."));
		
		SimulatorAction reset = new SimulatorAction(
				"Resetear", "reset.png", "Resetear la simulación", KeyEvent.
				VK_R, "control R", ()->System.err.println("Reseteando..."));
		
		//Añadimos las funcionalidades a las barras
		JToolBar bar = new JToolBar();
		bar.add(events);
		bar.add(play);
		bar.add(reset);
		
		//Y al menú
		simulator.add(play);
		simulator.add(reset);
		return bar;
	}
	
	private JToolBar createComponents5(JMenu file, JMenu generate) {
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
		bar.add(generateReport);
		bar.add(deleteReport);
		bar.add(saveReport);
		
		generate.add(generateReport);
		generate.add(deleteReport);
		file.add(saveReport);
		return bar;
	}
	
	public static void main(String ... args) {
		SwingUtilities.invokeLater(()-> new SimulatorLayout());
	}
}



/*
 * String t = new String(Files.readAllBytes(f.toPath()). "UTF-8"); // "ISO-8859-1"
 * 
 *  Files.write(f.toPath(), t.getBytes("UTF-8"));* 
 * 
 * 
 * */
 