package in.peerreview.flipnews.ServerProxy;

import org.json.JSONArray;

import java.util.List;

import in.peerreview.flipnews.storage.DataSource;

/**
 * Created by ddutta on 8/14/2016.
 */
public interface IProcessData {
    public void process(List<DataSource> lst);
    public void onError(String msg);

}
