package in.peerreview.FlipNews.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.ParcelUuid;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.view.MenuItem;


import java.util.ArrayList;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Color;

import android.app.ProgressDialog;

import android.widget.Button;
import android.widget.TextView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import in.peerreview.FlipNews.BluetoothSync.DeviceListActivity;
import in.peerreview.FlipNews.R;
import in.peerreview.FlipNews.ServerProxy.BackendAPI;
import in.peerreview.FlipNews.ServerProxy.BackendController;
import in.peerreview.FlipNews.BluetoothSync.BluetoothConnector;
import in.peerreview.FlipNews.BluetoothSync.BluetoothShare;
import in.peerreview.FlipNews.Utils.Notification;

public class MainActivity extends ActionBarActivity {
    private Toolbar mToolbar;
    private ViewFlipper flipper;
    private int  currentApiVersion;
    private float initialX, initialY;
    private static MainActivity sActivity = null;
    private static GestureDetector gd = null;
    final String TAG ="MainActivity";

    BluetoothShare bs = new BluetoothShare();
    //Test
    private TextView mStatusTv;
    private Button mActivateBtn;
    private Button mPairedBtn;
    private Button mScanBtn,mSendBtn;

    BackendController backendController = new BackendController();


    private ProgressDialog mProgressDlg;

    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();

    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flipper = (ViewFlipper) findViewById(R.id.flipper);

