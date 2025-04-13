package com.fernfog.mamutrahal;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class HelloActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hello);

        ViewPager2 viewPager2 = findViewById(R.id.viewPager);

        viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {

                switch (position) {
                    case 0:
                        return new HelloFragment();
                    case 1:
                        return new SetCityFragment();
                    case 2:
                        return new InterestFragment();
                    default:
                        return new HelloFragment();
                }
            }

            @Override
            public int getItemCount() {
                return 3;
            }
        });

    }
}