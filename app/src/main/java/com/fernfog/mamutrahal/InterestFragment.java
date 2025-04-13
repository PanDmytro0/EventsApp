package com.fernfog.mamutrahal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.material.button.MaterialButton;

public class InterestFragment extends Fragment {

    public InterestFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interest, container, false);

        CheckBox concertCheck = view.findViewById(R.id.concert);
        CheckBox partyCheck = view.findViewById(R.id.party);
        CheckBox nastolkaCheck = view.findViewById(R.id.nastolka);
        CheckBox cosplayCheck = view.findViewById(R.id.cosplay);
        CheckBox vystavaCheck = view.findViewById(R.id.vystava);
        CheckBox readingCheck = view.findViewById(R.id.reading);
        CheckBox performanceCheck = view.findViewById(R.id.performance);

        MaterialButton nextButton = view.findViewById(R.id.nextButton);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = prefs.edit();

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

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().finish();
                startActivity(new Intent(requireContext(), MainActivity.class));
            }
        });

        return view;
    }
}