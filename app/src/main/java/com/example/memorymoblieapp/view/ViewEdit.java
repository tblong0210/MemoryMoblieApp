package com.example.memorymoblieapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


import com.example.memorymoblieapp.Brightness;
import com.example.memorymoblieapp.Image;
import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.BrightnessRecViewAdapter;
import com.example.memorymoblieapp.adapter.ImageRecViewAdapter;
import com.example.memorymoblieapp.main.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ViewEdit extends AppCompatActivity {

    private ImageView rotatePic, flipPic, resizePic, paintPic, stickerPic, textPic;
    private LinearLayout emoteOption, cropOption, filterOption, brightnessOption;
    private RecyclerView filterRecView, brightnessRecView;

    private ArrayList<Image> images;
    private ArrayList<Brightness> brightnesses;

    BottomNavigationView nav_edit_view;

    Button cancelViewEditBtn, saveViewEditBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        initViews();
        initOptionActions();

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

        rotatePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewEdit.this, "rotated Image", Toast.LENGTH_SHORT).show();
                handleRotateImage();
            }
        });

        flipPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewEdit.this, "Flip Image", Toast.LENGTH_SHORT).show();
                handleFlipImage();
            }
        });

        resizePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewEdit.this, "Resize Image", Toast.LENGTH_SHORT).show();
                handleResizeImage();
            }
        });

        paintPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewEdit.this, "Draw Image", Toast.LENGTH_SHORT).show();
                handleAddPaintImage();
            }
        });

        stickerPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewEdit.this, "Add sticker Image", Toast.LENGTH_SHORT).show();
                handleAddStickerImage();
            }
        });

        textPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewEdit.this, "Add text Image", Toast.LENGTH_SHORT).show();
                handleAddTextImage();
            }
        });


    }

    private void handleRotateImage(){

    }
    private void handleFlipImage(){

    }
    private void handleResizeImage(){

    }

    private void handleAddPaintImage(){

    }

    private void handleAddStickerImage(){

    }
    private void handleAddTextImage(){

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

        filterRecView= findViewById(R.id.imgRecView);
        //set adapter to imgRecView
        images = new ArrayList<>();
        images.add(new Image("1",R.drawable.image1));
        images.add(new Image("2",R.drawable.image2));
        images.add(new Image("3",R.drawable.image3));
        images.add(new Image("4",R.drawable.image4));
        images.add(new Image("5",R.drawable.image1));
        images.add(new Image("6",R.drawable.image2));
        images.add(new Image("7",R.drawable.image3));
        images.add(new Image("8",R.drawable.image4));
        images.add(new Image("9",R.drawable.image1));
        images.add(new Image("10",R.drawable.image2));
        images.add(new Image("11",R.drawable.image3));
        images.add(new Image("12",R.drawable.image4));

        ImageRecViewAdapter adapterFilter = new ImageRecViewAdapter(this);
        adapterFilter.setImages(images);
        filterRecView.setAdapter(adapterFilter);
        filterRecView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        brightnessRecView= findViewById(R.id.brightnessRecView);
        //set adapter to imgRecView
        brightnesses = new ArrayList<>();
        brightnesses.add(new Brightness("1",R.mipmap.ic_brightness_dark_light));
        brightnesses.add(new Brightness("2",R.mipmap.ic_brightness_dark_light));
        brightnesses.add(new Brightness("3",R.mipmap.ic_brightness_dark_light));
        brightnesses.add(new Brightness("4",R.mipmap.ic_brightness_dark_light));
        brightnesses.add(new Brightness("5",R.mipmap.ic_brightness_dark_light));
        brightnesses.add(new Brightness("6",R.mipmap.ic_brightness_dark_light));
        brightnesses.add(new Brightness("7",R.mipmap.ic_brightness_dark_light));
        brightnesses.add(new Brightness("8",R.mipmap.ic_brightness_dark_light));
        brightnesses.add(new Brightness("9",R.mipmap.ic_brightness_dark_light));
        brightnesses.add(new Brightness("10",R.mipmap.ic_brightness_dark_light));
        brightnesses.add(new Brightness("11",R.mipmap.ic_brightness_dark_light));
        brightnesses.add(new Brightness("12",R.mipmap.ic_brightness_dark_light));

        BrightnessRecViewAdapter adapterBrightness = new BrightnessRecViewAdapter(this);
        adapterBrightness.setBrightnesses(brightnesses);
        brightnessRecView.setAdapter(adapterBrightness);
        brightnessRecView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

    }
}