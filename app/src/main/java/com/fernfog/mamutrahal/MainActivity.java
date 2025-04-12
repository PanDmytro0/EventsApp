package com.fernfog.mamutrahal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        TextView topBarText = findViewById(R.id.topBarText);
        BottomNavigationView bottomNavigationBar = findViewById(R.id.bottom_navigation);

        ImageButton settingsButton = findViewById(R.id.settingsButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        topBarText.setText("Найближчі події");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EventListener()).commit();

        bottomNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    topBarText.setText("Найближчі події");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EventListener()).commit();
                    return true;
                }

                if (item.getItemId() == R.id.nav_add) {
                    topBarText.setText("Додати подію");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddEventFragment()).commit();
                    return true;
                }

                if (item.getItemId() == R.id.nav_search) {
//                    mAuth.signOut();
//                    finish();
//                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    topBarText.setText("Пошук");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).commit();
                    return true;
                }

                if (item.getItemId() == R.id.nav_favorite) {
                    topBarText.setText("Улюбені");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavouriteFragment()).commit();
                    return true;
                }

                if (item.getItemId() == R.id.nav_person) {
                    topBarText.setText("Профіль");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                    return true;
                }

                return false;
            }
        });


    }
}