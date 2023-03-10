package com.example.memorymoblieapp.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.memorymoblieapp.R;

import com.example.memorymoblieapp.view.ViewImage;

import com.example.memorymoblieapp.view.ViewEdit;


public class MainActivity extends AppCompatActivity {
    private Button btnViewEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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