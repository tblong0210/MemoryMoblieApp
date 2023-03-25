package com.example.memorymoblieapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    private LinearLayout locationParent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        locationParent = findViewById(R.id.locationParent);
    }

    private void initActions(){
        Intent intent = getIntent();
        txtDateCreated.setText(intent.getStringExtra("date"));
        txtNameImage.setText(intent.getStringExtra("name"));
        txtImageCapacity.setText(intent.getStringExtra("capacity"));
        txtImageSize.setText(intent.getStringExtra("size"));
        txtPathImage.setText(intent.getStringExtra("path"));

        if(!intent.getStringExtra("location").isEmpty())
            txtLocationImage.setText(intent.getStringExtra("location"));
        else
            locationParent.setVisibility(View.GONE);

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
