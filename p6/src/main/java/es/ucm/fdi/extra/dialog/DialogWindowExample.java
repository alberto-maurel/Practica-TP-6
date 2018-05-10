package es.ucm.fdi.extra.dialog;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class DialogWindowExample extends JFrame  {

	private DialogWindow _dialog;
	
	List<String> _vehicles;
	List<String> _roads;
	List<String> _junctions;
	
	public DialogWindowExample() {
		super("Dialog Example");
		initGUI();
	}

	private void initGUI() {

		// initialize lists;
		_vehicles  = new ArrayList<>();
		_roads  = new ArrayList<>();
		_junctions  = new ArrayList<>();
		for(int i=0;i<10; i++) {
			_vehicles.add("v"+i);
			_roads.add("r"+i);	
			_junctions.add("j" + i);
		}
		
		JPanel mainPanel = new JPanel();
		mainPanel.add(new JLabel("If you click "));
		
		_dialog = new DialogWindow(this);
		_dialog.setData(_vehicles, _roads, _junctions);
		
		JButton here = new JButton("here");
		here.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int status = _dialog.open();
				if ( status == 0) {
					System.out.println("Canceled");
				} else {
					System.out.println("The following vehicles where selected:");
					for(String s : _dialog.getSelectedVehicles()) {
						System.out.println(s);
					}
					System.out.println("The following roads where selected:");
					for(String s : _dialog.getSelectedRoads()) {
						System.out.println(s);
					}
					System.out.println("The following junctions where selected:");
					for(String s : _dialog.getSelectedJunctions()) {
						System.out.println(s);
					}
				}
			}
		});
		mainPanel.add(here);
		mainPanel.add(new JLabel("a dialog window is opened and the main window blocks."));

		this.setContentPane(mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new DialogWindowExample();
			}
		});
	}
}