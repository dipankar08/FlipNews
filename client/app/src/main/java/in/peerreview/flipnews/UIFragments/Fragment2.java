
package in.peerreview.flipnews.UIFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import in.peerreview.flipnews.Activities.FlipOperation;
import in.peerreview.flipnews.R;

public class Fragment2 extends Fragment {

    View.OnClickListener clickListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);
        //view.setOnClickListener(clickListener);

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                FlipOperation.Get().delegateTouchEventsForFlip(event);
                return true;
            }
        });
        return view;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

}
