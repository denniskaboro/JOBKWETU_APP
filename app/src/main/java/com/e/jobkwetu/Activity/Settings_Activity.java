package com.e.jobkwetu.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.e.jobkwetu.R;

public class Settings_Activity extends AppCompatActivity {
    private Switch switch1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.hmtoolbar);
        toolbar.setTitle("SETTINGS");
        getSupportActionBar();
        toolbar.setNavigationIcon(R.drawable.ic_back_btn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        switch1=findViewById(R.id.switcht);
        //Save state to our app
        SharedPreferences sharedPreferences=getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        final boolean isDarkModeOn =sharedPreferences.getBoolean("isDarkModeOn",false);
        //check the state on opening the app
        if (isDarkModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            //set switch off TODO
            switch1.setChecked(true);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            //set switch on TODO
            switch1.setChecked(false);
        }

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isDarkModeOn) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("isDarkModeOn", false);
                    editor.apply();
                    //change state of switch
                    switch1.setChecked(false);

                }else {
                    //if dark mode is on
                    //it will turn it on
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    //set dark mode on true
                    editor.putBoolean("isDarkModeOn", true);
                    editor.apply();

                    //change state if the switch
                    switch1.setChecked(true);

                }
            }
        });

    }

}
