package com.example.memorymoblieapp.local_data_storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DataLocalManager {
    private static DataLocalManager instance;
    private static SharedPreferences sharedPreferences;

    private DataLocalManager(Context context) {
        sharedPreferences = context.getSharedPreferences(KeyData.SHARED_PREFERENCES.getKey(), Context.MODE_PRIVATE);
    }

    public static DataLocalManager getInstance(Context context) {
        if (instance == null) {
            instance = new DataLocalManager(context);
        }
        return instance;
    }

    /***
     *
     * @param key   is param key to get value (Ex: KeyData.SHARED_PREFERENCES.getKey())
     * @param data  is value
     * @param <E>
     *     note: only use to getObjectList()
     */
    public static <E> void saveObjectList(String key, ArrayList<E> data) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(data).getAsJsonArray();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, jsonArray.toString());
        editor.apply();
    }

    /***
     *
     * @param key   is param key to get value (Ex: KeyData.SHARED_PREFERENCES.getKey())
     * @param data  is value
     * @param <E>
     *     note: only use to getObject(), getStringData(), getStringList()
     */
    public static <E> void saveData(String key, E data) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, gson.toJson(data));
        editor.apply();
    }

    public static void saveIntegerData(String key, int data){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, data);
        editor.apply();
    }

    public static void saveFloatData(String key, float data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, data);
    }
    /***
     * Remove a key from SharedPreferences
     * @param key   is param key to get value (Ex: KeyData.SHARED_PREFERENCES.getKey())
     */
    public static <E> void remove(String key) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void saveSetStringData(String key, Set<String> data){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, data);
        editor.apply();
    }

    public static void saveBooleanData(String key, boolean data){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, data);
        editor.apply();
    }

    /***
     *
     * @param key is param key to get value of obj
     * @param cls is type of Class (ex: Album.class)
     * @return    ArrayList<Object>
     * @param <E> is class of object
     */
    public static <E> ArrayList<E> getObjectList(String key, Class<E> cls) {
        Gson gson = new Gson();
        ArrayList<E> listData = new ArrayList<>();
        String strJsonArray = sharedPreferences.getString(key, null);

        if (strJsonArray != null) {
            try {
                JSONArray jsonArray = new JSONArray(strJsonArray);
                JSONObject jsonObject;
                E data;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    data = gson.fromJson(jsonObject.toString(), cls);
                    listData.add(data);
                }

            } catch (JSONException e) {
                Log.e("Error as get array data", e.getMessage());
            }
        }

        return listData;
    }

    /***
     *
     * @param key is param key to get value of obj
     * @param cls is type of Class (ex: Album.class)
     * @return    an object
     * @param <E> is class of object
     */
    public static <E> E getObject(String key, Class<E> cls) {
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(key, null), cls);
    }

    public static Boolean getBooleanData(String key) {
        Gson gson = new Gson();
        return sharedPreferences.getBoolean(key, false);
    }

    public static String getStringData(String key) {
        return sharedPreferences.getString(key, null);
    }

    public static ArrayList<String> getStringList(String key) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(key, null), type);
    }

    public static Set<String> getSetList(String key){
        Set<String> data = new HashSet<>();
        return sharedPreferences.getStringSet(key, data);
    }

    public static int getIntegerData(String key){
        return sharedPreferences.getInt(key, -1);
    }

    public static float getFloatData(String key){
        return sharedPreferences.getFloat(key, -1);
    }
}
