package javabeeper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        public static int exec(Class klass, boolean wait, String... additionalArgs) throws IOException, InterruptedException {
                String javaHome = System.getProperty("java.home");
                String javaBin = javaHome +
                        File.separator + "bin" +
                        File.separator + "java";
                String classpath = System.getProperty("java.class.path");
                String className = klass.getCanonicalName();

                List<String> args = new ArrayList<>();
                
                args.add(javaBin);
                args.add("-cp");
                args.add(classpath);
                args.add(className);
                args.addAll(Arrays.asList(additionalArgs));
                
                ProcessBuilder builder = new ProcessBuilder(args);

                Process process = builder.start();
                if (wait) {
                    process.waitFor();
                    return process.exitValue();
                } else {
                    return 0;
                }
        }
        
        public static void execNoWait(Class klass, String... additionalArgs) {
            try {
                exec(klass, false, additionalArgs);
            } catch (IOException | InterruptedException ex) {
            }
        }
        
        public static int execAndWait(Class klass, String... additionalArgs) throws IOException, InterruptedException {
            return exec(klass, true, additionalArgs);
        }
        

}
