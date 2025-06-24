package cat.plexians.utils;

public class DateUtils {

    public DateUtils() {
    }

    public String dateFromYYYYMMDDtoMMDDYYYY(String date) {
        String[] parts = date.split("\\s");
        String[] parts2 = parts[0].split("-");
        String part1 = parts2[0];
        String part2 = parts2[1];
        String part3 = parts2[2];

        String fixedDate = part3 + "-" + part2 + "-" + part1 + " " + parts[1];
        return fixedDate;
    }

    public String afmoddmmyyyyhh24mi(String date) {
        String[] parts = date.split("\\s");
        String[] parts2 = parts[0].split("-");
        String time = parts[1].replaceAll(":", "");
        String part1 = parts2[0];
        String part2 = parts2[1];
        String part3 = parts2[2];

        String fixedDate = part3 + "-" + part2 + "-" + part1 + "" + time;

        fixedDate = fixedDate.substring(0, fixedDate.length() - 2);

        return fixedDate;
    }
}
