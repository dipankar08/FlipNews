package in.peerreview.flipnews.Utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import in.peerreview.flipnews.Activities.MainActivity;

/**
 * Created by ddutta on 8/10/2016.
 */
public class Experiment {
    static Activity sActivity = MainActivity.Get();
    public static void test(){
        Log.d("XXX","**************************************************Start Experimnet**************************************************");
        //shareImage();

        Log.d("XXX","**************************************************End Experimnet**************************************************");
    }

}
