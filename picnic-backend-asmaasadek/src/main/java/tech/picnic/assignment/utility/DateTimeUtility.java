package tech.picnic.assignment.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtility {
    public static Date convertUTCDateToString(String dateStr) throws ParseException {
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return sourceFormat.parse(dateStr);
    }

    public static String convertUTCDateToString(Date date) throws ParseException {
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return sourceFormat.format(date);
    }
}
