package in.peerreview.flipnews.Activities;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import in.peerreview.flipnews.Configarations.GlobalAppConfigurations;
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
            new SendReportToServer().execute(res);
            //sendToServer(res);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        L.d("##############################  handleUncaughtException  ###################################");

    }

}

class SendReportToServer extends AsyncTask<JSONObject, Void, Void> {

    private Exception exception;

    @Override
    protected Void doInBackground(JSONObject... data) {
        try {
            sendToServer(data[0]);
        } catch (Exception e) {
            this.exception = e;
        }
        System.exit(1); // kill off the crashed app
        return null;
    }
    private void sendToServer(JSONObject jsonObject) throws IOException {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(GlobalAppConfigurations.BACKEND_REPORT_URL);
            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            InputStream inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null)
                L.d("Success");
            else
                L.d("Failes to upload crash report");
    }
}