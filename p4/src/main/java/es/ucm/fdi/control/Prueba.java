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
			
		
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
		upperPanel.add(createTablePanel("Fichero", table5, new Dimension(200,200)));
		upperPanel.add(createTablePanel("Events Queue", table4, new Dimension(200,200)));
		upperPanel.add(createTablePanel("Reports", table6, new Dimension(200,200)));	
		
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
			"Salir", "exit.png", "salir de la aplicacion", KeyEvent.VK_S, "control shift S", ()->System.exit(0));
		
		SimulatorAction guardar = new SimulatorAction(
				"Guardar", "save.png", "guardar cosas", KeyEvent.VK_S, "control S", ()->System.err.println("guardando... "));
				
		SimulatorAction loadEventsFile = new SimulatorAction(
				"Cargar fichero de eventos", "open.png", "Carga un fichero de eventos", KeyEvent.VK_A, "control A", 
				()->System.err.println("abriendo archivo..."));
		
		SimulatorAction borrar = new SimulatorAction(
				"Borrar", "clear.png", "Borra la lista de eventos", KeyEvent.
				VK_C, "control C", ()->System.err.println("Borrando cola de eventos..."));
		
		JToolBar bar = new JToolBar();
		bar.add(loadEventsFile);
		bar.add(guardar);
		bar.add(borrar);
		bar.add(salir);
		
		
		add(bar, BorderLayout.NORTH);
	
		
		JMenu file = new JMenu("File");
		file.add(loadEventsFile);
		file.add(guardar);
		file.add(borrar);
		file.add(salir);
		
		JMenuBar menu = new JMenuBar();
		menu.add(file);
		setJMenuBar(menu);
	}
	
	private JScrollPane createTablePanel(String title, JTable table, Dimension d){
		Border b = BorderFactory.createLineBorder(Color.black, 2);
		JScrollPane p = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		p.setPreferredSize(d);
		p.setBorder(BorderFactory.createTitledBorder(b, title));
		//p.getViewport().getView().setBackground(Color.WHITE);
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
 