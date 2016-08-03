package in.peerreview.FlipNews.Utils;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import in.peerreview.FlipNews.Activities.MainActivity;

/**
 * Created by ddutta on 6/25/2016.
 */
public class Notification {
    public static void Log(String msg){
        Toast.makeText(MainActivity.Get(),msg, Toast.LENGTH_LONG).show();
        Log.i("Notification",msg);
    }

    public static void showErrorAndExit(final String msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.Get());
        alertDialogBuilder.setMessage(msg+" Press No to exit.");

        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Log(msg);
            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.Get().finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
