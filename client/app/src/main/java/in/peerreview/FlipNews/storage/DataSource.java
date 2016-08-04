package in.peerreview.flipnews.storage;

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

    public String getHead_image() {
        return head_image;
    }

    public String getRand_id() {
        return rand_id;
    }

    public String getTitle() {
        return title;
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
    }
}
