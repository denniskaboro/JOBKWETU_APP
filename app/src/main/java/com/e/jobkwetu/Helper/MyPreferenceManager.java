package com.e.jobkwetu.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.e.jobkwetu.Model.Register;
import com.e.jobkwetu.Model.User;

public class MyPreferenceManager {
    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "JOBKWETU";

    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_USERNAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_TOKEN = "user_token";
    private static final String KEY_USER_PHONE = "user_phone";
    private  static final  String KEY_USER_PROFILE = "user_profile";
    private static final String KEY_NOTIFICATIONS = "notifications";

    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void storeUser(User user) {
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_PHONE, user.getPhone());
        editor.putString(KEY_USER_USERNAME, user.getName());
        editor.putString(KEY_USER_TOKEN, user.getToken());
        editor.putString(KEY_USER_PROFILE,user.getProfile());
        editor.putString(KEY_USER_EMAIL,user.getEmail());
        editor.commit();

        Log.e(TAG, "User is stored in shared preferences. " + user.getPhone() + ", " + user.getName());
    }
    public void storeUser2(Register user) {
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_PHONE, user.getPhone());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_USERNAME, user.getUsername());
        editor.putString(KEY_USER_TOKEN, user.getToken());
        editor.commit();

        Log.e(TAG, "User is stored in shared preferences. " + user.getPhone() + ", " + user.getToken());
    }

    public User getUser() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String id, phone, name,token,profile,email;
            id = pref.getString(KEY_USER_ID, null);
            phone= pref.getString(KEY_USER_PHONE, null);
            name = pref.getString(KEY_USER_USERNAME, null);
            token = pref.getString(KEY_USER_TOKEN,null);
            profile=pref.getString(KEY_USER_PROFILE,null);
            email = pref.getString(KEY_USER_EMAIL,null);

            User user = new User(id, phone, name,token,profile,email);
            return user;
        }
        return null;
    }
    public User getUser2() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String id, phone, name,token,profile,email;
            id = pref.getString(KEY_USER_ID, null);
            phone= pref.getString(KEY_USER_PHONE, null);
            token = pref.getString(KEY_USER_TOKEN,null);
            email = pref.getString(KEY_USER_EMAIL,null);
            profile=pref.getString(KEY_USER_PROFILE,null);
            name = pref.getString(KEY_USER_USERNAME, null);
            User user = new User(id, phone, name,token,profile,email);
            return user;
        }
        return null;
    }

    public void addNotification(String notification) {

        // get old notifications
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }

    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}
