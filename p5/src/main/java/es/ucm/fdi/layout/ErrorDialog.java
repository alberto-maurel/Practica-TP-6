package es.ucm.fdi.layout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ErrorDialog extends JDialog {

	private int _status;

	public ErrorDialog(String error) {
		super(new JDialog(), true);
		setTitle("Error");
		_status = 0;
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		mainPanel.add(buttonsPanel, BorderLayout.PAGE_END);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 1;
				ErrorDialog.this.setVisible(false);
			}
		});
		buttonsPanel.add(okButton);

		mainPanel.add(buttonsPanel, BorderLayout.PAGE_END);

		JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel texto = new JLabel();
		texto.setVerticalAlignment(JLabel.CENTER);
		infoPanel.add(texto);
		texto.setText(error);
		
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		mainPanel.add(infoPanel, BorderLayout.PAGE_START);

		setContentPane(mainPanel);
		setMinimumSize(new Dimension(200, 200));
		setVisible(false);
	}

	public int open() {
		setLocation(getParent().getLocation().x + 400, getParent().getLocation().y + 400);
		pack();
		setVisible(true);
		return _status;
	}

}
