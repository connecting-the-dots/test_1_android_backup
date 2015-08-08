package com.focus.test1android;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends Activity {

  private Dialog progressDialog;
  static final String TAG = "LoginActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    // Check if there is a currently logged in user
    // and if it's linked to a Facebook account.
    ParseUser currentUser = ParseUser.getCurrentUser();
    if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
      showUserDetailsActivity();
    }
  }
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
  }

  public void onLoginClick(View v) {
    progressDialog = ProgressDialog.show(LoginActivity.this, "", "Logging in...", true);

    // "public_profile, email, user_status, user_friends, user_about_me, user_location"
    List<String> permissions = Arrays.asList("public_profile, user_friends");
    Log.d(TAG, Arrays.toString(permissions.toArray()));

    // for extended permissions, like "user_about_me", your app must be reviewed by Facebook team
    // (https://developers.facebook.com/docs/facebook-login/permissions/)
    ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException err) {
        progressDialog.dismiss();
        if (user == null) {
          Log.d(TAG, "Uh oh. The user cancelled the Facebook login.");
        } else if (user.isNew()) {
          Log.d(TAG, "User signed up and logged in through Facebook!");
          showUserDetailsActivity();
        } else {
          Log.d(TAG, "User logged in through Facebook!");
          Log.d(TAG, AccessToken.getCurrentAccessToken().getPermissions().toString());
          showUserDetailsActivity();
        }
      }
    });
  }

  private void showUserDetailsActivity() {
    Intent intent = new Intent(this, UserDetailsActivity.class);
    startActivity(intent);
  }
}
