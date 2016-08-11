package in.peerreview.flipnews.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ddutta on 8/10/2016.
 */
public class SimpleUtils {
    public static String getDateFormat(int offset){ //0, 1,2,3
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -offset);
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String formatted = format1.format(cal.getTime());
        return formatted;
    }
}
