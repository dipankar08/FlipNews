package in.peerreview.FlipNews.Activities;

import android.os.Environment;

import java.io.File;

import in.peerreview.FlipNews.Utils.Notification;

/**
 * Created by ddutta on 7/2/2016.
 */
public  class ActivityHelper {
    private static  final String IMAGES = "Images";
    private static  final String FlipNewsDir = "FlipNews";



    static void  createImageCache(){
        if( new File(MainActivity.getActivity().getApplicationContext().getFilesDir()+"/"+IMAGES+"/").exists() == false){
            if ( new File(MainActivity.getActivity().getApplicationContext().getFilesDir()+"/"+IMAGES+"/").mkdir() == true){
                Notification.Log("Creted a new tem dir");
            }
        }
        File sdCard = Environment.getExternalStorageDirectory();
        if( new File(sdCard+"/"+FlipNewsDir+"/").exists() == false){
            if ( new File(sdCard+"/"+FlipNewsDir+"/").mkdir() == true){
                Notification.Log("Creted a new tem dir in SDCARD");
            }
        }
    }
    public static File  getImageCache(){
        return new File(Environment.getExternalStorageDirectory()+"/"+FlipNewsDir+"/");
    }

}
