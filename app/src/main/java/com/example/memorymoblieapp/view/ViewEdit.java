package com.example.memorymoblieapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.memorymoblieapp.Brightness;
import com.example.memorymoblieapp.Filter;
import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.BrightnessRecViewAdapter;
import com.example.memorymoblieapp.adapter.FilterRecViewAdapter;
import com.example.memorymoblieapp.main.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ViewEdit extends AppCompatActivity {

    private ImageView imgViewEdit, rotatePic, flipPic, resizePic, paintPic, stickerPic, textPic;
    private LinearLayout  filterOption, brightnessOption;
    private RelativeLayout emoteOption, cropOption;
    private RecyclerView filterRecView, brightnessRecView;

    private ArrayList<Filter> filters;
    private ArrayList<Brightness> brightnesses;

    BottomNavigationView nav_edit_view, nav_crop_option, nav_emote_option;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_edit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.undoViewEdit:
                Toast.makeText(this, "Undo", Toast.LENGTH_SHORT).show();
                refreshPicture();
            case R.id.saveViewEdit:
                Toast.makeText(this, "Save Picture", Toast.LENGTH_SHORT).show();
                savePicture();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshPicture() {

    }
    private void savePicture(){

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
        nav_crop_option.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.rotatePic:
                        Toast.makeText(ViewEdit.this, "Rotate", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.flipPic:
                        Toast.makeText(ViewEdit.this, "Flip", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.resizePic:
                        Toast.makeText(ViewEdit.this, "Resize", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        nav_emote_option.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.paintPic:
                        Toast.makeText(ViewEdit.this, "Paint", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.stickerPic:
                        Toast.makeText(ViewEdit.this, "Sticker", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.textPic:
                        Toast.makeText(ViewEdit.this, "Text", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }
                return true;
            }
        });



//        imgViewEdit.setImageResource(R.drawable.image1);


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
        imgViewEdit = findViewById(R.id.imgViewEdit);


        emoteOption= findViewById(R.id.emoteOption);
        cropOption= findViewById(R.id.cropOption);
        filterOption= findViewById(R.id.filterOption);
        brightnessOption= findViewById(R.id.brightnessOption);



        nav_edit_view = findViewById(R.id.navigation_edit_view);
        nav_crop_option = findViewById(R.id.nav_crop_option);
        nav_emote_option = findViewById(R.id.nav_emote_option);



        filterRecView= findViewById(R.id.filterRecView);
        //set adapter to imgRecView
        filters = new ArrayList<>();
        filters.add(new Filter("1",R.drawable.image1));
        filters.add(new Filter("2",R.drawable.image2));
        filters.add(new Filter("3",R.drawable.image3));
        filters.add(new Filter("4",R.drawable.image4));
        filters.add(new Filter("5",R.drawable.image1));
        filters.add(new Filter("6",R.drawable.image2));
        filters.add(new Filter("7",R.drawable.image3));
        filters.add(new Filter("8",R.drawable.image4));
        filters.add(new Filter("9",R.drawable.image1));
        filters.add(new Filter("10",R.drawable.image2));
        filters.add(new Filter("11",R.drawable.image3));
        filters.add(new Filter("12",R.drawable.image4));

        FilterRecViewAdapter adapterFilter = new FilterRecViewAdapter(this);
        adapterFilter.setFilters(filters);
        filterRecView.setAdapter(adapterFilter);
        filterRecView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        brightnessRecView= findViewById(R.id.brightnessRecView);
        //set adapter to brightnessRecView
        brightnesses = new ArrayList<>();
        brightnesses.add(new Brightness("1",R.mipmap.ic_brightness_level));
        brightnesses.add(new Brightness("2",R.mipmap.ic_brightness_contrast));
        brightnesses.add(new Brightness("3",R.mipmap.ic_contrast));
        brightnesses.add(new Brightness("4",R.mipmap.ic_shadow));
        brightnesses.add(new Brightness("5",R.mipmap.ic_brightness_level));
        brightnesses.add(new Brightness("6",R.mipmap.ic_brightness_contrast));
        brightnesses.add(new Brightness("7",R.mipmap.ic_contrast));
        brightnesses.add(new Brightness("8",R.mipmap.ic_shadow));
        brightnesses.add(new Brightness("9",R.mipmap.ic_brightness_level));
        brightnesses.add(new Brightness("10",R.mipmap.ic_brightness_contrast));
        brightnesses.add(new Brightness("11",R.mipmap.ic_contrast));
        brightnesses.add(new Brightness("12",R.mipmap.ic_shadow));

        BrightnessRecViewAdapter adapterBrightness = new BrightnessRecViewAdapter(this);
        adapterBrightness.setBrightnesses(brightnesses);
        brightnessRecView.setAdapter(adapterBrightness);
        brightnessRecView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

    }
}