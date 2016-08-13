package in.peerreview.flipnews.Activities;

import java.util.List;

import in.peerreview.flipnews.storage.DataSource;

/**
 * Created by ddutta on 8/9/2016.
 */
public interface IFlipOperation {
    public void Next() ;
    public void Previous();
    public void setupFlipper();
    public void renderData(List<DataSource> d);
    public void reset();
    public void fillDetails();
}
