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
    public static String getDateFormat(int offset){ //0, 1,2,3
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -offset);
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String formatted = format1.format(cal.getTime());
        return formatted;
    }
    public static void setProvidersLogo(ImageView v, String name){
        Map logo_map = new HashMap() {{
            put("anandabazar", MainActivity.getActivity().getResources().getDrawable(R.drawable.anadabazar));
            put("bartaman", MainActivity.getActivity().getResources().getDrawable(R.drawable.bartaman));
            put("sangbadpratidin", MainActivity.getActivity().getResources().getDrawable(R.drawable.sangbadpratidin));
            put("eisomoy", MainActivity.getActivity().getResources().getDrawable(R.drawable.eisomoy));
            put("eisamay", MainActivity.getActivity().getResources().getDrawable(R.drawable.eisomoy));
            put("ZeeNews", MainActivity.getActivity().getResources().getDrawable(R.drawable.zeenews));
        }};

        v.setImageDrawable((Drawable) logo_map.get(name.trim()));
    }
}
