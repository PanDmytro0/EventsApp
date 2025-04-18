package com.fernfog.mamutrahal;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DetailedView extends AppCompatActivity {
    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detailed_view);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        ImageButton backButton = findViewById(R.id.buttonBack);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView imageView = findViewById(R.id.image);

        EventData eventData = getIntent().getParcelableExtra("event_data");

        TextView name = findViewById(R.id.name);
        TextView type = findViewById(R.id.type);
        TextView date = findViewById(R.id.date);
        TextView time = findViewById(R.id.time);
        TextView location = findViewById(R.id.location);
//        TextView price = findViewById(R.id.price);
        TextView desc = findViewById(R.id.desc);
        TextView link = findViewById(R.id.link);
        ImageButton favouriteButton = findViewById(R.id.favouriteButton);

        Gson gson = new Gson();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Button addToCalendar = findViewById(R.id.calendar);

        addToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();

                try {
                    calendar.setTime(new SimpleDateFormat("dd.MM.yyyy").parse(eventData.date));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.Events.TITLE, eventData.name);
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, eventData.location);
                intent.putExtra(CalendarContract.Events.DESCRIPTION, eventData.desccc);
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.getTimeInMillis());
                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                intent.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });

        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String json = gson.toJson(eventData);

                    favouriteButton.setImageResource(R.drawable.favorite_48dp_e95245_fill1_wght400_grad0_opsz48);

                    favouriteButton.setActivated(false);
                    databaseHelper.addPost(json);
            }
        });

        storageRef.child("images").child(eventData.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(imageView);
            }
        });

        name.setText(eventData.getName());

        date.setText(eventData.getDate());
        time.setText(eventData.getTime());
        location.setText(eventData.getLocation());
//        price.setText(eventData.getPrice());
        desc.setText(eventData.getDesccc());
        link.setText(eventData.getBuy());

        Linkify.addLinks(link, Linkify.WEB_URLS);

        String typeString = eventData.getType();
        type.setText(eventData.getType());

        switch (typeString) {
            case "Концерт":
                type.setTextColor(Color.parseColor("#9E88FF"));
                break;
            case "Вечірка":
                type.setTextColor(Color.parseColor("#ED2C7E"));
                break;
            case "Настільні ігри":
                type.setTextColor(Color.parseColor("#FF8D28"));
                break;
            case "Косплей":
                type.setTextColor(Color.parseColor("#FF4DD3"));
                break;
            case "Вистава":
                type.setTextColor(Color.parseColor("#71D8FF"));
                break;
            case "Читання":
                type.setTextColor(Color.parseColor("#F96729"));
                break;
            case "Перформанс":
                type.setTextColor(Color.parseColor("#CD8800"));
                break;
        }

        ImageView profilePic = findViewById(R.id.UserIcon);
        MaterialButton userEmail = findViewById(R.id.emailText);

        userEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailedView.this, PersonProfileActivity.class);
                intent.putExtra("email", eventData.user);
                startActivity(intent);
            }
        });

        Log.d("user", eventData.user);

        try {
            Query query = usersRef.orderByChild("email").equalTo(eventData.user);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();

                        String image = child.child("image").getValue(String.class);
                        String nickname = child.child("nickname").getValue(String.class);

                        Log.d("image", image);

                        storageRef.child("users").child(image).getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(DetailedView.this).load(uri).circleCrop().into(profilePic);
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    e.printStackTrace();
                                    Toast.makeText(DetailedView.this, "Помилка завантаження зображення", Toast.LENGTH_SHORT).show();
                                });


                        userEmail.setText(nickname);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }
}