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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

public class AlertWindow extends JFrame implements SnoozeObserver {

	private static final long serialVersionUID = 3261204676180469008L;

	JButton snoozeButton = new JButton("Snooze");
	JPanel panel1 = new JPanel();
	JTextField snoozeTimeMinutes = new JTextField("20");
	JTextField snoozeTimeHours = new JTextField("Hours");
	JTextField snoozeTimeMinutes2 = new JTextField("Minutes2");
	JTextField snoozeTimeSeconds = new JTextField("Seconds");
	private JCheckBox soundEnabled = new JCheckBox("Enable audio (note: snooze automatically enables audio)");


	private SnoozeController snoozeController;

	private void setupWidgets() {
		setEnterAsActionForButtons();

		setTitle("Restart timer");
		getContentPane().add(panel1, BorderLayout.NORTH);
		panel1.setLayout(new GridLayout(2, 4));
		panel1.add(snoozeButton);
		panel1.add(snoozeTimeMinutes);
		panel1.add(snoozeTimeHours);
		panel1.add(snoozeTimeMinutes2);
		panel1.add(snoozeTimeSeconds);
		JTextField arbitraryLabel = new JTextField("You can put an arbitrary label here.");
		panel1.add(arbitraryLabel);
		panel1.add(soundEnabled);
		soundEnabled.setSelected(true);
		soundEnabled.setMnemonic(KeyEvent.VK_A);
		soundEnabled.addActionListener(new EnableAudioCheckBoxActionListener(snoozeController, soundEnabled));

		ActionListener snoozeActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				snoozeController.restartSnoozing(Double.parseDouble(snoozeTimeMinutes.getText()));
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
		snoozeTimeMinutes.setText(Double.toString(snoozeDurationMinutes));
	}

	@Override
	public void setSoundEnabled(boolean soundEnabled) {
		this.soundEnabled.setSelected(soundEnabled);
		
	}

}
