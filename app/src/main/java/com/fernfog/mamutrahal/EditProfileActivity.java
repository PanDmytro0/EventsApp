package com.fernfog.mamutrahal;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditProfileActivity extends AppCompatActivity {

    String image;
    String email;
    String nickname;
    String desc;
    ImageView profilePic;
    public static final int PICK_IMAGE_FILE = 1;
    Uri imageUri;

    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();

        image = intent.getStringExtra("image");
        email = intent.getStringExtra("email");
        nickname = intent.getStringExtra("nickname");
        desc = intent.getStringExtra("desc");

        EditText nicknameEditText = findViewById(R.id.usernameEditText);
        EditText descEditText = findViewById(R.id.descEditText);
        MaterialButton saveSettings = findViewById(R.id.saveSettings);

        nicknameEditText.setHint(nickname);
        descEditText.setHint(desc);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        profilePic = findViewById(R.id.profileImage);

        ImageButton addProfileImage = findViewById(R.id.addProfileImage);

        addProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile();
            }
        });

        storageRef.child("users").child(image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(EditProfileActivity.this).load(uri).circleCrop().into(profilePic);
            }
        });

        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Query query = usersRef.orderByChild("email").equalTo(email);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                DatabaseReference profileRef = child.getRef();

                                String newNickname = nicknameEditText.getText().toString().trim();
                                String newDesc = descEditText.getText().toString().trim();

                                if (imageUri != null) {
                                    String fileName = imageUri.getLastPathSegment();
                                    StorageReference imageRef = storageRef.child("users/" + fileName);
                                    imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                                        profileRef.child("image").setValue(fileName);
                                    });
                                }

                                if (!newNickname.isEmpty()) {
                                    profileRef.child("nickname").setValue(newNickname);
                                }

                                if (!newDesc.isEmpty()) {
                                    profileRef.child("desc").setValue(newDesc);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("EditProfile", "onCancelled: " + error.getMessage());
                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
                imageUri = data.getData();
                Glide.with(EditProfileActivity.this).load(data.getData()).circleCrop().into(profilePic);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}