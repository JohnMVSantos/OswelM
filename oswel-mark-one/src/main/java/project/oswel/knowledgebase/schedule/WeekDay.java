package project.oswel.knowledgebase.schedule;

/**
 * WeekDays provides information for different ways of representing the days
 * of the week and provides proper indexing for each days. 
 * @author John Santos
 */
public enum WeekDay {
    SUNDAY      (0),
    MONDAY      (1),
    TUESDAY     (2),
    WEDNESDAY   (3),
    THURSDAY    (4),
    FRIDAY      (5),
    SATURDAY    (6);    

    private final int index;

    /**
     * Constructor
     * @param index This is the index that corresponds to the week day.
     */
    private WeekDay(int index) { this.index = index; }
    
    /**
     * This method returns the index of the given week day.
     * @return index that represents the weekday.
     */
    public int getIndex() { return this.index; }

    public static String[] getDaysOfWeek() {
        String[] daysOfWeek = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday"};
        return daysOfWeek;
    }

    /**
     * Checks if the given name string exists in the WeekDay enum.
     * It will only match if the name is in all lower case.
     * @param name The string to be checked.
     * @return The matching WeekDay enum, if it exists
     * @throws IllegalArgumentException if the provided name did not match 
     * any cases.
     */
    public static WeekDay getWeekDayFromString (String name) 
                                        throws IllegalArgumentException {
        switch (name) {
            case "sunday":
                return WeekDay.SUNDAY;
            case "monday":
                return WeekDay.MONDAY;
            case "tuesday":
                return WeekDay.TUESDAY;
            case "wednesday":
                return WeekDay.WEDNESDAY;
            case "thursday":
                return WeekDay.THURSDAY;
            case "friday":
                return WeekDay.FRIDAY;
            case "saturday":
                return WeekDay.SATURDAY;
            default:
                throw new IllegalArgumentException(
                    "Week day " + name + " is not valid.");
        }
    }

    // JavaDoc automatically provides documentation for this from 
    // the super implementation.
    @Override 
    public String toString() {
        switch (this) {
            case SUNDAY:
                return "sunday";
            case MONDAY:
                return "monday";
            case TUESDAY:
                return "tuesday";
            case WEDNESDAY:
                return "wednesday";
            case THURSDAY:
                return "thursday";
            case FRIDAY:
                return "friday";
            case SATURDAY:
                return "saturday";
            default:
                return "unknown";
        }
    }

}
