package com.focus.test1android;

/**
 * Created by AdrianHsu on 15/8/3.
 */
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.widget.ProfilePictureView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class AddFriendActivity extends Activity {

  static final String TAG = "AddFriendActivity";
  private static ProfilePictureView userProfilePictureView;
  private static TextView userNameView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.friend);

    showFriendList();
  }
  public void showFriendList() {
    new GraphRequest(
      AccessToken.getCurrentAccessToken(),
      "/me/friends",
      null,
      HttpMethod.GET,
      new GraphRequest.Callback() {
        public void onCompleted(GraphResponse response) {
          /* handle the result */
          Log.v(TAG, response.toString());
          JSONObject json = response.getJSONObject();
          try {
            JSONArray jarray = json.getJSONArray("data");
            Log.v(TAG, jarray.toString());
            userProfilePictureView = (ProfilePictureView) findViewById(R.id.userProfilePicture);
            userNameView = (TextView) findViewById(R.id.userName);

            userProfilePictureView.setProfileId(jarray.getJSONObject(0).getString("id"));
            userNameView.setText(jarray.getJSONObject(0).getString("name"));

          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }
      ).executeAsync();
  }
  // UserDetailsActivity with take this function, thus makes it static
  public static void registerFriendList() {
    
    new GraphRequest(
      AccessToken.getCurrentAccessToken(),
      "/me/friends",
      null,
      HttpMethod.GET,
      new GraphRequest.Callback() {
        public void onCompleted(GraphResponse response) {
          /* handle the result */
          Log.v(TAG, response.toString());
          JSONObject json = response.getJSONObject();
          
          try {
            // jarray is with data include "facebookId" and "name"
            JSONArray jarray = json.getJSONArray("data");
            for(int i = 0 ; i < jarray.length(); i++) {

              final ParseObject myFriendRelation = new ParseObject("FriendRelation");

              Log.v(TAG, "friend " + i + ": " + jarray.getJSONObject(i));

              ParseUser currentUser = ParseUser.getCurrentUser();
              long tmpFacebookId = jarray.getJSONObject(i).getLong("id");
              String userObjectId = currentUser.getObjectId();

              // self info of this friend-and-I relationship
              myFriendRelation.put("user", currentUser);
              myFriendRelation.put("profile", jarray.getJSONObject(i));
              myFriendRelation.put("facebookId", tmpFacebookId);
              myFriendRelation.put("userObjectId", userObjectId);

              // take facebookId as key to get friend info
              ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
              query.whereEqualTo("facebookId", tmpFacebookId);
              query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> friend, ParseException e) {
                  if (e == null) {
                    // prevent from null pointer exception
                    if (friend.size() != 0) {
                      Log.i(TAG, friend.get(0).getString("installationId"));
                      // choose friend.get(0) since there's only one friend
                      // matches the given facebookId
                      myFriendRelation.put("installationId", 
                        friend.get(0).getString("installationId"));
                      myFriendRelation.saveInBackground();
                      Log.i(TAG, "done relation");
                    } else {
                      Log.d(TAG, "Error: There is no friend of you with this facebookId");
                    }
                  } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                  }
                }
              });
            }
            ParseUser.getCurrentUser().saveInBackground();
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }
      ).executeAsync();
}
}