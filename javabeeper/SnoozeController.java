package javabeeper;

import java.util.ArrayList;
import java.util.List;

public class SnoozeController {

	private static final int HEART_BEAT_PERIOD_MILLISECONDS = 1000;
	private static final int MILLISECONDS_PER_SECOND = 1000;
	private static final int SECONDS_PER_MINUTE = 60;
	private MainWindow mainWindow;
	private MonitorWindow monitorWindow;
	private List<SnoozeObserver> observers = new ArrayList<SnoozeObserver>();

	public static void main(String args[]) throws Exception {
		final SnoozeController controller = new SnoozeController();
		javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				controller.addObserver(controller.monitorWindow = new MonitorWindow(controller));
				controller.addObserver(controller.mainWindow = new MainWindow(controller));
			}
		});
		controller.heartBeatLoop();
	}

	protected void addObserver(SnoozeObserver observer) {
		observers.add(observer);
	}

	public synchronized void setSnoozeDurationFromGui(final double paramSnoozeDurationMinutes) {
		nextWakeTimeMilliseconds = System.currentTimeMillis() + fromMinutesToMilliseconds(paramSnoozeDurationMinutes);
		snoozeDurationMinutes = paramSnoozeDurationMinutes;
		updateObservers();
	}

	public synchronized void startSnoozing(final double paramSnoozeDurationMinutes) {
		snoozing = true;
		hideAlert();
		setSnoozeDurationFromGui(paramSnoozeDurationMinutes);
	}

	private void updateObservers() {
		for (SnoozeObserver observer : observers) {
			observer.setSnoozeDuration(snoozeDurationMinutes);
		}
	}

	private long nextWakeTimeMilliseconds = System.currentTimeMillis() + fromMinutesToMilliseconds(20);
	private boolean snoozing = false;
	private long nextIrritateTimeMilliseconds = System.currentTimeMillis() + fromMinutesToMilliseconds(1);
	private double snoozeDurationMinutes = 20;

	public synchronized double getSnoozeDurationMinutes() {
		return snoozeDurationMinutes;
	}

	/**
	 * Wakes once per second and issues events if needed: - Updates time
	 * remaining display, - and displays alert if time has run out, -
	 * "Irritates", i.e. re-shows alert every minute if it was not "snoozed"
	 */
	private void heartBeatLoop() {
		long nextHeartBeatTime = System.currentTimeMillis();
		while (true) {
			try {
				long heartBeatSleepDurationMilliseconds;
				do {
					nextHeartBeatTime = nextHeartBeatTime + HEART_BEAT_PERIOD_MILLISECONDS;
					heartBeatSleepDurationMilliseconds = nextHeartBeatTime - System.currentTimeMillis();
				} while (heartBeatSleepDurationMilliseconds < 0); // If for e.g. system is suspended, this loop may not get to execute every heartbeat.
				Thread.sleep(heartBeatSleepDurationMilliseconds);
			} catch (InterruptedException e) {
				// Do Nothing
			}

			updateRemainingTimeDisplay();
			
			if (shouldIrritate()) {
				showAlert();
				updateNextIrritateTime();
			}
			if (timeToShowAlert()) {
				showAlert();
				snoozing = false;
			}
		}
	}

	private void updateNextIrritateTime() {
		nextIrritateTimeMilliseconds = System.currentTimeMillis() + MILLISECONDS_PER_SECOND + fromMinutesToMilliseconds(1);
	}

	private void showAlert() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mainWindow.beepAndShow();
			}
		});
	}

	private void hideAlert() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mainWindow.setVisible(false);
			}
		});
	}

	private void updateRemainingTimeDisplay() {
		long timeNow = System.currentTimeMillis();
		final double minutesRemaining = fromMillisecondsToMinutes(nextWakeTimeMilliseconds - timeNow);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				monitorWindow.setTimeRemainingDisplay(minutesRemaining);
			}
		});
	}

	private boolean shouldIrritate() {
		return (!snoozing) && hasPassedTimePoint(nextIrritateTimeMilliseconds);
	}

	private boolean hasPassedTimePoint(long timePointMilliseconds) {
		return System.currentTimeMillis() > timePointMilliseconds;
	}

	private boolean isSnoozing() {
		return snoozing;
	}

	private boolean timeToShowAlert() {
		// Heartbeats only once every second, so go now rather than come in late.
		long timePointMilliseconds = nextWakeTimeMilliseconds - HEART_BEAT_PERIOD_MILLISECONDS; 
		return snoozing && hasPassedTimePoint(timePointMilliseconds);
	}

	private static long fromMinutesToMilliseconds(final double durationMinutes) {
		double durationMilliseconds = durationMinutes * MILLISECONDS_PER_SECOND * SECONDS_PER_MINUTE;
		return (long) durationMilliseconds;
	}

	private double fromMillisecondsToMinutes(long durationMilliseconds) {
		return ((double) durationMilliseconds) / (MILLISECONDS_PER_SECOND * SECONDS_PER_MINUTE);
	}

}
