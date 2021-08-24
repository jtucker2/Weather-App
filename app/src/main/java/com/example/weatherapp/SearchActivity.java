package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.example.weatherapp.databinding.ActivitySearchBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;

    private RecyclerView citiesRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                finish();
            }
        });

        citiesRecView = binding.citiesList;

        ArrayList<ListCity> listCities = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(readCitiesJson());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject currentObj = jsonArray.getJSONObject(i);
                listCities.add(new ListCity(currentObj.getString("name"), currentObj.getString("country")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CitiesRecViewAdapter adapter = new CitiesRecViewAdapter();
        adapter.setListCities(listCities);

        citiesRecView.setAdapter(adapter);
        citiesRecView.setLayoutManager(new LinearLayoutManager(this));

        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.filter(s);
                return true;
            }
        });
    }

    public void hideKeyboard() {
        Activity activity = this;
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public String readCitiesJson() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("world-cities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }
}