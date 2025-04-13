package com.fernfog.mamutrahal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.material.button.MaterialButton;

public class SetCityFragment extends Fragment {

    boolean lutsk = false;
    boolean kyiv = false;
    boolean lviv = false;

    public SetCityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_city, container, false);

        CheckBox lutskCheck = view.findViewById(R.id.lutsk);
        CheckBox kyivCheck = view.findViewById(R.id.kyiv);
        CheckBox lvivCheck = view.findViewById(R.id.lviv);

        MaterialButton nextButton = view.findViewById(R.id.nextButton);

        lutskCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                lutsk = b;
            }
        });

        kyivCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                kyiv = b;
            }
        });

        lvivCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                lviv = b;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
                SharedPreferences.Editor editor = prefs.edit();

                editor.putBoolean("lutsk", lutsk);
                editor.putBoolean("kyiv", kyiv);
                editor.putBoolean("lviv", lviv);
                editor.apply();

                ViewPager2 viewPager2 = requireActivity().findViewById(R.id.viewPager);

                viewPager2.setCurrentItem(2);
            }
        });

        return view;
    }
}