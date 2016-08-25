package in.peerreview.flipnews.Utils;

/**
 * Created by ddutta on 8/18/2016.
 */
import android.util.Log;

import org.json.JSONObject;

public class L {
    public static void d(String msg){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        Log.d("Dipankar",stackTraceElements[3].getFileName()+"::"+stackTraceElements[3].getMethodName()+"::"+stackTraceElements[3].getLineNumber()+" =>"+msg);
    }
    public static void e(String msg){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        Log.e("Dipankar",stackTraceElements[3].getFileName()+"::"+stackTraceElements[3].getMethodName()+"::"+stackTraceElements[3].getLineNumber()+" =>"+msg);

    }
}
