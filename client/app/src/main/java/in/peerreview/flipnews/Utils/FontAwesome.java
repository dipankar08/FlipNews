package in.peerreview.flipnews.Utils;

import android.graphics.Typeface;
import android.widget.TextView;

import in.peerreview.flipnews.LaunchActivity.LaunchActivity;

/**
 * Created by ddutta on 8/6/2016.
 */
public class FontAwesome {
    private static Typeface font;
    public static void setup(){
        font = Typeface.createFromAsset(LaunchActivity.Get().getAssets(), "fonts/fontawesome.ttf");
    }
    public static void setIcon(TextView v, String iconId){
        if(v == null) return;
        v.setTypeface(font);
        if(iconId != null)
            v.setText(iconId);
    }
}
