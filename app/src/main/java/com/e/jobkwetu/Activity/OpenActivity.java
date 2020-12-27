package com.e.jobkwetu.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.e.jobkwetu.Register_User.LoginActivity;

public class OpenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Save state to our app
        SharedPreferences sharedPreferences=getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        final boolean isDarkModeOn =sharedPreferences.getBoolean("isDarkModeOn",false);
        //check the state on opening the app
        if (isDarkModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            //set switch off TODO
            //switch1.setChecked(true);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            //set switch on TODO
            //switch1.setChecked(false);
        }

        startActivity(new Intent(OpenActivity.this, LoginActivity.class));
        finish();
        //setContentView(R.layout.activity_open);
    }
}