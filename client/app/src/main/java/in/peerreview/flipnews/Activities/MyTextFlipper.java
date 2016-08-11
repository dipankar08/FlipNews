package in.peerreview.flipnews.Activities;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.peerreview.flipnews.R;
import in.peerreview.flipnews.ServerProxy.BackendController;
import in.peerreview.flipnews.UIFragments.CoreFragmentAnimation;
import in.peerreview.flipnews.storage.DataSource;
import in.peerreview.flipnews.storage.ImageCacheManager;

/**
 * Created by ddutta on 8/9/2016.
 */
public class MyTextFlipper implements IFlipOperation {

    private static int cur_idx = 0;
    private static int CUTOFF = 5;
    private static List<DataSource>  dataSourceList = new ArrayList<DataSource>();

    private static ViewGroup conatiner;


    @Override
    public void Next() {
        if(dataSourceList.size() - cur_idx < CUTOFF) {
            BackendController.Get().loadDataFromServer();
        }

        if(cur_idx >= 0 && cur_idx <= dataSourceList.size() -1){
            renderItem();
            cur_idx++;



        } else{

        }
    }

    @Override
    public void Previous() {
        if(cur_idx >= 1 && cur_idx <= dataSourceList.size()){
            cur_idx--;
            renderItem();

        } else{

        }
    }

    @Override
    public void setupFlipper() {
        conatiner = (ViewGroup) CoreFragmentAnimation.Get().getView();
    }

    @Override
    public void renderData(List<DataSource> d) {
        dataSourceList.addAll(d);
    }

    @Override
    public void reset() {

    }
    public void renderItem(){
        DataSource d = dataSourceList.get(cur_idx);
        ImageCacheManager.renderImage((ImageView) conatiner.findViewById(R.id.image), d.getHead_image(), d.getRand_id());

        ((TextView) conatiner.findViewById(R.id.text1)).setText(d.getTitle().trim());
        ((TextView) conatiner.findViewById(R.id.text2)).setText(d.getDetails().trim());
        ((TextView) conatiner.findViewById(R.id.news_count)).setText((cur_idx+1)+"/"+dataSourceList.size());

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
