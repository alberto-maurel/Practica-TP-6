package es.ucm.fdi.layout;

import javax.swing.SwingUtilities;

import es.ucm.fdi.extra.dialog.DialogWindowExample;

public class LayoutTests {
	//Adjuntamos un main que lanza el cuadro para seleccionar de que objetos queremos generar
	//el output para ver como queda antes de insertarlo en el layout
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new DialogWindowExample();
			}
		});
	}
}
