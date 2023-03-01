package com.example.memorymoblieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ViewEdit extends AppCompatActivity {

    ImageView imgViewEdit, rotateOption;
    RadioGroup rgCut, rgFilter;
    RadioButton rbCutFirstOption, rbCutSecondOption, rbCutThirdOption, rbCutFourthOption, rbFilterFirstOption, rbFilterSecondOption, rbFilterThirdOption;
    Button cutPic, rotatePic, filterPic,saveViewEditBtn,cancelViewEditBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit);

        initViews();
        initOptionActions();

        handleRgCuttingPicture();
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

    private void handleRgCuttingPicture(){
        int checkedButton = rgCut.getCheckedRadioButtonId();
        switch (checkedButton) {
            case R.id.rbCutFirstOption:
                Toast.makeText(ViewEdit.this, "Default", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rbCutSecondOption:
                Toast.makeText(ViewEdit.this, "1:1", Toast.LENGTH_SHORT).show();
                break;

            case R.id.rbCutThirdOption:
                Toast.makeText(ViewEdit.this, "3:4", Toast.LENGTH_SHORT).show();

                break;

            case R.id.rbCutFourthOption:
                Toast.makeText(ViewEdit.this, "4:16", Toast.LENGTH_SHORT).show();
                break;
        }

            rgCut.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbCutFirstOption:
                        Toast.makeText(ViewEdit.this, "Default", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rbCutSecondOption:
                        Toast.makeText(ViewEdit.this, "1:1", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.rbCutThirdOption:
                        Toast.makeText(ViewEdit.this, "3:4", Toast.LENGTH_SHORT).show();

                        break;

                    case R.id.rbCutFourthOption:
                        Toast.makeText(ViewEdit.this, "4:16", Toast.LENGTH_SHORT).show();
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
                }
            }
        });
    }

    private void initOptionActions(){
        rotatePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rgCut.clearCheck();
                rgCut.setVisibility(View.GONE);
                cutPic.setEnabled(true);
                cutPic.getResources().getColor(R.color.white);

                rgFilter.clearCheck();
                rgFilter.setVisibility(View.GONE);
                filterPic.setEnabled(true);
                filterPic.getResources().getColor(R.color.white);

                rotateOption.setVisibility(View.VISIBLE);
                rotatePic.setEnabled(false);
                filterPic.getResources().getColor(R.color.black);

                refreshPicture();

            }
        });

        cutPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rgCut.clearCheck();
                rbCutFirstOption.setChecked(true);
                rgCut.setVisibility(View.VISIBLE);
                cutPic.setEnabled(false);
                cutPic.getResources().getColor(R.color.black);

                rgFilter.clearCheck();
                rgFilter.setVisibility(View.GONE);
                filterPic.setEnabled(true);
                filterPic.getResources().getColor(R.color.white);


                rotateOption.setVisibility(View.GONE);
                rotatePic.setEnabled(true);
                filterPic.getResources().getColor(R.color.white);

                refreshPicture();
            }
        });

        filterPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rgCut.clearCheck();
                rgCut.setVisibility(View.GONE);
                cutPic.setEnabled(true);
                cutPic.getResources().getColor(R.color.white);

                rgFilter.clearCheck();
                rbFilterFirstOption.setChecked(true);
                rgFilter.setVisibility(View.VISIBLE);
                filterPic.setEnabled(false);
                filterPic.getResources().getColor(R.color.black);

                rotateOption.setVisibility(View.GONE);
                rotatePic.setEnabled(true);
                filterPic.getResources().getColor(R.color.white);

                refreshPicture();
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
        rgCut = findViewById(R.id.rgCut);
        rgFilter = findViewById(R.id.rgFilter);
        rbCutFirstOption = findViewById(R.id.rbCutFirstOption);
        rbCutSecondOption = findViewById(R.id.rbCutSecondOption);
        rbCutThirdOption = findViewById(R.id.rbCutThirdOption);
        rbCutFourthOption = findViewById(R.id.rbCutFourthOption);
        rbFilterFirstOption = findViewById(R.id.rbFilterFirstOption);
        rbFilterSecondOption = findViewById(R.id.rbFilterSecondOption);
        rbFilterThirdOption = findViewById(R.id.rbFilterThirdOption);
        cutPic=findViewById(R.id.cutPic);
        rotatePic=findViewById(R.id.rotatePic);
        filterPic=findViewById(R.id.filterPic);
        cancelViewEditBtn=findViewById(R.id.cancelViewEditBtn);
        saveViewEditBtn=findViewById(R.id.saveViewEditBtn);
    }
}