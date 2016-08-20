package in.peerreview.flipnews.UIFragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import in.peerreview.flipnews.Activities.FlipOperation;
import in.peerreview.flipnews.Activities.MainActivity;
import in.peerreview.flipnews.R;

public class Fragment1 extends Fragment {

    private static List<FrameLayout> mFragmentContainers = new ArrayList<FrameLayout>();
    private float mLastPosY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Influte
        View v = inflater.inflate(R.layout.fragment1, container, false);

        // Understing the touch events..
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
                        final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 200;
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i("Dipankar", "Right to Left");
                                FlipOperation.Get().Previous();
                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i("Dipankar", "Left to Right");
                                FlipOperation.Get().Next();
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

        // Adding all nested Fragments...
        mFragmentContainers.add((FrameLayout)v.findViewById(R.id.fragment4Container));
        mFragmentContainers.add((FrameLayout)v.findViewById(R.id.fragment5Container));
        FragmentTransaction trans = MainActivity.Get().getSupportFragmentManager().beginTransaction();
        for( FrameLayout f: mFragmentContainers ){
            trans.add(f.getId(), new Fragment4(), "Fragment4");
        }
        trans.commit();

        return v;
    }

    public void showFragment(int id){
        if(id == 0 ){
            OvershootInterpolator interpolator =  new OvershootInterpolator(5);
            mFragmentContainers.get(id).animate().setInterpolator(interpolator).translationY(mFragmentContainers.get(0).getHeight() - 200).setDuration(500);
        }
        if(id == 1 ){
            //OvershootInterpolator interpolator =  new OvershootInterpolator(5);
            //mFragmentContainers.get(0).animate().setInterpolator(interpolator).(mFragmentContainers.get(0).getHeight() - 200).setDuration(500);
            mFragmentContainers.get(id).setVisibility(View.VISIBLE);
        }
    }
    public void hideFragment(int id){
        if(id == 0 ){
            OvershootInterpolator interpolator =  new OvershootInterpolator(5);
            mFragmentContainers.get(id).animate().setInterpolator(interpolator).translationY( mFragmentContainers.get(0).getHeight()).setDuration(500);
        }
        if(id == 1 ){
            mFragmentContainers.get(id).setVisibility(View.GONE);
        }
    }

    public void toggleFragment(int id){
        if(id == 0){
            if(mFragmentContainers.get(id).getTranslationY() == mFragmentContainers.get(0).getHeight()){
                showFragment(id);
            } else{
                hideFragment(id);
            }
        }
        else if(id == 1 ){
           if (mFragmentContainers.get(id).getVisibility() == View.VISIBLE){
               hideFragment(id);
           } else{
               showFragment(id);
           }
        }
    }
}
