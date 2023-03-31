package com.example.memorymoblieapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.memorymoblieapp.Brightness;
import com.example.memorymoblieapp.Filter;
import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.BrightnessRecViewAdapter;
import com.example.memorymoblieapp.adapter.FilterRecViewAdapter;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class ViewEdit extends AppCompatActivity {


    private ImageView imgViewEdit;
    private LinearLayout  filterOption;
    private RelativeLayout emoteOption, cropOption, brightnessOption, blurOption;
    private RecyclerView filterRecView, brightnessRecView;

    private TextView viewTxtAdd;
    private SeekBar seekBarBrightnessLevel, seekBarContrast, seekBarShadow, seekBarBlurLevel;

    private ArrayList<Filter> filters;

    private ArrayList<Brightness> brightnesses;

    private Bitmap originImage;


    BottomNavigationView nav_edit_view, nav_crop_option, nav_emote_option, nav_brightness_option;



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
        inflater.inflate(R.menu.menu_view_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.resetViewEdit:
                Toast.makeText(this, "Undo", Toast.LENGTH_SHORT).show();
                refreshPicture();
                break;
            case R.id.saveViewEdit:
                Toast.makeText(this, "Save Picture", Toast.LENGTH_SHORT).show();
                savePicture();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshPicture() {
        imgViewEdit.setImageBitmap(originImage);
        seekBarBrightnessLevel.setProgress(100);
        seekBarContrast.setProgress(0);
        imgViewEdit.setAlpha(1.0f);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(new float[] {
                1, 0, 0, 0, 0,
                0, 1, 0, 0, 0,
                0, 0, 1, 0, 0,
                0, 0, 0, 1, 0
        });
        imgViewEdit.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }
    private void savePicture() {
//        BitmapDrawable drawable = (BitmapDrawable) imgViewEdit.getDrawable();
//        Bitmap createdImage = drawable.getBitmap();
//        File pictureFile = new File("/user/abc", createdImage.toString() + ".jpg");
//        FileOutputStream out = new FileOutputStream(pictureFile);
//        createdImage.compress(Bitmap.CompressFormat.PNG, 100, out);
//        out.flush();
//        out.close();

        BitmapDrawable drawable = (BitmapDrawable) imgViewEdit.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        String filename = "modified_image.jpg";
        File file = new File(getExternalFilesDir(null), filename);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            System.out.println("Image saved to " + file.getAbsolutePath());
            Toast.makeText(ViewEdit.this, "Image saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ViewEdit.this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }

    private void initOptionActions() {
        nav_edit_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cropPic:
                        cropOption.setVisibility(View.VISIBLE);

                        blurOption.setVisibility(View.GONE);
                        filterOption.setVisibility(View.GONE);
                        brightnessOption.setVisibility(View.GONE);
                        emoteOption.setVisibility(View.GONE);
                        break;

                    case R.id.filterPic:
                        filterOption.setVisibility(View.VISIBLE);

                        blurOption.setVisibility(View.GONE);
                        cropOption.setVisibility(View.GONE);
                        brightnessOption.setVisibility(View.GONE);
                        emoteOption.setVisibility(View.GONE);

                        break;
                    case R.id.brightnessPic:
                        brightnessOption.setVisibility(View.VISIBLE);
                        nav_brightness_option.setSelectedItemId(R.id.brightnessLevelPic);

                        blurOption.setVisibility(View.GONE);
                        filterOption.setVisibility(View.GONE);
                        cropOption.setVisibility(View.GONE);
                        emoteOption.setVisibility(View.GONE);

                        break;
                    case R.id.emotePic:
                        emoteOption.setVisibility(View.VISIBLE);

                        blurOption.setVisibility(View.GONE);
                        filterOption.setVisibility(View.GONE);
                        brightnessOption.setVisibility(View.GONE);
                        cropOption.setVisibility(View.GONE);

                        break;
                    case R.id.blurPic:
                        blurOption.setVisibility(View.VISIBLE);

                        emoteOption.setVisibility(View.GONE);
                        filterOption.setVisibility(View.GONE);
                        brightnessOption.setVisibility(View.GONE);
                        cropOption.setVisibility(View.GONE);
                        handleBlurLevel();

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
                        handleRotateImage();
                        break;

                    case R.id.flipHorizontalPic:
                        Toast.makeText(ViewEdit.this, "Flip", Toast.LENGTH_SHORT).show();
                        handleFlipImageHorizontal();
                        break;
                    case R.id.flipVerticalPic:

                        Toast.makeText(ViewEdit.this, "Resize", Toast.LENGTH_SHORT).show();
                        handleFlipImageVertical();
                        break;
                    case R.id.firstResizePic:
                        handleCropImage(9f, 16f);
                        break;

                    case R.id.secondResizePic:
                        handleCropImage(3f, 4f);
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
                        handleAddPaintImage();
                        break;

                    case R.id.stickerPic:
                        Toast.makeText(ViewEdit.this, "Sticker", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.textPic:
                        Toast.makeText(ViewEdit.this, "Text", Toast.LENGTH_SHORT).show();
                        handleAddText("Hello World");
                        break;

                    default:
                        break;
                }
                return true;
            }
        });

        nav_brightness_option.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.brightnessLevelPic:
                        Toast.makeText(ViewEdit.this, "brightness", Toast.LENGTH_SHORT).show();
                        seekBarContrast.setVisibility(View.GONE);
                        seekBarShadow.setVisibility(View.GONE);
                        seekBarBrightnessLevel.setVisibility(View.VISIBLE);
                        handleBrightnessLevel();
                        break;

                    case R.id.contrastPic:
                        Toast.makeText(ViewEdit.this, "contrast", Toast.LENGTH_SHORT).show();
                        seekBarContrast.setVisibility(View.VISIBLE);
                        seekBarShadow.setVisibility(View.GONE);
                        seekBarBrightnessLevel.setVisibility(View.GONE);
                        handleContrastLevel();

                        break;

                    case R.id.shadowPic:
                        Toast.makeText(ViewEdit.this, "Shadow", Toast.LENGTH_SHORT).show();
                        seekBarContrast.setVisibility(View.GONE);
                        seekBarShadow.setVisibility(View.VISIBLE);
                        seekBarBrightnessLevel.setVisibility(View.GONE);
                        handleShadowLevel();
                    default:
                        break;
                }
                return true;
            }
        });


