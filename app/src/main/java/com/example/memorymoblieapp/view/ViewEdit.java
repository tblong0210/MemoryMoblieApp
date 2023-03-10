package com.example.memorymoblieapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.main.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewEdit extends AppCompatActivity {

    private ImageView rotatePic, flipPic, resizePic, paintPic, stickerPic, textPic;
    private LinearLayout emoteOption, cropOption, filterOption, brightnessOption;

    BottomNavigationView nav_edit_view;

    Button cancelViewEditBtn, saveViewEditBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        initOptionActions();

        handleCropPicture();
        handleFilteringPicture();
        handleEmotePicture();


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

    private void refreshPicture() {

    }

    private void handleCropPicture() {


    }

    private void handleFilteringPicture() {
    }

    private void handleEmotePicture(){}
    private void initOptionActions() {
        nav_edit_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cropPic:
                       cropOption.setVisibility(View.VISIBLE);

                       filterOption.setVisibility(View.GONE);
                       brightnessOption.setVisibility(View.GONE);
                       emoteOption.setVisibility(View.GONE);
                        refreshPicture();
                        break;

                    case R.id.filterPic:
                        filterOption.setVisibility(View.VISIBLE);

                        cropOption.setVisibility(View.GONE);
                        brightnessOption.setVisibility(View.GONE);
                        emoteOption.setVisibility(View.GONE);

                        break;
                    case R.id.brightnessPic:
                        brightnessOption.setVisibility(View.VISIBLE);

                        filterOption.setVisibility(View.GONE);
                        cropOption.setVisibility(View.GONE);
                        emoteOption.setVisibility(View.GONE);

                        refreshPicture();
                        break;
                    case R.id.emotePic:
                        emoteOption.setVisibility(View.VISIBLE);

                        filterOption.setVisibility(View.GONE);
                        brightnessOption.setVisibility(View.GONE);
                        cropOption.setVisibility(View.GONE);

                        refreshPicture();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        cancelViewEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewEdit.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        rotatePic = findViewById(R.id.rotatePic);
        flipPic = findViewById(R.id.flipPic);
        resizePic = findViewById(R.id.resizePic);
        paintPic = findViewById(R.id.paintPic);
        stickerPic = findViewById(R.id.stickerPic);
        textPic= findViewById(R.id.textPic);

        emoteOption= findViewById(R.id.emoteOption);
        cropOption= findViewById(R.id.cropOption);
        filterOption= findViewById(R.id.filterOption);
        brightnessOption= findViewById(R.id.brightnessOption);



        nav_edit_view = findViewById(R.id.navigation_edit_view);
        cancelViewEditBtn = findViewById(R.id.cancelViewEditBtn);
        saveViewEditBtn = findViewById(R.id.saveViewEditBtn);


    }
}