package com.example.memorymoblieapp.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.memorymoblieapp.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class ViewDetails extends AppCompatActivity {
    private TextView txtDateCreated;
    private TextView txtNameImage;
    private TextView txtImageCapacity;
    private TextView txtImageSize;
    private TextView txtPathImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initViews();
        initActions();
    }

    private void initViews() {
        txtDateCreated = findViewById(R.id.txtDateCreated);
        txtNameImage = findViewById(R.id.txtNameImage);
        txtImageCapacity = findViewById(R.id.txtImageCapacity);
        txtImageSize = findViewById(R.id.txtImageSize);
        txtPathImage = findViewById(R.id.txtPathImage);
    }

    @SuppressLint("SetTextI18n")
    private void initActions() {
        Intent intent = getIntent();
        String pathImage = intent.getStringExtra("path");
        String pathImageShow = pathImage.substring(0, pathImage.lastIndexOf('/'));

        File currentFile = new File(pathImage);
        Bitmap bitmap = BitmapFactory.decodeFile(currentFile.getAbsolutePath());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ROOT);

        long fileSizeNumber = Math.round(currentFile.length() * 1.0 / 1000);
        String fileSizeResult;
        if (fileSizeNumber > 2000)
            fileSizeResult = String.format(Locale.ROOT, "%.2f MB", fileSizeNumber * 1.0 / 1000);
        else
            fileSizeResult = String.format(Locale.ROOT, "%d KB", fileSizeNumber);


        txtDateCreated.setText(sdf.format(currentFile.lastModified()));
        txtNameImage.setText(currentFile.getName());
        txtImageCapacity.setText(fileSizeResult);
        txtImageSize.setText(bitmap.getWidth() + "x" + bitmap.getHeight());
        txtPathImage.setText(pathImageShow);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
