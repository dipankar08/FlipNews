package in.peerreview.flipnews.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
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

}
