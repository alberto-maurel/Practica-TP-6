package es.ucm.fdi.layout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import es.ucm.fdi.control.Controller;
import es.ucm.fdi.control.SimulatorAction;
import es.ucm.fdi.control.Stepper;
import es.ucm.fdi.extra.dialog.DialogWindow;
import es.ucm.fdi.model.Describable;
import es.ucm.fdi.model.Junction;
import es.ucm.fdi.model.Road;
import es.ucm.fdi.model.TrafficSimulator.Listener;
import es.ucm.fdi.model.TrafficSimulator.UpdateEvent;
import es.ucm.fdi.model.Vehicle;
import es.ucm.fdi.model.Event;
import es.ucm.fdi.util.TextStream;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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

@SuppressWarnings("serial")
public class SimulatorLayout extends JFrame implements Listener {
	
	Controller controlador;
	SimulatorTable vehiclesTable;
	SimulatorTable roadsTable;
	SimulatorTable junctionsTable;
	SimulatorTable eventsTable;
	JSpinner spinner;
	JSpinner delaySpinner;
	JTextField tiempoAct;
	JTextArea fichero;
	JLabel lowerBarMessage;	
	GraphLayoutClass grafo;
	Boolean eventosCargados;
	Stepper stepper;
	
	//Botones
	private SimulatorAction loadEventsFile;
	private SimulatorAction guardar;
	private SimulatorAction borrar;
	private SimulatorAction events;
	private SimulatorAction play;
	private SimulatorAction stop;
	private SimulatorAction reset;
	private SimulatorAction getOutput;
	private SimulatorAction generateReport;
	private SimulatorAction deleteReport;
	private SimulatorAction saveReport;
	private SimulatorAction salir;
	private JCheckBoxMenuItem redirectOutput;
	
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
		String[] columnNamesQueue = {"#", "Time", "Type"};
		ArrayList<Event> eventsArray = new ArrayList<>();
		
		JPanel upperPanel = new JPanel();
		//Por un lado los JTextArea
		fichero = new JTextArea();
		fichero.setEditable(true);
		JTextArea reports = new JTextArea();
		reports.setEditable(false);
		//Y por otro la tabla de eventos
		eventsTable = new SimulatorTable("Events Queue", columnNamesQueue, 
				(ArrayList<? extends Describable>) eventsArray);
		
