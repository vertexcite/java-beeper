package javabeeper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

// TODO: could have one instance of this created by a factory.
class EnableAudioCheckBoxActionListener implements ActionListener {
	private final SnoozeController snoozeController;
	private final JCheckBox soundEnabled;

	public EnableAudioCheckBoxActionListener(SnoozeController paramSnoozeController, JCheckBox soundEnabled) {
		snoozeController = paramSnoozeController;
		this.soundEnabled = soundEnabled;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		snoozeController.setSoundEnabled(soundEnabled.isSelected());
	}
}
