package com.fernfog.mamutrahal;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class AddEventFragment extends Fragment {

    FirebaseStorage storage = FirebaseStorage.getInstance("gs://nedosocialnewtork.appspot.com");
    StorageReference storageReference = storage.getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    public static final int PICK_IMAGE_FILE = 1;
    ImageButton chooseFileButton;
    MaterialButton submitButton;
    EditText shortDescription;
    Uri image;
    StorageReference imageRef;

    public AddEventFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        try {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            mAuth.getCurrentUser().reload();

            chooseFileButton = view.findViewById(R.id.chooseImageFileButton);
            submitButton = view.findViewById(R.id.submitArticleButton);
            shortDescription = view.findViewById(R.id.description);

            chooseFileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFile();
                }
            });

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (image != null) {
                        imageRef = storageReference.child("images/"+image.getLastPathSegment());
                        imageRef.putFile(image);

                        EditText nameText = view.findViewById(R.id.name);
                        String nameString = nameText.getEditableText().toString();

                        EditText buyText = view.findViewById(R.id.site);
                        String buyString = buyText.getEditableText().toString();

                        EditText dateText = view.findViewById(R.id.dateText);
                        String dateString = dateText.getEditableText().toString();

                        EditText descText = view.findViewById(R.id.description);
                        String descString = descText.getEditableText().toString();

                        EditText locationText = view.findViewById(R.id.location);
                        String locationString = locationText.getEditableText().toString();

                        EditText priceText = view.findViewById(R.id.price);
                        String priceString = priceText.getEditableText().toString();

                        EditText timeText = view.findViewById(R.id.time);
                        String timeString = timeText.getEditableText().toString();

                        Map<String, Object> data = new HashMap<>();
                        data.put("image", imageRef.getName());
                        data.put("buy", buyString);
                        data.put("user" , mUser.getEmail());
                        data.put("date", dateString);
                        data.put("desc", descString);
                        data.put("location", locationString);
                        data.put("name", nameString);
                        data.put("price", priceString);
                        data.put("time", timeString);
                        data.put("type", "1");

                        FirebaseUser currentUser = mAuth.getCurrentUser();

                        if (currentUser == null) {

                        } else {
                            if (!nameString.isEmpty()
                                    && !buyString.isEmpty()
                                    && !dateString.isEmpty()
                                    && !descString.isEmpty()
                                    && !locationString.isEmpty()
                                    && !priceString.isEmpty()
                                    && !timeString.isEmpty()) {
                                db.collection("events").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(v.getContext(), "Success", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    public void openFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");
        startActivityForResult(intent, PICK_IMAGE_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_FILE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                image = data.getData();
                Glide.with(AddEventFragment.this).load(data.getData()).into(chooseFileButton);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}