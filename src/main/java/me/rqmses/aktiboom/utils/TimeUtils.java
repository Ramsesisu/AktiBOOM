package me.rqmses.aktiboom.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
    public static Date roundHalf(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int unroundedMinutes = calendar.get(Calendar.MINUTE);
        int mod = unroundedMinutes % 30;
        calendar.add(Calendar.MINUTE, 30-mod);
        return calendar.getTime();
    }

    public static int getWeekDay(String string) throws IOException {

        String dateInString = string;
        if (dateInString.length() > 8) {
            String[] date = dateInString.split("\\.");
            if (date[2].length() == 4) {
                dateInString = dateInString.substring(0, 6) + dateInString.substring(8, 10);
            } else {
                dateInString = dateInString.substring(0, 8);
            }
        }
        if (dateInString.length() < 8) {
            if (dateInString.length() == 6) {
                dateInString += new SimpleDateFormat("yy").format(new Date());
            } else if (dateInString.length() == 5) {
                dateInString += new SimpleDateFormat(".yy").format(new Date());
            } else {
                throw new IOException();
            }
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy", Locale.GERMANY);
        LocalDate dateTime = LocalDate.parse(dateInString, formatter);
        Date date = Date.from(dateTime.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static String lineToDate(int line) {
        switch (line) {
            case 1:
                return "08:00";
            case 2:
                return "08:30";
            case 3:
                return "09:00";
            case 4:
                return "09:30";
            case 5:
                return "10:00";
            case 6:
                return "10:30";
            case 7:
                return "11:00";
            case 8:
                return "11:30";
            case 9:
                return "12:00";
            case 10:
                return "12:30";
            case 11:
                return "13:00";
            case 12:
                return "13:30";
            case 13:
                return "14:00";
            case 14:
                return "14:30";
            case 15:
                return "15:00";
            case 16:
                return "15:30";
            case 17:
                return "16:00";
            case 18:
                return "16:30";
            case 19:
                return "17:00";
            case 20:
                return "17:30";
            case 21:
                return "18:00";
            case 22:
                return "18:30";
            case 23:
                return "19:00";
            case 24:
                return "19:30";
            case 25:
                return "20:00";
            case 26:
                return "20:30";
            case 27:
                return "21:00";
            case 28:
                return "21:30";
            case 29:
                return "22:00";
            case 30:
                return "22:30";
            case 31:
                return "23:00";
            case 32:
                return "23:30";
            case 33:
                return "00:00";
            case 34:
                return "00:30";
            case 35:
                return "01:00";
            case 36:
                return "01:30";
            case 37:
                return "02:00";
            default:
                return "07:30";
        }
    }
}
