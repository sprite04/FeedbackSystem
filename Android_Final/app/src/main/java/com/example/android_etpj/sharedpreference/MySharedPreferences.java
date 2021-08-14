package com.example.android_etpj.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private static final String LOGIN="LOGIN";
    // private static final String ROLE="ROLE";
    private Context context;

    public MySharedPreferences(Context context) {
        this.context = context;
    }

    /*
     * My adding start here
     * */

    public void putRoleValue(String key, String value){
        SharedPreferences sharedPreference = context.getSharedPreferences(LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreference.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public void putUserID(String key, String value){
        SharedPreferences sharedPreference = context.getSharedPreferences(LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreference.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public String getUserRole(String key){
        SharedPreferences sharedPreference = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        return sharedPreference.getString(key,"");
    }
    public String getUserID(String key){
        SharedPreferences sharedPreference = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        return sharedPreference.getString(key,"");
    }


    /*
     * My adding end here
     * */

    public void putBooleanValue(String key, boolean value){
        SharedPreferences sharedPreference = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreference.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public boolean getBooleanValue(String key){
        SharedPreferences sharedPreference= context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        return sharedPreference.getBoolean(key,false);
    }

    public void putIntergerValue(String key, int value){
        SharedPreferences sharedPreference= context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreference.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public int getIntergerValue(String key){
        SharedPreferences sharedPreference= context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        return sharedPreference.getInt(key,-1);
    }

    public void putStringValue(String key, String value){
        SharedPreferences sharedPreference= context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreference.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public String getStringValue(String key){
        SharedPreferences sharedPreference= context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        return sharedPreference.getString(key,"");
    }

}
