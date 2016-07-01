package in.peerreview.FlipNews.ServerProxy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import in.peerreview.FlipNews.Activities.MainActivity;
import in.peerreview.FlipNews.R;
import in.peerreview.FlipNews.Utils.Notification;
import in.peerreview.FlipNews.storage.CacheManager;
import in.peerreview.FlipNews.storage.DataSource;
import in.peerreview.FlipNews.storage.FileSaveLoad;

/**
 * Created by ddutta on 6/19/2016.
 */
public class BackendController {
    private static int limit = 10;
    private static int next_page = 0;
    private static String queury ="";

    private static JSONArray current_news_list = new JSONArray();

    public static void firstBootLoad(){
        try {
            Toast.makeText(MainActivity.Get(),"Try to load Data", Toast.LENGTH_SHORT).show();
            JSONArray arr = FileSaveLoad.loadData();
            if( arr == null){
                Notification.Log(" Doing Network call");
                Toast.makeText(MainActivity.Get(),"Doing network call", Toast.LENGTH_SHORT).show();
                fecthNextSetOfpages();

            } else {
                Toast.makeText(MainActivity.Get(),"Getting from Cache", Toast.LENGTH_SHORT).show();
                Notification.Log("Loaded from file cache...");
                current_news_list = arr;
                renderData();
            }

        } catch (JSONException e) {
            Toast.makeText(MainActivity.Get(),"Some Error wile loading data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static void processNetworkData(JSONArray arr) throws JSONException {
        current_news_list = concatArray(current_news_list,arr);
        FileSaveLoad.storeData(current_news_list); //store the new list
    }



    private static void renderData() throws JSONException {
        JSONArray arr = current_news_list;
        Notification.Log("renderData Index <"+next_page*limit+" , "+((next_page + 1)* limit));
        for(int i = next_page*limit; i < (next_page + 1)* limit ;i++){
            JSONObject ele = arr.getJSONObject(i);
            DataSource d= new DataSource(ele);
            addMoreView(d);
        }
        next_page++;
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
    public static void addMoreView(DataSource d) {
        LayoutInflater inflater = (LayoutInflater) MainActivity.Get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.card, MainActivity.Get().getFlipper(), false);
        CacheManager.renderImage((ImageView) view.findViewById(R.id.image),d.getHead_image(),d.getRand_id());
        TextView textView1 = (TextView)view.findViewById(R.id.text1);
        TextView textView2 = (TextView)view.findViewById(R.id.text2);
        TextView textView3 = (TextView)view.findViewById(R.id.text3);
        textView1.setText(d.getTitle().trim());
        textView2.setText(d.getDetails().trim());
        textView3.setText("Powered by "+ d.getSource_name().trim());
/*
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/Durga.ttf");
        textView1.setTypeface(typeFace);
        Typeface typeFace2=Typeface.createFromAsset(getAssets(),"fonts/NikoshLightBan.ttf");
        textView2.setTypeface(typeFace2);

*/
       // view.setOnTouchListener(gestureListener);
        MainActivity.Get().getFlipper().addView(view);
        MainActivity.Get().getFlipper().invalidate();
    }

    public static void fixNext(int index) {
        if((current_news_list.length() - index) < 5 ){ // we need to do the next call.
            Notification.Log("Calling for next page fetch of news..");
            fecthNextSetOfpages();
        } else if(next_page * limit -index < 5){
            try {
                renderData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static void fecthNextSetOfpages() {

        Notification.Log("Loading : Page:"+next_page+" Limit: "+limit);
        try {
            BackendAPI.getData(queury, next_page, limit);
        }
        catch (Exception e){
            Notification.Log(e.toString());
        }
    }
}


class BackendAPI {
    private static String TAG ="DIPANKAR";
    public static void getData(String queury, int next_page, int limit) throws JSONException {

        RequestParams params = new RequestParams();
        params.put("queury", queury);
        params.put("page", next_page);
        params.put("limit", limit);

        HttpUtils.get("", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("Dip", response.toString());
                try {
                if(response.getString("status").equals("OK")){
                    Log.d(TAG,"We get some data");
                    JSONArray arr = response.getJSONArray("result");

                    BackendController.processNetworkData(arr);

                } else{
                        Log.d(TAG,"Some error in server");

                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {

            //todo
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Notification.showErrorAndExit("Not able to received data from server, Error Code: "+statusCode);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Notification.showErrorAndExit("Not able to received data from server, Error Code: "+statusCode);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Notification.showErrorAndExit("Not able to received data from server, Error Code: "+statusCode);
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}

class HttpUtils {
    private static final String BASE_URL = "http://peerreview.in:5555/news";


    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
         client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }


    public static void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}