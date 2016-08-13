package in.peerreview.flipnews.Activities;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import in.peerreview.flipnews.R;
import in.peerreview.flipnews.ServerProxy.BackendController;
import in.peerreview.flipnews.UIFragments.MyFragmentManager;
import in.peerreview.flipnews.Utils.ShareNews;

/**
 * Created by ddutta on 8/5/2016.
 */
public class GenericClickActions {

    public static void performClickAction(View v){
        Log.d("Dipankar","quickActionBtnClicked"+v.getId());

        switch (v.getId()){
            case R.id.bookmark:
                break;
            case R.id.share:
                ShareNews.share();
                break;
            case R.id.details_btn:
                MyFragmentManager.Get().show(2);
            case R.id.back_to_card_btn:
                MyFragmentManager.Get().show(1);
            case R.id.next_btn:
                MyFragmentManager.Get().show(1);
            default:
                break;
        }
    }

}
