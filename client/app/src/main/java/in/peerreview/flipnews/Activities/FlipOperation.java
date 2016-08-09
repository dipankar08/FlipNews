package in.peerreview.flipnews.Activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.peerreview.flipnews.R;
import in.peerreview.flipnews.ServerProxy.BackendController;
import in.peerreview.flipnews.UIFragments.CoreFragmentAnimation;
import in.peerreview.flipnews.Utils.Logging;
import in.peerreview.flipnews.storage.DataSource;
import in.peerreview.flipnews.storage.ImageCacheManager;

/**
 * Created by ddutta on 8/5/2016.
 */
public class FlipOperation {

    private static FlipOperation sFlipOperation = new FlipOperation();
    public static FlipOperation Get(){
        return sFlipOperation;
    }

    private float initialX, initialY;

    private static IFlipOperation flipOperation = new MyTextFlipper();

    boolean is_up(float y1, float y2){
        return (y1 > y2 && (y1-y2) > 1);
    }
    boolean is_down(float y1, float y2){
        return (y1 < y2 && (y2 - y1) > 1);
    }


    public boolean delegateTouchEventsForFlip(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = touchevent.getX();
                initialY = touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                float finalX = touchevent.getX();
                float finalY = touchevent.getY();
                if (is_up(initialY , finalY)  ) {
                    flipOperation.Next();
                } else if (is_down(initialY , finalY)  ) {
                    flipOperation.Previous();
                }

                break;
        }
        return true;
    }

    /**************************************************************************
        APIS
    ****************************************************************************/
    public static void setupFlipper() {
        flipOperation.setupFlipper();
    }

    public static void renderData(List<DataSource> d){
        flipOperation.renderData(d);
    }

    public void reset(){
        flipOperation.reset();
    }

    public void Next() {
        flipOperation.Next();
    }

    public void Previous() {
        flipOperation.Previous();
    }
}
