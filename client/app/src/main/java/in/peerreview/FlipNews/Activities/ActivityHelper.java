package in.peerreview.FlipNews.Activities;

import java.io.File;

import in.peerreview.FlipNews.Utils.Notification;

/**
 * Created by ddutta on 7/2/2016.
 */
public  class ActivityHelper {
    private static  final String IMAGES = "Images";

    static void  createImageCache(){
        if( new File(MainActivity.getActivity().getApplicationContext().getFilesDir()+"/"+IMAGES+"/").exists() == false){
            if ( new File(MainActivity.getActivity().getApplicationContext().getFilesDir()+"/"+IMAGES+"/").mkdir() == true){
                Notification.Log("Creted a new tem dir");
            }
        }
    }


}
