package in.peerreview.flipnews.ServerProxy;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import in.peerreview.flipnews.Utils.Logging;

//################################  CallBack #################
interface IBackendAPIResultCallBack {
    void onSuccess(JSONArray result) throws JSONException;
    void onError(String msg);
}


public class BackendAPI {
    private static String TAG ="Logging";


    public static void getData(Map<String, String> queryList, int next_page, int limit , final IBackendAPIResultCallBack resultCallBack, boolean is_blocking ) throws JSONException {

        RequestParams params = new RequestParams();

        if(queryList != null) {
            for (Map.Entry<String, String> entry : queryList.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                params.put(key, value);
            }
        }

        params.put("page", next_page);
        params.put("limit", limit);

        Log.d("Dipankar","BackendAPI.getdata: "+params.toString());

        HttpUtils.get("", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("Dip", response.toString());
                try {
                    if(response.getString("status").equals("OK")){
                        Log.d(TAG,"We get some data");
                        JSONArray arr = response.getJSONArray("result");

                        //BackendController.processNetworkData(arr);
                        Logging.Log("News retrived of length "+arr.length());
                        resultCallBack.onSuccess(arr);

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

                resultCallBack.onError("Not able to received data from server, Error Code: "+statusCode+"Message:"+throwable.getMessage());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Logging.showErrorAndExit("Not able to received data from server, Error Code: "+statusCode);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Logging.showErrorAndExit("Not able to received data from server, Error Code: "+statusCode);
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
