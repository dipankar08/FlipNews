package in.peerreview.flipnews.Activities;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import in.peerreview.flipnews.R;
import in.peerreview.flipnews.ServerProxy.BackendController;

/**
 * Created by ddutta on 8/5/2016.
 */
public class GenericClickActions {

    //Making singleton
    private static GenericClickActions sGenericClickActions = new GenericClickActions();
    public static GenericClickActions Get(){
        return sGenericClickActions;
    }

    private void SetupSettingButtonListners() {
        View.OnClickListener myListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add your API call here//
                if (v.getId() == R.id.notification) {
                    //do something and add other if else..

                }
            }
        };

        ((Button) MainActivity.getActivity().findViewById(R.id.notification)).setOnClickListener(myListner);
    }

    private void SetupCatButtonListners() {
        View.OnClickListener myListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add your API call here//
                if (v.getId() == R.id.allNews) {
                    //do something and add other if else..
                    BackendController.Get().firstTimeNetworkLoad();

                }
            }
        };

        ((Button)  MainActivity.getActivity().findViewById(R.id.allNews)).setOnClickListener(myListner);
        ((Button)  MainActivity.getActivity().findViewById(R.id.kolkata)).setOnClickListener(myListner);
        ((Button)  MainActivity.getActivity().findViewById(R.id.state)).setOnClickListener(myListner);
        ((Button)  MainActivity.getActivity().findViewById(R.id.india)).setOnClickListener(myListner);
        ((Button)  MainActivity.getActivity().findViewById(R.id.international)).setOnClickListener(myListner);
        ((Button)  MainActivity.getActivity().findViewById(R.id.sport)).setOnClickListener(myListner);
        ((Button)  MainActivity.getActivity().findViewById(R.id.lifestyle)).setOnClickListener(myListner);
    }

    public void onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.options) {
            MainActivity.getActivity().setContentView(R.layout.settings);
            SetupSettingButtonListners();
        }

        if (id == android.R.id.home) {
            MainActivity.getActivity().setContentView(R.layout.categories);
            SetupCatButtonListners();
        }

    }
}
