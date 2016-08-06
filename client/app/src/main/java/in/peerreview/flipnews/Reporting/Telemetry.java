package in.peerreview.flipnews.Reporting;

import android.os.Bundle;

//import com.google.firebase.analytics.FirebaseAnalytics;

import in.peerreview.flipnews.Activities.MainActivity;

/**
 * Created by ddutta on 8/4/2016.
 */
public class Telemetry {
   // private static FirebaseAnalytics mFirebaseAnalytics;
   //private static FirebaseAnalytics mFirebaseAnalytics;
    public static void init(){
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(MainActivity.getActivity());
    }
    public static void log(String type,Bundle params){
        //Bundle params = new Bundle();
        //params.putString("image_name", name);
       // params.putString("full_text", text);
        //mFirebaseAnalytics.logEvent("share_image", params);
    }
}
