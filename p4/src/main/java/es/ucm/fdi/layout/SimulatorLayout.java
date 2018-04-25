package es.ucm.fdi.layout;

import javax.swing.text.Style;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.control.SimulatorAction;
import es.ucm.fdi.extra.graphlayout.GraphComponent;
import es.ucm.fdi.extra.graphlayout.GraphLayoutExample;
import es.ucm.fdi.model.Describable;
import es.ucm.fdi.model.Junction;
import es.ucm.fdi.model.Road;
import es.ucm.fdi.model.TrafficSimulator.Listener;
import es.ucm.fdi.model.TrafficSimulator.UpdateEvent;
import es.ucm.fdi.model.Vehicle;
import es.ucm.fdi.util.TextStream;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings("serial")
public class SimulatorLayout extends JFrame implements Listener {
	
	Controller controlador;
	SimulatorTable vehiclesTable;
	SimulatorTable roadsTable;
	SimulatorTable junctionsTable;
	SimulatorTable eventsTable;
	JSpinner spinner;
	JTextPane tiempoAct;
	JTextArea fichero;
	
	
	GraphLayoutClass grafo;
	Boolean eventosCargados;
	
	//Botones
	private SimulatorAction loadEventsFile;
	private SimulatorAction guardar;
	private SimulatorAction borrar;
	private SimulatorAction events;
	private SimulatorAction play;
	private SimulatorAction reset;
	private SimulatorAction redirectOutput;
	private SimulatorAction generateReport;
	private SimulatorAction deleteReport;
	private SimulatorAction saveReport;
	private SimulatorAction salir;
	
	
	/**
	 * Creación del Layout principal
	 * @param ctrl - Controlador de la aplicación
	 */
	public SimulatorLayout(Controller ctrl) {
		super("Traffic Simulator");
		controlador = ctrl;
		eventosCargados = false;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Creación del panel superior
		//
		//
		String[] columnNamesQueue = {"#", "Time", "Type", "Involves"};
		ArrayList<Vehicle> eventsArray = new ArrayList<>();
		
		JPanel upperPanel = new JPanel();
		//Por un lado los JTextArea
		fichero = new JTextArea();
		fichero.setEditable(true);
		JTextArea reports = new JTextArea();
		reports.setEditable(false);
		//Y por otro la tabla de eventos
		eventsTable = new SimulatorTable("Events Queue", columnNamesQueue, eventsArray);
		
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
		upperPanel.add(createTextAreaPanel("Fichero", fichero, new Dimension(200,200)));
		upperPanel.add(eventsTable);
		upperPanel.add(createTextAreaPanel("Reports", reports, new Dimension(200,200)));	
		
		
		//Creación de la barra superior
		//
		//
		spinner = new JSpinner();
		tiempoAct = new JTextPane();
		addBars(fichero, reports);
		
		
		//Creación del panel inferior izquierdo
		//
		//
		String[] columnNamesVehicle = {"ID", "Road", "Location", "Speed", "Km", "Faulty units", "Itinerary"};
		String[] columnNamesRoad = {"ID", "Source", "Target", "Length", "Max Speed", "Vehicles"};
		String[] columnNamesJunction = {"ID", "Green", "Red"};
		ArrayList<Vehicle> vehiclesArray = new ArrayList<>();
		ArrayList<Road> roadsArray = new ArrayList<>();
		ArrayList<Junction> junctionsArray = new ArrayList<>();
		
		JPanel leftLowerPanel = new JPanel();
		leftLowerPanel.setLayout(new BoxLayout(leftLowerPanel, BoxLayout.Y_AXIS));
		vehiclesTable = new SimulatorTable("Vehicles", columnNamesVehicle , vehiclesArray);
		leftLowerPanel.add(vehiclesTable);
		roadsTable = new SimulatorTable("Roads", columnNamesRoad , roadsArray);
		leftLowerPanel.add(roadsTable);
		junctionsTable = new SimulatorTable("Junctions", columnNamesJunction , junctionsArray);
		leftLowerPanel.add(junctionsTable);
		
		leftLowerPanel.setSize(100, 100);
		leftLowerPanel.setBackground(Color.WHITE);
		
		
		//Creación del grafo
		//
		//
		grafo = new GraphLayoutClass();
		
		
		//Montamos todo el layout junto
		//
		//
		JSplitPane lowerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftLowerPanel, grafo);
		JSplitPane middleSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperPanel, lowerSplit);		
		add(middleSplit, BorderLayout.CENTER);
		
		setSize(1000, 1000);
		setVisible(true);
		
		middleSplit.setDividerLocation(.35);
		lowerSplit.setDividerLocation(.5);
		controlador.addSimulatorListener(this);
		controlador.modifyInputStream(new ByteArrayInputStream(fichero.getText().getBytes()));
	}
	
	private JScrollPane createTextAreaPanel(String title, JTextArea tArea, Dimension d) {
		Border b = BorderFactory.createLineBorder(Color.black, 2);
		JScrollPane p = new JScrollPane(tArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		p.setPreferredSize(d);
		p.setBorder(BorderFactory.createTitledBorder(b, title));
		
		/* En caso de que se haya añadido un -i archivo.txt 
		 * a los argumentos de main, lo mostramos por defecto.
		 * Tenemos en cuenta más adelante que -o es ignorado
		 */
		if (controlador.getInput() != null) {
			BufferedReader in = new BufferedReader(new InputStreamReader(controlador.getInput()));
			try {
				tArea.read(in, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return p;
		
	}
	
	/**
	 * Crea la barra de botones y la añade al layout principal
	 * @param fichero - JTextÁrea en el que se muestran los eventos cargados
	 * @param reports - JTextÁrea en el que se muestran los reportes generados
	 */
	private void addBars(JTextArea fichero, JTextArea reports) {
		//Creamos los menús
		JMenu file = new JMenu("File");
		JMenu simulator = new JMenu("Simulator");
		JMenu generate = new JMenu("Generate");
		
		//Creación de los 3 primeros botones
		//
		JToolBar bar = new JToolBar();
		bar.add(createComponents1(file, fichero));		
		
		
		//Creación de los 3 siguientes botones, spinner y textArea para mostrar el tick actual
		//
		JLabel label = new JLabel("Steps: ");
		spinner.setValue(controlador.getPasos());
		spinner.setMaximumSize(new Dimension(50,50));
		
		/* Tenemos en cuenta el argumento -t
		 * para inicializar el valor del JSpinner.
		 * Por defecto, el valor es 10.
		 */		
		
		JLabel timeLabel = new JLabel("Time: ");
		tiempoAct = new JTextPane();
		tiempoAct.setMaximumSize(new Dimension(50,50));
		tiempoAct.setEditable(false);
		tiempoAct.setText("" + 0);
		
		JToolBar bar2 = new JToolBar();
		bar2.add(label);
		bar2.add(spinner);
		bar2.add(timeLabel);
		bar2.add(tiempoAct);
		bar2.setPreferredSize(new Dimension(8,8));
		bar2.setFloatable(false);
		bar.add(createComponents2(simulator, reports));
		bar.add(bar2);
		
		//Creación de los últimos botones
		//
		bar.add(createComponents3(file, generate, reports));
		
		
		//Creación de la barra completa
		//
		JMenuBar menu = new JMenuBar();
		menu.add(file);
		menu.add(simulator);
		menu.add(generate);
		setJMenuBar(menu);
		add(bar, BorderLayout.NORTH);
		
	}
	
	/**
	 * Función que crea los tres primeros botones y los añade a las barras de tareas y menú
	 * @param file - Menú file, en el que se insertarán las funcionalidades
	 * @param fichero - JTextÁrea en el que se muestran los eventos cargados
	 * @return JToolBar con los botones creados
	 */
	private JToolBar createComponents1(JMenu file, JTextArea fichero) {
		
		guardar = new SimulatorAction(
				"Guardar fichero de eventos", "save.png", "Guardar fichero de texto",
				KeyEvent.VK_S, "control S", ()->saveFile(fichero, "Guardar eventos como..."));
			
		loadEventsFile = new SimulatorAction(
				"Cargar fichero de eventos", "open.png", "Carga un fichero de eventos", KeyEvent.VK_A, "control A", ()->loadFile(fichero));
			
		borrar = new SimulatorAction(
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
	
	/**
	 * Función que crea los tres segundos botones y los añade a las barras de tareas y menú
	 * @param simulator - Menú simulator, en el que se insertarán los botones
	 * @param spinner - JSpinner que indica los ticks a ejecutar
	 * @param reports - JTextÁrea en el que se muestran los reports de la simulación
	 * @return
	 */
	private JToolBar createComponents2(JMenu simulator, JTextArea reports) {
		events = new SimulatorAction(
				"Eventos", "events.png", "Añadir eventos a la cola hasta el tick deseado", KeyEvent.
				VK_E, "control E", ()->cargarEventos());
		
		play = new SimulatorAction(
				"Ejecutar", "play.png", "Ejecutar la simulación", KeyEvent.
				VK_P, "control P", ()-> controlador.run((int) spinner.getValue()));
		
		//TODO: el reset es un poco cutre
		reset = new SimulatorAction(
				"Resetear", "reset.png", "Resetear la simulación", KeyEvent.
				VK_R, "control R", ()->controlador.reset());
		
		redirectOutput = new SimulatorAction(
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
	
	/**
	 * Función que crea los últimos botones, y los añade a las barras de tareas y menú
	 * @param file - Menú file, en el que se insertarán las funcionalidades
	 * @param generate - Menú generate, en el que se insertarán las funcionalidades
	 * @param reports - JTextÁrea en el que se muestran los reports de la simulación
	 * @return
	 */
	private JToolBar createComponents3(JMenu file, JMenu generate, JTextArea reports) {
		
		generateReport = new SimulatorAction(
				"Generar Reporte", "report.png", "Generar el reporte de la simulación", KeyEvent.
				VK_G, "control G", ()-> {
					reports.setText("");
					controlador.generarInformes(new TextStream(reports));
					});
		
		deleteReport = new SimulatorAction(
				"Borrar Reporte", "delete_report.png", "Borrar el reporte de la simulación", KeyEvent.
				VK_B, "control B", ()->reports.setText(""));
		
	    saveReport = new SimulatorAction(
				"Guardar Reporte", "save_report.png", "Guardar el reporte de la simulación", KeyEvent.
				VK_G, "control shift G", ()->saveFile(reports, "Guardar reportes como..."));
		
		salir = new SimulatorAction(
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
	
	/**
	 * Carga los eventos de un fichero
	 * @param fichero - Fichero del cual cargamos los eventos
	 */
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
	
	/**
	 * Guarda los reportes en un fichero
	 * @param reports - JTextÁrea en el que se muestran los reports de la simulación
	 * @param inputDialog
	 */
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
	
	private void cargarEventos() {
		controlador.cargarEventos();
		//Y a continuación los cargamos en el simulador
		controlador.cargarEventosEnElSimulador((int) spinner.getValue()); // (int) spinner.getValue()
	}
	
	//TODO: cosas
	//Implementación listeners
	public void registered(UpdateEvent ue) {
		actualizarLayout(ue);
		grafo.registered(ue);
	}
	
	public void reset(UpdateEvent ue) {
		actualizarLayout(ue);
		grafo.reset(ue);
	}
	
	public void newEvent(UpdateEvent ue) {
		actualizarLayout(ue);
		grafo.registered(ue);		
	}
	
	public void advanced(UpdateEvent ue) {
		actualizarLayout(ue);
		grafo.advanced(ue);
	}
	
	public void error(UpdateEvent ue, String error) {
		
		
	}
	
	/**
	 * Función que agrupa la funcionalidad de refrescar el layout
	 * @param ue - UpdateEvent con la información del listener
	 */
	private void actualizarLayout(UpdateEvent ue) {
		vehiclesTable.actualizar((ArrayList<? extends Describable>) ue.getVehicles());	
		junctionsTable.actualizar((ArrayList<? extends Describable>) ue.getJunctions());
		roadsTable.actualizar((ArrayList<? extends Describable>) ue.getRoads());	
		eventsTable.actualizar((ArrayList<? extends Describable>) ue.getEventQueue());	
		tiempoAct.setText("" + controlador.getPasos());
		habilitarBotones();
		repaint();
	}
	
	private void habilitarBotones() {
		if(controlador.hayEventosCargados()) {
			guardar.setEnabled(true);
			borrar.setEnabled(true);
			play.setEnabled(true);
			reset.setEnabled(true);
			redirectOutput.setEnabled(true);
			generateReport.setEnabled(true);
			deleteReport.setEnabled(true);
			saveReport.setEnabled(true);
		}
		else {
			guardar.setEnabled(false);
			borrar.setEnabled(false);
			play.setEnabled(false);
			reset.setEnabled(false);
			redirectOutput.setEnabled(false);
			generateReport.setEnabled(false);
			deleteReport.setEnabled(false);
			saveReport.setEnabled(false);
		}
	}
	
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
 