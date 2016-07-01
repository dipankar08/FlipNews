package in.peerreview.FlipNews.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;


/*
 * Created by ddutta on 6/30/2016.
 Tasks:
 1: The data should be reloaded every one hour.
 2: If connection problem is caused, reload occurs on connection appearance and also on the next hour alloted.
 */
public class MyService extends Service {


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /*
        Add service method calls here.
         */
        // Remove this part.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;

    }

}
