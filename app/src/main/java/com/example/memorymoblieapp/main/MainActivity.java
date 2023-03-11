package com.example.memorymoblieapp.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.memorymoblieapp.R;

import com.example.memorymoblieapp.fragment.AlbumFragment;

import com.example.memorymoblieapp.view.ViewEdit;


public class MainActivity extends AppCompatActivity {
    private Button btnViewEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FragmentAlbum
        AlbumFragment fragmentAlbum = new AlbumFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, fragmentAlbum).commit();

        btnViewEdit = findViewById(R.id.btnViewEdit);

        btnViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewEdit.class);
                startActivity(intent);
            }
        });
    }
}