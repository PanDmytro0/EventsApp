package com.fernfog.mamutrahal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();

        String query = intent.getStringExtra("searchQuery");
        String date = intent.getStringExtra("date");
        String city = intent.getStringExtra("city");
        String price = intent.getStringExtra("price");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("events").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                FragmentManager fragmentManager = getSupportFragmentManager();
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

                    Log.wtf("data", query);

                    if (
                            (query == null || query.isEmpty() || (eventData.name != null && eventData.name.toLowerCase().contains(query.toLowerCase())))
                                    &&
                                    (date == null || date.isEmpty() || date.equals(eventData.date))
                                    &&
                                    (city == null || city.isEmpty() || (eventData.location != null && eventData.location.toLowerCase().contains(city.toLowerCase())))
                                    &&
                                    (price == null || price.isEmpty() || (eventData.price != null && Integer.parseInt(eventData.price) <= Integer.parseInt(price)))
                    ) {
                        transaction.add(R.id.EventHandler, fragment1);
                    }


                }

                transaction.commit();
            }
        });
    }
}