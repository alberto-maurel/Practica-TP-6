package es.ucm.fdi.layout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.control.SimulatorAction;
import es.ucm.fdi.extra.graphlayout.GraphComponent;
import es.ucm.fdi.extra.graphlayout.GraphLayoutExample;
import es.ucm.fdi.model.Describable;
import es.ucm.fdi.model.TrafficSimulator.Listener;
import es.ucm.fdi.model.TrafficSimulator.UpdateEvent;
import es.ucm.fdi.model.Vehicle;
import es.ucm.fdi.util.TextStream;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings("serial")
public class SimulatorLayout extends JFrame implements Listener {
	
	Controller controlador;
	SimulatorTable vehiclesTable;
	
	public SimulatorLayout(Controller ctrl) {
		super("Traffic Simulator");
		controlador = ctrl;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTextArea fichero = new JTextArea();
		fichero.setEditable(true);
		JTextArea reports = new JTextArea();
		reports.setEditable(false);
		
		addBars(fichero, reports);
			
		String[] columnNamesQueue = {"#", "Time", "Type", "Involves"};
		String[] columnNamesVehicle = {"ID", "Road", "Location", "Speed", "Km", "Faulty units", "Itinerary"};
		String[] columnNamesRoad = {"ID", "Source", "Target", "Length", "Max Speed", "Vehicles"};
		String[] columnNamesJunction = {"ID", "Green", "Red"};
		Object[][] data = {};
		
		JTable eventsQueueTable = new JTable(data, columnNamesQueue);
		eventsQueueTable.setSize(100,100);		
		//JTable vehiclesTable = new JTable(data, columnNamesVehicle);
		//vehiclesTable.setSize(100,100);
		ArrayList<Vehicle> vehiclesArray = new ArrayList<>();
		JTable roadsTable = new JTable(data, columnNamesRoad);
		roadsTable.setSize(100,100);
		JTable junctionsTable = new JTable(data, columnNamesJunction);
		junctionsTable.setSize(100,100);
		
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
		upperPanel.add(createTextAreaPanel("Fichero", fichero, new Dimension(200,200)));
		upperPanel.add(createTablePanel("Events Queue", eventsQueueTable, new Dimension(200,200)));
		upperPanel.add(createTextAreaPanel("Reports", reports, new Dimension(200,200)));	
		
		JPanel leftLowerPanel = new JPanel();
		leftLowerPanel.setLayout(new BoxLayout(leftLowerPanel, BoxLayout.Y_AXIS));
		//leftLowerPanel.add(createTablePanel("Vehicles", vehiclesTable, new Dimension(200,200)));
		vehiclesTable = createSimulatorTablePanel("Vehicles", columnNamesVehicle , vehiclesArray, new Dimension(200,200));
		leftLowerPanel.add(vehiclesTable);
		leftLowerPanel.add(createTablePanel("Roads", roadsTable, new Dimension(200,200)));
		leftLowerPanel.add(createTablePanel("Junctions", junctionsTable, new Dimension(200,200)));
		leftLowerPanel.setSize(100, 100);
		leftLowerPanel.setBackground(Color.WHITE);
		
		JPanel rightLowerPanel = new JPanel();
		rightLowerPanel.setSize(100, 100);
		GraphLayoutClass grafo = new GraphLayoutClass();
		rightLowerPanel.add(grafo);	
		rightLowerPanel.setBackground(Color.WHITE);
		//Y añadimos el grafo a los listeners
		controlador.addSimulatorListener(grafo);
	
		JSplitPane lowerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftLowerPanel, rightLowerPanel);
		JSplitPane middleSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperPanel, lowerSplit);		
		add(middleSplit, BorderLayout.CENTER);
		
		setSize(1000, 1000);
		setVisible(true);
		
		middleSplit.setDividerLocation(.35);
		lowerSplit.setDividerLocation(.5);
		controlador.addSimulatorListener(this);
	}

	private JScrollPane createTablePanel(String title, JTable table, Dimension d) {
		
		Border b = BorderFactory.createLineBorder(Color.black, 2);
		JScrollPane p = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		p.setPreferredSize(d);
		p.setBorder(BorderFactory.createTitledBorder(b, title));
		return p;
		
	}
	
	private SimulatorTable createSimulatorTablePanel(String title, String[] columnNames, ArrayList<? extends Describable> objectList , Dimension d) {
		return new SimulatorTable(title, columnNames, objectList);
	}
	
	private JScrollPane createTextAreaPanel(String title, JTextArea tArea, Dimension d) {
		
		Border b = BorderFactory.createLineBorder(Color.black, 2);
		JScrollPane p = new JScrollPane(tArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		p.setPreferredSize(d);
		p.setBorder(BorderFactory.createTitledBorder(b, title));
		return p;
		
	}
	
	private void addBars(JTextArea fichero, JTextArea reports) {
		
		JMenu file = new JMenu("File");
		JMenu simulator = new JMenu("Simulator");
		JMenu generate = new JMenu("Generate");
		
		JToolBar bar = new JToolBar();
		bar.add(createComponents1(file, fichero));		
		
		JLabel label = new JLabel("Steps: ");
		JSpinner selector = new JSpinner();
		selector.setPreferredSize(new Dimension(50,50));
		
		JLabel label2 = new JLabel("Time: ");
		JTextArea text = new JTextArea();
		//TODO: arreglar esto para que no salga tan grande
		text.setPreferredSize(new Dimension(50,50));
		text.setEditable(false);
		
		JToolBar bar2 = new JToolBar();
		bar2.add(label);
		bar2.add(selector);
		bar2.add(label2);
		bar2.add(text);
		bar2.setPreferredSize(new Dimension(8,8));
		bar2.setFloatable(false);
		
	
		bar.add(createComponents2(simulator, selector, reports));
		bar.add(bar2);
		bar.add(createComponents3(file, generate, reports));
		
		
		JMenuBar menu = new JMenuBar();
		menu.add(file);
		menu.add(simulator);
		menu.add(generate);
		setJMenuBar(menu);
		add(bar, BorderLayout.NORTH);
		
	}
	
	private JToolBar createComponents1(JMenu file, JTextArea fichero) {
		
		SimulatorAction guardar = new SimulatorAction(
				"Guardar fichero de eventos", "save.png", "Guardar fichero de texto",
				KeyEvent.VK_S, "control S", ()->saveFile(fichero, "Guardar eventos como..."));
			
		SimulatorAction loadEventsFile = new SimulatorAction(
				"Cargar fichero de eventos", "open.png", "Carga un fichero de eventos", KeyEvent.VK_A, "control A", ()->loadFile(fichero));
			
		SimulatorAction borrar = new SimulatorAction(
				"Borrar", "clear.png", "Borra la lista de eventos", KeyEvent.
				VK_C, "control C", ()->fichero.setText(""));
		
		//Agregamos las acciones a la barra
		JToolBar bar = new JToolBar();
		bar.add(loadEventsFile);
		bar.add(guardar);
		bar.add(borrar);
		bar.setFloatable(false);
		
		//Y al menú
		file.add(loadEventsFile);
		file.add(guardar);
		return bar;
	}
	
	private void loadFile(JTextArea fichero) {
		
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        ".ini Files", "ini");
		   chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(fichero);
		File file = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) { 
		  file = chooser.getSelectedFile();    
		}
		try {
			controlador.modifyInputStream(new FileInputStream(file));
		}
		catch (Exception e){
			
		}
		controlador.cargarEventos();
		// Cargar eventos en la cola de eventos (tabla central)
		try {
			@SuppressWarnings("resource")
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			line = in.readLine();
			while(line != null) {
				  fichero.append(line + "\n");
				  line = in.readLine();
				}
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
	
	private JToolBar createComponents2(JMenu simulator, JSpinner spinner, JTextArea reports) {
		
		//TODO: añadir eventos a la tabla pero solo hasta el tick que es
		SimulatorAction events = new SimulatorAction(
				"Eventos", "events.png", "Añadir eventos a la cola hasta el tick deseado", KeyEvent.
				VK_E, "control E", ()->controlador.cargarEventos());
		
		SimulatorAction play = new SimulatorAction(
				"Ejecutar", "play.png", "Ejecutar la simulación", KeyEvent.
				VK_P, "control P", ()-> controlador.run((int) spinner.getValue()));
		
		//TODO: el reset es un poco cutre
		SimulatorAction reset = new SimulatorAction(
				"Resetear", "reset.png", "Resetear la simulación", KeyEvent.
				VK_R, "control R", ()->controlador.reset());
		
		SimulatorAction redirectOutput = new SimulatorAction(
				"Ejecutar y generar reporte", "report.png", "Corre la simulación generando el reporte automáticamente",
				KeyEvent.VK_B, "control B", ()->{
					TextStream ts = new TextStream(reports);
					controlador.modifyOutputStream(ts);
					controlador.run((int) spinner.getValue());					
		});
		
		//Añadimos las funcionalidades a las barras
		JToolBar bar = new JToolBar();
		bar.add(events);
		bar.add(play);
		bar.add(reset);
		bar.setFloatable(false);
		
		//Y al menú
		simulator.add(play);
		simulator.add(reset);
		simulator.add(redirectOutput);
		return bar;
	}
	
	private JToolBar createComponents3(JMenu file, JMenu generate, JTextArea reports) {
		
		SimulatorAction generateReport = new SimulatorAction(
				"Generar Reporte", "report.png", "Generar el reporte de la simulación", KeyEvent.
				VK_G, "control G", ()-> {
					reports.setText("");
					controlador.generarInformes(new TextStream(reports));
					});
		
		SimulatorAction deleteReport = new SimulatorAction(
				"Borrar Reporte", "delete_report.png", "Borrar el reporte de la simulación", KeyEvent.
				VK_B, "control B", ()->reports.setText(""));
		
		SimulatorAction saveReport = new SimulatorAction(
				"Guardar Reporte", "save_report.png", "Guardar el reporte de la simulación", KeyEvent.
				VK_G, "control shift G", ()->saveFile(reports, "Guardar reportes como..."));
		
		SimulatorAction salir = new SimulatorAction(
				"Salir", "exit.png", "Salir de la aplicacion", KeyEvent.VK_S, "control shift S", ()->System.exit(0));
		
		JToolBar bar = new JToolBar();
		bar.add(generateReport);
		bar.add(deleteReport);
		bar.add(saveReport);
		bar.setFloatable(false);
		bar.add(salir);
			
		generate.add(generateReport);
		generate.add(deleteReport);
		
		file.add(saveReport);
		file.add(salir);
		
		return bar;
	}
	
	private void saveFile(JTextArea reports, String inputDialog) {
		
		 String filename = JOptionPane.showInputDialog(inputDialog);
	        JFileChooser savefile = new JFileChooser();
	        savefile.setSelectedFile(new File(filename));
	        BufferedWriter writer;
	        int sf = savefile.showSaveDialog(null);
	        if(sf == JFileChooser.APPROVE_OPTION) {
	            try {
	            	writer = new BufferedWriter(new FileWriter(savefile.getSelectedFile()));
	            	writer.write(reports.getText());
	                writer.close();
	                JOptionPane.showMessageDialog(null, "File has been saved","File Saved",JOptionPane.INFORMATION_MESSAGE);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	}
	
	//TODO: cosas
	public void registered(UpdateEvent ue) {
		vehiclesTable.actualizar((ArrayList<? extends Describable>) ue.getVehicles());		
	}
	public void reset(UpdateEvent ue) {}
	public void newEvent(UpdateEvent ue) {}
	public void advanced(UpdateEvent ue) {
		vehiclesTable.actualizar((ArrayList<? extends Describable>) ue.getVehicles());		
		repaint();
	}
	public void error(UpdateEvent ue, String error) {}
	
	/*
	public static void main(String ... args) {
		SwingUtilities.invokeLater(()-> new SimulatorLayout());
	}
	*/
}



/*
 * String t = new String(Files.readAllBytes(f.toPath()). "UTF-8"); // "ISO-8859-1"
 * 
 *  Files.write(f.toPath(), t.getBytes("UTF-8"));* 
 * 
 * 
 * */
 