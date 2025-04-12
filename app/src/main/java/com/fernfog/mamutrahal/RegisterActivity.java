package com.fernfog.mamutrahal;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseStorage storage = FirebaseStorage.getInstance("gs://nedosocialnewtork.appspot.com");
    StorageReference storageReference = storage.getReference();

    public static final int PICK_IMAGE_FILE = 1;
    Uri image;
    ImageView userImage;
    ImageButton addUserImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextInputEditText email = findViewById(R.id.email);
        TextInputEditText password = findViewById(R.id.password);
        TextInputEditText nickname = findViewById(R.id.nickname);

        MaterialButton button = findViewById(R.id.loginButton);

        userImage = findViewById(R.id.profileImage);
        addUserImage = findViewById(R.id.addProfileImage);

        addUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        Map<String, Object> userData = new HashMap<>();

        try {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String emailString = email.getText().toString();
                    String passwordString = password.getText().toString();
                    String usernameString = nickname.getText().toString();

                    if ((image != null) && !emailString.isEmpty() && !passwordString.isEmpty() && !usernameString.isEmpty()) {
                        mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    userData.put("nickname", usernameString);
                                    userData.put("email", emailString);
                                    userData.put("image", image.getLastPathSegment());
                                    userData.put("desc", "");

                                    usersRef.child(usersRef.push().getKey()).setValue(userData);

                                    StorageReference imageRef = storageReference.child("users/"+image.getLastPathSegment());
                                    imageRef.putFile(image);

                                    Toast.makeText(RegisterActivity.this, "Реєстрація успішна!", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), HelloActivity.class));
                                }
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Заповніть всю інформацію!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                Glide.with(RegisterActivity.this).load(data.getData()).circleCrop().into(userImage);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}