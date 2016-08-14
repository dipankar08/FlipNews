package in.peerreview.flipnews.Activities;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.peerreview.flipnews.R;
import in.peerreview.flipnews.ServerProxy.BackendController;
import in.peerreview.flipnews.ServerProxy.IProcessData;
import in.peerreview.flipnews.UIFragments.MyFragmentManager;
import in.peerreview.flipnews.Utils.SimpleUtils;
import in.peerreview.flipnews.storage.DataSource;
import in.peerreview.flipnews.storage.ImageCacheManager;

/**
 * Created by ddutta on 8/9/2016.
 */
public class MyTextFlipper implements IFlipOperation {

    private static ViewGroup conatiner;

    private static final int None = -100;

    private int cur_idx = None;
    private int targetDateOffset = 0;
    private static final int CUTOFF = 15;



    Map<String,List<DataSource>> dataSourceHistory = new HashMap<String,List<DataSource>>();
    private static List<DataSource>  workingDataSourceList = new ArrayList<DataSource>();
    private boolean m_isPromoFragmnetShowing = false;

    private boolean verifyWorkingSetAndDownload(){
        final String date_Key = SimpleUtils.getDateFormat(targetDateOffset);
        if((cur_idx == None || workingDataSourceList.size() - cur_idx < CUTOFF ) //meet cutoff
                && dataSourceHistory.get(SimpleUtils.getDateFormat(targetDateOffset)) == null ) { // doent exist
            BackendController.Get().getdataFromServerByDate(date_Key, new IProcessData() {
                @Override
                public void process(List<DataSource> lst) {
                    dataSourceHistory.put(date_Key, lst);
                }

                @Override
                public void onError(String msg) {

                }
            });

        }
        return true;
    }

    private boolean verifyMovingToNextWorkingSet() {

        final String date_Key = SimpleUtils.getDateFormat(targetDateOffset);

        if( cur_idx == workingDataSourceList.size() -1 ){
            cur_idx = None;
        }

        if(cur_idx == None){ // last entry or on first boot.
            View v =  MyFragmentManager.Get().getView(3);

            ((TextView)v.findViewById(R.id.date_text)).setText(SimpleUtils.getDateReadable(targetDateOffset));
            ((TextView)v.findViewById(R.id.subtitle_text)).setText("Loading...");

            MyFragmentManager.Get().show(3);
            m_isPromoFragmnetShowing = true;

            if(dataSourceHistory.get(date_Key) == null){
                Log.d("Dipankar","We are really doing some wrong and we must do something...");
                return true;
            }
            workingDataSourceList = dataSourceHistory.get(date_Key);
            targetDateOffset++;
            cur_idx = -1;

            ((TextView)v.findViewById(R.id.subtitle_text)).setText("We have "+workingDataSourceList.size()+" older news.");

            return true;
        } else { // we need to move to next set,
            m_isPromoFragmnetShowing = false;
            MyFragmentManager.Get().show(1);
        }
        return false; // not moving...
    }

    private boolean verifyMovingToPreviousWorkingSet(){
        final String date_Key = SimpleUtils.getDateFormat(targetDateOffset-2);

        if( cur_idx == 0){
            cur_idx = None;
        }

        if(cur_idx == None){ // last entry or on first boot.
            View v =  MyFragmentManager.Get().getView(3);

            ((TextView)v.findViewById(R.id.date_text)).setText(SimpleUtils.getDateReadable(targetDateOffset -2 ));
            ((TextView)v.findViewById(R.id.subtitle_text)).setText("Loading...");

            MyFragmentManager.Get().show(3);
            m_isPromoFragmnetShowing = true;

            if(dataSourceHistory.get(date_Key) == null){
                Log.d("Dipankar","We are really doing some wrong and we must do something...");
                return true;
            }
            workingDataSourceList = dataSourceHistory.get(date_Key);
            targetDateOffset--;
            cur_idx = workingDataSourceList.size();

            ((TextView)v.findViewById(R.id.subtitle_text)).setText("We have "+workingDataSourceList.size()+" older news.");

            return true;
        } else { // we need to move to next set,
            m_isPromoFragmnetShowing = false;
            MyFragmentManager.Get().show(1);
        }
        return false; // not moving...
    }


    @Override
    public void Next() {
        verifyWorkingSetAndDownload();
        if(verifyMovingToNextWorkingSet() == false){
            cur_idx++;
            renderItem();
        }
    }

    @Override
    public void Previous() {
        if(verifyMovingToPreviousWorkingSet() == false){
            cur_idx--;
            renderItem();
        }
    }

    @Override
    public void setupFlipper() {
        conatiner = (ViewGroup) MyFragmentManager.Get().getView();
    }

    @Override
    public void renderData(List<DataSource> d) {
        //workingDataSourceList.addAll(d);
    }

    @Override
    public void reset() {

    }

    @Override
    public boolean fillDetails() {
        if(cur_idx < 0 || workingDataSourceList.size() == 0  ){
            Log.d("Dipankar","The data is not yet loaded");
            return false;
        }
        DataSource d = workingDataSourceList.get(cur_idx);
        View v = MyFragmentManager.Get().getView(2);
        SimpleUtils.setProvidersLogo(((ImageView)v.findViewById(R.id.source_logo)),d.getSource_name());
        ((TextView)v.findViewById(R.id.full_news)).setText(d.getDetails().trim());
        ((TextView)v.findViewById(R.id.news_title)).setText(d.getTitle().trim());
        ((TextView)v.findViewById(R.id.news_time)).setText(d.getTime() +"  -  " +d.getDate());
        ImageCacheManager.renderImage((ImageView) v.findViewById(R.id.source_image), d.getHead_image(), d.getRand_id());
        return true;
    }

    public void renderItem(){
        //Crash fix: Sometime the fragmnet is not yet infutaed,. In that case Let;s retry or else just reyrn.
        if(conatiner == null ){
            conatiner = (ViewGroup) MyFragmentManager.Get().getView();
        }
        if(conatiner == null){
            Log.d("Dipankar", "Layout is not yet influted");
            return;
        }
        if(cur_idx > workingDataSourceList.size()-1 ) return;

        DataSource d = workingDataSourceList.get(cur_idx);
        ImageCacheManager.renderImage((ImageView) conatiner.findViewById(R.id.image), d.getHead_image(), d.getRand_id());

        ((TextView) conatiner.findViewById(R.id.text1)).setText(d.getTitle().trim());
        ((TextView) conatiner.findViewById(R.id.text2)).setText(d.getDetails().trim());
        ((TextView) conatiner.findViewById(R.id.news_count)).setText((cur_idx+1)+"/"+workingDataSourceList.size());

        ImageView providerImage = (ImageView) conatiner.findViewById(R.id.provider_logo);
        Map logo_map = new HashMap() {{
            put("Anadabazar", MainActivity.getActivity().getResources().getDrawable(R.drawable.anadabazar));
            put("bartaman", MainActivity.getActivity().getResources().getDrawable(R.drawable.bartaman));
            put("sangbadpratidin", MainActivity.getActivity().getResources().getDrawable(R.drawable.sangbadpratidin));
            put("eisomoy", MainActivity.getActivity().getResources().getDrawable(R.drawable.eisomoy));
            put("eisamay", MainActivity.getActivity().getResources().getDrawable(R.drawable.eisomoy));
            put("ZeeNews", MainActivity.getActivity().getResources().getDrawable(R.drawable.zeenews));
        }};

        providerImage.setImageDrawable((Drawable) logo_map.get(d.getSource_name().trim()));




        conatiner.invalidate();
    }
}
