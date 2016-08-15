/*
  Aim is to provide Offline access of data...
 */
package in.peerreview.flipnews.storage;

import android.os.Environment;
import android.util.Log;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappyDB;
import com.snappydb.SnappydbException;

import java.util.Arrays;
import java.util.List;


import in.peerreview.flipnews.Activities.MainActivity;

/**
 * Created by ddutta on 8/15/2016.
 */

interface  IDatabaseOperations {

    List<DataSource> getData(String[] keys);
    boolean setData(String[] keys, List<DataSource> s);
    boolean exist(String[] key);

}

public class DataBaseProxy implements IDatabaseOperations{

    private final String STORAGE_TYPE = "SnappyDB";

    private DB snappydb = null;

    private static DataBaseProxy sDataBaseProxy = null;
    public static DataBaseProxy Get(){
        if(sDataBaseProxy == null) {
            sDataBaseProxy = new DataBaseProxy();
            sDataBaseProxy.setup();
        }

        return sDataBaseProxy;
    }

    private void setup(){
        try {
           // snappydb =  DBFactory.open(MainActivity.Get().getApplicationContext(), "books");
            snappydb = new SnappyDB.Builder(MainActivity.Get().getApplicationContext())
                    .directory(Environment.getExternalStorageDirectory().getAbsolutePath()) //optional
                    .name("flipNews")//optional
                    .build();
            Log.d("Dipankar","DataBaseProxy: setup done.");
        } catch (SnappydbException e) {
            Log.d("Dipankar","DataBaseProxy: setup failed.");
            e.printStackTrace();
        }
    }

    public void destroy(){
        if (snappydb != null) {
            try {
                snappydb.close();
                snappydb.destroy();
                snappydb = null;
                Log.d("Dipankar","DataBaseProxy: destroy passed.");
            } catch (SnappydbException e) {
                e.printStackTrace();
                Log.d("Dipankar","DataBaseProxy: destroy failed.");
            }

        }
    }

    @Override
    public List<DataSource> getData(String[] keys) {
        if(snappydb == null) return null;

        try {
            DataSource[] a = null;
            synchronized (snappydb) {
                a = snappydb.getObjectArray(keys[0], DataSource.class);
            }
            Log.d("Dipankar","DataBaseProxy: getData passed.");
            return Arrays.asList(a);
        } catch (SnappydbException e) {
            e.printStackTrace();
            Log.d("Dipankar","DataBaseProxy: getdata failed.");
        }

        return null;
    }

    @Override
    public boolean setData(String[] keys, List<DataSource> s) {
        if(snappydb == null) return false;

        try {
            synchronized (snappydb) {
                snappydb.put(keys[0], s.toArray());
            }
            Log.d("Dipankar","DataBaseProxy: setData passed.");
            return true;
        } catch (SnappydbException e) {
            Log.d("Dipankar","DataBaseProxy: setData failed.");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean exist(String[] keys) {
        if(snappydb == null) return false;
        try {
            synchronized (snappydb) {
                return snappydb.exists(keys[0]);
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
            return false;
        }
    }
}
