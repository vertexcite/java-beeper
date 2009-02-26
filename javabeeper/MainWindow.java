/*
 * Created on 22/10/2003
 */
package javabeeper;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainWindow extends JFrame implements SnoozeObserver {

	private static final long serialVersionUID = 3261204676180469008L;

	JButton snoozeButton = new JButton("Snooze");
	JPanel panel1 = new JPanel();
	JTextField snoozeTimeMinutes = new JTextField("20");

	private SnoozeController controller;

	private void setupWidgets() {
		setTitle("Restart timer");
		getContentPane().add(panel1, BorderLayout.NORTH);
		panel1.setLayout(new GridLayout(2, 2));
		panel1.add(snoozeButton);
		panel1.add(snoozeTimeMinutes);
		panel1.add(new JTextField("You can put an arbitrary label here."));

		ActionListener snoozeActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.startSnoozing(Double.parseDouble(snoozeTimeMinutes.getText()));
			}
		};

		snoozeButton.addActionListener(snoozeActionListener);
		snoozeTimeMinutes.addActionListener(snoozeActionListener);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension max = getMaximumSize();
		setBounds(0, 0, max.width, max.height);
		setVisible(true);
	}

	public MainWindow(SnoozeController paramController) {
		controller = paramController;
		setupWidgets();
	}

	void beepAndShow() {
		Toolkit.getDefaultToolkit().beep();
		setVisible(true);
	}

	@Override
	public void setSnoozeDuration(double snoozeDurationMinutes) {
		snoozeTimeMinutes.setText(Double.toString(snoozeDurationMinutes));
	}

}
