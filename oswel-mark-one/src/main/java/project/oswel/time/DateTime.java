package project.oswel.time;

import java.util.GregorianCalendar;
import java.util.stream.Collectors;
import java.text.Normalizer;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.HashMap;
import java.time.ZoneId;
import java.util.Locale;
import java.util.List;
import java.util.Set;

/**
 * This class provides methods to get the time and date information
 * either based on the local time or based on the city specified.
 * @author John Santos
 */
public class DateTime {

    /**
     * This method formats the time from 24Hour format to 12 Hour format.
     * @param hour The 24 Hour of the day.
     * @param minute The minutes of the hour. 
     * @return The 12 hour time format (String).
     */
    public static String formatTime(int hour, int minute) {
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

    /**
     * This method returns the current local time set in the device.
     * @return The current time (String).
     */
    public static String getCurrentTime() {
        LocalTime localTime = LocalTime.now();
        String[] currentTime = localTime.toString().split(":");
        int hour = Integer.parseInt(currentTime[0]);
        int minute = Integer.parseInt(currentTime[1]);
        return formatTime(hour, minute);
    }

    /**
     * This method gets the current time in the city provided.
     * @param city The name of the city for which to get the time.
     * @return The time at a particular city (String).
     */
    public static String getCurrentTimeCity(String city) {
        Set<String> zids = ZoneId.getAvailableZoneIds();
        String tzCityName = Normalizer.normalize(city, Normalizer.Form.NFKD)
                .replaceAll("[^\\p{ASCII}-_ ]", "")
                .replace(' ', '_');
        List<String> possibleTimeZones = zids.stream()
                .filter(zid -> zid.endsWith("/" + tzCityName))
                .collect(Collectors.toList());
        TimeZone chosenTimeZone = TimeZone.getTimeZone(
                                        possibleTimeZones.get(0));
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(chosenTimeZone);
        
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return formatTime(hour, minute);
    }

    /**
     * This method returns a HashMap where the country name is the key and 
     * the value is the 2 letter country code.
     * @return HashMap<Country, Country Code>
     */
    public static HashMap<String, String> getCountryCodes() {
        String[] isoCountries = Locale.getISOCountries();
        HashMap<String, String> countriesMapping = new HashMap<String, String>();
        for (String country : isoCountries) {
            Locale locale = new Locale("en", country);
            String code = locale.getCountry();
            String name = locale.getDisplayCountry();
            countriesMapping.put(name.toLowerCase(), code.toLowerCase());
        }
        return countriesMapping;
    }

    /**
     * This method returns the current weekday as of today. For example,
     * monday, tuesday, wednesday, thursday, friday, saturday, sunday.
     * @return The current weekday (String).
     */
    public static String getCurrentDay() {
        LocalDate today = LocalDate.now();
        return today.getDayOfWeek().toString().toLowerCase();
    }

    /**
     * This method returns the current date as of today.
     * @return The current date (String).
     */
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

    /**
     * This method returns the current start and end dates for the week. 
     * Assuming that Sunday is the start of the week and Saturday is the
     * the end of the week. 
     * @return The start and end dates (String[2]).
     */
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
