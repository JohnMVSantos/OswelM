package project.oswel.knowledgebase.schedule;

import java.text.Normalizer;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class DateTime {

    public static String getCurrentTime() {
        LocalTime localTime = LocalTime.now();
        String[] currentTime = localTime.toString().split(":");
        int hour = Integer.parseInt(currentTime[0]);
        int minute = Integer.parseInt(currentTime[1]);
        String identifier = "";

        if (hour >= 12) {
            identifier = "PM";
            if (hour > 12) {
                hour -= 12;
            }
        } else {
            identifier = "AM";
            if (hour == 0) {
                hour = 12;
            }
        }
        return String.format("%d:%02d %s", hour, minute, identifier);
    }

    public static String getCurrentTimeCity(String city) {
        Set<String> zids = ZoneId.getAvailableZoneIds();
        String tzCityName = Normalizer.normalize(city, Normalizer.Form.NFKD)
                .replaceAll("[^\\p{ASCII}-_ ]", "")
                .replace(' ', '_');
        List<String> possibleTimeZones = zids.stream()
                .filter(zid -> zid.endsWith("/" + tzCityName))
                .collect(Collectors.toList());
        TimeZone chosenTimeZone = TimeZone.getTimeZone(possibleTimeZones.get(0));
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(chosenTimeZone);
        
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String identifier = "";

        if (hour >= 12) {
            identifier = "PM";
            if (hour > 12) {
                hour -= 12;
            }
        } else {
            identifier = "AM";
            if (hour == 0) {
                hour = 12;
            }
        }
        
        return String.format("%d:%02d %s", hour, minute, identifier);
    }

    public static String getCurrentDay() {
        LocalDate today = LocalDate.now();
        return today.getDayOfWeek().toString().toLowerCase();
    }

    public static String getCurrentDate() {
        LocalDate today = LocalDate.now();
        today.getDayOfWeek();
        String weekDay = today.getDayOfWeek().toString();
        weekDay = weekDay.substring(0,1).toUpperCase() + 
                    weekDay.substring(1).toLowerCase();
        String[] date = today.toString().split("-");
        int year = Integer.parseInt(date[0]);
        String month = Month.getStringFromInt(Integer.parseInt(date[1]));
        int day = Integer.parseInt(date[2]);
        return String.format(
            "%s, %s %02d, %d", weekDay, month, day, year);
    } 

    public static String[] getStartEndWeekDates() {
        LocalDate today = LocalDate.now();

        // Go backward to get Sunday.
        LocalDate sunday = today;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday = sunday.minusDays(1);
        }

        // Go forward to get Saturday.
        LocalDate saturday = today;
        while (saturday.getDayOfWeek() != DayOfWeek.SATURDAY) {
        saturday = saturday.plusDays(1);
        }
        
        String[] dates = {sunday.toString(), saturday.toString()};
        return dates;
    }
}
