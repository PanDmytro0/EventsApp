package com.fernfog.mamutrahal;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class EventListener extends Fragment {

    public EventListener() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_listener, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        boolean lutsk = sharedPreferences.getBoolean("lutsk", false);
        boolean kyiv = sharedPreferences.getBoolean("kyiv", false);
        boolean lviv = sharedPreferences.getBoolean("lviv", false);

        boolean concert = sharedPreferences.getBoolean("concert", false);
        boolean party = sharedPreferences.getBoolean("party", false);
        boolean nastolka = sharedPreferences.getBoolean("nastolka", false);
        boolean cosplay = sharedPreferences.getBoolean("cosplay", false);
        boolean vystava = sharedPreferences.getBoolean("vystava", false);
        boolean reading = sharedPreferences.getBoolean("reading", false);
        boolean performance = sharedPreferences.getBoolean("performance", false);

        db.collection("events").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!isAdded()) return;

                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    EventData eventData = new EventData(
                            documentSnapshot.getString("name"),
                            documentSnapshot.getString("buy"),
                            documentSnapshot.getString("date"),
                            documentSnapshot.getString("desc"),
                            documentSnapshot.getString("image"),
                            documentSnapshot.getString("location"),
                            documentSnapshot.getString("price"),
                            documentSnapshot.getString("time"),
                            documentSnapshot.getString("type"),
                            documentSnapshot.getString("user")
                    );

                    Event fragment1 = new Event(eventData, false);
                    if (concert && eventData.type.contains("Концерт") ||
                            (party && eventData.type.contains("Вечірка")) ||
                            (nastolka && eventData.type.contains("Настільні ігри")) ||
                            (vystava && eventData.type.contains("Косплей")) ||
                            (cosplay && eventData.type.contains("Вистава")) ||
                            (reading && eventData.type.contains("Читання")) ||
                            (performance && eventData.type.contains("Перформанс"))) {
                        if ((lutsk && eventData.location.contains("Луцьк")) || (kyiv && eventData.location.contains("Київ")) || (lviv && eventData.location.contains("Львів")))
                            transaction.add(R.id.EventHandler, fragment1);
                    }
                }

                if (isAdded()) {
                    transaction.commitAllowingStateLoss();
                }
            }
        });

        return view;
    }

}