package es.ucm.fdi.layout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.control.SimulatorAction;
import es.ucm.fdi.model.TrafficSimulator.Listener;
import es.ucm.fdi.model.TrafficSimulator.UpdateEvent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@SuppressWarnings("serial")
public class SimulatorLayout extends JFrame implements Listener {
	Controller controlador;
	
	public SimulatorLayout(Controller ctrl) {
		super("Traffic Simulator");
		controlador = ctrl;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Creación de uno de los paneles que tiene las tablas dentro
		JTextArea fichero = new JTextArea();
		fichero.setEditable(true);
		JTextArea reports = new JTextArea();
		reports.setEditable(true);
		
		addBars(fichero, reports);		
			
		String[] columnNames = {"Nombre", "Apellido1", "Apellido2"};
		Object[][] data = {
				{"Laura", "Castilla", "Castellano"},
				{"Alberto", "Maurel", "Serrano"}
				}; 
		
		JTable table1 = new JTable(data, columnNames);
		table1.setSize(100,100);
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
	
	private void addBars(JTextArea fichero, JTextArea reports){
		SimulatorAction salir = new SimulatorAction(
			"Salir", "exit.png", "Salir de la aplicacion", KeyEvent.VK_S, "control shift S", ()->System.exit(0));
		
		JMenu file = new JMenu("File");
		JMenu simulator = new JMenu("Simulator");
		JMenu generate = new JMenu("Generate");
		
		JToolBar bar = new JToolBar();
		
		bar.add(createComponents1(file, fichero));
		
		JLabel label = new JLabel("Steps: ");
		JSpinner selector = new JSpinner();
		selector.setPreferredSize(new Dimension(50,50));
		JToolBar bar2 = new JToolBar();
		
		bar2.add(label);
		bar2.add(selector);
		
		JLabel label2 = new JLabel("Time: ");
		JTextArea text = new JTextArea();
		text.setPreferredSize(new Dimension(50,50));
		
		bar2.add(label2);
		bar2.add(text);
		
		bar2.setPreferredSize(new Dimension(8,8));
		bar2.setFloatable(false);
		
		bar.add(createComponents2(simulator, selector));
		bar.add(bar2);
		bar.add(createComponents5(file, generate, reports));
		
		bar.add(salir);
		file.add(salir);
		
		JMenuBar menu = new JMenuBar();
		menu.add(file);
		menu.add(simulator);
		menu.add(generate);
		setJMenuBar(menu);
		add(bar, BorderLayout.NORTH);
	}
	
	private JToolBar createComponents1(JMenu file, JTextArea fichero) {
		SimulatorAction guardar = new SimulatorAction(
				"Guardar", "save.png", "Guardar cosas", KeyEvent.VK_S, "control S", ()->System.err.println("guardando... "));
		
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        ".ini Files", "ini");
		    fileChooser.setFileFilter(filter);
		
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
	
	@SuppressWarnings("unused")
	private void loadFile(JTextArea fichero) {
		JFileChooser chooser = new JFileChooser();
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
		
		// Crear la lista de eventos con el controller (JOptionPane para error)
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
	
	private JToolBar createComponents2(JMenu simulator, JSpinner spinner) {
		SimulatorAction events = new SimulatorAction(
				"Eventos", "events.png", "???", KeyEvent.
				VK_E, "control E", ()->System.err.println("???..."));
		
		SimulatorAction play = new SimulatorAction(
				"Ejecutar", "play.png", "Ejecutar la simulación", KeyEvent.
				VK_P, "control P", ()-> controlador.run((int) spinner.getValue()));
		
		SimulatorAction reset = new SimulatorAction(
				"Resetear", "reset.png", "Resetear la simulación", KeyEvent.
				VK_R, "control R", ()->System.err.println("Reseteando..."));
		
		//Añadimos las funcionalidades a las barras
		JToolBar bar = new JToolBar();
		bar.add(events);
		bar.add(play);
		bar.add(reset);
		bar.setFloatable(false);
		
		//Y al menú
		simulator.add(play);
		simulator.add(reset);
		return bar;
	}
	

	private JToolBar createComponents5(JMenu file, JMenu generate, JTextArea reports) {
		SimulatorAction generateReport = new SimulatorAction(
				"Generar Reporte", "report.png", "Generar el reporte de la simulación", KeyEvent.
				VK_G, "control G", ()->System.err.println("Generando..."));
		
		SimulatorAction deleteReport = new SimulatorAction(
				"Borrar Reporte", "delete_report.png", "Borrar el reporte de la simulación", KeyEvent.
				VK_B, "control B", ()->reports.setText(""));
		
		SimulatorAction saveReport = new SimulatorAction(
				"Guardar Reporte", "save_report.png", "Guardar el reporte de la simulación", KeyEvent.
				VK_G, "control shift G", ()->saveSimulationReport(reports));
		
		JToolBar bar = new JToolBar();
		bar.add(generateReport);
		bar.add(deleteReport);
		bar.add(saveReport);
		bar.setFloatable(false);
		
		generate.add(generateReport);
		generate.add(deleteReport);
		file.add(saveReport);
		return bar;
	}
	
	private void saveSimulationReport(JTextArea reports) {
		 String filename = JOptionPane.showInputDialog("Guardar como...");
	        JFileChooser savefile = new JFileChooser();
	        savefile.setSelectedFile(new File(filename));
	        BufferedWriter writer;
	        int sf = savefile.showSaveDialog(null);
	        if(sf == JFileChooser.APPROVE_OPTION){
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
	
	public interface Describable {
		/**
		* @param out - a map to fill in with key- value pairs
		* @return the passed- in map, with all fields filled out.
		*/
		void describe(Map<String, String> out);
	}
	
	/*private class ListOfMapsTableModel extends AbstractTableModel {
		private String[] fieldNames;
		private MultiTreeMap<String, String> elements = new MultiTreeMap<>();
		
		@Override
		public String getColumnName(int columnIndex) {
			return fieldNames[columnIndex];
		}
		@Override
		public int getRowCount() {
			return elements.size();
		}
		@Override
		public int getColumnCount() {
			return fieldNames.length;
		}
		@Override // ineficiente: ¿puedes mejorarlo?
		public Object getValueAt(int rowIndex, int columnIndex) {
			return elements.get(rowIndex)
					.describe(new HashMap<String, String>())
						.get(fieldNames[columnIndex]);
		}
	}*/
	
	//TODO: cosas
	public void registered(UpdateEvent ue) {}
	public void reset(UpdateEvent ue) {}
	public void newEvent(UpdateEvent ue) {}
	public void advanced(UpdateEvent ue) {}
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
 