//        imgViewEdit.setImageResource(R.drawable.image1);


    }

    private void handleBlurLevel(){
        seekBarBlurLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float limit = 10f;
                float alpha;
                if(progress < limit){
                   alpha = limit /100;
                }else{
                    alpha = (float) progress / 100;
                }
                imgViewEdit.setAlpha(alpha);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
    private void handleRotateImage(){
        BitmapDrawable drawable = (BitmapDrawable) imgViewEdit.getDrawable();
        Bitmap originalBitmap = drawable.getBitmap();
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
        imgViewEdit.setImageBitmap(rotatedBitmap);
        //originalBitmap.recycle();

    }
    private void handleFlipImageHorizontal(){
        BitmapDrawable drawable = (BitmapDrawable) imgViewEdit.getDrawable();
        Bitmap originalBitmap = drawable.getBitmap();
        Matrix matrix = new Matrix();
        matrix.setScale(-1, 1);
        matrix.postTranslate(originalBitmap.getWidth(), 0);

        Bitmap flippedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
        imgViewEdit.setImageBitmap(flippedBitmap);
        //originalBitmap.recycle();

    }

    private void handleFlipImageVertical(){
        BitmapDrawable drawable = (BitmapDrawable) imgViewEdit.getDrawable();
        Bitmap originalBitmap = drawable.getBitmap();
        Matrix matrix = new Matrix();
        matrix.setScale(1, -1);
        matrix.postTranslate(0, originalBitmap.getHeight());
        Bitmap flippedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
        imgViewEdit.setImageBitmap(flippedBitmap);
        //originalBitmap.recycle();

    }
    private void handleCropImage(float firstRatio, float secondRatio){
        // Get the original dimensions
        int width = originImage.getWidth();
        int height = originImage.getHeight();

        if(firstRatio == 3f && secondRatio == 4f) {
            int newHeight = (int) (width * firstRatio / secondRatio);

            // Calculate the y-coordinate for the top of the new image
            int y = (height - newHeight) / 2;

            // Create a new bitmap with the desired dimensions
            Bitmap croppedBitmap = Bitmap.createBitmap(originImage, 0, y, width, newHeight);
            imgViewEdit.setImageBitmap(croppedBitmap);
        }
        else{
            // Calculate the new height based on a 16:9 aspect ratio
            int newWidth = (int)(width * firstRatio / secondRatio);

            // Calculate the y-coordinate for the top of the new image
            int y = (height - newWidth) / 2;

            // Create a new bitmap with the desired dimensions
            Bitmap croppedBitmap = Bitmap.createBitmap(originImage, y,0 , newWidth, width);
            imgViewEdit.setImageBitmap(croppedBitmap);
        }
        //originalBitmap.recycle();
    }

    private void handleAddPaintImage(){
        BitmapDrawable drawable = (BitmapDrawable) imgViewEdit.getDrawable();
        Bitmap originalBitmap = drawable.getBitmap();
        Bitmap newBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(originalBitmap, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        canvas.drawLine(100, 100, 500, 500, paint);
        imgViewEdit.setImageBitmap(newBitmap);

    }

    private void handleAddText(String text){
        viewTxtAdd.setText(text);

        // Set the text color of the TextView object to the desired color
//        viewTxtAdd.setTextColor(Color.RED);
        // Create a Bitmap object with the same size as the ImageView object
        Bitmap bitmap = Bitmap.createBitmap(imgViewEdit.getWidth(), imgViewEdit.getHeight(), Bitmap.Config.ARGB_8888);

// Create a Canvas object with the Bitmap object as its parameter
        Canvas canvas = new Canvas(bitmap);
// Draw the ImageView object onto the Canvas object
        imgViewEdit.draw(canvas);

// Draw the TextView object onto the Canvas object at the desired location
        canvas.drawText(text, 100, 100, viewTxtAdd.getPaint());

// Set the ImageView object to the modified Bitmap object
        imgViewEdit.setImageBitmap(bitmap);
    }

    private void handleBrightnessLevel(){

        seekBarBrightnessLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // calculate brightness value from progress (0-100)
                float brightness = (float) progress / 100;

                // create a color filter with the calculated brightness
                ColorFilter filter = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    filter = new LightingColorFilter(Color.rgb(brightness, brightness, brightness), 0);
                }

                // apply the color filter to the ImageView
                imgViewEdit.setColorFilter(filter);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

    }
    private void handleContrastLevel(){
        seekBarContrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the contrast level of the image
                float contrast = (float)((progress + 100) / 100.0);
                ColorMatrix colorMatrix = new ColorMatrix();
                colorMatrix.set(new float[] {
                        contrast, 0, 0, 0, 0,
                        0, contrast, 0, 0, 0,
                        0, 0, contrast, 0, 0,
                        0, 0, 0, 1, 0
                });
                imgViewEdit.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed
            }
        });

    }

    private void handleShadowLevel(){

    }
    private void handleAddStickerImage(){

    }
    private void handleAddTextImage(){

    }

    private void initViews() {
        imgViewEdit = findViewById(R.id.imgViewEdit);
        Intent intent = getIntent();
        String path = intent.getStringExtra("path_image");
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        imgViewEdit.setImageBitmap(bitmap);
        BitmapDrawable drawable = (BitmapDrawable) imgViewEdit.getDrawable();
        originImage = drawable.getBitmap();

        viewTxtAdd = findViewById(R.id.viewTxtAdd);


        emoteOption = findViewById(R.id.emoteOption);
        cropOption = findViewById(R.id.cropOption);
        filterOption = findViewById(R.id.filterOption);
        brightnessOption = findViewById(R.id.brightnessOption);
        blurOption = findViewById(R.id.blurOption);


        nav_edit_view = findViewById(R.id.navigation_edit_view);
        nav_crop_option = findViewById(R.id.nav_crop_option);
        nav_emote_option = findViewById(R.id.nav_emote_option);
        nav_brightness_option = findViewById(R.id.nav_brightness_option);



        filterRecView = findViewById(R.id.filterRecView);
        //set adapter to imgRecView
        filters = new ArrayList <> ();
        filters.add(new Filter("1", R.drawable.image1));
        filters.add(new Filter("2", R.drawable.image2));
        filters.add(new Filter("3", R.drawable.image3));
        filters.add(new Filter("4", R.drawable.image4));
        filters.add(new Filter("5", R.drawable.image1));
        filters.add(new Filter("6", R.drawable.image2));
        filters.add(new Filter("7", R.drawable.image3));
        filters.add(new Filter("8", R.drawable.image4));
        filters.add(new Filter("9", R.drawable.image1));
        filters.add(new Filter("10", R.drawable.image2));
        filters.add(new Filter("11", R.drawable.image3));
        filters.add(new Filter("12", R.drawable.image4));

        FilterRecViewAdapter adapterFilter = new FilterRecViewAdapter(this);
        adapterFilter.setFilters(filters);
        filterRecView.setAdapter(adapterFilter);
        filterRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        seekBarContrast = findViewById(R.id.seekBarContrast);
        seekBarBrightnessLevel = findViewById(R.id.seekBarBrightnessLevel);
        seekBarShadow = findViewById(R.id.seekBarShadow);
        seekBarBlurLevel = findViewById(R.id.seekBarBlurLevel);

    }
}