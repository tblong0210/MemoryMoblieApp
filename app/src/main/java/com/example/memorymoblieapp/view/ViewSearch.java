package com.example.memorymoblieapp.view;

import android.os.Bundle;
import android.text.style.IconMarginSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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

        initViews();
        initActions();
    }

    private void initViews(){
        btnTypeFilter = findViewById(R.id.imgView);
        searchView    = findViewById(R.id.searchView);
        recyclerView  = findViewById(R.id.recyclerView);
    }

    private void initActions(){

    }
}
