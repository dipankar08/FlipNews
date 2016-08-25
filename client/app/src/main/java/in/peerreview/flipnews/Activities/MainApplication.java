package in.peerreview.flipnews.Activities;

import android.app.Application;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;

import in.peerreview.flipnews.Utils.L;
import in.peerreview.flipnews.Utils.SimpleUtils;

/**
 * Created by ddutta on 8/25/2016.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate ()
    {
        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (Thread thread, Throwable e)
            {
                handleUncaughtException (thread, e);
            }
        });
    }

    public void handleUncaughtException (Thread thread, Throwable e)
    {
        L.d("##############################  handleUncaughtException  ###################################");
        //L.d(e.printStackTrace()); // not all Android versions will print the stack trace automatically
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        String s=  errors.toString();

        try {
            JSONObject res = SimpleUtils.getSystemInfo();
            res.put("Exception_string",s);
            res.put("Exception_Full",e.getMessage());
            res.put("Exception_Cause",e.getCause ().getStackTrace ().toString());
            res.put("Exception_type",e.getCause());
            res.put("Exception_pinToFile",e.getCause ().getStackTrace()[0].getFileName());
            res.put("Exception_pinToLine",e.getCause ().getStackTrace()[0].getLineNumber());
            L.e(res.toString());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        L.d("##############################  handleUncaughtException  ###################################");
        System.exit(1); // kill off the crashed app
    }
}
