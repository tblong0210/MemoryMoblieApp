package com.example.memorymoblieapp.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.memorymoblieapp.R;

public class ViewDetails extends AppCompatActivity {
    private TextView txtDateCreated;
    private TextView txtNameImage;
    private TextView txtImageCapacity;
    private TextView txtImageSize;
    private TextView txtPathImage;
    private TextView txtLocationImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_details);

        initViews();
        initActions();
    }

    private void initViews(){
        txtDateCreated = findViewById(R.id.txtDateCreated);
        txtNameImage = findViewById(R.id.txtNameImage);
        txtImageCapacity = findViewById(R.id.txtImageCapacity);
        txtImageSize = findViewById(R.id.txtImageSize);
        txtPathImage = findViewById(R.id.txtPathImage);
        txtLocationImage = findViewById(R.id.txtLocationImage);
    }

    private void initActions(){

    }
}
