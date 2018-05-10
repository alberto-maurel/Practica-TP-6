package es.ucm.fdi.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import es.ucm.fdi.model.Describable;

@SuppressWarnings("serial")
public class SimulatorTable extends JPanel {
	
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
		
		add(new JScrollPane(table));
		setPreferredSize(new Dimension(200,200));
		setBackground(Color.WHITE);
	}
	
	private class MyTableModel extends AbstractTableModel {
		
		String[] fieldNames;
		List <? extends Describable> elements;	
		
		public MyTableModel(String[] fn, ArrayList<? extends Describable> e) {
			fieldNames = fn;
			elements = e;
			setBackground(Color.WHITE);
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
		//No hemos mejorado la eficiencia de esta función. Tenemos dos ideas, pero no vemos ninguna
		//de las dos lo suficientemente clara como para implementarla:
		//1. Llamar al describe una única vez y cachear los resultados para no repetir llamadas.
		//	 Sin embargo: ¿Cómo podemos saber la siguiente ocasión si han cambiado o no los elementos?
		//   No vemos claro donde hacer ese cálculo (podríamos hacerlo cuando nos piden el primer 
		//   elemento de la fila, pero no sabemos si llamarán a la función siempre en orden).
		//2. Implementar un describe que devuelva solo un elemento de la tabla. Sin embargo, los 
		//   elementos tendrían que tener una especie de clave que les indicase que elemento devolver,
		//   con lo que el describe se volvería un gran switch.
		public Object getValueAt(int rowIndex, int columnIndex) {
			HashMap<String, String> out = new HashMap<>();
			elements.get(rowIndex).describe(out, Integer.toString(rowIndex));
			return out.get(fieldNames[columnIndex]);
		}	
	}

	public void actualizar(ArrayList<? extends Describable> e) {
		MyTableModel innerTable = new MyTableModel(fieldNames,  e);
		table.setModel(innerTable);
		innerTable.fireTableDataChanged();
	}
}
