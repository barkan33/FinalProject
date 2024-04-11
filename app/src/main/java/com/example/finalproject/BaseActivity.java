package com.example.finalproject;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public abstract class BaseActivity extends AppCompatActivity {
    protected static final String PREFS_NAME = "UserData";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveUserDictPref();
    }

    private void saveUserDictPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String userDictJson = sharedPreferences.getString("user_dict_json", "");
        Gson gson = new Gson();
        Map<String, User> userDict = gson.fromJson(userDictJson, new TypeToken<Map<String, User>>() {
        }.getType());


        MyApplication myApplication = (MyApplication) getApplicationContext();
        User currentUser = myApplication.getCurrentUser();

//        userDict[currentUser.getUsername()] = currentUser; // לא עבד
        userDict.replace(currentUser.getUsername(), currentUser);


        String updatedUserDictJson = gson.toJson(userDict);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_dict_json", updatedUserDictJson);
        editor.apply();
    }


    protected void addUserToDictPref(User user) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String userDictJson = sharedPreferences.getString("user_dict_json", "{}");
        Gson gson = new Gson();
        Map<String, User> userDict = gson.fromJson(userDictJson, new TypeToken<Map<String, User>>() {
        }.getType());

        userDict.put(user.getUsername(), user);

        userDictJson = gson.toJson(userDict);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_dict_json", userDictJson);
        editor.apply();
    }
}
