package br.com.romanni.metricsgenerator.utils;


import java.time.LocalDateTime;

public class MOVTCMetricsDateUtil {
    private static LocalDateTime actualDate = LocalDateTime.now();



    public static boolean isInRecoveryMonthTime(LocalDateTime date) {
        if (date == null) return false;
        return isTheSameYear(date) && isTheSameMonth(date);
    }

    public static boolean isTheSameYear(LocalDateTime date) {
        return actualDate.getYear() == date.getYear();
    }

    public static boolean isTheSameMonth(LocalDateTime date) {
        return actualDate.getMonth() == date.getMonth();
    }

    public static boolean isTheSameDay(LocalDateTime date) {
        return actualDate.getDayOfMonth() == date.getDayOfMonth();
    }
}
