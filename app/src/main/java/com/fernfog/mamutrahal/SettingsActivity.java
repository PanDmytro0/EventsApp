package com.fernfog.mamutrahal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {

    boolean lutsk = false;
    boolean kyiv = false;
    boolean lviv = false;

    boolean concert = false;
    boolean party = false;
    boolean nastolka = false;
    boolean cosplay = false;
    boolean vystava = false;
    boolean reading = false;
    boolean performance = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialButton accessibilityButton = findViewById(R.id.accessibilityButton);
        MaterialButton changePasswordButton = findViewById(R.id.changePasswordButton);
        MaterialButton logOutButton = findViewById(R.id.logOutButton);

        ImageButton backButton = findViewById(R.id.buttonBack);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, ChangePasswordActivity.class));
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(intent);
            }
        });

        accessibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
        SharedPreferences.Editor editor = prefs.edit();

        CheckBox lutskCheck = findViewById(R.id.lutsk);
        CheckBox kyivCheck = findViewById(R.id.kyiv);
        CheckBox lvivCheck = findViewById(R.id.lviv);

        lutsk = prefs.getBoolean("lutsk", false);
        kyiv = prefs.getBoolean("kyiv", false);
        lviv = prefs.getBoolean("lviv", false);

        lutskCheck.setChecked(lutsk);
        kyivCheck.setChecked(kyiv);
        lvivCheck.setChecked(lviv);

        CheckBox concertCheck = findViewById(R.id.concert);
        CheckBox partyCheck = findViewById(R.id.party);
        CheckBox nastolkaCheck = findViewById(R.id.nastolka);
        CheckBox cosplayCheck = findViewById(R.id.cosplay);
        CheckBox vystavaCheck = findViewById(R.id.vystava);
        CheckBox readingCheck = findViewById(R.id.reading);
        CheckBox performanceCheck = findViewById(R.id.performance);

        concert = prefs.getBoolean("concert", false);
        party = prefs.getBoolean("party", false);
        nastolka = prefs.getBoolean("nastolka", false);
        cosplay = prefs.getBoolean("cosplay", false);
        vystava = prefs.getBoolean("vystava", false);
        reading = prefs.getBoolean("reading", false);
        performance = prefs.getBoolean("performance", false);

        concertCheck.setChecked(concert);
        partyCheck.setChecked(party);
        nastolkaCheck.setChecked(nastolka);
        cosplayCheck.setChecked(cosplay);
        vystavaCheck.setChecked(vystava);
        readingCheck.setChecked(reading);
        performanceCheck.setChecked(performance);

        lutskCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                lutsk = b;
                editor.putBoolean("lutsk", lutsk);
                editor.apply();
            }
        });

        kyivCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                kyiv = b;
                editor.putBoolean("kyiv", kyiv);
                editor.apply();
            }
        });

        lvivCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                lviv = b;
                editor.putBoolean("lviv", lviv);
                editor.apply();
            }
        });


        concertCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("concert", b);
                editor.apply();
            }
        });

        partyCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("party", b);
                editor.apply();
            }
        });

        nastolkaCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("nastolka", b);
                editor.apply();
            }
        });

        cosplayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("cosplay", b);
                editor.apply();
            }
        });

        vystavaCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("vystava", b);
                editor.apply();
            }
        });

        readingCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("reading", b);
                editor.apply();
            }
        });

        performanceCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("performance", b);
                editor.apply();
            }
        });
    }
}