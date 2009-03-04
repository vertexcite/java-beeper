package javabeeper;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MonitorWindow extends JFrame implements SnoozeObserver {

	private SnoozeController snoozeController;
	private JPanel panel1 = new JPanel();
	private JTextField resetSnoozeDelayTime = new JTextField("20");
	private JTextArea timeRemaining = new JTextArea(minutesAsTimeStringHHMMSS(0));

	private void setupWidgets() {
		setTitle("Java Beeper");
		timeRemaining.setFont(new Font("Serif", Font.BOLD, 72));
		getContentPane().add(panel1, BorderLayout.NORTH);
		panel1.setLayout(new GridLayout(2, 2));
		panel1.add(resetSnoozeDelayTime);
		panel1.add(timeRemaining);
		timeRemaining.setEditable(false);

		ActionListener snoozeActionListener = new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				snoozeController.restartSnoozing(Double.parseDouble(resetSnoozeDelayTime.getText()));
			}
		};

		resetSnoozeDelayTime.addActionListener(snoozeActionListener);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension max = getMaximumSize();
		setBounds(0, 0, max.width, max.height);
		setVisible(true);
	}

	public MonitorWindow(SnoozeController paramSnoozeController) {
		snoozeController = paramSnoozeController;
		setupWidgets();

	}

	private static final long serialVersionUID = -3301563184699815704L;

	public void setTimeRemainingDisplay(double paramMinutesRemaining) {
		String timeStringHHMMSS = minutesAsTimeStringHHMMSS(paramMinutesRemaining);
		timeRemaining.setText(timeStringHHMMSS);
		setTitle("Java Beeper: " + timeStringHHMMSS);
	}

	private String minutesAsTimeStringHHMMSS(double paramMinutesRemaining) {
		String signString = "";
		double absMinutesRemaining = Math.abs(paramMinutesRemaining);
		if(paramMinutesRemaining < 0) {
			signString = "-";
			absMinutesRemaining += 1.0/60.0; // Since truncation will be towards zero.
		}
		int hoursDigits = (int) absMinutesRemaining / 60;
		int wholeMinutes = (int) absMinutesRemaining;
		int minutesDigits = wholeMinutes - hoursDigits * 60;
		int secondsDigits = (int) ((absMinutesRemaining - wholeMinutes) * 60);
		String timeStringHHMMSS = String.format("%s%02d:%02d:%02d", signString, hoursDigits, minutesDigits, secondsDigits);
		return timeStringHHMMSS;
	}

	@Override
	public void setSnoozeDuration(double snoozeDurationMinutes) {
		resetSnoozeDelayTime.setText(Double.toString(snoozeDurationMinutes));
	}

}
