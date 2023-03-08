package com.example.memorymoblieapp.view;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.memorymoblieapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewImage extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting_image);
        initApp();

    }

    private void initApp(){
        bottomNavigationView = findViewById(R.id.navSetting);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.favorite:
                        Toast.makeText(ViewImage.this, "favorite", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.unfavorite:favorite:
                        Toast.makeText(ViewImage.this, "unfavorite", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.edit:
                        Toast.makeText(ViewImage.this, "edit", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.share:
                        Toast.makeText(ViewImage.this, "share", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.trash:
                        Toast.makeText(ViewImage.this, "trah", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.more:
                        Toast.makeText(ViewImage.this, "nore", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }
}
