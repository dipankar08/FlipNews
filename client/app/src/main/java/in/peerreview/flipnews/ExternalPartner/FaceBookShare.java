package in.peerreview.flipnews.ExternalPartner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import in.peerreview.flipnews.Activities.FlipOperation;
import in.peerreview.flipnews.Activities.MainActivity;
import in.peerreview.flipnews.R;
import in.peerreview.flipnews.Utils.SimpleUtils;
import in.peerreview.flipnews.storage.DataSource;

public class FaceBookShare {

    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private static final Activity sActivity = MainActivity.Get();



    private static FaceBookShare sFaceBookShare = null;
    public static FaceBookShare Get(){
        if(sFaceBookShare == null)
            sFaceBookShare = new FaceBookShare();
        return sFaceBookShare;
    }

    public void share()
    {
            FacebookSdk.sdkInitialize(sActivity.getApplicationContext());

            callbackManager = CallbackManager.Factory.create();

            List<String> permissionNeeds = Arrays.asList("publish_actions");

            //this loginManager helps you eliminate adding a LoginButton to your UI
            LoginManager manager = LoginManager.getInstance();

            manager.logInWithPublishPermissions(sActivity, permissionNeeds);

            manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    sharePhotoToFacebook();
                }

                @Override
                public void onCancel() {
                    Log.d("Dipankar","FaceBookShare: User canceled login");
                }

                @Override
                public void onError(FacebookException exception) {
                    Log.d("Dipankar","FaceBookShare: FacebookException Occureed"+exception.getMessage());
                }
            });

    }

    private void sharePhotoToFacebook(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap image = BitmapFactory.decodeFile(SimpleUtils.getScreenSort().getAbsolutePath(), options);
        DataSource d = FlipOperation.Get().getCurrentNew();
        if( d == null){
            Log.d("Dipankar","No News to share");
            return;
        }
        String title = d.getTitle();
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption(title+"\n To get latest news please install FlipNews ")
                .setImageUrl(Uri.parse("https://play.google.com/apps/testing/in.peerreview.flipnews"))
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .setContentUrl(Uri.parse("https://play.google.com/apps/testing/in.peerreview.flipnews"))
                .build();

        ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onCancel() {
                Log.d("Dipankar","share:: onCancel called");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Dipankar","share:: FacebookException called" + error.getMessage());
            }

            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(sActivity.getApplicationContext(),"News sucessfully post in your facebook wall",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onActivityResult(int requestCode, int responseCode, Intent data)
    {
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }

}