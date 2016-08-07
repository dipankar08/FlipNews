package in.peerreview.flipnews.BluetoothSync;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import in.peerreview.flipnews.Utils.Logging;

/**
 * Created by ddutta on 6/26/2016.
 */
public class SendBLEData {
    private OutputStream outputStream;
    private InputStream inStream;
        public SendBLEData(){
            init();

    }
    private void init()  {
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
                            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                            socket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
                            socket.connect();
                            outputStream = socket.getOutputStream();
                            inStream = socket.getInputStream();
                        }

                        Logging.Log("No appropriate paired devices.");
                    } else {
                        Logging.Log("Bluetooth is disabled.");
                    }
             }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void write(String s) {
        try {
            outputStream.write(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytes = 0;
        int b = BUFFER_SIZE;

        while (true) {
            try {
                bytes = inStream.read(buffer, bytes, BUFFER_SIZE - bytes);
               // Logging.Log(bytes.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
