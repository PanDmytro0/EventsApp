package com.fernfog.mamutrahal;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailedView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detailed_view);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        ImageView imageView = findViewById(R.id.image);

        EventData eventData = getIntent().getParcelableExtra("event_data");

        TextView name = findViewById(R.id.name);
        TextView type = findViewById(R.id.type);
        TextView date = findViewById(R.id.date);
        TextView time = findViewById(R.id.time);
        TextView location = findViewById(R.id.location);
        TextView price = findViewById(R.id.price);
        TextView desc = findViewById(R.id.desc);
        TextView buy = findViewById(R.id.buy);

        storageRef.child("images").child(eventData.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(imageView);
            }
        });

        name.setText(eventData.getName());
//        type.setText(eventData.getType());
        date.setText(eventData.getDate());
        time.setText(eventData.getTime());
        location.setText(eventData.getLocation());
        price.setText(eventData.getPrice());
        desc.setText(eventData.getDesccc());
        buy.setText(eventData.getBuy());

    }
}