package in.peerreview.flipnews.UIFragments;



import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import in.peerreview.flipnews.R;
import java.util.ArrayList;
import java.util.Stack;

public class MyFragmentManager {

    private static MyFragmentManager sCoreFragmentAnimation = null;
    public Activity sActivity;
    private FrameLayout mFragmentContainer;
    private Fragment mCurrentFragment;
    private Fragment1 mFragment1;
    private Fragment2 mFragment2;
    private Fragment3 mFragment3;
    private Fragment4 mFragment4;
    private Stack<Fragment> mStackFragments;



    public static MyFragmentManager Get() {
        if (sCoreFragmentAnimation == null) {
            sCoreFragmentAnimation = new MyFragmentManager();
        }
        return sCoreFragmentAnimation;
    }

    public void setup(Activity mActivity) {
        this.sActivity = mActivity;
        this.mFragment1 = new Fragment1();
        this.mFragment2 = new Fragment2();
        this.mFragment3 = new Fragment3();
        this.mStackFragments = new Stack();
        FragmentTransaction trans = this.sActivity.getFragmentManager().beginTransaction();

        trans.add(R.id.fragment_container, (Fragment)this.mFragment3, "Fragment3");
        trans.hide((Fragment)this.mFragment3);

        trans.add(R.id.fragment_container, (Fragment)this.mFragment2, "Fragment2");
        trans.hide((Fragment)this.mFragment2);

        trans.add(R.id.fragment_container, (Fragment)this.mFragment1, "Fragment1");
        trans.commit();

        this.mCurrentFragment = this.mFragment1;
    }

    public void show(int id) {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<Fragment>();
        fragmentArrayList.add(this.mFragment1);
        fragmentArrayList.add(this.mFragment2);
        fragmentArrayList.add(this.mFragment3);
        fragmentArrayList.add(this.mFragment4);
        this.showFragment((Fragment)fragmentArrayList.get(id - 1));
    }

    private void showFragment(Fragment fragment) {

        if (fragment.isVisible()) {
            return;
        }
        FragmentTransaction trans = this.sActivity.getFragmentManager().beginTransaction();
        //trans.setCustomAnimations(2131099652, 2131099653, 2131099652, 2131099653);
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

    public View getView() {

        return this.mFragment1.getView();
    }
}