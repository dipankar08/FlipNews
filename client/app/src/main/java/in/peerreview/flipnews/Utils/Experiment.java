package in.peerreview.flipnews.Utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import in.peerreview.flipnews.Activities.MainActivity;

/**
 * Created by ddutta on 8/10/2016.
 */
public class Experiment {
    static Activity sActivity = MainActivity.Get();
    public static void test(){
        Log.d("XXX","**************************************************Start Experimnet**************************************************");

        Log.d("XXX","**************************************************End Experimnet**************************************************");
    }

}
