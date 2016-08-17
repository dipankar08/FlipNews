package in.peerreview.flipnews.storage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import in.peerreview.flipnews.Activities.ActivityHelper;
import in.peerreview.flipnews.Utils.L;

/**
 * Created by ddutta on 6/19/2016.
 */
public class ImageCacheManager {

    public static void renderImage(ImageView bmImage, String url,String name){
        new DownloadImageTask(bmImage).execute(url,name);
    }
    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            /* This function is done in backgruoud
              The main purpose to downoad the image and store it by it's rand name to the disk.
              If the file found in the image cache. Good Just retuirn it.
             */
            String murl = urls[0];
            String name = urls[1]+ ".jpg";
            //downlaod the image for later user..
            L.d("Seraching Path in Cache"+name);
            String existingImagePath = getPathIfPreDownloadedfromCache(name);
            if(existingImagePath != null) {
                L.d("Found Images Path in Cache"+name);
                return getBitMap(existingImagePath);
            }
            L.d("Not Found and we need to download the images"+name);

            String filepath =null;
            try {
                L.d("Start Downloging Images..." + murl);
                URL url = new URL(murl);
                /*
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setReadTimeout(15000);
                urlConnection.connect();
                */
                InputStream is = null;
                try {
                    is = url.openStream();
                } catch (Exception e) {
                    L.d("Not able read Image URL data . Please investigate -->" + murl);
                    return null;
                }
                File fileWithinMyDir = ActivityHelper.getImageCache();   //new File(MainActivity.getActivity().getApplicationContext().getFilesDir()+"/"+IMAGES+"/");
                Log.i("Local filename:", "" + name);
                File file = new File(fileWithinMyDir, name);
                if (file.createNewFile()) {
                    file.createNewFile();
                }
                FileOutputStream os = new FileOutputStream(file);

                byte[] b = new byte[2048];
                int length;

                while ((length = is.read(b)) != -1) {
                    os.write(b, 0, length);
                }
                filepath = file.getAbsolutePath();

                is.close();
                os.close();


            } catch (Exception e) {
                filepath = null;
                L.d("Error: Not able to generate image path"+e.getMessage());
                e.printStackTrace();
            }

            if(filepath !=null) {
                return getBitMap(filepath);
            } else {

            }
            return null;
        }

        private String getPathIfPreDownloadedfromCache(String name) {
            File images[] = ActivityHelper.getImageCache().listFiles();
            if(images == null) return null;
            for (File f : images){
                if(f.getName().equals(name)){
                    return f.getAbsolutePath();
                }
            }
            return null;
        }

        private Bitmap getBitMap(String path) {
            /* Build the bitmap iamge from file in disk*/
            try {
                Log.d("getBitMap", path);
                File sd = Environment.getExternalStorageDirectory();
                File image = new File(path);
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
                return bitmap;
            } catch(Exception e){
                Log.d("getBitMap","Error: "+e.toString());
            }
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            if(result != null)
                bmImage.setImageBitmap(result);
        }
    }
}

