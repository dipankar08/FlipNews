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
    NewsActionsFragment newsActionsFragment;
    UserPreferencesFragment userPreferencesFragment;
    UserSettingFragment userSettingFragment;

    View mDarkHoverView;


    boolean mIsAnimating = false;
    String current_backstack = null;
    private static Activity mActivity =  MainActivity.getActivity();

    final private String CRAD_DETAILS_FRAGMENTS = "CRAD_DETAILS_FRAGMENTS";
    final private String CRAD_ACTIONS_FRAGMENTS = "CRAD_ACTIONS_FRAGMENTS";

    private static CoreFragmentAnimation sCoreFragmentAnimation = new CoreFragmentAnimation();
    public static CoreFragmentAnimation Get(){
        return sCoreFragmentAnimation;
    }

    public void setupFragments() {

        //Building all fragments/views
        newsCardFragment = (NewsCardFragment) mActivity.getFragmentManager().findFragmentById(R.id.move_fragment);
        newsCardDetailsFragment = new NewsCardDetailsFragment();
        mDarkHoverView = mActivity.findViewById(R.id.dark_hover_view);

        newsCardDetailsFragment = new NewsCardDetailsFragment();
        newsActionsFragment = new NewsActionsFragment();
        userPreferencesFragment = new UserPreferencesFragment();
        userSettingFragment = new UserSettingFragment();


        //additinal work
        newsCardDetailsFragment.setOnTextFragmentAnimationEnd(this);
        newsActionsFragment.setOnTextFragmentAnimationEnd(this);



        mDarkHoverView.setAlpha(0);
        //Backstack change listner
        mActivity.getFragmentManager().addOnBackStackChangedListener(this);
    }





    // --------------------------Animation for CardDetailsFragment --------------------------------------------------------------
    public void pushCardDetailsFragment() {
        FragmentManager fm = mActivity.getFragmentManager();
        if( fm.getBackStackEntryCount() > 0 && fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName().equals("CRAD_DETAILS_FRAGMENTS")){
            Log.d("DIpankar"," We alread in CRAD_DETAILS_FRAGMENTS");
            return;
        }
        if (mIsAnimating) {
            return;
        }
        mIsAnimating = true;
        Animator.AnimatorListener listener = new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator arg0) {
                    FragmentTransaction transaction = mActivity.getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.animator.slide_fragment_in, 0, 0, R.animator.slide_fragment_out);
                    transaction.add(R.id.move_to_back_container, newsCardDetailsFragment);
                    transaction.addToBackStack("CRAD_DETAILS_FRAGMENTS");
                    transaction.commit();
                    mIsAnimating = false;
                }
        };
        //Animation.
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
    //dont call this explicily but call backToMainFragment
    public void popCardDetailsFragment() {
        if (mIsAnimating) {
            return;
        }
        mIsAnimating = true;
        //animation
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
    // -------------------------- end Animation for CardDetailsFragment --------------------------------------------------------------

    // --------------------------Animation for CRAD_ACTIONS_FRAGMENTS --------------------------------------------------------------
    public void pushNewsActionsFragment() {
        FragmentManager fm = mActivity.getFragmentManager();
        if( fm.getBackStackEntryCount() > 0 && fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName().equals(CRAD_ACTIONS_FRAGMENTS)){
            Log.d("DIpankar"," We alread in CRAD_ACTIONS_FRAGMENTS");
            return;
        }
        if (mIsAnimating) {
            return;
        }
        mIsAnimating = true;
        Animator.AnimatorListener listener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator arg0) {
                FragmentTransaction transaction = mActivity.getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.animator.actions_slide_fragment_in, 0, 0, R.animator.actions_slide_fragment_out);
                transaction.add(R.id.move_to_back_container, newsActionsFragment);
                transaction.addToBackStack(CRAD_ACTIONS_FRAGMENTS);
                transaction.commit();
                mIsAnimating = false;
            }
        };
        //Animation.

        View movingFragmentView = newsActionsFragment.getView();

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
    //dont call this explicily but call backToMainFragment
    public void popNewsActionsFragmen() {
        if (mIsAnimating) {
            return;
        }
        mIsAnimating = true;
        //animation
        View movingFragmentView = newsActionsFragment.getView();

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
    // -------------------------- end Animation for CardDetailsFragment --------------------------------------------------------------
    public void backToMainFragment(){
        mActivity.getFragmentManager().popBackStack();
    }

    @Override
    public void onBackStackChanged() {

       if(CRAD_DETAILS_FRAGMENTS.equals(current_backstack)){
           popCardDetailsFragment();
       } else if(CRAD_ACTIONS_FRAGMENTS.equals((current_backstack))){
           popCardDetailsFragment();
       }

        FragmentManager fm = mActivity.getFragmentManager();
        if( fm.getBackStackEntryCount() > 0){
            current_backstack = fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName();
        } else {
            current_backstack = null;
        }
    }

    public void onAnimationEnd() {
        mIsAnimating = false;
    }

    public View getView() {
         return newsCardFragment.getView();
    }


}
