package in.peerreview.flipnews.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.peerreview.flipnews.Activities.MainActivity;
import in.peerreview.flipnews.R;

/**
 * Created by ddutta on 8/10/2016.
 */
public class SimpleUtils {
    private static Activity sActivity = MainActivity.Get();

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

    /*
        Aim: Get the screen sort of the project,,
     */
    public static File getScreenSort() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".png";

            // create bitmap screen capture
            View v1 = MainActivity.Get().getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            return imageFile;


        } catch (Exception e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
        return null;
    }

    /*
        Amim: Share Image as a shared intent
     */
    public static void share() {
        File imageFile = getScreenSort();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        sActivity.startActivity(intent);
    }

    /*
        Amim: Print keyhash programmatically...
    */
    public static void printKeyHash() {
        try {
            PackageInfo info = sActivity.getPackageManager().getPackageInfo("in.peerreview.flipnews", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {


        } catch (NoSuchAlgorithmException e) {

        }
    }
    //extractLogToFile
    private String extractLogToFile()
    {
        PackageManager manager = MainActivity.Get().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo (MainActivity.Get().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {
        }
        String model = Build.MODEL;
        if (!model.startsWith(Build.MANUFACTURER))
            model = Build.MANUFACTURER + " " + model;

        // Make file name - file must be saved to external storage or it wont be readable by
        // the email app.
        String path = Environment.getExternalStorageDirectory() + "/" + "MyApp/";
        String fullName = path + "adblogs.txt";

        // Extract to file.
        File file = new File (fullName);
        InputStreamReader reader = null;
        FileWriter writer = null;
        try
        {
            // For Android 4.0 and earlier, you will get all app's log output, so filter it to
            // mostly limit it to your app's output.  In later versions, the filtering isn't needed.
            String cmd = (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) ?
                    "logcat -d -v time MyApp:v dalvikvm:v System.err:v *:s" :
                    "logcat -d -v time";

            // get input stream
            Process process = Runtime.getRuntime().exec(cmd);
            reader = new InputStreamReader (process.getInputStream());

            // write output stream
            writer = new FileWriter (file);
            writer.write ("Android version: " +  Build.VERSION.SDK_INT + "\n");
            writer.write ("Device: " + model + "\n");
            writer.write ("App version: " + (info == null ? "(null)" : info.versionCode) + "\n");

            char[] buffer = new char[10000];
            do
            {
                int n = reader.read (buffer, 0, buffer.length);
                if (n == -1)
                    break;
                writer.write (buffer, 0, n);
            } while (true);

            reader.close();
            writer.close();
        }
        catch (Exception e)
        {
            if (writer != null)
                try {
                    writer.close();
                } catch (Exception e1) {
                }
            if (reader != null)
                try {
                    reader.close();
                } catch (Exception e1) {
                }

            // You might want to write a failure message to the log here.
            return null;
        }

        return fullName;
    }
    public static JSONObject getSystemInfo() {
        try {
            JSONObject obj = new JSONObject();

            obj.put("DEVICE_ID", Settings.Secure.getString(MainActivity.Get().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));

            return obj;
        } catch (JSONException e) {
            return null;
        }
    }

}
