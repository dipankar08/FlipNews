package in.peerreview.flipnews.Utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import in.peerreview.flipnews.Activities.MainActivity;

/**
 * Created by ddutta on 8/13/2016.
 */
public class ShareNews {
    private static Activity sActivity = MainActivity.Get();
    public static void share(){
        View rootView = sActivity.getWindow().getDecorView().findViewById(android.R.id.content);
        Bitmap mBitmap = getScreenShot(rootView);
        File f = store(mBitmap,"try_to_share.png");
        if(f != null){
            shareImage(f);
        }

    }
    public static Bitmap getScreenShot(View view) {

        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);

        // this is the important code :)
        // Without it the view will have a dimension of 0,0 and the bitmap will be null
        screenView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        screenView.layout(0, 0, screenView.getMeasuredWidth(), screenView.getMeasuredHeight());

        screenView.buildDrawingCache(true);

        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }
    public static File store(Bitmap bm, String fileName){
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";

        try {
            File dir = new File(dirPath);
            if(!dir.exists())
                dir.mkdirs();
            File file = new File(dirPath, fileName);
            if(!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static void shareImage(File file){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            sActivity.startActivity(Intent.createChooser(intent, "Share this news"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(sActivity, "No App Available to share this news", Toast.LENGTH_SHORT).show();
        }
    }
}
