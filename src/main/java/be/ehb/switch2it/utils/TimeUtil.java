package be.ehb.switch2it.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.*;

/**
 * @author Guillaume Vandecasteele
 * @since 2018
 */
public final class TimeUtil {

    private TimeUtil() {
    }

    public static String getFormattedDateTime(DateTime dateTime) {
        return dateTime.toString("yyyy/MM/dd HH:mm:ss");
    }

    public static String getFormattedIntervalBetweenThenAndNow(DateTime start) {
        DateTime now = DateTime.now();
        int months = Months.monthsBetween(start, now).getMonths();
        now = now.minusMonths(months);
        int weeks = Weeks.weeksBetween(start, now).getWeeks();
        now = now.minusWeeks(weeks);
        int days = Days.daysBetween(start, now).getDays();
        now = now.minusDays(days);
        int hours = Hours.hoursBetween(start, now).getHours();
        now = now.minusHours(hours);
        int minutes = Minutes.minutesBetween(start, now).getMinutes();
        now = now.minusMinutes(minutes);
        int seconds = Seconds.secondsBetween(start, now).getSeconds();
        StringBuilder sb = new StringBuilder();
        if (months > 0) sb.append(months).append(" months");
        if (weeks > 0) sb.append(sb.length() > 0 ? ", " : "").append(weeks).append(" weeks");
        if (days > 0) sb.append(sb.length() > 0 ? ", " : "").append(days).append(" days");
        if (hours > 0) sb.append(sb.length() > 0 ? ", " : "").append(hours).append(" hours");
        if (minutes > 0) sb.append(sb.length() > 0 ? ", " : "").append(minutes).append(" minutes");
        sb.append(sb.length() > 0 ? ", " : "").append(seconds).append(" seconds");
        return sb.toString();
    }

    public static DateTime convertBuildTimeToDateTime(String buildTime) {
        if (StringUtils.isNotEmpty(buildTime)) {
            return new DateTime(buildTime, DateTimeZone.UTC).withZone(DateTimeZone.getDefault());
        }
        return null;
    }
}