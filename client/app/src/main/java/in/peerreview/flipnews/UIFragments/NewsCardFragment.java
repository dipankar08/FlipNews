
package in.peerreview.flipnews.UIFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import in.peerreview.flipnews.Activities.FlipOperation;
import in.peerreview.flipnews.R;

public class NewsCardFragment extends Fragment {

    View.OnClickListener clickListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_card_holder, container, false);

        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        Log.i("Dipankar", "onFling has been called!");
                        final int SWIPE_MIN_DISTANCE = 80;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 200;
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i("Duipankar", "Right to Left");
                                FlipOperation.Get().flipperNext();
                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i("Dipankar", "Left to Right");
                                FlipOperation.Get().flipperPrev();
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        Log.e("", "Longpress detected");
                        CoreFragmentAnimation.Get().pushCardDetailsFragment();
                    }
                    // event when double tap occurs
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        float x = e.getX();
                        float y = e.getY();

                        Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");
                        CoreFragmentAnimation.Get().pushCardDetailsFragment();

                        return true;
                    }
                });

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

        return v;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

}
