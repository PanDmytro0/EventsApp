package com.fernfog.mamutrahal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PersonProfileActivity extends AppCompatActivity {

    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView nicknameTextView;
    TextView descTextView;
    TextView countText;
    TextView rankText;
    ImageView profilePic;
    TextView topBarText;

    String image;
    String email;
    String nickname;
    String desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_person_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email = getIntent().getStringExtra("email");
        topBarText = findViewById(R.id.topBarText);
        profilePic = findViewById(R.id.profileImage);
        nicknameTextView = findViewById(R.id.username);
        descTextView = findViewById(R.id.desc);
        countText = findViewById(R.id.eventsText);
        rankText = findViewById(R.id.rankText);

        loadUserData(email);
        loadUserEvents(email);
    }

    private void loadUserData(String userEmail) {
        Query query = usersRef.orderByChild("email").equalTo(userEmail);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    image = child.child("image").getValue(String.class);
                    nickname = child.child("nickname").getValue(String.class);
                    desc = child.child("desc").getValue(String.class);

                    StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                            .child("users").child(image);

                    storageRef.getDownloadUrl().addOnSuccessListener(uri ->
                            Glide.with(PersonProfileActivity.this).load(uri).circleCrop().into(profilePic)
                    );

                    topBarText.setText(nickname);
                    nicknameTextView.setText(nickname);
                    descTextView.setText(desc);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadUserEvents(String userEmail) {
        db.collection("events").get().addOnSuccessListener(queryDocumentSnapshots -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            int eventsCount = 0;

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

                if (eventData.user != null && eventData.user.contains(userEmail)) {
                    Event fragment = new Event(eventData, false);
                    transaction.add(R.id.EventsHandler, fragment);
                    eventsCount++;
                }
            }

            transaction.commit();

            countText.setText(String.valueOf(eventsCount));
            rankText.setText(getRankFromCount(eventsCount));
        });
    }

    private String getRankFromCount(int count) {
        if (count < 1) return "Новачок";
        else if (count <= 3) return "Активний";
        else if (count <= 7) return "Дослідник";
        else if (count <= 15) return "Знавець";
        else if (count <= 25) return "Частий гість";
        else if (count <= 40) return "Постійний";
        else if (count <= 60) return "Експерт";
        else if (count <= 80) return "Гуру";
        else if (count <= 100) return "Легенда";
        else return "Ікона міста";
    }
}
