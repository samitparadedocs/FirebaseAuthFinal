package info.androidhive.firebase.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Samit
 */
public class Pref {
    private SharedPreferences mSharedPreferences;
    private final String PREF_NAME = "samit_pref";
    private SharedPreferences.Editor mEditor;

    public Pref(Context context) {
        // TODO Auto-generated constructor stub
        mSharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        mEditor = mSharedPreferences.edit();
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public void putUserName(String eventId) {
        mEditor.putString("userName", eventId);
        mEditor.commit();
    }


    public String getUserName() {
        return mSharedPreferences.getString("userName", "");
    }

    public void putUserType(String eventId) {
        mEditor.putString("UserType", eventId);
        mEditor.commit();
    }



    public String getUserType() {
        return mSharedPreferences.getString("UserType", "");
    }

    public void putUserUniqueId(String userUniqueID) {
        mEditor.putString("UserId", userUniqueID);
        mEditor.commit();
    }
    public String getUserUniqueId() {
        return mSharedPreferences.getString("UserId", "");
    }


    public void putcontact_number(String userUniqueID) {
        mEditor.putString("contact_number", userUniqueID);
        mEditor.commit();
    }
    public String getcontact_number() {
        return mSharedPreferences.getString("UserId", "");
    }

    public void putaddress(String userUniqueID) {
        mEditor.putString("address", userUniqueID);
        mEditor.commit();
    }
    public String getaddress() {
        return mSharedPreferences.getString("address", "");
    }
}
