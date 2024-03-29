package javabeeper;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javabeeper.Utilities.HoursMinutesSeconds;

public class SnoozeController {

	private static final int HEART_BEAT_PERIOD_MILLISECONDS = 1000;
	public static final String versionString = "Version 0.18";
	
	/**
	 * Small offset for time calculation to help alleviate rounding when converting from milliseconds to time display.
	 */
	private static final long EPSILON_MILLISECONDS = 1;
        private static final double PERIODIC_IRRITATION_INTERVAL_MINUTES = 1;
        public static final String BEEPER_LOGGER_ID = "Beeper";
        public static final String USE_FULL_SCREEN_COMMAND_LINE_PARAM = "useFullScreen";
        public static final String SNOOZE_SOCKET_MESSAGE_CLIENT_CAN_QUIT = "Client-can-quit.";
        private boolean runningAsSlave;
        private static final String AS_SLAVE_COMMAND_LINE_PARAM = "asSlave";

        private static void setNimbusLookAndFeel() {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
             * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             */
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(BEEPER_LOGGER_ID).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>
        }
	
	private AlertWindow alertWindow;
	private MonitorWindow monitorWindow;
	private List<SnoozeObserver> observers = new ArrayList<>();

	private long nextWakeTimeMilliseconds = System.currentTimeMillis();
	private boolean snoozing = false;
	private long nextIrritateTimeMilliseconds = System.currentTimeMillis() + Utilities.fromMinutesToMilliseconds(PERIODIC_IRRITATION_INTERVAL_MINUTES);
	private double snoozeDurationMinutes = 20;
	private long resynchReferenceTime = -1;
	private Thread heartBeatThread;
	private boolean soundEnabled = true;

        
        /**
         * @param args the command line arguments
         */

