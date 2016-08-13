package in.peerreview.flipnews.Activities;

import android.widget.ViewFlipper;

import java.util.List;

import in.peerreview.flipnews.UIFragments.MyFragmentManager;
import in.peerreview.flipnews.Utils.Logging;
import in.peerreview.flipnews.storage.DataSource;

/**
 * Created by ddutta on 8/9/2016.
 */
public class MyViewFlipper implements IFlipOperation {
    private static ViewFlipper flipper;

    public void Next() {
        if (flipper.getDisplayedChild() < flipper.getChildCount() - 1) {
            flipper.showNext();
            //>>>>>>>>>BackendController.Get().loadDataFromServer(flipper.getDisplayedChild());
            Logging.Log("Showing IDX:" + flipper.getDisplayedChild());
        } else {
            flipper.setDisplayedChild(0);
        }
    }

    public void Previous() {
        if (flipper.getDisplayedChild() != 0) {
            flipper.showPrevious();
            Logging.Log("Showing IDX:" + flipper.getDisplayedChild());
        } else {
            flipper.setDisplayedChild(flipper.getChildCount() - 1);
        }
    }

    public void setupFlipper() {
        flipper = (ViewFlipper) MyFragmentManager.Get().getView();
        flipper.setInAnimation(MainActivity.Get(), android.R.anim.fade_in);
        flipper.setOutAnimation(MainActivity.Get(), android.R.anim.fade_out);
    }

    public void renderData(List<DataSource> d) {
/*
        LayoutInflater inflater = (LayoutInflater) MainActivity.Get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.card, flipper, false);
        ImageCacheManager.renderImage((ImageView) view.findViewById(R.id.image), d.getHead_image(), d.getRand_id());
        TextView textView1 = (TextView) view.findViewById(R.id.text1);
        TextView textView2 = (TextView) view.findViewById(R.id.text2);
        ImageView providerImage = (ImageView) view.findViewById(R.id.provider_logo);
        textView1.setText(d.getTitle().trim());
        textView2.setText(d.getDetails().trim());
        Map logo_map = new HashMap() {{
            put("Anadabazar", MainActivity.getActivity().getResources().getDrawable(R.drawable.anadabazar));
            put("bartaman", MainActivity.getActivity().getResources().getDrawable(R.drawable.bartaman));
            put("sangbadpratidin", MainActivity.getActivity().getResources().getDrawable(R.drawable.sangbadpratidin));
            put("eisomoy", MainActivity.getActivity().getResources().getDrawable(R.drawable.eisomoy));
            put("eisamay", MainActivity.getActivity().getResources().getDrawable(R.drawable.eisomoy));
            put("ZeeNews", MainActivity.getActivity().getResources().getDrawable(R.drawable.zeenews));
        }};
        providerImage.setImageDrawable((Drawable) logo_map.get(d.getSource_name().trim()));
        Log.d("Dipankar", d.getSource_name().trim());

        // view.setOnTouchListener(gestureListener);
        flipper.addView(view);
        flipper.invalidate();
        */
    }



    public void reset(){
        flipper.removeAllViews();
    }

    @Override
    public void fillDetails() {

    }

    /*    public void fixNext(int index) {
        if((current_news_list.length() - index) < 5 ){ // we need to do the next call.
            Logging.Log("Calling for next page fetch of news..");
            fecthNextSetOfpages();
        } else if((next_page -1) * limit -index < 5){
            try {
                renderData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
*/
}
