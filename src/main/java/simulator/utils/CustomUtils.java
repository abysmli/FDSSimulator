package simulator.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CustomUtils {

    public static final String dateFormat = "dd-MM-yyyy hh:mm:ss";

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

    public static String ConvertMilliSecondsToFormattedDate(long milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return simpleDateFormat.format(calendar.getTime());
    }
}
