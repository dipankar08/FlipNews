package in.peerreview.flipnews.Activities;

import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import in.peerreview.flipnews.ExternalPartner.FaceBookShare;
import in.peerreview.flipnews.R;
import in.peerreview.flipnews.UIFragments.MyFragmentManager;
import in.peerreview.flipnews.Utils.Experiment;
import in.peerreview.flipnews.Utils.SimpleUtils;

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
                SimpleUtils.share();
                break;
            case R.id.details_btn:
                FlipOperation.Get().showDetails();
                break;
            case R.id.back_to_card_btn:
                MyFragmentManager.Get().show(1);
                break;
            case R.id.next_btn:
                MyFragmentManager.Get().show(1);
                break;
            case R.id.experiment:
                Experiment.test();
                FaceBookShare.Get().share();
                break;
            case R.id.more_image_btn:
                MyFragmentManager.Get().toggle(5);
                break;
            default:
                Toast.makeText(MainActivity.Get().getApplicationContext(),"The action is not part of this alpha version",Toast.LENGTH_SHORT).show();
        }
    }

}
