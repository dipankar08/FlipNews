package in.peerreview.flipnews.FirebaseServices;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.peerreview.flipnews.Utils.Notification;
import in.peerreview.flipnews.Utils.NotificationData;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyGcmListenerService";

    @Override
    public void onMessageReceived(RemoteMessage message) {

        Log.d("Dipankar","Some message received from Firebase..."+message);

        String image = message.getNotification().getIcon();
        String title = message.getNotification().getTitle();
        String text = message.getNotification().getBody();
        String sound = message.getNotification().getSound();

        int id = 0;
        Object obj = message.getData().get("id");
        if (obj != null) {
            id = Integer.valueOf(obj.toString());
        }

        Notification.sendNotification(new NotificationData(image, id, title, text, sound));
    }
}