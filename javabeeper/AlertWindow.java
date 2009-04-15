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
import java.awt.event.KeyEvent;

import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

public class AlertWindow extends JFrame implements SnoozeObserver {

	private static final long serialVersionUID = 3261204676180469008L;

	private JTextField snoozeTimeHours = new JTextField("0");
	private JTextField snoozeTimeMinutes = new JTextField("0");
	private JTextField snoozeTimeSeconds = new JTextField("0");
	private JCheckBox soundEnabled = new JCheckBox("Enable audio (note: snooze automatically enables audio)");


	private SnoozeController snoozeController;

	private void setupWidgets() {
		setEnterAsActionForButtons();

		setTitle("Restart timer");
		JPanel panel1 = new JPanel();
		getContentPane().add(panel1, BorderLayout.NORTH);
		panel1.setLayout(new GridLayout(5, 2));

		JButton snoozeButton = new JButton("Snooze");
		panel1.add(snoozeButton);
		JTextField arbitraryLabel = new JTextField("You can put an arbitrary label here.");
		panel1.add(arbitraryLabel);
        
		panel1.add(new JLabel("Hours"));
		panel1.add(snoozeTimeHours);

		panel1.add(new JLabel("Minutes"));
		panel1.add(snoozeTimeMinutes);
		
		panel1.add(new JLabel("Seconds"));
		panel1.add(snoozeTimeSeconds);

		panel1.add(soundEnabled);
		soundEnabled.setSelected(true);
		soundEnabled.setMnemonic(KeyEvent.VK_A);
		soundEnabled.addActionListener(new EnableAudioCheckBoxActionListener(snoozeController, soundEnabled));
        
		
		ActionListener snoozeActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				double hours = 0;
				double minutes = 0;
				double seconds = 0;
				try {
				    hours = Double.parseDouble(snoozeTimeHours.getText());
				    minutes = Double.parseDouble(snoozeTimeMinutes.getText());
				    seconds = Double.parseDouble(snoozeTimeSeconds.getText());

				}
				catch (NumberFormatException e) {
					// Do nothing about error, but proceed with values gathered so far.
				}
			
				double totalSnoozeTimeMinutes = (hours * Utilities.MINUTES_PER_HOUR) + (minutes) + (seconds / Utilities.SECONDS_PER_MINUTE);
				snoozeController.restartSnoozing(totalSnoozeTimeMinutes);
			}
		};

		snoozeButton.addActionListener(snoozeActionListener);
		snoozeTimeHours.addActionListener(snoozeActionListener);
		snoozeTimeMinutes.addActionListener(snoozeActionListener);
		snoozeTimeSeconds.addActionListener(snoozeActionListener);
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
		snoozeController = paramController;
		setupWidgets();
	}

	void beepAndShow() {
		if(snoozeController.isSoundEnabled()) {
			Toolkit.getDefaultToolkit().beep();
		}
		setVisible(true);
	}

	@Override
	public void setSnoozeDuration(double snoozeDurationMinutes) {
		// TODO Set the values for HH, MM and SS correctly, just doing MM for now.
		snoozeTimeMinutes.setText(Double.toString(snoozeDurationMinutes));
	}

	@Override
	public void setSoundEnabled(boolean soundEnabled) {
		this.soundEnabled.setSelected(soundEnabled);
		
	}

}
