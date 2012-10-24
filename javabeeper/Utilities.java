package javabeeper;

/**
 * Commonly used utility methods.
 */
public class Utilities {

        public static HoursMinutesSeconds minutesToHoursMinutesSeconds(double paramDurationMinutes) {
            double absDurationMinutes = Math.abs(paramDurationMinutes);
            long absDurationSeconds = Math.round(absDurationMinutes * SECONDS_PER_MINUTE);
            
            final long hours = absDurationSeconds / SECONDS_PER_HOUR;
            final long minutes = absDurationSeconds / SECONDS_PER_MINUTE - MINUTES_PER_HOUR * hours;
            final long seconds = absDurationSeconds - minutes * SECONDS_PER_MINUTE - hours * SECONDS_PER_HOUR;
            
            return new HoursMinutesSeconds(hours, minutes, seconds);
        }
    
        public static class HoursMinutesSeconds {
            public long hours;
            public long minutes;
            public long seconds;

            public HoursMinutesSeconds(long hours, long minutes, long seconds) {
                this.hours = hours;
                this.minutes = minutes;
                this.seconds = seconds;
            }
            
            public double asMinutes() {
                return (hours * Utilities.MINUTES_PER_HOUR) + (minutes) + (((double)seconds) / Utilities.SECONDS_PER_MINUTE);                
            }
        }

	public static final long SECONDS_PER_HOUR = 3600;
	public static final long MINUTES_PER_HOUR = 60;
	
	public static String minutesAsTimeStringHHMMSS(double paramDurationMinutes) {
		String signString = "";
		if(paramDurationMinutes < 0) {
			signString = "-";
		}
                HoursMinutesSeconds hms = minutesToHoursMinutesSeconds(paramDurationMinutes);

                String timeStringHHMMSS = String.format("%s%02d:%02d:%02d", signString, hms.hours, hms.minutes, hms.seconds );
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
