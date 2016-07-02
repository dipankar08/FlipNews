package in.peerreview.FlipNews.storage;

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
import java.util.HashMap;
import java.util.Map;

import in.peerreview.FlipNews.Activities.MainActivity;
import in.peerreview.FlipNews.Utils.Notification;

/**
 * Created by ddutta on 6/19/2016.
 */
public class ImageCacheManager {

    private static final String IMAGES = "Images";

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
            Log.d("DIPANKAR","Seraching Path in Cache"+name);
            String existingImagePath = getPathIfPreDownloadedfromCache(name);
            if(existingImagePath != null) {
                Log.d("DIPANKAR","Found Images Path in Cache"+name);
                return getBitMap(existingImagePath);
            }
            Log.d("DIPANKAR","Not Found and we need to download the images"+name);

            String filepath =null;
            try {
                Log.d("DIPANKAR","Start Downloging Images..."+murl);
                URL url = new URL(murl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setReadTimeout(15000);
                urlConnection.connect();
               // File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
                File fileWithinMyDir = new File(MainActivity.getActivity().getApplicationContext().getFilesDir()+"/"+IMAGES+"/");
                Log.i("Local filename:", "" + name);
                File file = new File(fileWithinMyDir, name);
                if (file.createNewFile()) {
                    file.createNewFile();
                }
                FileOutputStream fileOutput = new FileOutputStream(file);
                InputStream inputStream = urlConnection.getInputStream();
                int totalSize = urlConnection.getContentLength();
                int downloadedSize = 0;
                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                }
                fileOutput.close();
                if (downloadedSize == totalSize) filepath = file.getPath();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                filepath = null;
                e.printStackTrace();

            } catch (Exception e) {
                filepath = null;
                e.printStackTrace();
            }
            //Notification.Log("File Path: "+filepath);
            if(filepath !=null) {
                return getBitMap(filepath);
            } else {
               Log.d("DIPANKAR","Error: Not able to generate image path");
            }
            return null;
        }

        private String getPathIfPreDownloadedfromCache(String name) {
            File images[] = new File(MainActivity.getActivity().getApplicationContext().getFilesDir()+"/"+IMAGES+"/").listFiles();
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

