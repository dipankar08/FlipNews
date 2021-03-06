package in.peerreview.flipnews.UIFragments;



import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import in.peerreview.flipnews.R;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

public class MyFragmentManager {

    private static MyFragmentManager sCoreFragmentAnimation = null;
    public FragmentActivity sActivity;
    private FrameLayout mFragmentContainer;
    private Fragment mCurrentFragment;
    private Fragment1 mFragment1;
    private Fragment2 mFragment2;
    private Fragment3 mFragment3;
    private Fragment4 mFragment4;

    private Stack<Fragment> mStackFragments;
    ArrayList<Fragment> fragmentArrayList = null;


    public static MyFragmentManager Get() {
        if (sCoreFragmentAnimation == null) {
            sCoreFragmentAnimation = new MyFragmentManager();
        }
        return sCoreFragmentAnimation;
    }

    public void setup(FragmentActivity mActivity) {
        this.sActivity = mActivity;
        this.mFragment1 = new Fragment1();
        this.mFragment2 = new Fragment2();
        this.mFragment3 = new Fragment3();

        fragmentArrayList = new ArrayList<Fragment>();
        fragmentArrayList.add(this.mFragment1);
        fragmentArrayList.add(this.mFragment2);
        fragmentArrayList.add(this.mFragment3);
        fragmentArrayList.add(this.mFragment4);

        this.mStackFragments = new Stack();
        FragmentTransaction trans = this.sActivity.getSupportFragmentManager().beginTransaction();

        trans.add(R.id.fragment_container, (Fragment)this.mFragment3, "Fragment3");
        trans.hide((Fragment)this.mFragment3);

        trans.add(R.id.fragment_container, (Fragment)this.mFragment2, "Fragment2");
        trans.hide((Fragment)this.mFragment2);

        trans.add(R.id.fragment_container, (Fragment)this.mFragment1, "Fragment1");
        trans.commit();

        this.mCurrentFragment = this.mFragment1;
    }

    public void show(int id) {
        if(id >= 4){ //Fragment 4.. all the belows to Fragment1
            mFragment1.showFragment(id-4);
            return;
        }
        this.showFragment((Fragment)fragmentArrayList.get(id - 1));
    }
    public void hide(int id) {
        if(id >= 4){ //Fragment 4.. all the belows to Fragment1
            mFragment1.hideFragment(id-4);
            return;
        }
    }
    public void toggle(int id) {
        if(id >= 4) { //Fragment 4.. all the belows to Fragment1
            mFragment1.toggleFragment(id - 4);
            return;
        }
    }
    public void setFragmentData(int id,Map<String, Object> map) {
        if(id >= 4) { //Fragment 4.. all the belows to Fragment1
            mFragment1.setFragmentData(id - 4,map); // pases ad index 0,1,2..
            return;
        }
    }


    private void showFragment(Fragment fragment) {


        Log.d("Dipankar","MyFragmentManager::MyshowFragment::  Switching fragmnet In:"+fragment.getTag()+", Out:"+mCurrentFragment.getTag());
        if (fragment.isVisible()) {
            return;
        }
        FragmentTransaction trans = this.sActivity.getSupportFragmentManager().beginTransaction();
        //trans.setCustomAnimations(R.animator.slide_in, R.animator.slide_out, R.animator.slide_in, R.animator.slide_out);
        //trans.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);


        fragment.getView().bringToFront();
        this.mCurrentFragment.getView().bringToFront();
        trans.hide(this.mCurrentFragment);
        trans.show(fragment);
        trans.addToBackStack(null);
        this.mStackFragments.push(this.mCurrentFragment);
        trans.commit();
        this.mCurrentFragment = fragment;
    }

    public void onBackPressed() {
        if (this.sActivity.getFragmentManager().getBackStackEntryCount() > 0) {
            this.sActivity.getFragmentManager().popBackStack();
            this.mCurrentFragment = this.mStackFragments.pop();
        }
    }

    public View getView( ) {
        return this.mFragment1.getView();
    }
    public View getView(int id) {
        return fragmentArrayList.get(id - 1).getView();
    }
}