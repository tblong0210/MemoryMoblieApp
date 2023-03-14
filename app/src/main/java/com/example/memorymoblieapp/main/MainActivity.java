package com.example.memorymoblieapp.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.memorymoblieapp.R;

import com.example.memorymoblieapp.fragment.ImageFragment;
import com.example.memorymoblieapp.fragment.TitleContentContainerFragment;
import com.example.memorymoblieapp.view.ViewEdit;


public class MainActivity extends AppCompatActivity {
    private Button btnViewEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FragmentAlbum
//        AlbumFragment albumFragment = new AlbumFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, albumFragment).commit();

        ImageFragment imageFragment = new ImageFragment(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, imageFragment).commit();

        TitleContentContainerFragment titleContentContainerFragment = new TitleContentContainerFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_title_content_container, titleContentContainerFragment).commit();


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