package in.peerreview.flipnews.Activities;

import android.view.MotionEvent;
import android.widget.ViewFlipper;

import in.peerreview.flipnews.ServerProxy.BackendController;
import in.peerreview.flipnews.Utils.Logging;

/**
 * Created by ddutta on 8/5/2016.
 */
public class FlipOperation {

    private static FlipOperation sFlipOperation = new FlipOperation();
    public static FlipOperation Get(){
        return sFlipOperation;
    }
   // private static flipper = MainActivity.getActivity().getFlipper();
   private float initialX, initialY;


    FlipOperation(){

    }
    boolean is_up(float y1, float y2){
        return (y1 > y2 && (y1-y2) > 1);
    }
    boolean is_down(float y1, float y2){
        return (y1 < y2 && (y2 - y1) > 1);
    }


    public boolean delegateTouchEventsForFlip(MotionEvent touchevent) {
        ViewFlipper flipper = MainActivity.getActivity().getFlipper();
        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = touchevent.getX();
                initialY = touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                float finalX = touchevent.getX();
                float finalY = touchevent.getY();
                if (is_up(initialY , finalY)  ) {
                    if (flipper.getDisplayedChild() < flipper.getChildCount() -1) {
                        flipperNext();
                    }
                    else {
                        flipper.setDisplayedChild(0);
                    }

                } else if (is_down(initialY , finalY)  ) {
                    if (flipper.getDisplayedChild() != 0) {
                        flipperPrev();
                    } else {
                        flipper.setDisplayedChild(flipper.getChildCount() - 1);
                    }
                }

                break;
        }
        return true;
    }

    public void flipperNext() {
        ViewFlipper flipper = MainActivity.getActivity().getFlipper();
        flipper.showNext();
        BackendController.Get().fixNext(flipper.getDisplayedChild());
        Logging.Log("Showing IDX:" + flipper.getDisplayedChild());
    }
    public void flipperPrev() {
        ViewFlipper flipper = MainActivity.getActivity().getFlipper();
        flipper.showPrevious();
        Logging.Log("Showing IDX:" + flipper.getDisplayedChild());
    }
}
