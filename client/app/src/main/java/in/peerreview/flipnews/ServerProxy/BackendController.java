package in.peerreview.flipnews.ServerProxy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.peerreview.flipnews.Activities.FlipOperation;
import in.peerreview.flipnews.Activities.MainActivity;
import in.peerreview.flipnews.R;
import in.peerreview.flipnews.Utils.Logging;
import in.peerreview.flipnews.Utils.SimpleUtils;
import in.peerreview.flipnews.storage.ImageCacheManager;
import in.peerreview.flipnews.storage.DataSource;
import in.peerreview.flipnews.storage.FileSaveLoad;

/**
 * Created by ddutta on 6/19/2016.
 */
public class BackendController implements IBackendAPIResultCallBack {

    private static int limit = 10;
    private static int next_page = 1;
    private static boolean is_data_fetch_in_progress = false;
    JSONArray current_news_list = new JSONArray();



    private static BackendController sBackendController = new BackendController();


    public static BackendController Get(){
        return sBackendController;
    }

    private List<DataSource> getDataSourceFromJson(JSONArray arr) throws JSONException {
        List<DataSource> dataSourceList = new ArrayList<DataSource>();
        try {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject ele = arr.getJSONObject(i);
                dataSourceList.add(new DataSource(ele));
            }
            return dataSourceList;
        } catch(Exception e){
            Log.d("Dipankar ","Error in building DataSource "+e.getMessage());
        }

        return null;
    }

    public void firstBootLoad(){
        try {
            JSONArray arr = FileSaveLoad.loadData();
            if( arr == null){
                Logging.Log(" Doing Network call");
                fecthNextSetOfpages();

            } else {
                Logging.Log("Loaded from file cache... and let;s return pull data to FilpOperations.");

                FlipOperation.renderData(getDataSourceFromJson(arr));
                //Calculate Next page
                next_page = arr.length()/limit;
            }

        } catch (JSONException e) {
            Toast.makeText(MainActivity.Get(),"Some Error wile loading data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private static JSONArray concatArray(JSONArray... arrs)
            throws JSONException {
        JSONArray result = new JSONArray();
        for (JSONArray arr : arrs) {
            for (int i = 0; i < arr.length(); i++) {
                result.put(arr.get(i));
            }
        }
        return result;
    }




    private void fecthNextSetOfpages() {
        Logging.Log("Loading : Page:"+next_page+" Limit: "+limit);
        try {
            if(is_data_fetch_in_progress == false ) {
                BackendAPI.getData(null, next_page, limit, this,false);
                is_data_fetch_in_progress = true;
            } else {
                Logging.Log("is_data_fetch_in_progress ...");
            }
        }
        catch (Exception e){
            Logging.Log(e.toString());
            is_data_fetch_in_progress = false;
        }
    }


    public  void firstTimeNetworkLoad() {
        // this will call for the first time which load 50 data from beggining...

        Logging.Log("Calling : firstTimeNetworkLoad");
        try {
            if(is_data_fetch_in_progress == false ) {
                Logging.Log("Calling : Doing NEtwork Calls..");

                BackendAPI.getData(null, 1, 20, this,true/*UX Blocking..*/);
                is_data_fetch_in_progress = true;
            } else {
                Logging.Log("is_data_fetch_in_progress ...");
            }
        }
        catch (Exception e){
            Logging.Log(e.toString());
            is_data_fetch_in_progress = false;
        }
    }
    @Override
    public void onSuccess(JSONArray result) throws JSONException {
        getDataSourceFromJson(result);
        current_news_list = concatArray(current_news_list,result);
        FileSaveLoad.storeData(current_news_list); //store the new list
        is_data_fetch_in_progress = false;
        next_page++;
    }

    @Override
    public void onError(String msg) {
        Logging.showErrorAndExit(msg);
        is_data_fetch_in_progress = false;
    }




    //calling By dates and no cache...
    public void getdataFromServerByDate(final String date ,final IProcessData callback) { //SimpleUtils.getDateFormat(dateOffset)
        try {
            if(is_data_fetch_in_progress == false ) {
                Map<String,String> query = new HashMap<String,String>();
                query.put("date",date);
                BackendAPI.getData(query, //query
                        next_page,
                        limit,
                        new IBackendAPIResultCallBack() {
                            @Override
                            public void onSuccess(JSONArray result) throws JSONException {
                               // //USE CALLBACK
                                is_data_fetch_in_progress = false;
                                callback.process(getDataSourceFromJson(result));
                            }

                            @Override
                            public void onError(String msg) {
                                is_data_fetch_in_progress = false;
                            }
                        },
                        false);
                is_data_fetch_in_progress = true;
            } else {
                Logging.Log("is_data_fetch_in_progress ...");
            }
        }
        catch (Exception e){
            Logging.Log(e.toString());
            is_data_fetch_in_progress = false;
        }
    }

}

