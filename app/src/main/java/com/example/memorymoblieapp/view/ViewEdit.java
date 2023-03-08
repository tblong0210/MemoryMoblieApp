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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.main.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewEdit extends AppCompatActivity {

    private ImageView imgViewEdit, rotateOption;
    private RadioGroup rgCrop, rgFilter, rgBrightness, rgEmote;
    private RadioButton rbCropFirstOption, rbCropSecondOption, rbCropThirdOption, rbCropFourthOption,
            rbFilterFirstOption, rbFilterSecondOption, rbFilterThirdOption,
            rbBrightntessFirstOption, rbBrightntessSecondOption, rbBrightntessThirdOption,
            rbEmoteFirstOption, rbEmoteSecondOption, rbEmoteThirdOption;

    BottomNavigationView nav_edit_view;

    Button cancelViewEditBtn, saveViewEditBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit);

        initViews();
        initOptionActions();

        handleRgCropPicture();
        handleRgFilteringPicture();

        rotateOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ViewEdit.this, "Rotate Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void refreshPicture(){

    }

    private void handleRgCropPicture(){
        int checkedButton = rgCrop.getCheckedRadioButtonId();
        switch (checkedButton) {
            case R.id.rbCropFirstOption:
                Toast.makeText(ViewEdit.this, "Default", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rbCropSecondOption:
                Toast.makeText(ViewEdit.this, "1:1", Toast.LENGTH_SHORT).show();
                break;

            case R.id.rbCropThirdOption:
                Toast.makeText(ViewEdit.this, "3:4", Toast.LENGTH_SHORT).show();

                break;

            case R.id.rbCropFourthOption:
                Toast.makeText(ViewEdit.this, "4:16", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

            rgCrop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbCropFirstOption:
                        Toast.makeText(ViewEdit.this, "Default", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rbCropSecondOption:
                        Toast.makeText(ViewEdit.this, "1:1", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.rbCropThirdOption:
                        Toast.makeText(ViewEdit.this, "3:4", Toast.LENGTH_SHORT).show();

                        break;

                    case R.id.rbCropFourthOption:
                        Toast.makeText(ViewEdit.this, "4:16", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;

                }
            }
        });
    }

    private void handleRgFilteringPicture(){
        int checkedButton = rgFilter.getCheckedRadioButtonId();
        switch (checkedButton) {
            case R.id.rbFilterFirstOption:
                Toast.makeText(ViewEdit.this, "Free", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rbFilterSecondOption:
                Toast.makeText(ViewEdit.this, "Ruc rp", Toast.LENGTH_SHORT).show();
                break;

            case R.id.rbFilterThirdOption:
                Toast.makeText(ViewEdit.this, "Sac hong", Toast.LENGTH_SHORT).show();

                break;
            default:
                break;

        }

        rgFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbFilterFirstOption:
                        Toast.makeText(ViewEdit.this, "Free", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rbFilterSecondOption:
                        Toast.makeText(ViewEdit.this, "Ruc rp", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.rbFilterThirdOption:
                        Toast.makeText(ViewEdit.this, "Sac hong", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initOptionActions(){
        nav_edit_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.cropPic:
                        rgCrop.clearCheck();
                        rgCrop.setVisibility(View.VISIBLE);

                        rgFilter.clearCheck();
                        rgFilter.setVisibility(View.GONE);


                        rgBrightness.clearCheck();
                        rgBrightness.setVisibility(View.GONE);


                        rgEmote.clearCheck();
                        rgEmote.setVisibility(View.GONE);



//                rotateOption.setVisibility(View.VISIBLE);
//                rotatePic.setEnabled(false);
//                filterPic.getResources().getColor(R.color.black);

                        refreshPicture();
                        break;

                    case R.id.filterPic:
                        rgFilter.clearCheck();
                        rgFilter.setVisibility(View.VISIBLE);


                        rgCrop.clearCheck();
                        rgCrop.setVisibility(View.GONE);


                        rgBrightness.clearCheck();
                        rgBrightness.setVisibility(View.GONE);


                        rgEmote.clearCheck();
                        rgEmote.setVisibility(View.GONE);

                        break;
                    case R.id.brightnessPic:
                        rgBrightness.clearCheck();
                        rgBrightness.setVisibility(View.VISIBLE);


                        rgCrop.clearCheck();
                        rgCrop.setVisibility(View.GONE);

                        rgFilter.clearCheck();
                        rgFilter.setVisibility(View.GONE);

                        rgEmote.clearCheck();
                        rgEmote.setVisibility(View.GONE);


//                rotateOption.setVisibility(View.VISIBLE);
//                rotatePic.setEnabled(false);
//                filterPic.getResources().getColor(R.color.black);

                        refreshPicture();
                        break;
                    case R.id.emotePic:
                        rgEmote.clearCheck();
                        rgEmote.setVisibility(View.VISIBLE);


                        rgCrop.clearCheck();
                        rgCrop.setVisibility(View.GONE);


                        rgBrightness.clearCheck();
                        rgBrightness.setVisibility(View.GONE);


                        rgFilter.clearCheck();
                        rgFilter.setVisibility(View.GONE);



//                rotateOption.setVisibility(View.VISIBLE);
//                rotatePic.setEnabled(false);
//                filterPic.getResources().getColor(R.color.black);

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
        imgViewEdit = findViewById(R.id.imgViewEdit);
        rotateOption = findViewById(R.id.rotateOption);

        rgCrop = findViewById(R.id.rgCrop);
        rgFilter = findViewById(R.id.rgFilter);
        rgEmote = findViewById(R.id.rgEmote);
        rgBrightness = findViewById(R.id.rgBrightness);

        rbCropFirstOption = findViewById(R.id.rbCropFirstOption);
        rbCropSecondOption = findViewById(R.id.rbCropSecondOption);
        rbCropThirdOption = findViewById(R.id.rbCropThirdOption);
        rbCropFourthOption = findViewById(R.id.rbCropFourthOption);

        rbFilterFirstOption = findViewById(R.id.rbFilterFirstOption);
        rbFilterSecondOption = findViewById(R.id.rbFilterSecondOption);
        rbFilterThirdOption = findViewById(R.id.rbFilterThirdOption);

        rbBrightntessFirstOption= findViewById(R.id.rbBrightnessFirstOption);
        rbBrightntessSecondOption = findViewById(R.id.rbBrightnessSecondOption);
        rbBrightntessThirdOption = findViewById(R.id.rbBrightnessThirdOption);

        rbEmoteFirstOption = findViewById(R.id.rbEmoteFirstOption);
        rbEmoteSecondOption = findViewById(R.id.rbEmoteSecondOption);
        rbEmoteThirdOption = findViewById(R.id.rbEmoteThirdOption);

        nav_edit_view= findViewById(R.id.navigation_edit_view);
        cancelViewEditBtn=findViewById(R.id.cancelViewEditBtn);
        saveViewEditBtn=findViewById(R.id.saveViewEditBtn);
    }
}