package com.focus.test1android;

/**
 * Created by Harvey Yang on 2015/8/5.
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ItemDAO{

    public static final String TABLE_NAME = "AppHourBlock";
    public static final String KEY_ID = "_id";
    public static final String APPHOURBLOCK = "appHourBlock";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    APPHOURBLOCK + " TEXT)";

    private SQLiteDatabase db;
    public ItemDAO(Context context) {
        db = MyDBHelper.getDatabase(context);
    }

    public void close() {
        db.close();
    }

    public long insert(JSONObject _apphourblock) throws JSONException {

        String appInString = _apphourblock.toString();

        ContentValues cv = new ContentValues();

        cv.put(APPHOURBLOCK, appInString);

        return db.insert(TABLE_NAME, null, cv);
    }


    public boolean update(JSONObject _apphourblock, long id) throws JSONException {
        String appInString = _apphourblock.toString();

        ContentValues cv = new ContentValues();

        cv.put(APPHOURBLOCK, appInString);

        String where = KEY_ID + "=" + id;

        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    public boolean delete(long id){

        String where = KEY_ID + "=" + id;
        return db.delete(TABLE_NAME, where , null) > 0;
    }

    public List<String> getAll() throws JSONException {
        /*
        JSONArray hourblocks = new JSONArray();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);
        Log.v("ItemDAO", "start counting");
        while(cursor.moveToNext()){
            Log.v("ItemDAO", "count + 1");
            String appInString = cursor.getString(1);
            hourblocks.put(new JSONObject(appInString));
        }
        Log.v("ItemDAO", "stop counting");

        cursor.close();
        return hourblocks;
        */
        List<String> hourblocks= new ArrayList<String>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);
        Log.v("ItemDAO", "start counting");
        while(cursor.moveToNext()){
            Log.v("ItemDAO", "count + 1");
            hourblocks.add(cursor.getString(1));
        }
        Log.v("ItemDAO", "stop counting");

        cursor.close();
        return hourblocks;
    }

    public JSONObject get(long id) throws JSONException {

        String where = KEY_ID + "=" + id;
        String appInString;

        Cursor cursor = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            appInString = cursor.getString(1);
            cursor.close();
            return (new JSONObject(appInString));
        }
        cursor.close();
        return null;
    }

    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        return result;
    }
}
