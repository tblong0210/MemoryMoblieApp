package com.example.memorymoblieapp.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.memorymoblieapp.R;
<<<<<<< HEAD
import com.example.memorymoblieapp.view.ViewImage;
=======
import com.example.memorymoblieapp.view.ViewEdit;
>>>>>>> 848bf78c4f11e2f34c69c52f3d0d58a1487eb7be

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
                Intent intent = new Intent(MainActivity.this, ViewImage.class);
                startActivity(intent);
            }
        });
    }
}