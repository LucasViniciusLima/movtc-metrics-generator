package br.com.romanni.metricsgenerator.utils;


import br.com.romanni.metricsgenerator.models.Costumer;

import java.time.LocalDateTime;

public class MOVTCMetricsDateUtil {

    private static LocalDateTime actualDate = LocalDateTime.now();

    public static void setActualDateToLastMonth() {
        actualDate = actualDate.minusMonths(1);
    }

    public static boolean isInRecoveryMonthTime(LocalDateTime date) {
        if (date == null) return false;
        return isTheSameAsActualYear(date) && isTheSameMonth(date);
    }

    public static boolean isRecentRenewed(Costumer costumer) {
        var startedDate = costumer.getExpirationDate().minusYears(1);
        return isTheSameAsActualYear(startedDate) &&
                isTheSameMonth(startedDate) &&
                !isTheSameYearAndMonth(costumer.getCreatedDate(), startedDate);
    }

    public static boolean isRecentPurchase(Costumer costumer) {
        var startedDate = costumer.getExpirationDate().minusYears(1);
        return isTheSameAsActualYear(startedDate) &&
                isTheSameMonth(startedDate) &&
                isTheSameYearAndMonth(costumer.getCreatedDate(), startedDate);
    }

    public static boolean isTheSameAsActualYear(LocalDateTime date) {
        return actualDate.getYear() == date.getYear();
    }

    public static boolean isTheSameMonth(LocalDateTime date) {
        return actualDate.getMonth() == date.getMonth();
    }

    public static boolean isTheSameDay(LocalDateTime date) {
        return actualDate.getDayOfMonth() == date.getDayOfMonth();
    }

    public static boolean isSignatureFirstYear(Costumer costumer) {
        if (costumer.getCreatedDate() == null) return false;
        return (actualDate.getYear() - costumer.getCreatedDate().getYear()) < 2;
    }

    private static boolean isTheSameYearAndMonth(LocalDateTime startDate, LocalDateTime endDate) {
        return isTheSameYear(startDate, endDate) && isTheSameMonth(endDate, endDate);
    }

    private static boolean isTheSameYear(LocalDateTime expirationDate, LocalDateTime creationDate) {
        return expirationDate.getYear() == creationDate.getYear();
    }

    private static boolean isTheSameMonth(LocalDateTime expirationDate, LocalDateTime creationDate) {
        return expirationDate.getMonth() == creationDate.getMonth();
    }

    public static LocalDateTime getActualDate() {
        return actualDate;
    }
}
