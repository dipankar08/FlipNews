package in.peerreview.flipnews.Utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import in.peerreview.flipnews.Activities.MainActivity;
import in.peerreview.flipnews.LaunchActivity.LaunchActivity;
import in.peerreview.flipnews.R;

/**
 * Created by ddutta on 8/7/2016.
 */
public class Notification {

    private static MainActivity sActivity = MainActivity.getActivity();
    private  final int welcome_notification_id = 40001;


    public static void sendwelcomeNotification(){

        //Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(sActivity);
        builder.setSmallIcon(R.drawable.blank);
        builder.setTicker("Welcome");
        builder.setContentTitle("Sample Title");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentText("This is some text");

        //adding actaions
        Intent intent = new Intent(sActivity, LaunchActivity.class);
        intent.putExtra("title", "hello");
        intent.putExtra("text", "world");
        PendingIntent pIntent = PendingIntent.getActivity(sActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.blank, "Action Button", pIntent);
        builder.setContentIntent(pIntent);

        // Dismiss Notification
        builder.setAutoCancel(true);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) sActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationmanager.notify(0, builder.build());
    }
    /**
     * Sending a Notification
     */
    public static void sendNotification(NotificationData notificationData) {

        Intent intent = new Intent(sActivity, MainActivity.class);
        intent.putExtra(NotificationData.TEXT, notificationData.getTextMessage());

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(sActivity, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = null;
        try {

            notificationBuilder = new NotificationCompat.Builder(sActivity);
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            notificationBuilder.setContentTitle(URLDecoder.decode(notificationData.getTitle(), "UTF-8"));
            notificationBuilder.setContentText(URLDecoder.decode(notificationData.getTextMessage(), "UTF-8"));
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            notificationBuilder.setContentIntent(pendingIntent);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (notificationBuilder != null) {
            NotificationManager notificationManager =
                    (NotificationManager) sActivity.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationData.getId(), notificationBuilder.build());
        } else {
            Log.d("DIPANKAR", "Some Error Occures");
        }
    }
}

