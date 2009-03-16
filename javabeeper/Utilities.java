package javabeeper;

/**
 * Commonly used utility methods.
 */
public class Utilities {

	public static final long SECONDS_PER_HOUR = 3600;
	public static final long MINUTES_PER_HOUR = 60;
	
	public static String minutesAsTimeStringHHMMSS(double paramDurationMinutes) {
		String signString = "";
		double absDurationMinutes = Math.abs(paramDurationMinutes);
		if(paramDurationMinutes < 0) {
			signString = "-";
		}
		long absDurationSeconds = Math.round(absDurationMinutes * SECONDS_PER_MINUTE);
		long wholeHours = absDurationSeconds / SECONDS_PER_HOUR;
		long minutesDigits = absDurationSeconds/ SECONDS_PER_MINUTE - MINUTES_PER_HOUR * wholeHours;
		long secondsDigits = absDurationSeconds - minutesDigits * SECONDS_PER_MINUTE - wholeHours * SECONDS_PER_HOUR;
		String timeStringHHMMSS = String.format("%s%02d:%02d:%02d", signString, wholeHours, minutesDigits, secondsDigits );
		return timeStringHHMMSS;
	}

	public static long fromMinutesToMilliseconds(final double durationMinutes) {
		double durationMilliseconds = durationMinutes * MILLISECONDS_PER_SECOND * SECONDS_PER_MINUTE;
		return (long) durationMilliseconds;
	}

	public static double fromMillisecondsToMinutes(long durationMilliseconds) {
		return ((double) durationMilliseconds) / (MILLISECONDS_PER_SECOND * SECONDS_PER_MINUTE);
	}

	static final int MILLISECONDS_PER_SECOND = 1000;
	static final int SECONDS_PER_MINUTE = 60;

}
