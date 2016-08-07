package in.peerreview.flipnews.Utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

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
        builder.setSmallIcon(R.drawable.mamatha);
        builder.setTicker("Welcome");
        builder.setContentTitle("Sample Title");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentText("This is some text");

        //adding actaions
        Intent intent = new Intent(sActivity, LaunchActivity.class);
        intent.putExtra("title", "hello");
        intent.putExtra("text", "world");
        PendingIntent pIntent = PendingIntent.getActivity(sActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.mamatha, "Action Button", pIntent);
        builder.setContentIntent(pIntent);

        // Dismiss Notification
        builder.setAutoCancel(true);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) sActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationmanager.notify(0, builder.build());
    }
}
