package javabeeper;


public class SnoozeController {

	private static final int MILLISECONDS_PER_SECOND = 1000;
	private static final int SECONDS_PER_MINUTE = 60;
	private MainWindow mainWindow;
	private MonitorWindow monitorWindow;

	public static void main(String args[]) throws Exception {
		final SnoozeController controller = new SnoozeController();
		javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				controller.mainWindow = new MainWindow(controller);
				controller.monitorWindow = new MonitorWindow(controller);
			}
		});
		controller.heartBeat();
	}

	public synchronized void setSnoozeDurationFromGui(final double snoozeDurationMinutes) {
		nextWakeTimeMilliseconds = System.currentTimeMillis() + fromMinutesToMilliseconds(snoozeDurationMinutes);
	}

	private long nextWakeTimeMilliseconds = System.currentTimeMillis() + fromMinutesToMilliseconds(20);
	private boolean snoozing = false;
	private long nextIrritateTimeMilliseconds = 0;

	public synchronized void snoozeActionTriggered(double snoozeDurationMinutes) {
		snoozing = true;
		nextWakeTimeMilliseconds = System.currentTimeMillis() + fromMinutesToMilliseconds(snoozeDurationMinutes);
		hideAlert();
	}



	/**
	 * Wakes once per second and issues events if needed:
	 * - Updates time remaining display, 
	 * - and displays alert if time has run out,
	 * - "Irritates", i.e. re-shows alert every minute if it was not "snoozed"
	 */
	private void heartBeat() {
		final long heartBeatPeriodMilliseconds = 1000;
		long nextHeartBeatTime = System.currentTimeMillis();
		while (true) {
			try {
				nextHeartBeatTime = nextHeartBeatTime + heartBeatPeriodMilliseconds;
				long heartBeatSleepDurationMilliseconds = nextHeartBeatTime - System.currentTimeMillis();
				Thread.sleep(heartBeatSleepDurationMilliseconds);
			} catch (InterruptedException e) {
				// Do Nothing
			}

			if (isSnoozing()) {
				updateRemainingTimeDisplay();
			} 
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
		nextIrritateTimeMilliseconds = System.currentTimeMillis() + fromMinutesToMilliseconds(1);
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
		final double minutesRemaining = fromMillisecondsToMinutes(nextWakeTimeMilliseconds - timeNow );
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
		long timePointMilliseconds = nextWakeTimeMilliseconds-1000; // Heartbeats only once every second, so go now if it seems we might be late for next one.
		return snoozing && hasPassedTimePoint(timePointMilliseconds);
	}


	private static long fromMinutesToMilliseconds(final double durationMinutes) {
		double durationMilliseconds = durationMinutes * MILLISECONDS_PER_SECOND * SECONDS_PER_MINUTE;
		return (long) durationMilliseconds;
	}
	
	private double fromMillisecondsToMinutes(long durationMilliseconds) {
		return ((double) durationMilliseconds)/(MILLISECONDS_PER_SECOND * SECONDS_PER_MINUTE);
	}

}
