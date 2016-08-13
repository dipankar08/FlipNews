package in.peerreview.flipnews.storage;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ddutta on 6/19/2016.
 */
public class DataSource {
    private final String details;
    private final String head_image;
    private final String rand_id;
    private String title;
    private String remore_url;
    private String time;

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    public String getEpoch() {
        return epoch;
    }

    private String epoch;

    public String getHead_image() {
        return head_image;
    }

    public String getRand_id() {
        return rand_id;
    }

    public String getTitle() {
        return title;
    }
    public String getTime() {
        return time;
    }
    public String getDate() {
        return date;
    }

    public String getRemore_url() {
        return remore_url;
    }

    public String getSource_name() {
        return source_name;
    }

    public String getMain_image_url() {
        return main_image_url;
    }

    public String getDetails() {
        return details;

    }

    private String source_name;
    private String main_image_url;

    public  DataSource(JSONObject ele) throws JSONException {

            this.title = ele.getString("title");
            this.details = ele.getString("details").toString();
            this.head_image = ele.getString("head_image").toString();
            this.rand_id = ele.getString("rand_id").toString();
            this.source_name = ele.getString("source");
            this.remore_url = ele.getString("url");
            this.date = ele.getString("date");
            this.time = ele.getString("time");
            this.epoch = ele.getString("epoch");

    }
}
