package com.example.memorymoblieapp.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;

import com.example.memorymoblieapp.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

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
        String pathImage = intent.getStringExtra("path");
        String pathImageShow = pathImage.substring(0,pathImage.lastIndexOf('/'));

        File currentFile = new File(pathImage);
        Bitmap bitmap = BitmapFactory.decodeFile(currentFile.getAbsolutePath());
        ExifInterface exif = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ROOT);

        long fileSizeNumber = Math.round(currentFile.length() * 1.0 / 1000);
        String fileSizeResult;
        if (fileSizeNumber > 2000)
            fileSizeResult = String.format(Locale.ROOT, "%.2f MB", fileSizeNumber * 1.0 / 1000);
        else
            fileSizeResult = String.format(Locale.ROOT, "%d KB", fileSizeNumber);

        try {
            exif = new ExifInterface(currentFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("Error get location image: ", e.getMessage());
        }

        txtDateCreated.setText(sdf.format(currentFile.lastModified()));
        txtNameImage.setText(currentFile.getName());
        txtImageCapacity.setText(fileSizeResult);
        txtImageSize.setText(bitmap.getWidth() + "x" + bitmap.getHeight());
        txtPathImage.setText(pathImageShow);

        float[] latLong = new float[2];
        if (exif.getLatLong(latLong)) {
            double latitude = latLong[0];
            double longitude = latLong[1];
            txtLocationImage.setText(latitude + "," + longitude);
        } else {
            locationParent.setVisibility(View.GONE);
        }

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
