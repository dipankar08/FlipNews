package in.peerreview.FlipNews.ServerProxy;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import in.peerreview.FlipNews.Activities.MainActivity;
import in.peerreview.FlipNews.R;
import in.peerreview.FlipNews.Utils.Notification;
import in.peerreview.FlipNews.storage.ImageCacheManager;
import in.peerreview.FlipNews.storage.DataSource;
import in.peerreview.FlipNews.storage.FileSaveLoad;

/**
 * Created by ddutta on 6/19/2016.
 */
public class BackendController implements IBackendAPIResultCallBack {

    private static int limit = 10;
    private static int next_page = 1;
    private static String queury ="";
    private static boolean is_data_fetch_in_progress = false;

    private static JSONArray current_news_list = new JSONArray();

    public void firstBootLoad(){
        try {
            JSONArray arr = FileSaveLoad.loadData();
            if( arr == null){
                Notification.Log(" Doing Network call");
                fecthNextSetOfpages();

            } else {
                Notification.Log("Loaded from file cache...");
                current_news_list = arr;
                renderData();
            }

        } catch (JSONException e) {
            Toast.makeText(MainActivity.Get(),"Some Error wile loading data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private static void renderData() throws JSONException {
        JSONArray arr = current_news_list;
        Notification.Log("renderData Index <"+(next_page -1)*limit+" , "+((next_page)* limit));
        for(int i = (next_page-1)*limit; i < (next_page)* limit ;i++){
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
        ImageCacheManager.renderImage((ImageView) view.findViewById(R.id.image),d.getHead_image(),d.getRand_id());
        TextView textView1 = (TextView)view.findViewById(R.id.text1);
        TextView textView2 = (TextView)view.findViewById(R.id.text2);
        ImageView providerImage = (ImageView)view.findViewById(R.id.provider_logo);
        textView1.setText(d.getTitle().trim());
        textView2.setText(d.getDetails().trim());
        Map logo_map = new HashMap() {{
            put("Anadabazar",MainActivity.getActivity().getResources().getDrawable(R.drawable.eisomoy));
            put("bartaman",MainActivity.getActivity().getResources().getDrawable(R.drawable.eisomoy));
            put("sangbadpratidin",MainActivity.getActivity().getResources().getDrawable(R.drawable.eisomoy));
            put("ZeeNews",MainActivity.getActivity().getResources().getDrawable(R.drawable.eisomoy));
        }};
        providerImage.setImageDrawable((Drawable) logo_map.get(d.getSource_name().trim()));
        Log.d("Dipankar",d.getSource_name().trim());
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

    public void fixNext(int index) {
        if((current_news_list.length() - index) < 5 ){ // we need to do the next call.
            Notification.Log("Calling for next page fetch of news..");
            fecthNextSetOfpages();
        } else if((next_page -1) * limit -index < 5){
            try {
                renderData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void fecthNextSetOfpages() {
        Notification.Log("Loading : Page:"+next_page+" Limit: "+limit);
        try {
            if(is_data_fetch_in_progress == false ) {
                BackendAPI.getData(queury, next_page, limit, this);
                is_data_fetch_in_progress = true;
            } else {
                Notification.Log("is_data_fetch_in_progress ...");
            }
        }
        catch (Exception e){
            Notification.Log(e.toString());
            is_data_fetch_in_progress = false;
        }
    }

    @Override
    public void onSuccess(JSONArray result) throws JSONException {
        current_news_list = concatArray(current_news_list,result);
        FileSaveLoad.storeData(current_news_list); //store the new list
        is_data_fetch_in_progress = false;
    }

    @Override
    public void onError(String msg) {
        Notification.showErrorAndExit(msg);
        is_data_fetch_in_progress = false;
    }
}

