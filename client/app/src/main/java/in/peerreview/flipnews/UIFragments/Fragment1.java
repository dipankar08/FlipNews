package in.peerreview.flipnews.UIFragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import in.peerreview.flipnews.Activities.FlipOperation;
import in.peerreview.flipnews.Activities.MainActivity;
import in.peerreview.flipnews.R;
import in.peerreview.flipnews.Utils.L;

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
        trans.add(mFragmentContainers.get(0).getId(), new Fragment4(), "Fragment4");
        trans.add(mFragmentContainers.get(1).getId(), new Fragment5(), "Fragment5");

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

    public void setFragmentData(int id, Map<String,Object> data){
        FrameLayout cur = mFragmentContainers.get(id);
        if(id == 0 ) {

        }
        else if( id == 1 ){
            //webview

            FrameLayout video_frame = (FrameLayout) cur.getRootView().findViewById(R.id.video_frame);
            FrameLayout image_frame = (FrameLayout) cur.getRootView().findViewById(R.id.image_frame);

            List<String> video = (List<String>) data.get("video");
            List<String> images = (List<String>) data.get("images");
            if(video != null && video.size() > 0) {
                WebView browser = (WebView) cur.getRootView().findViewById(R.id.video);
                browser.setWebViewClient(new WebViewClient());
                L.d("<<<<< "+data.get("video"));
                browser.getSettings().setJavaScriptEnabled(true);
                browser.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                browser.loadUrl(video.get(0));
                video_frame.setVisibility(View.VISIBLE);
                image_frame.setVisibility(View.GONE);
            } else{
                /*
                L.d("<<<<< "+data.get("images"));
                ViewPager mPager = (ViewPager) cur.getRootView().findViewById(R.id.image_pager);//(ViewPager) findViewById(R.id.pager);
                mPager.removeAllViews();
                for(String im : images)
                {
                    ImageView image = new ImageView(MainActivity.Get());
                    image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80,60));
                    image.setMaxHeight(200);
                    image.setMaxWidth(200);

                    // Adds the view to the layout
                    mPager.addView(image);
                }
                //mPager.addView();
                //((CirclePageIndicator)cur.getRootView().findViewById(R.id.image_viewpager_indicator)).setViewPager(mPager);
                */
                image_frame.setVisibility(View.VISIBLE);
                video_frame.setVisibility(View.GONE);

            }
        }
    }
}
