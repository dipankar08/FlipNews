package in.peerreview.flipnews.UIFragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;

import in.peerreview.flipnews.Activities.MainActivity;
import in.peerreview.flipnews.R;

/**
 * Created by ddutta on 8/6/2016.
 */
public class CoreFragmentAnimation implements OnTextFragmentAnimationEndListener, FragmentManager.OnBackStackChangedListener{
    /* Fragments ...*/
    Fragment2 mFragment2;
    Fragment1 mFragment1;

    View mDarkHoverView;

    boolean mDidSlideOut = false;
    boolean mIsAnimating = false;
    private static Activity mActivity =  MainActivity.getActivity();


    private static CoreFragmentAnimation sCoreFragmentAnimation = new CoreFragmentAnimation();
    public static CoreFragmentAnimation Get(){
        return sCoreFragmentAnimation;
    }

    public void setupFragments() {

        mDarkHoverView = mActivity.findViewById(R.id.dark_hover_view);
        mDarkHoverView.setAlpha(0);

        mFragment2 = (Fragment2) mActivity.getFragmentManager().findFragmentById(R.id.move_fragment);

        mFragment1 = new Fragment1();

        mActivity.getFragmentManager().addOnBackStackChangedListener(this);

        mFragment2.setClickListener(mClickListener);
        mFragment1.setClickListener(mClickListener);
        mFragment1.setOnTextFragmentAnimationEnd(this);
        mDarkHoverView.setOnClickListener(mClickListener);
    }

    View.OnClickListener mClickListener = new View.OnClickListener () {
        @Override
        public void onClick(View view) {
            switchFragments();
        }
    };

    private void switchFragments () {
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
                    transaction.setCustomAnimations(R.animator.slide_fragment_in, 0, 0,
                            R.animator.slide_fragment_out);
                    transaction.add(R.id.move_to_back_container, mFragment1);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            };
            slideBack (listener);
        }
    }
    @Override
    public void onBackStackChanged() {
        if (!mDidSlideOut) {
            slideForward(null);
        }

    }

    /**
     * This method animates the image fragment into the background by both
     * scaling and rotating the fragment's view, as well as adding a
     * translucent dark hover view to inform the user that it is inactive.
     */
    public void slideBack(Animator.AnimatorListener listener)
    {
        View movingFragmentView = mFragment2.getView();

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

    /**
     * This method animates the image fragment into the foreground by both
     * scaling and rotating the fragment's view, while also removing the
     * previously added translucent dark hover view. Upon the completion of
     * this animation, the image fragment regains focus since this method is
     * called from the onBackStackChanged method.
     */
    public void slideForward(Animator.AnimatorListener listener)
    {
        View movingFragmentView = mFragment2.getView();

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

    public void onAnimationEnd() {
        mIsAnimating = false;
    }

    public View getView() {
        return mFragment2.getView();
    }


}
