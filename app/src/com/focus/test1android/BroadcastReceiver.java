package com.focus.test1android;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by AdrianHsu on 15/8/8.
 */
public class BroadcastReceiver extends ParsePushBroadcastReceiver {

    public static final String TAG = "MyApp";
    @Override
    protected void onPushOpen(Context context, Intent intent) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        final String userObjectId = currentUser.getObjectId();
        final JSONObject profile = currentUser.getJSONObject("profile");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRelation");
        // include the pointer field if you want to be able to
        // extract the data from it after the query will be returned.

        query.whereEqualTo("userObjectId", userObjectId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> friend, ParseException e) {
                if (e == null) {

                    for (int i = 0; i < friend.size(); i++) {

                        HashMap<String, String> param = new HashMap<String, String>();
                        try {
                            param.put("from", String.valueOf(profile.get("name")));
                            param.put("to", friend.get(i).getString("installationId"));
                            param.put("message", String.valueOf(profile.get("name") + ": 別再耍廢了，把你怒踢下線！"));

                            Log.v(TAG, "from " + String.valueOf(profile.get("name")));
                            Log.v(TAG, "to " + friend.get(i).getString("installationId"));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            ParseCloud.callFunction("replyKick", param);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                    }
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }

}
