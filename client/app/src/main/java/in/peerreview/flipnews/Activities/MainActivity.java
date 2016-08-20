package in.peerreview.flipnews.Activities;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.view.MenuItem;


import android.content.Intent;

import in.peerreview.flipnews.ExternalPartner.FaceBookShare;
import in.peerreview.flipnews.R;
import in.peerreview.flipnews.ServerProxy.BackendController;
import in.peerreview.flipnews.UIFragments.MyFragmentManager;
import in.peerreview.flipnews.Utils.Experiment;
import in.peerreview.flipnews.Utils.ImageProcessing;
import in.peerreview.flipnews.storage.DataBaseProxy;

public class MainActivity extends FragmentActivity {

    private int currentApiVersion;
    private static MainActivity sActivity = null;
    final String TAG = "MainActivity";
    public static MainActivity getActivity() {
        return sActivity;
    }

    public static MainActivity Get() {
        return sActivity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isStoragePermissionGranted() == true) {
            CompleteonCreate();
        } else {
            finish();
        }
    }


    private void CompleteonCreate(){
        MyFragmentManager.Get().setup(this);
        FlipOperation.setupFlipper();


        BackendController.Get().firstBootLoad();
        hideBars();
        //initToolbar();
        ActivityHelper.createImageCache();
        Experiment.test();
        //setImageTheme(); This will not be used..
    }

    //setImage Theme
    private void setImageTheme(){
        FrameLayout layout =(FrameLayout)findViewById(R.id.move_to_back_container);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        Drawable drawable = new BitmapDrawable(ImageProcessing.getGaussianBlurImage(BitmapFactory.decodeResource(getResources(), R.drawable.flower)));
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
           layout.setBackgroundDrawable( drawable);
        } else {
            layout.setBackground(drawable);
        }
    }

    //Permisstion Work..
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {
                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            CompleteonCreate();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data)
    {
        FaceBookShare.Get().onActivityResult(requestCode, responseCode, data);
        super.onActivityResult(requestCode, responseCode, data);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void backMain(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
    public void quickActionBtnClicked(View v){
        GenericClickActions.performClickAction(v);
    }


    private void hideBars() {
        currentApiVersion = Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        // This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        }
    }


    //Key handlaer
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.d(TAG, "onKeyDown called");
        Log.v(TAG, event.toString());
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            FlipOperation.Get().Previous();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            FlipOperation.Get().Next();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_POWER) {
            // Do something here...
            event.startTracking(); // Needed to track long presses
            return true;
        }
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyUp called");
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            return false;
        }

        return false;
    }



    @Override
    public void onPause() {
        //Write all unionization here.. OnDestroy is not getting called..
        DataBaseProxy.Get().destroy();

        super.onPause();
    }

    @Override
    public void onDestroy() {
//        unregisterReceiver(mReceiver);

        super.onDestroy();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

            if (this.getFragmentManager().getBackStackEntryCount() > 0)
            {
                MyFragmentManager.Get().onBackPressed();
            }

            else
            {
                super.onBackPressed();
            }
    }
}