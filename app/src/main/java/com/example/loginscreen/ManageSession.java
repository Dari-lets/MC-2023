package com.example.loginscreen;
import android.content.Context;
import android.content.SharedPreferences;

public class ManageSession {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String USER_USERNAME = "USERNAME";
    private static final String USER_AVATAR = "AVATAR";

    //Constructor:: creates a "SharedPreferences" object from the "SharedPreferences" class (don't need to touch this)
    public ManageSession(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //setLoggedIn method takes a boolean and stores whether the user logged in or not. Reference this when you want to change whether the person stays logged in or not (set status to true/false)
    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    //isLoggedIn checks whether the person is logged in already or not
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setUsername(String username){
        editor.putString(USER_USERNAME, username);
        editor.apply();
    }

    public String getUsername(){
        return sharedPreferences.getString(USER_USERNAME, "DEFAULT");
    }

    public void deleteUsernamePreference(){
        editor.remove(USER_USERNAME);
        editor.apply();
    }

    public void setAvatar(int avatar){
        editor.putInt(USER_AVATAR, avatar);
        editor.apply();
    }

    public int getAvatar(){
        return sharedPreferences.getInt(USER_AVATAR, 0);
    }

    public void deleteAvatarPreference(){
        editor.remove(USER_AVATAR);
        editor.apply();
    }

}
