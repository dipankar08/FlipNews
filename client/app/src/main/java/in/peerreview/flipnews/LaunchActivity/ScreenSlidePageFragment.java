package in.peerreview.flipnews.LaunchActivity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.peerreview.flipnews.R;
import in.peerreview.flipnews.Utils.FontAwesome;

public class ScreenSlidePageFragment extends Fragment {

    private int page;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (null != arguments && arguments.getInt("page", -1) != -1) {
            page = arguments.getInt("page");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("DIP",""+page);
        int layoutId = getResources().getIdentifier("promo_screen_fragment"+page, "layout", LaunchActivity.Get().getPackageName());
        ViewGroup rootView = (ViewGroup) inflater.inflate( layoutId, container, false);
        switch(page){
            case 0:
                FontAwesome.setIcon((TextView)rootView.findViewById( R.id.product_logo1),null);
                break;
            /*
            case 1:
                FontAwesome.setIcon((TextView)rootView.findViewById( R.id.product_logo2),null);
                break;
            */
        }
        return rootView;
    }
}