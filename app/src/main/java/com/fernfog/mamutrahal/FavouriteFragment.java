package com.fernfog.mamutrahal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

public class FavouriteFragment extends Fragment {

    private Gson gson = new Gson();

    public FavouriteFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (data data: databaseHelper.getAllPosts()) {
            EventData event = gson.fromJson(data.json, EventData.class);

            Event fragment1 = new Event(event, true, data);

            transaction.add(R.id.EventHandler, fragment1);

            Log.wtf("data", event.name);
        }

        transaction.commit();

        return view;
    }
}