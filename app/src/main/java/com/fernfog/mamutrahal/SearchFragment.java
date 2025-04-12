package com.fernfog.mamutrahal;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;

public class SearchFragment extends Fragment {

    String query;
    String date;
    String city;
    String price;

    MaterialButton deleteFilters;

    public SearchFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ImageButton filtersButton = view.findViewById(R.id.filterButton);

        SearchView searchView = view.findViewById(R.id.searchView);

        ImageButton searchButton = view.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ResultsActivity.class);

                intent.putExtra("searchQuery", query);
                intent.putExtra("date", date);
                intent.putExtra("city", city);
                intent.putExtra("price", price);

                startActivity(intent);
            }
        });

        deleteFilters = view.findViewById(R.id.deleteFilters);
        deleteFilters.setVisibility(View.INVISIBLE);

        deleteFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFilters.setVisibility(View.INVISIBLE);
                query = "";
                date = "";
                city = "";
                price = "";
            }
        });

        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.BLACK);

        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        searchIcon.setColorFilter(Color.BLACK);

        ImageView closeIcon = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeIcon.setColorFilter(Color.BLACK);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(view.getContext(), ResultsActivity.class);

                intent.putExtra("searchQuery", query);
                intent.putExtra("date", date);
                intent.putExtra("city", city);
                intent.putExtra("price", price);

                startActivity(intent);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query = newText;
                return false;
            }
        });

        filtersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());

                bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        Log.d("jafjwjjoifwa", "anlkflkeanfm");
                        deleteFilters.setVisibility(View.VISIBLE);
                    }
                });

                View viewDialog = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_filter, null);

                Button button = viewDialog.findViewById(R.id.dateButton);
                SeekBar seekBar = viewDialog.findViewById(R.id.seekBar);
                RadioGroup radioGroup = viewDialog.findViewById(R.id.radioGroup);
                TextView textView = viewDialog.findViewById(R.id.textPrice);

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        RadioButton radioButton = viewDialog.findViewById(i);
                        city = radioButton.getText().toString();
                    }
                });

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        price = String.valueOf(i);
                        textView.setText(price);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                view.getContext(),
                                (view1, year1, monthOfYear, dayOfMonth) -> {
                                    String selectedDate = dayOfMonth + "." + (monthOfYear + 1) + "." + year1;
                                    date = selectedDate;
                                },
                                year, month, day
                        );

                        datePickerDialog.show();
                    }
                });



                bottomSheetDialog.setContentView(viewDialog);

                bottomSheetDialog.show();
            }
        });

        return view;
    }
}