	public static void main(String args[]) throws Exception {
            
            setNimbusLookAndFeel();

            final SnoozeController controller = new SnoozeController();
            
            if(Arrays.asList(args).contains(AS_SLAVE_COMMAND_LINE_PARAM)) {
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Snooze slave");

                controller.runningAsSlave = true;
                controller.alertAsSeparateProcess = false;
                controller.setSnoozeDurationMinutesAndDoSnooze(Double.parseDouble(args[Arrays.asList(args).indexOf(AS_SLAVE_COMMAND_LINE_PARAM)+1]));
                
                int port = Integer.parseInt(args[Arrays.asList(args).indexOf(AS_SLAVE_COMMAND_LINE_PARAM)+2]);
                final SocketIpcClient socketIpcClient = new SocketIpcClient(port);
                
                controller.addObserver(socketIpcClient);
                socketIpcClient.startListening();

                controller.useFullScreen = (Arrays.asList(args).contains(USE_FULL_SCREEN_COMMAND_LINE_PARAM));
            }
            
            if(controller.alertAsSeparateProcess) {
                controller.tryStartIpcServer();
            }
            
            
            /* Create and display the form */

            java.awt.EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    if(!controller.runningAsSlave) {
                        controller.monitorWindow = new MonitorWindow();
                        controller.monitorWindow.setController(controller);
                        controller.addObserver(controller.monitorWindow);
                        controller.monitorWindow.setVisible(true);
                    }

                    if(controller.runningAsSlave || !controller.alertAsSeparateProcess) {
                        controller.setupAlertWindow();
                    }

                    controller.showAlert();
                    controller.updatAllObservers();
                }

            });

            
            controller.heartBeatLoop();
	}

        public void setupAlertWindow() {
            alertWindow = new AlertWindow();
            alertWindow.setController(this);
            addObserver(alertWindow);
        }

        private boolean alertAsSeparateProcess=false;

        public boolean isAlertAsSeparateProcess() {
            return alertAsSeparateProcess;
        }
        private SocketIpcServer socketServer;

	protected void addObserver(SnoozeObserver observer) {
		observers.add(observer);
                observer.setSnoozeDuration(snoozeDurationMinutes);
                observer.setSoundEnabled(soundEnabled);
	}

	public synchronized void setSnoozeDurationMinutesAndDoSnooze(final double paramSnoozeDurationMinutes) {
		long currentTimeMilliseconds = System.currentTimeMillis();
		nextWakeTimeMilliseconds = currentTimeMilliseconds + Utilities.fromMinutesToMilliseconds(paramSnoozeDurationMinutes);

		flagResynchRequired(currentTimeMilliseconds);

		snoozeDurationMinutes = paramSnoozeDurationMinutes;
		updateAndSnoozeAllObservers();
	}

	/**
	 * Either start snoozing, or if already snoozing, continue, but reset snooze time.
	 * @param paramSnoozeDurationMinutes Amount of time to snooze for.
	 */
        public synchronized void restartSnoozing(final HoursMinutesSeconds hoursMinutesSeconds) {
		snoozing = true;
		hideAlert();
		setSoundEnabled(true);
		setSnoozeDurationMinutesAndDoSnooze(hoursMinutesSeconds.asMinutes());
		updateRemainingTimeDisplay();

                if(runningAsSlave) {
                    System.exit(0);
                }
	}

	private void updateAndSnoozeAllObservers() {
		for (SnoozeObserver observer : observers) {
			observer.setSnoozeDurationAndDoSnooze(snoozeDurationMinutes);
		}
	}

	private void updatAllObservers() {
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
		heartBeatThread = Thread.currentThread();
		int heartBeatIndex = 0;
		long nextHeartBeatTime = System.currentTimeMillis();
		while (true) {
			try {
				
				long heartBeatSleepDurationMilliseconds = nextHeartBeatTime - System.currentTimeMillis();
				while (heartBeatSleepDurationMilliseconds <= 0) { 
					// If for e.g. system is suspended, the heartbeat loop may not get to execute every heartbeat.
					nextHeartBeatTime = nextHeartBeatTime + HEART_BEAT_PERIOD_MILLISECONDS;
					heartBeatSleepDurationMilliseconds = nextHeartBeatTime - System.currentTimeMillis();
				}  

				Thread.sleep(heartBeatSleepDurationMilliseconds);
				heartBeatIndex++;

				long localResynchReferenceTime = readAndClearResynchReference();
				if(localResynchReferenceTime > 0){
					nextHeartBeatTime = localResynchReferenceTime + HEART_BEAT_PERIOD_MILLISECONDS;
				}
			} catch (InterruptedException e) {
				// Do Nothing
			}

			updateRemainingTimeDisplay();

			if (!alertAsSeparateProcess && shouldIrritate()) {
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

	/**
	 * Reads and resets reference time from which heart beat period should be equi-spaced.
	 * @return time from which heart beat period should be spaced, or -1 if no change is necessary.
	 */
	private synchronized long readAndClearResynchReference() {
		long returnValue = resynchReferenceTime;
		resynchReferenceTime = -1;
		return returnValue;
	}

	private synchronized void flagResynchRequired(long currentTimeMilliseconds) {
		resynchReferenceTime = currentTimeMilliseconds;
                if(heartBeatThread == null) {
                    return;
                }
		heartBeatThread.interrupt();
	}

	private void updateNextIrritateTime() {
		nextIrritateTimeMilliseconds = System.currentTimeMillis() + Utilities.MILLISECONDS_PER_SECOND + Utilities.fromMinutesToMilliseconds(PERIODIC_IRRITATION_INTERVAL_MINUTES);
	}


        private void showAlert() {
                String fullScreenParam = "";
                if (alertAsSeparateProcess) {
                    if(useFullScreen) {
                        fullScreenParam = USE_FULL_SCREEN_COMMAND_LINE_PARAM;
                    }
                    Utilities.execNoWait(SnoozeController.class, fullScreenParam, AS_SLAVE_COMMAND_LINE_PARAM, String.valueOf(snoozeDurationMinutes), String.valueOf(socketServer.getPort()));
          
                    
                } 
                for(SnoozeObserver observer:observers) {
                    observer.itIsTimeToShowAlert();
                }
	}

        private boolean useFullScreen=false;

	private void hideAlert() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    @Override
			public void run() {
                            if(useFullScreen) {
                                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
                            }

                            if(alertWindow == null) {
                                return;
                            }
                            alertWindow.setVisible(false);
			}
		});
	}

	private void updateRemainingTimeDisplay() {
		long timeNow = System.currentTimeMillis();
		final double minutesRemaining = Utilities.fromMillisecondsToMinutes(nextWakeTimeMilliseconds - timeNow + EPSILON_MILLISECONDS);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    @Override
			public void run() {
                                if(monitorWindow == null) {
                                    return;
                                }
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
		long timePointMilliseconds = nextWakeTimeMilliseconds - HEART_BEAT_PERIOD_MILLISECONDS/2; 
		// Heart beats only once every second, so go now rather than come in late.
		return snoozing && hasPassedTimePoint(timePointMilliseconds);
	}

	public synchronized boolean isSoundEnabled() {
		return soundEnabled;
	}

	public synchronized void setSoundEnabled(final boolean paramSoundEnabled) {
		soundEnabled  = paramSoundEnabled;
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    @Override
			public void run() {
				for (SnoozeObserver observer : observers) {
					observer.setSoundEnabled(paramSoundEnabled);
				}
			}
		});
	}

    boolean useFullScreenActive() {
        return useFullScreen;
    }

    void setUseFullScreen(boolean useFullScreen) {
        this.useFullScreen = useFullScreen;
    }

    private void tryStartIpcServer() {
        socketServer = new SocketIpcServer(this);
        socketServer.listen();
        addObserver(socketServer);
    }

    void setUseSeparateProcess(boolean alertAsSeparateProcess) {
        this.alertAsSeparateProcess = alertAsSeparateProcess;
        if(runningAsSlave || !alertAsSeparateProcess) {
            setupAlertWindow();
            socketServer.close();
            removeObserver(socketServer);
        } else {
            removeObserver(alertWindow);
            tryStartIpcServer();
        }
        
    }

    void removeObserver(SnoozeObserver observer) {
        observers.remove(observer);
    }

}
