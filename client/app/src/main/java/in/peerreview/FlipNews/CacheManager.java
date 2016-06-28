package in.peerreview.FlipNews;

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

/**
 * Created by ddutta on 6/19/2016.
 */
public class CacheManager {

    static Map image_lookup_map = new HashMap();

    public static void renderImage(ImageView bmImage, String url,String name){
        new DownloadImageTask(bmImage).execute(url,name);
    }
    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String murl = urls[0];
            String name = urls[1];
            //downlaod the image for later user..
            if(image_lookup_map.containsKey(murl)) {
                return getBitMap((String) image_lookup_map.get(murl));
            }
            String filepath =null;
            try {
                Log.d("Downloading...",murl);
                URL url = new URL(murl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setReadTimeout(15000);
                urlConnection.connect();
               // File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
                File fileWithinMyDir = MainActivity.getActivity().getApplicationContext().getFilesDir();
                String filename = name+".jpg";
                Log.i("Local filename:", "" + filename);
                File file = new File(fileWithinMyDir, filename);
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
                    //Log.i("Progress:", "downloadedSize:" + downloadedSize + "totalSize:" + totalSize);
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
            if(filepath !=null) {
                image_lookup_map.put(murl, filepath);
                return getBitMap((String) image_lookup_map.get(murl));
            }
            return null;
        }

        private Bitmap getBitMap(String path) {
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

