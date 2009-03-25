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

import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

public class AlertWindow extends JFrame implements SnoozeObserver {

	private static final long serialVersionUID = 3261204676180469008L;

	JButton snoozeButton = new JButton("Snooze");
	JPanel panel1 = new JPanel();
	JTextField snoozeTimeMinutes = new JTextField("20");

	private SnoozeController controller;

	private void setupWidgets() {
		setEnterAsActionForButtons();

		setTitle("Restart timer");
		getContentPane().add(panel1, BorderLayout.NORTH);
		panel1.setLayout(new GridLayout(2, 2));
		panel1.add(snoozeButton);
		panel1.add(snoozeTimeMinutes);
		JTextField arbitraryLabel = new JTextField("You can put an arbitrary label here.");
		panel1.add(arbitraryLabel);

		ActionListener snoozeActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.restartSnoozing(Double.parseDouble(snoozeTimeMinutes.getText()));
			}
		};

		snoozeButton.addActionListener(snoozeActionListener);
		snoozeTimeMinutes.addActionListener(snoozeActionListener);
		arbitraryLabel.addActionListener(snoozeActionListener);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension max = getMaximumSize();
		setBounds(0, 0, max.width, max.height);
		setVisible(true);
	}

	private void setEnterAsActionForButtons() {
		InputMap im = (InputMap) UIManager.getDefaults().get("Button.focusInputMap");
        Object pressedAction = im.get(KeyStroke.getKeyStroke("pressed SPACE"));
        Object releasedAction = im.get(KeyStroke.getKeyStroke("released SPACE"));

        im.put(KeyStroke.getKeyStroke("pressed ENTER"), pressedAction);
        im.put(KeyStroke.getKeyStroke("released ENTER"), releasedAction);
	}

	public AlertWindow(SnoozeController paramController) {
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
