package com.example.memorymoblieapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.style.IconMarginSpan;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;

public class ViewSearch extends AppCompatActivity {
    private ImageView btnTypeFilter;
    private SearchView searchView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_picture);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initActions();
    }

    private void initViews(){
        btnTypeFilter = findViewById(R.id.typeFilter);
        searchView    = findViewById(R.id.searchView);
        recyclerView  = findViewById(R.id.recyclerView);
    }

    private void initActions(){
        btnTypeFilter.setOnClickListener(view -> createMenuPopup(btnTypeFilter, R.menu.search_type));
    }

    private void createMenuPopup(View item, int menuItem){
        PopupMenu popupMenu = new PopupMenu(item.getContext(), item);
        popupMenu.inflate(menuItem);
        popupMenu.setGravity(Gravity.END);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int item = menuItem.getItemId();
                if(item == R.id.name){
                    Toast.makeText(ViewSearch.this, "Find by name", Toast.LENGTH_SHORT).show();
                }
                else if(item == R.id.date){
                    Toast.makeText(ViewSearch.this, "Find by date", Toast.LENGTH_SHORT).show();
                }
                else if(item == R.id.location){
                    Toast.makeText(ViewSearch.this, "Find by location", Toast.LENGTH_SHORT).show();
                }
                else if(item == R.id.album){
                    Toast.makeText(ViewSearch.this, "Find by album", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
