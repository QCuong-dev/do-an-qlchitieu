package com.example.qlchitieu.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHelper {
    private static final String PREF_NAME="QLCTPrefs";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SharedPrefHelper(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveString(String key,String value){
        editor.putString(key,value);
        editor.apply();
    }
    public void saveInt(String key,int value){
        editor.putInt(key,value);
        editor.apply();
    }
    public void saveBoolean(String key,Boolean value){
        editor.putBoolean(key,value);
        editor.apply();
    }
    public String getString(String key,String defaultValue){
        return sharedPreferences.getString(key,defaultValue);
    }
    public int getInt(String key,int defaultValue){
        return sharedPreferences.getInt(key,defaultValue);
    }
    public Boolean getBoolean(String key,Boolean defaultValue){
        return sharedPreferences.getBoolean(key,defaultValue);
    }
    /** Delete only key */
    public void remove(String key){
        editor.remove(key);
        editor.apply();
    }
    public void clearAll(){
        editor.clear();
        editor.apply();
    }
}
