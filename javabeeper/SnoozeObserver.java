package javabeeper;

/**
 * Classes that implement the SnoozeObserver interface are usually user interface screens that 
 * display info.  The SnoozeController uses this interface to send this info to implementors.  Usually
 * they just display this info in the appropriate parts of their UI.
 *
 */
public interface SnoozeObserver {
	void setSnoozeDuration(double snoozeDurationMinutes);

	void setSoundEnabled(boolean soundEnabled);
}
