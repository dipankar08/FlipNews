package in.peerreview.FlipNews;

import android.util.Log;

import org.json.JSONArray;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by ddutta on 6/25/2016.
 */
public class FileSaveLoad {
    public static void storeData(JSONArray arr){
        try
        {
            FileOutputStream myFileOutputStream = new FileOutputStream(MainActivity.getActivity().getApplicationContext().getFilesDir() + "cache.pkl");
            ObjectOutputStream myObjectOutputStream = new ObjectOutputStream(myFileOutputStream);
            myObjectOutputStream.writeObject(arr.toString());
            myObjectOutputStream.close();
            Notification.Log("Saved properly of data length "+arr.length());
        }
        catch (Exception e)
        {
            Log.e("Error.",Log.getStackTraceString(e));
        }
    }
    public static JSONArray loadData(){
        try
        {
            String response = null;

            FileInputStream myFileOutputStream = new FileInputStream (MainActivity.getActivity().getApplicationContext().getFilesDir() + "cache.pkl");
            ObjectInputStream  myObjectOutputStream = new ObjectInputStream(myFileOutputStream);
            response = (String) myObjectOutputStream.readObject();
            myObjectOutputStream.close();

            JSONArray array = new JSONArray(response);
            Notification.Log("File Load  properly with datalength"+array.length());
            return array;
        }
        catch (Exception e)
        {
            Notification.Log("Error. No file Doest exist"+e.toString());
        }
        return null;
    }
}
