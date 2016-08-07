package in.peerreview.flipnews.BluetoothSync;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Set;

import in.peerreview.flipnews.Activities.MainActivity;
import in.peerreview.flipnews.Utils.Logging;

/**
 * Created by ddutta on 6/26/2016.
 */
public class BluetoothShare {
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    public  BluetoothShare(){
        BA = BluetoothAdapter.getDefaultAdapter();
    }
    public void on(){
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
           MainActivity.Get().startActivityForResult(turnOn, 0);
            Logging.Log("Turned on");
        }
        else
        {
            Logging.Log("Already on");
            //Toast.makeText(getApplicationContext(),"Already on", Toast.LENGTH_LONG).show();
        }
    }
    public void off(){
        BA.disable();
        Logging.Log("Turned off");
    }
    public  void visible(){
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        MainActivity.Get().startActivityForResult(getVisible, 0);
    }
    public void list(){
        pairedDevices = BA.getBondedDevices();
        ArrayList list = new ArrayList();

        for(BluetoothDevice bt : pairedDevices){
            list.add(bt.getName());
            Logging.Log(bt.getName());
        }
    }

}
