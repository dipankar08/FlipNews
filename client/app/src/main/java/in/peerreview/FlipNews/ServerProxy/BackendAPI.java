package in.peerreview.FlipNews.ServerProxy;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import in.peerreview.FlipNews.Utils.Notification;

//################################  CallBack #################
interface IBackendAPIResultCallBack {
    void onSuccess(JSONArray result) throws JSONException;
    void onError(String msg);
}


public class BackendAPI {
    private static String TAG ="Notification";


    public static void getData(String queury, int next_page, int limit , final IBackendAPIResultCallBack resultCallBack) throws JSONException {

        RequestParams params = new RequestParams();
        params.put("queury", queury);
        params.put("page", next_page);
        params.put("limit", limit);

        Log.d("Notification","Query ::::::::::::::::::::::::: "+params.toString());

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

                resultCallBack.onError("Not able to received data from server, Error Code: "+statusCode);
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
