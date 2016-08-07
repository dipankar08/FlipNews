package in.peerreview.flipnews.UIFragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import in.peerreview.flipnews.Activities.MainActivity;
import in.peerreview.flipnews.R;

/**
 * Created by ddutta on 8/6/2016.
 */
public class CoreFragmentAnimation implements OnTextFragmentAnimationEndListener, FragmentManager.OnBackStackChangedListener{
    /* Fragments ...*/
    NewsCardFragment newsCardFragment;
    NewsCardDetailsFragment newsCardDetailsFragment;

    View mDarkHoverView;

    boolean mDidSlideOut = false;
    boolean mIsAnimating = false;
    private static Activity mActivity =  MainActivity.getActivity();

    private enum CURRNET_FRAGMENT_NAME {

    };

    private static CoreFragmentAnimation sCoreFragmentAnimation = new CoreFragmentAnimation();
    public static CoreFragmentAnimation Get(){
        return sCoreFragmentAnimation;
    }

    public void setupFragments() {

        //Building all fragments/views
        newsCardFragment = (NewsCardFragment) mActivity.getFragmentManager().findFragmentById(R.id.move_fragment);
        newsCardDetailsFragment = new NewsCardDetailsFragment();
        mDarkHoverView = mActivity.findViewById(R.id.dark_hover_view);

        //adding all lstners
        //newsCardFragment.setClickListener(mClickListenerSwitchBetweenCardDetailsFragment);
       // newsCardDetailsFragment.setClickListener(mClickListenerSwitchBetweenCardDetailsFragment);
        //mDarkHoverView.setOnClickListener(mClickListenerSwitchBetweenCardDetailsFragment);

        //additinal work
        newsCardDetailsFragment.setOnTextFragmentAnimationEnd(this);
        mActivity.getFragmentManager().addOnBackStackChangedListener(this);


        mDarkHoverView.setAlpha(0);
    }



    // Fragment switch 1: Switching between Crad Details
    View.OnClickListener mClickListenerSwitchBetweenCardDetailsFragment = new View.OnClickListener () {
        @Override
        public void onClick(View view) {
            switchBetweenCardDetailsFragment();
        }
    };

    public void switchBetweenCardDetailsFragment() {
        if (mIsAnimating) {
            return;
        }
        mIsAnimating = true;
        if (mDidSlideOut) {
            mDidSlideOut = false;
            mActivity.getFragmentManager().popBackStack();
        } else {
            mDidSlideOut = true;
            Animator.AnimatorListener listener = new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator arg0) {
                    FragmentTransaction transaction = mActivity.getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.animator.slide_fragment_in, 0, 0, R.animator.slide_fragment_out);
                    transaction.add(R.id.move_to_back_container, newsCardDetailsFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            };
            slideBackCardDetails(listener);
        }
    }

    public void slideBackCardDetails(Animator.AnimatorListener listener)
    {
        View movingFragmentView = newsCardFragment.getView();

        PropertyValuesHolder rotateX =  PropertyValuesHolder.ofFloat("rotationX", 40f);
        PropertyValuesHolder scaleX =  PropertyValuesHolder.ofFloat("scaleX", 0.8f);
        PropertyValuesHolder scaleY =  PropertyValuesHolder.ofFloat("scaleY", 0.8f);

        ObjectAnimator movingFragmentAnimator = ObjectAnimator.ofPropertyValuesHolder(movingFragmentView, rotateX, scaleX, scaleY);
        ObjectAnimator darkHoverViewAnimator = ObjectAnimator.ofFloat(mDarkHoverView, "alpha", 0.0f, 0.5f);

        ObjectAnimator movingFragmentRotator = ObjectAnimator.ofFloat(movingFragmentView, "rotationX", 0);
        movingFragmentRotator.setStartDelay(mActivity.getResources().getInteger(R.integer.half_slide_up_down_duration));

        AnimatorSet s = new AnimatorSet();
        s.playTogether(movingFragmentAnimator, darkHoverViewAnimator, movingFragmentRotator);
        s.addListener(listener);
        s.start();
    }

    public void slideForwardCardDetails(Animator.AnimatorListener listener)
    {
        View movingFragmentView = newsCardFragment.getView();

        PropertyValuesHolder rotateX =  PropertyValuesHolder.ofFloat("rotationX", 40f);
        PropertyValuesHolder scaleX =  PropertyValuesHolder.ofFloat("scaleX", 1.0f);
        PropertyValuesHolder scaleY =  PropertyValuesHolder.ofFloat("scaleY", 1.0f);
        ObjectAnimator movingFragmentAnimator = ObjectAnimator.ofPropertyValuesHolder(movingFragmentView, rotateX, scaleX, scaleY);

        ObjectAnimator darkHoverViewAnimator = ObjectAnimator.ofFloat(mDarkHoverView, "alpha", 0.5f, 0.0f);
        ObjectAnimator movingFragmentRotator = ObjectAnimator.ofFloat(movingFragmentView, "rotationX", 0);
        movingFragmentRotator.setStartDelay(mActivity.getResources().getInteger(R.integer.half_slide_up_down_duration));

        AnimatorSet s = new AnimatorSet();
        s.playTogether(movingFragmentAnimator, movingFragmentRotator, darkHoverViewAnimator);
        s.setStartDelay(mActivity.getResources().getInteger(R.integer.slide_up_down_duration));
        s.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mIsAnimating = false;
            }
        });
        s.start();
    }


    @Override
    public void onBackStackChanged() {
        if (!mDidSlideOut) {
            slideForwardCardDetails(null);
        }

    }
    public void onAnimationEnd() {
        mIsAnimating = false;
    }

    public View getView() {
         return newsCardFragment.getView();
    }


}
