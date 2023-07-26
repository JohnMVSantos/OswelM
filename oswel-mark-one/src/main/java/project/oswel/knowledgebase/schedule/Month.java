package project.oswel.knowledgebase.schedule;

/**
 * Months encapsulates information on different ways of representing the
 * months of the year.
 * @author John Santos
 */
public enum Month {
    JANUARY     ("january", 1),
    FEBRUARY    ("february", 2),
    MARCH       ("march", 3),
    APRIL       ("april", 4),
    MAY         ("may", 5),
    JUNE        ("june", 6),
    JULY        ("july", 7),
    AUGUST      ("august", 8),
    SEPTEMBER   ("september", 9),
    OCTOBER     ("october", 10),
    NOVEMBER    ("november", 11),
    DECEMBER    ("december", 12);

    private final String monthName;
    private final int monthNumber;
    
    /**
     * Constructor
     * @param monthName This is the string representation of the month.
     * @param monthNumber This is the numerical representation of the month.
     */
    private Month(String monthName, int monthNumber) {
        this.monthName = monthName;
        this.monthNumber = monthNumber;
    }

    /**
     * Checks if the given name string exists in the Month enum.
     * It will only match if the name is in all lower case.
     * @param name The string to be checked.
     * @return The matching Month enum, if it exists
     * @throws IllegalArgumentException if the provided name did not match 
     * any cases.
     */
    public static Month getMonthFromString (String name) 
                                        throws IllegalArgumentException {
        switch (name) {
            case "january":
                return Month.JANUARY;
            case "february":
                return Month.FEBRUARY;
            case "march":
                return Month.MARCH;
            case "april":
                return Month.APRIL;
            case "may":
                return Month.MAY;
            case "june":
                return Month.JUNE;
            case "july":
                return Month.JULY;
            case "august":
                return Month.AUGUST;
            case "september":
                return Month.SEPTEMBER;
            case "october":
                return Month.OCTOBER;
            case "november":
                return Month.NOVEMBER;
            case "december":
                return Month.DECEMBER;
            default:
                throw new IllegalArgumentException(
                    "Week day " + name + " is not valid.");
        }
    }

    /**
     * This method returns the name of the month represented by an integer.
     * @param number The integer which the month represents.
     * @return The name of the month (String).
     */
    public static String getStringFromInt (int number) {
        switch (number) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "unknown";
        }
    }

    // JavaDoc automatically provides documentation for this from 
    // the super implementation.
    @Override 
    public String toString() {
        switch (this) {
            case JANUARY:
                return "january";
            case FEBRUARY:
                return "february";
            case MARCH:
                return "march";
            case APRIL:
                return "april";
            case MAY:
                return "may";
            case JUNE:
                return "june";
            case JULY:
                return "july";
            case AUGUST:
                return "august";
            case SEPTEMBER:
                return "september";
            case OCTOBER:
                return "october";
            case NOVEMBER:
                return "november";
            case DECEMBER:
                return "december";
            default:
                return "unknown";
        }
    }
}
