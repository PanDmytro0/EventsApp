package com.fernfog.mamutrahal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

public class Event extends Fragment {

    EventData eventData;
    private Gson gson = new Gson();
    boolean toRemove;
    data daataa;

    public Event(EventData eventData, boolean toRemove) {
        this.eventData = eventData;
        this.toRemove = toRemove;
    }

    public Event(EventData eventData, boolean toRemove, data data) {
        this.eventData = eventData;
        this.toRemove = toRemove;
        daataa = data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        Button button = view.findViewById(R.id.open);
        ImageButton buttonFavourite = view.findViewById(R.id.favouriteButton);

        if (toRemove) {
            buttonFavourite.setImageResource(R.drawable.favorite_48dp_e95245_fill1_wght400_grad0_opsz48);
        } else {
            buttonFavourite.setImageResource(R.drawable.favorite_48dp_e95245_fill0_wght400_grad0_opsz48);
        }

        TextView nameText = view.findViewById(R.id.nameText);
        TextView descText = view.findViewById(R.id.descriptionText);
        TextView dateText = view.findViewById(R.id.dateText);
        TextView timeText = view.findViewById(R.id.timeText);
        ImageView imageView = view.findViewById(R.id.imageOfEvent);

        ViewGroup.LayoutParams imageViewParams = imageView.getLayoutParams();

        CardView cardView = view.findViewById(R.id.cardView);

//        cardView.post(new Runnable() {
//            @Override
//            public void run() {
//                imageViewParams.height = cardView.getHeight();
//                imageViewParams.width = cardView.getWidth() / 2;
//
//                imageView.setLayoutParams(imageViewParams);
//            }
//        });

        descText.setText(eventData.getDesccc());
        dateText.setText(eventData.getDate());
        timeText.setText(eventData.getTime());
        nameText.setText(eventData.getName());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());

        storageRef.child("images").child(eventData.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(view.getContext()).load(uri).transform(new RoundedCorners(20)).into(imageView);
            }
        });

        buttonFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (toRemove) {
                    databaseHelper.deletePost(daataa.id);
                    getFragmentManager().beginTransaction().remove(Event.this).commit();
                } else {
                    String json = gson.toJson(eventData);

                    buttonFavourite.setImageResource(R.drawable.favorite_48dp_e95245_fill1_wght400_grad0_opsz48);

                    buttonFavourite.setActivated(false);

                    databaseHelper.addPost(json);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailedView.class);
                intent.putExtra("event_data", eventData);

                startActivity(intent);
            }
        });

        return view;
    }
}