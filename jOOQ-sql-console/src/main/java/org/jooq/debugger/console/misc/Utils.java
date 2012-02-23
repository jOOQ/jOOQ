package org.jooq.debugger.console.misc;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author Christopher Deckers & others
 */
public class Utils {

	private Utils() {}
	
    public static enum DurationFormatPrecision {
        DAY,
        HOUR,
        MINUTE,
        SECOND,
        MILLISECOND,
    }

    public static String formatDuration(long duration) {
        return formatDuration(duration, DurationFormatPrecision.MILLISECOND);
    }

    public static String formatDuration(long duration, DurationFormatPrecision precision) {
        StringBuilder sb = new StringBuilder();
        if (duration < 0) {
            duration = -duration;
            sb.append("-");
        }
        if (duration / (24 * 1000 * 60 * 60) != 0 || precision == DurationFormatPrecision.DAY) {
            sb.append(duration / (24 * 1000 * 60 * 60)).append("d ");
            if (precision == DurationFormatPrecision.DAY) {
                return sb.toString().trim();
            }
        }
        if (duration / (1000 * 60 * 60) != 0 || precision == DurationFormatPrecision.HOUR) {
            sb.append((duration / (1000 * 60 * 60)) % 24).append("h ");
            if (precision == DurationFormatPrecision.HOUR) {
                return sb.toString().trim();
            }
        }
        if (duration / (1000 * 60) != 0 || precision == DurationFormatPrecision.MINUTE) {
            sb.append((duration / (1000 * 60)) % 60).append("min ");
            if (precision == DurationFormatPrecision.MINUTE) {
                return sb.toString().trim();
            }
        }
        if (duration / 1000 != 0 || precision == DurationFormatPrecision.SECOND) {
            sb.append((duration / 1000) % 60).append("s ");
            if (precision == DurationFormatPrecision.SECOND) {
                return sb.toString().trim();
            }
        }
        sb.append(duration % 1000).append("ms");
        return sb.toString();
    }
    
    public static String formatDateTimeTZ(Date date) {
    	// TODO: GMT vs TZ
    	return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).format(date);
    }

    public static String formatDateTimeGMT(Date date) {
    	// TODO: GMT vs TZ
    	return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).format(date);
    }
    
    public static String formatDateGMT(Date date) {
    	// TODO: GMT vs TZ
    	return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }
    
    public static boolean equals(Object o1, Object o2) {
        return (o1 == o2) || (o1 != null && o1.equals(o2));
    }

}
