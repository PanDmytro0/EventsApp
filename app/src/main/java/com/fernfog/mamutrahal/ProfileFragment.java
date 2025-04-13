package com.fernfog.mamutrahal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProfileFragment extends Fragment {
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ImageView profileImage;
    TextView nicknameTextView;
    TextView descTextView;
    String image;
    String email;
    String nickname;
    String desc;

    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

    public ProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        nicknameTextView = view.findViewById(R.id.username);
        descTextView = view.findViewById(R.id.desc);
        profileImage = view.findViewById(R.id.profileImage);

        MaterialButton editProfile = view.findViewById(R.id.editProfile);

        editProfile.setVisibility(View.INVISIBLE);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editProfile = new Intent(requireContext(), EditProfileActivity.class);
                editProfile.putExtra("image", image);
                editProfile.putExtra("email", email);
                editProfile.putExtra("nickname", nickname);
                editProfile.putExtra("desc", desc);
                startActivity(editProfile);
            }
        });

        try {
            Query query = usersRef.orderByChild("email").equalTo(mUser.getEmail());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();

                        image = child.child("image").getValue(String.class);
                        nickname = child.child("nickname").getValue(String.class);
                        email = child.child("email").getValue(String.class);
                        desc = child.child("desc").getValue(String.class);

                        storageRef.child("users").child(image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(view.getContext()).load(uri).circleCrop().into(profileImage);
                            }
                        });

                        nicknameTextView.setText(child.child("nickname").getValue(String.class));
                        descTextView.setText(child.child("desc").getValue(String.class));

                        editProfile.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            TextView countText = view.findViewById(R.id.eventsText);
            final int[] eventsCount = {0};

            db.collection("events").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    FragmentManager fragmentManager = getChildFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    ArrayList<String> dateList = new ArrayList<>();

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


                        if (eventData.user != null && eventData.user.contains(mUser.getEmail())) {
                            dateList.add(eventData.date);
                            transaction.add(R.id.EventsHandler, fragment1);
                            eventsCount[0]++;
                        }
                    }

                    transaction.commit();

                    countText.setText("" + eventsCount[0]);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }
}