		upperPanel.setLayout(new GridLayout(1, 3));
		upperPanel.add(createTextAreaPanel("Fichero", fichero, new Dimension(200,200)));
		upperPanel.add(eventsTable);
		upperPanel.add(createTextAreaPanel("Reports", reports, new Dimension(200,200)));	
		
		
		//Creación de la barra superior
		delaySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 100));
		spinner = new JSpinner();
		addBars(fichero, reports);
		
		
		//Creación de la barra inferior
		JToolBar lowerBar = new JToolBar();
		lowerBar.setFloatable(false);
		lowerBarMessage = new JLabel(" ");
		lowerBar.add(lowerBarMessage);
		add(lowerBar, BorderLayout.SOUTH);
		
		
		//Creación del panel inferior izquierdo
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
		grafo = new GraphLayoutClass();
		
		// Añadimos PopUpMenu para plantillas de eventos
		TemplateMenu tMenu = new TemplateMenu(fichero, loadEventsFile, guardar, borrar);
		// Conectamos el menú popup al editor de texto
		fichero.setComponentPopupMenu(tMenu);				
		
		//Montamos todo el layout junto
		JSplitPane lowerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftLowerPanel, grafo);
		JSplitPane middleSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperPanel, lowerSplit);		
		add(middleSplit, BorderLayout.CENTER);
		
		setSize(1000, 1000);
		setVisible(true);
		
		middleSplit.setDividerLocation(.35);
		lowerSplit.setDividerLocation(.5);
		/* Para el modo batch se registra el controlador como listener,
		 * por lo que para notificar los errores solo con diálogo, damos
		 * de baja ese primer listener creado y añadimos este
		 */
		if (controlador.getListenersSize() > 0) {
			controlador.removeFirstListener();
			controlador.addSimulatorListener(this);
		}
		controlador.modifyInputStream(new ByteArrayInputStream(fichero.getText().getBytes()));
	}
	
	/*
	 * Creación de un panel de texto
	 * @param title - Título del recuadro
	 * @param tArea - JTextArea donde se muestran los eventos/reportes
	 * @param d - Dimensión del JScrollPane creado
	 */
	private JScrollPane createTextAreaPanel(String title, JTextArea tArea, Dimension d) {
		Border b = BorderFactory.createLineBorder(Color.black, 2);
		JScrollPane p = new JScrollPane(tArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
				JOptionPane.showMessageDialog(this,	getExceptionCause(e), 
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		return p;
		
	}
	
	/**
	 * Crea la barra de botones y la añade al layout principal
	 * @param fichero - JTextArea en el que se muestran los eventos cargados
	 * @param reports - JTextArea en el que se muestran los reportes generados
	 */
	private void addBars(JTextArea fichero, JTextArea reports) {
		//Creamos los menús
		JMenu file = new JMenu("File");
		JMenu simulator = new JMenu("Simulator");
		JMenu generate = new JMenu("Generate");
		
		//Creación de los 3 primeros botones
		JToolBar bar = new JToolBar();
		bar.add(createComponents1(file, fichero));		
		
		
				
		
		//Creación de los 3 siguientes botones, spinner y textArea para mostrar el tick actual
		JLabel delayLabel = new JLabel("Delay: ");
		delaySpinner.setValue(0);
		delaySpinner.setMaximumSize(new Dimension(50,50));
		
		JLabel label = new JLabel("Steps: ");
		spinner.setValue(controlador.getPasosAEjecutar());
		spinner.setMaximumSize(new Dimension(50,50));
		
		/* Tenemos en cuenta el argumento -t
		 * para inicializar el valor del JSpinner.
		 * Por defecto, el valor es 10.
		 */		
		
		JLabel timeLabel = new JLabel("Time: ");
		tiempoAct = new JTextField();
		tiempoAct.setBackground(Color.WHITE);
		tiempoAct.setMaximumSize(new Dimension(50,50));
		tiempoAct.setEditable(false);
		tiempoAct.setText("" + 0);
		
		JToolBar bar2 = new JToolBar();
		bar2.add(delayLabel);
		bar2.add(delaySpinner);
		bar2.add(label);
		bar2.add(spinner);
		bar2.add(timeLabel);
		bar2.add(tiempoAct);
		bar2.setPreferredSize(new Dimension(8,8));
		bar2.setFloatable(false);
		bar.add(createComponents2(simulator, reports));
		bar.add(bar2);
		
		//Creación de los últimos botones
		bar.add(createComponents3(file, generate, reports));
		
		//Creación de la barra completa
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
		
		loadEventsFile = new SimulatorAction(
					"Cargar fichero de eventos", "open.png", "Carga un fichero de eventos",
				KeyEvent.VK_A, "control A", ()->loadFile(fichero));
		
		guardar = new SimulatorAction(
					"Guardar fichero de eventos", "save.png", "Guardar fichero de texto",
				KeyEvent.VK_S, "control S", ()->saveFile(fichero, "Guardar eventos como...",
					"Eventos guardados con éxito", "events.ini"));
			
		borrar = new SimulatorAction(
					"Borrar", "clear.png", "Borra la lista de eventos", KeyEvent.
				VK_C, "control C", ()->fichero.setText(""));
		
		
		guardar.setEnabled(false);
		borrar.setEnabled(false);
		
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
	
	private void ejecutarSimulacionPorPasos(){
		;
		stepper = new Stepper(
				() -> SwingUtilities.invokeLater(() -> {
						habilitarBotones(false);
						stop.setEnabled(true);
					})		
				, 
				() -> controlador.run(1), 
				() -> SwingUtilities.invokeLater(() -> {
					habilitarBotones(true);
					stop.setEnabled(false);
				})
		);
		stepper.start(
				(int) spinner.getValue(), 
				(int) delaySpinner.getValue()
		);
		
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
				"Eventos", "events.png", "Añadir eventos a la cola", KeyEvent.
				VK_E, "control E", ()-> cargarEventos());
		
		play = new SimulatorAction(
				"Ejecutar", "play.png", "Ejecutar la simulación", KeyEvent.
				VK_P, "control P", ()-> ejecutarSimulacionPorPasos());
		
		stop = new SimulatorAction(
				"Stop", "stop.png", "Parar la simulación", KeyEvent.
				VK_S, "control S", () -> {stepper.stop();});
		
		reset = new SimulatorAction(
				"Resetear", "reset.png", "Resetear la simulación", KeyEvent.
				VK_R, "control R", ()-> { controlador.reset(); reports.setText("");});
		
		getOutput = new SimulatorAction(
				"Ejecutar y generar reporte", "report.png", 
			"Corre la simulación generando el reporte automáticamente",
				KeyEvent.VK_B, "control B", ()->{
					TextStream ts = new TextStream(reports);
					controlador.modifyOutputStream(ts);
					controlador.run((int) spinner.getValue());					
		});
		
		redirectOutput = new JCheckBoxMenuItem("Redirigir salida", false);
		
		redirectOutput.addItemListener(new ItemListener() {
		    public void itemStateChanged(ItemEvent e) {   
		       if(redirectOutput.getState()){
		           controlador.modifyOutputStream(new TextStream(reports));
		       } else {
		    	   controlador.modifyOutputStream(System.out);
		       }
		    }
		});
		
		play.setEnabled(false);
		stop.setEnabled(false);
		getOutput.setEnabled(false);
		redirectOutput.setEnabled(false);
		
		//Añadimos las funcionalidades a las barras
		JToolBar bar = new JToolBar();
		bar.add(events);
		bar.add(play);
		bar.add(stop);
		bar.add(reset);
		bar.setFloatable(false);
		
		//Y al menú
		simulator.add(play);
		simulator.add(stop);
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
					"Generar Reporte", "report.png", "Generar el reporte de la simulación", 
				KeyEvent.VK_G, "control G", () -> generateSelectedReports(reports));
		
		deleteReport = new SimulatorAction(
					"Borrar Reporte", "delete_report.png", "Borrar el reporte de la simulación",
				KeyEvent.VK_B, "control B", ()->reports.setText(""));
		
	    saveReport = new SimulatorAction(
					"Guardar Reporte", "save_report.png", "Guardar el reporte de la simulación",
				KeyEvent.VK_G, "control shift G", ()->saveFile(reports, "Guardar reportes como...",
					"Informe guardado con éxito", "reports.out"));
		
		salir = new SimulatorAction(
				"Salir", "exit.png", "Salir de la aplicacion", KeyEvent.VK_S, "control shift S", ()->System.exit(0));
		
		generateReport.setEnabled(false);
		deleteReport.setEnabled(false);
		saveReport.setEnabled(false);
		
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
	 * Función que corre la simulación
	 */
	private void runSimulation() {
		controlador.run((int) spinner.getValue());
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
		  try {
				controlador.modifyInputStream(new FileInputStream(file));
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(this,	getExceptionCause(e), 
						"Error", JOptionPane.ERROR_MESSAGE);
			}		 
			try {
				controlador.cargarEventos();
				@SuppressWarnings("resource")
				BufferedReader in = new BufferedReader(new FileReader(file));
				String line;
				line = in.readLine();
				fichero.setText("");
				while(line != null) {
					  fichero.append(line + "\n");
					  line = in.readLine();
					}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this,	getExceptionCause(e), 
						"Error", JOptionPane.ERROR_MESSAGE);
			}	
		}		
	}
		
	/**
	 * Guarda JTextArea en un fichero
	 * @param text - JTextArea en el que se muestran el archivo a guardar
	 * @param inputDialog
	 */
	private void saveFile(JTextArea text, String inputDialog, String lowerBarText, String selected) {	
		 JFileChooser savefile = new JFileChooser();
	     savefile.setSelectedFile(new File(selected));
	     BufferedWriter writer;
	     int sf = savefile.showSaveDialog(null);
	     if(sf == JFileChooser.APPROVE_OPTION) {
	         try {
	        	writer = new BufferedWriter(new FileWriter(savefile.getSelectedFile()));
	           	writer.write(text.getText());
	            writer.close();
	            JOptionPane.showMessageDialog(null, "File has been saved","File Saved", 
	            		JOptionPane.INFORMATION_MESSAGE);
	            lowerBarMessage.setText(lowerBarText);
	         } catch (IOException e) {
	        	 JOptionPane.showMessageDialog(this, getExceptionCause(e), 
							"Error", JOptionPane.ERROR_MESSAGE);
	         }
	     }
	}
	
	private void cargarEventos() {
		//Reiniciamos el flujo para poder volver a coger los datos
		controlador.modifyInputStream(new ByteArrayInputStream(fichero.getText().getBytes()));
		try {
			controlador.cargarEventos();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,	getExceptionCause(e), 
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		//Y a continuación los cargamos en el simulador
		controlador.cargarEventosEnElSimulador();			
	}
	
	//Implementación listeners
	public void registered(UpdateEvent ue) {
		SwingUtilities.invokeLater(() ->{
			actualizarLayout(ue, false);
			grafo.registered(ue);
		});
	}
	
	public void reset(UpdateEvent ue) {
		SwingUtilities.invokeLater(() ->{
			actualizarLayout(ue, false);
			grafo.reset(ue);
			lowerBarMessage.setText("El simulador se ha reseteado correctamente =D");
		});
	}
	
	public void newEvent(UpdateEvent ue) {
		SwingUtilities.invokeLater(() ->{
			actualizarLayout(ue, true);
			grafo.registered(ue);		
			lowerBarMessage.setText("Los eventos se han añadido correctamente =D");
		});
	}
	
	public void advanced(UpdateEvent ue) {
		SwingUtilities.invokeLater(() ->{
			actualizarLayout(ue, false);
			grafo.advanced(ue);
			lowerBarMessage.setText("La simulación se ha ejecutado correctamente =D");
		});
	}
	
	/**
	 * Genera el cuadro informativo del error
	 * @param ue - UpdateEvent con la información del listener
	 * @param error - String con la información del error concreto a mostrar
	 */
	public void error(UpdateEvent ue, String errorMessage) {
		error(errorMessage);
	}
	/**
	 * Genera el cuadro informativo del error
	 * @param error - String con la información del error concreto a mostrar
	 */
	public void error(String errorMessage) {
		lowerBarMessage.setText("Error");
		JOptionPane.showMessageDialog(this,	errorMessage, 
				"Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Función que agrupa la funcionalidad de refrescar el layout
	 * @param ue - UpdateEvent con la información del listener
	 */
	private void actualizarLayout(UpdateEvent ue, boolean conBotonesInclusive) {
		vehiclesTable.actualizar(ue.getVehicles());	
		junctionsTable.actualizar(ue.getJunctions());
		roadsTable.actualizar(ue.getRoads());	
		eventsTable.actualizar(ue.getEventQueue());	
		tiempoAct.setText("" + (controlador.getPasos()));
		habilitarBotones(conBotonesInclusive);
	}
	
	private void habilitarBotones(boolean activar) {

		redirectOutput.setEnabled(activar);

		for (Action a : new Action[] {
				guardar, borrar, play, reset, 
				getOutput, generateReport, deleteReport,
				deleteReport, saveReport
		} ) {
			a.setEnabled(activar);
		}
	}
	
	void generateSelectedReports(JTextArea reports) {
		reports.setText("");	
		
		ArrayList<String> vehicles  = new ArrayList<>();
		ArrayList<String> roads = new ArrayList<>();
		ArrayList<String> junctions = new ArrayList<>();
	
		for(Junction j: controlador.getJunctions()){
			junctions.add(j.getId());
		}
		
		for(Road j: controlador.getRoads()){
			roads.add(j.getId());
		}
	
		for(Vehicle j: controlador.getVehicles()){
			vehicles.add(j.getId());
		}
		
		DialogWindow newDialog = new DialogWindow(this);
		newDialog.setData(vehicles, roads, junctions); 
		newDialog.open();
		
		controlador.generarInformes(new TextStream(reports), newDialog.getSelectedJunctions(),
				newDialog.getSelectedRoads(), newDialog.getSelectedVehicles());	
	}
	
	private String getExceptionCause(Exception e) {
		String s = "";
		Throwable t = e;
		do {
			s += t.getMessage();
			t = t.getCause();
		}
		while (t != null);
		return s;
	}
	
}
 