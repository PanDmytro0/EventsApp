package com.fernfog.mamutrahal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileFragment extends Fragment {
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        db.collection("events").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
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
                            Integer.parseInt(documentSnapshot.getString("type")),
                            documentSnapshot.getString("user")
                    );

                    Event fragment1 = new Event(eventData, false);


                    if (eventData.user != null && eventData.user.contains(mUser.getEmail())) {
                        transaction.add(R.id.EventsHandler, fragment1);
                    }
                }

                transaction.commit();
            }
        });

        return view;
    }
}