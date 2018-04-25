package es.ucm.fdi.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import es.ucm.fdi.extra.graphlayout.GraphComponent;
import es.ucm.fdi.model.Describable;
import es.ucm.fdi.model.TrafficSimulator.Listener;
import es.ucm.fdi.model.TrafficSimulator.UpdateEvent;

public class SimulatorTable extends JPanel {// implements TableModelListener {
	
	private JTable table;
	private String title;
	private String[] fieldNames;
	
	public SimulatorTable(String t, String[] fn, ArrayList<? extends Describable> e) {
		
		super(new BorderLayout());
		
		title = t;
		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black, 2),
				title));
		
		
		fieldNames = fn;
		table = new JTable(new MyTableModel(fn, e));
		
		add(new JScrollPane(table), BorderLayout.CENTER);
		setPreferredSize(new Dimension(100,100));
	}
	
	/*public static void main(String ... args) {
		JFrame jf = new JFrame();
		jf.setSize(400, 400);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.add (new SimulatorTable("test"));
		jf.setVisible(true);
	}*/
	
	private class MyTableModel extends AbstractTableModel {
		
		String[] fieldNames;
		List <? extends Describable> elements;	
		
		public MyTableModel(String[] fn, ArrayList<? extends Describable> e) {
			fieldNames = fn;
			elements = e;
		}
		
		@Override // fieldNames es un String[] con nombrs de col.
		public String getColumnName(int columnIndex) {
			return fieldNames[columnIndex];
		}
		@Override // elements contiene la lista de elementos
		public int getRowCount() {
			return elements.size();
		}
		@Override
		public int getColumnCount() {
			return fieldNames.length;
		}
		@Override // ineficiente: ¿puedes mejorarlo?
		public Object getValueAt(int rowIndex, int columnIndex) {
			HashMap<String, String> out = new HashMap<>();
			elements.get(rowIndex).describe(out);
			return out.get(fieldNames[columnIndex]);
		}	
	}

	public void actualizar(ArrayList<? extends Describable> e) {
		MyTableModel innerTable = new MyTableModel(fieldNames,  e);
		table.setModel(innerTable);
		innerTable.fireTableDataChanged();
	}
}
