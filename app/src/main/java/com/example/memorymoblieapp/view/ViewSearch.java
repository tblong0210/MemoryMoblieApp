package com.example.memorymoblieapp.view;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.ImageSearchAdapter;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewSearch extends AppCompatActivity {
    private ImageView btnTypeFilter;
    private SearchView searchView;
    private RecyclerView recyclerView;
    ArrayList<String> pathImages;
    private int typeSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_picture);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initViews();
        initActions();
    }

    private void initViews() {
        pathImages = DataLocalManager.getStringList(KeyData.IMAGE_PATH_LIST.getKey());
        List<String> test = pathImages.subList(0,20);
        btnTypeFilter = findViewById(R.id.typeFilter);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        typeSearch = R.id.name;

        ImageSearchAdapter adapter = new ImageSearchAdapter();
        adapter.setImages(test);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initActions() {
        btnTypeFilter.setOnClickListener(view -> createMenuPopup(btnTypeFilter, R.menu.search_type));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }

    private void createMenuPopup(View item, int menuItem) {
        PopupMenu popupMenu = new PopupMenu(item.getContext(), item);
        popupMenu.inflate(menuItem);
        popupMenu.setGravity(Gravity.END);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                typeSearch = menuItem.getItemId();
                if (typeSearch == R.id.name) {
                    Toast.makeText(ViewSearch.this, "Find by name", Toast.LENGTH_SHORT).show();
                    searchView.setQueryHint(getString(R.string.query_hint_name));
                } else if (typeSearch == R.id.album) {
                    Toast.makeText(ViewSearch.this, "Find by album", Toast.LENGTH_SHORT).show();
                    searchView.setQueryHint(getString(R.string.query_hint_album));
                }
                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