        flipper.setInAnimation(this, android.R.anim.fade_in);
        flipper.setOutAnimation(this, android.R.anim.fade_out);
        sActivity = this;
        backendController.firstBootLoad();
        hideBars();
        initToolbar();
        initGestureDetector();
        ActivityHelper.createImageCache();



    }

    private void SetupSettingButtonListners() {
        View.OnClickListener myListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //Add your API call here//
                if(v.getId() == R.id.notification){
                    //do something and add other if else..

                }
            }
        };

        ((Button) findViewById(R.id.notification)).setOnClickListener(myListner);
    }

    private void SetupCatButtonListners() {
        View.OnClickListener myListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add your API call here//
                if(v.getId() == R.id.allNews){
                    //do something and add other if else..
                    backendController.firstTimeNetworkLoad();

                }
            }
        };

        ((Button) findViewById(R.id.allNews)).setOnClickListener(myListner);
        ((Button) findViewById(R.id.kolkata)).setOnClickListener(myListner);
        ((Button) findViewById(R.id.state)).setOnClickListener(myListner);
        ((Button) findViewById(R.id.india)).setOnClickListener(myListner);
        ((Button) findViewById(R.id.international)).setOnClickListener(myListner);
        ((Button) findViewById(R.id.sport)).setOnClickListener(myListner);
        ((Button) findViewById(R.id.lifestyle)).setOnClickListener(myListner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.truiton_view_flipper, menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.options) {
            setContentView(R.layout.settings);
            SetupSettingButtonListners();
        }

        if (id == android.R.id.home) {
            setContentView(R.layout.categories);
            SetupCatButtonListners();
        }

        return super.onOptionsItemSelected(item);
    }

    public void backMain(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public ViewFlipper getFlipper(){return flipper;}

    private void hideBars() {
         currentApiVersion = Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        // This work only for android 4.4+
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }
    }
    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        }
    }


    public static MainActivity getActivity(){
        return sActivity;
    }
    public static MainActivity Get(){
        return sActivity;
    }

    //####################################  Touch Framwe work ################################################
    boolean is_up(float y1, float y2){
        return (y1 > y2 && (y1-y2) > 1);
    }
    boolean is_down(float y1, float y2){
        return (y1 < y2 && (y2 - y1) > 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent touchevent) {
        gd.onTouchEvent(touchevent);
        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = touchevent.getX();
                initialY = touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                float finalX = touchevent.getX();
                float finalY = touchevent.getY();
                if (is_up(initialY , finalY)  ) {
                    if (flipper.getDisplayedChild() < flipper.getChildCount() -1) {
                        flipperNext();
                    }
                    else {
                        flipper.setDisplayedChild(0);
                    }

                } else if (is_down(initialY , finalY)  ) {
                    if (flipper.getDisplayedChild() != 0) {
                        flipperPrev();
                    } else {
                        flipper.setDisplayedChild(flipper.getChildCount() - 1);
                    }
                }
 /*TruitonFlipper.setInAnimation(this, R.anim.in_left);
 TruitonFlipper.setOutAnimation(this, R.anim.out_right);*/

 /*TruitonFlipper.setInAnimation(this, R.anim.in_right);
 TruitonFlipper.setOutAnimation(this, R.anim.out_left);*/

                break;
        }
        return true;
    }

    private void flipperNext() {
        flipper.showNext();
        backendController.fixNext(flipper.getDisplayedChild());
        Notification.Log("Showing IDX:" + flipper.getDisplayedChild());
    }
    private void flipperPrev() {
        flipper.showPrevious();
        Notification.Log("Showing IDX:" + flipper.getDisplayedChild());
    }

    //###################################################################   Gusture Listner Implemetaion
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    void initGestureDetector(){
        gd = new GestureDetector(getApplicationContext(), new GestureListener());
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Right to left
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Left to right
            }

            if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Bottom to top
            }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Top to bottom
            }
            return false;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();

            Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");
            showToolbar();

            return true;
        }
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e)
        {
            Log.d("OnDoubleTapListener", "onSingleTapConfirmed");
            hideToolbar();
            return false;
        }
    }


    //############################## Volume Key Operations .....................................

    //Key handlaer
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.d(TAG,"onKeyDown called");
        Log.v(TAG, event.toString());
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            flipperNext();
            return true;
        }
        else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            flipperPrev();
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_POWER) {
            // Do something here...
            event.startTracking(); // Needed to track long presses
            return true;
        }
        return false;
    }
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG,"onKeyUp called");
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            return false;
        }
        else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            return false;
        }

        return false;
    }

    //Life cycle
    void bluetoothsetup(){

        mStatusTv 			= (TextView) findViewById(R.id.tv_status);
        mActivateBtn 		= (Button) findViewById(R.id.btn_enable);
        mPairedBtn 			= (Button) findViewById(R.id.btn_view_paired);
        mScanBtn 			= (Button) findViewById(R.id.btn_scan);
        mSendBtn 			= (Button) findViewById(R.id.btn_send);
        mBluetoothAdapter	= BluetoothAdapter.getDefaultAdapter();

        mProgressDlg 		= new ProgressDialog(this);

        mProgressDlg.setMessage("Scanning...");
        mProgressDlg.setCancelable(false);
        mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                mBluetoothAdapter.cancelDiscovery();
            }
        });

        if (mBluetoothAdapter == null) {
            showUnsupported();
        } else {
            mPairedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                    if (pairedDevices == null || pairedDevices.size() == 0) {
                        showToast("No Paired Devices Found");
                    } else {
                        ArrayList<BluetoothDevice> list = new ArrayList<BluetoothDevice>();

                        list.addAll(pairedDevices);

                        Intent intent = new Intent(MainActivity.this, DeviceListActivity.class);

                        intent.putParcelableArrayListExtra("device.list", list);

                        startActivity(intent);
                    }
                }
            });

            mScanBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mBluetoothAdapter.startDiscovery();
                }
            });
            mSendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    try{
                        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (blueAdapter != null) {
                            if (blueAdapter.isEnabled()) {
                                Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();

                                if(bondedDevices.size() > 0) {
                                    Object[] devices = (Object []) bondedDevices.toArray();
                                    int position =0;
                                    BluetoothDevice device = (BluetoothDevice) devices[position];
                                    ParcelUuid[] uuids = device.getUuids();
                                    BluetoothConnector bc = new BluetoothConnector(device,false,blueAdapter,null);
                                    bc.connect();

                                }

                                Notification.Log("No appropriate paired devices.");
                            } else {
                                Notification.Log("Bluetooth is disabled.");
                            }
                        }

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            mActivateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();

                        showDisabled();
                    } else {
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

                        startActivityForResult(intent, 1000);
                    }
                }
            });

            if (mBluetoothAdapter.isEnabled()) {
                showEnabled();
            } else {
                showDisabled();
            }
        }

        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);
    }
    @Override
    public void onPause() {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {
//        unregisterReceiver(mReceiver);

        super.onDestroy();
    }

    private void showEnabled() {
        mStatusTv.setText("Bluetooth is On");
        mStatusTv.setTextColor(Color.BLUE);

        mActivateBtn.setText("Disable");
        mActivateBtn.setEnabled(true);

        mPairedBtn.setEnabled(true);
        mScanBtn.setEnabled(true);
    }

    private void showDisabled() {
        mStatusTv.setText("Bluetooth is Off");
        mStatusTv.setTextColor(Color.RED);

        mActivateBtn.setText("Enable");
        mActivateBtn.setEnabled(true);

        mPairedBtn.setEnabled(false);
        mScanBtn.setEnabled(false);
    }

    private void showUnsupported() {
        mStatusTv.setText("Bluetooth is unsupported by this device");

        mActivateBtn.setText("Enable");
        mActivateBtn.setEnabled(false);

        mPairedBtn.setEnabled(false);
        mScanBtn.setEnabled(false);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                    showToast("Enabled");

                    showEnabled();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mDeviceList = new ArrayList<BluetoothDevice>();

                mProgressDlg.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mProgressDlg.dismiss();

                Intent newIntent = new Intent(MainActivity.this, DeviceListActivity.class);

                newIntent.putParcelableArrayListExtra("device.list", mDeviceList);

                startActivity(newIntent);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                mDeviceList.add(device);

                showToast("Found device " + device.getName());
            }
        }
    };
    //##############################  TOOL BAR ##########################################
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the mToolbar object
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_list_white_18dp);
    }
    private void hideToolbar() {
        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showToolbar() {
        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    //####################################################################################
}