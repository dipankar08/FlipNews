package in.peerreview.flipnews.Utils;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import in.peerreview.flipnews.Activities.MainActivity;
import in.peerreview.flipnews.R;

/**
 * Created by ddutta on 8/10/2016.
 */
public class SimpleUtils {
    final static Map logo_map = new HashMap() {{
        put("anandabazar", MainActivity.getActivity().getResources().getDrawable(R.drawable.anadabazar));
        put("bartaman", MainActivity.getActivity().getResources().getDrawable(R.drawable.bartaman));
        put("sangbadpratidin", MainActivity.getActivity().getResources().getDrawable(R.drawable.sangbadpratidin));
        put("eisomoy", MainActivity.getActivity().getResources().getDrawable(R.drawable.eisomoy));
        put("eisamay", MainActivity.getActivity().getResources().getDrawable(R.drawable.eisomoy));
        put("ZeeNews", MainActivity.getActivity().getResources().getDrawable(R.drawable.zeenews));
    }};



    public static String getDateFormat(int offset){ //0, 1,2,3
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -offset);
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String formatted = format1.format(cal.getTime());
        return formatted;
    }
    private static String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static String getDateReadable(int offset){ //0, 1,2,3
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -offset);
        if(offset ==0){
            return "Today";
        } else if(offset == 1){
            return "Yesterday";
        } else if(offset < 5){
            return new SimpleDateFormat("EEEE").format(cal.getTime());
        } else {
            return new SimpleDateFormat("MMMM dd, yyyy").format(cal.getTime());
        }
    }

    public static void setProvidersLogo(ImageView v, String name){


        v.setImageDrawable((Drawable) logo_map.get(name.trim()));
    }
}
