package javabeeper;

import java.util.ArrayList;
import java.util.List;

public class SnoozeController {

	private static final int HEART_BEAT_PERIOD_MILLISECONDS = 2000;
	private static final int MILLISECONDS_PER_SECOND = 1000;
	private static final int SECONDS_PER_MINUTE = 60;
	public static final String versionString = "Version 0.13 (zero point thirteen)";
	
	/**
	 * Small offset for time calculation to help alleviate rounding when converting from milliseconds to time display.
	 */
	private static final long EPSILON_MILLISECONDS = 1;
	
	private MainWindow mainWindow;
	private MonitorWindow monitorWindow;
	private List<SnoozeObserver> observers = new ArrayList<SnoozeObserver>();

	private long nextWakeTimeMilliseconds = System.currentTimeMillis();
	private boolean snoozing = false;
	private long nextIrritateTimeMilliseconds = System.currentTimeMillis() + fromMinutesToMilliseconds(1);
	private double snoozeDurationMinutes = 20;
	private boolean resynchRequired = false;
	private long resynchReferenceTime;

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

	public synchronized void setSnoozeDurationMinutes(final double paramSnoozeDurationMinutes) {
		long currentTimeMilliseconds = System.currentTimeMillis();
		nextWakeTimeMilliseconds = currentTimeMilliseconds + fromMinutesToMilliseconds(paramSnoozeDurationMinutes);

		resynchReferenceTime = currentTimeMilliseconds;
		setResynchRequired(true);

		snoozeDurationMinutes = paramSnoozeDurationMinutes;
		updateObservers();
	}

	/**
	 * Either start snoozing, or if already snoozing, continue, but reset snooze time.
	 * @param paramSnoozeDurationMinutes Amount of time to snooze for.
	 */
	public synchronized void restartSnoozing(final double paramSnoozeDurationMinutes) {
		snoozing = true;
		hideAlert();
		setSnoozeDurationMinutes(paramSnoozeDurationMinutes);
		updateRemainingTimeDisplay();
	}

	private void updateObservers() {
		for (SnoozeObserver observer : observers) {
			observer.setSnoozeDuration(snoozeDurationMinutes);
		}
	}


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
				
				long heartBeatSleepDurationMilliseconds = nextHeartBeatTime - System.currentTimeMillis();
				while (heartBeatSleepDurationMilliseconds < 0) { 
					// If for e.g. system is suspended, the heartbeat loop may not get to execute every heartbeat.
					nextHeartBeatTime = nextHeartBeatTime + HEART_BEAT_PERIOD_MILLISECONDS;
					heartBeatSleepDurationMilliseconds = nextHeartBeatTime - System.currentTimeMillis();
				}  
				
				Thread.sleep(heartBeatSleepDurationMilliseconds);
				
				if(getResynchRequired()){
					nextHeartBeatTime = resynchReferenceTime + HEART_BEAT_PERIOD_MILLISECONDS;
					// TODO: It would be nicer if testing whether a resynch is required and reading the resynch tie were in the same synchronized block.
					setResynchRequired(false);
					continue;
				}
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
				updateNextIrritateTime();
				snoozing = false;
			}
		}
	}

	private synchronized boolean getResynchRequired() {
		return resynchRequired;
	}

	private synchronized void setResynchRequired(boolean paramResynchRequired) {
		resynchRequired  = paramResynchRequired;
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
		final double minutesRemaining = fromMillisecondsToMinutes(nextWakeTimeMilliseconds - timeNow + EPSILON_MILLISECONDS);
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

	private synchronized boolean timeToShowAlert() {
		long timePointMilliseconds = nextWakeTimeMilliseconds - HEART_BEAT_PERIOD_MILLISECONDS; 
		// Heart beats only once every second, so go now rather than come in late.
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
