package com.example.memorymoblieapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.memorymoblieapp.Brightness;
import com.example.memorymoblieapp.FilterItem;
import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.ColorRecViewAdapter;
import com.example.memorymoblieapp.adapter.FilterRecViewAdapter;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;

import com.example.memorymoblieapp.obj.FilterImageKey;
import com.example.memorymoblieapp.obj.ColorClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ViewEdit extends AppCompatActivity {

    private Canvas mCanvas;
    private Paint mPaint;
    private CardView parent_list_color;

    private ArrayList<String> picturePaths;

    private LinearLayout filterOption;
    private ImageView imgViewEdit, colorItemChosen;
    private RelativeLayout emoteOption, cropOption, brightnessOption;
    private RecyclerView filterRecView, colorRecView;

    private TextView viewTxtAdd;
    private String path_image;
    private SeekBar seekBarBrightnessLevel, seekBarContrast, seekBarBlur;

    private ArrayList<Brightness> brightnesses;
    private ArrayList<FilterItem> filterItems;
    private ArrayList<ColorClass> colors;

    private ColorRecViewAdapter adapterColor;
    private FilterRecViewAdapter adapterFilter;
    private Bitmap originImage;
    BottomNavigationView nav_edit_view, nav_crop_option, nav_emote_option, nav_brightness_option;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] permissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.INTERNET};

        if (!checkPermissionList(permissionList))
            ActivityCompat.requestPermissions(ViewEdit.this, permissionList, 1);

        else {
            initViews();
            initOptionActions();
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            initViews();
            initOptionActions();
        }
    }

    private boolean checkPermissionList(String[] permissionList) {
        int permissionCheck;
        for (String per : permissionList) {
            permissionCheck = ContextCompat.checkSelfPermission(this, per);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) return false;
        }
        return true;
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
                try {
                    savePicture();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshPicture() {
        colorRecView.setVisibility(View.GONE);
        seekBarBrightnessLevel.setProgress(0);
        seekBarContrast.setProgress(100);
        seekBarBlur.setProgress(10);
        imgViewEdit.setImageBitmap(originImage);
    }

    private void savePicture() throws IOException {
        ArrayList<String> getPicturePaths = new ArrayList<>();
        picturePaths = new ArrayList<>();
        getPicturePaths = DataLocalManager.getStringList(KeyData.IMAGE_PATH_LIST.getKey());

        String new_path = path_image;
        ArrayList<Integer> numberSlag = new ArrayList<>();
        for (int i = 0; i < new_path.length(); i++) {
            if (new_path.charAt(i) == '/') {
                numberSlag.add(i);
            }
        }
        new_path = new_path.substring(0, numberSlag.get(numberSlag.size() - 1));
        UUID uuid = UUID.randomUUID(); // Tạo một đối tượng UUID ngẫu nhiên
        String uniqueString = uuid.toString(); // Chuyển UUID thành chuỗi
        new_path = new_path + "/" + uniqueString + ".jpeg";

        if (getPicturePaths != null) {
            getPicturePaths.add(new_path);
            picturePaths.addAll(getPicturePaths);
        }

        // Chuyển đổi ImageView thành Bitmap
        BitmapDrawable drawable = (BitmapDrawable) imgViewEdit.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

// Lưu ảnh vào thư mục trong bộ nhớ trong
        FileOutputStream fileOutputStream = new FileOutputStream(new_path);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        DataLocalManager.saveData(KeyData.IMAGE_PATH_LIST.getKey(), picturePaths);
        Intent intent = new Intent(ViewEdit.this, ViewImage.class);
        intent.putExtra("path_image", new_path);
        startActivity(intent);
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
                        break;

                    case R.id.filterPic:
                        filterOption.setVisibility(View.VISIBLE);
                        cropOption.setVisibility(View.GONE);
                        brightnessOption.setVisibility(View.GONE);
                        emoteOption.setVisibility(View.GONE);

                        Drawable drawable = imgViewEdit.getDrawable();
                        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        drawable.draw(canvas);
                        adapterFilter.setImageFilterView(bitmap);
                        break;
                    case R.id.brightnessPic:
                        brightnessOption.setVisibility(View.VISIBLE);
                        nav_brightness_option.setSelectedItemId(R.id.brightnessLevelPic);

                        filterOption.setVisibility(View.GONE);
                        cropOption.setVisibility(View.GONE);
                        emoteOption.setVisibility(View.GONE);

                        break;
                    case R.id.emotePic:
                        emoteOption.setVisibility(View.VISIBLE);

                        filterOption.setVisibility(View.GONE);
                        brightnessOption.setVisibility(View.GONE);
                        cropOption.setVisibility(View.GONE);

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
                        colorRecView.setVisibility(View.VISIBLE);
                        handleAddPaintImage();
                        break;

                    case R.id.stickerPic:
                        Toast.makeText(ViewEdit.this, "Sticker", Toast.LENGTH_SHORT).show();
                        colorRecView.setVisibility(View.GONE);
                        break;
                    case R.id.textPic:
                        colorRecView.setVisibility(View.GONE);
                        Toast.makeText(ViewEdit.this, "Text", Toast.LENGTH_SHORT).show();
//                        handleAddText();
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
//                        seekBarShadow.setVisibility(View.GONE);
                        seekBarBlur.setVisibility(View.GONE);
                        seekBarBrightnessLevel.setVisibility(View.VISIBLE);
                        handleBrightnessLevel();
                        break;

                    case R.id.contrastPic:
                        Toast.makeText(ViewEdit.this, "contrast", Toast.LENGTH_SHORT).show();
                        seekBarContrast.setVisibility(View.VISIBLE);
//                        seekBarShadow.setVisibility(View.GONE);
                        seekBarBlur.setVisibility(View.GONE);
                        seekBarBrightnessLevel.setVisibility(View.GONE);
                        handleContrastLevel();

                        break;

                    case R.id.blurPic:
                        Toast.makeText(ViewEdit.this, "Blur", Toast.LENGTH_SHORT).show();
                        seekBarContrast.setVisibility(View.GONE);
                        seekBarBlur.setVisibility(View.VISIBLE);
                        seekBarBrightnessLevel.setVisibility(View.GONE);
                        handleBlurLevel();
                    default:
                        break;
                }
                return true;
            }
        });


//        imgViewEdit.setImageResource(R.drawable.image1);


    }

    private void handleBlurLevel() {
        Bitmap originalBitmap = ((BitmapDrawable) imgViewEdit.getDrawable()).getBitmap();
        Bitmap blurredBitmap = originalBitmap.copy(originalBitmap.getConfig(), true);
        imgViewEdit.setImageBitmap(blurredBitmap);


        seekBarBlur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                float blurRadius = (float) progress / 100.0f * 25.0f;
                if (blurredBitmap != null) {
                    blurBitmap(originalBitmap, blurredBitmap, blurRadius);
                    imgViewEdit.setImageBitmap(blurredBitmap);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void blurBitmap(Bitmap originalBitmap, Bitmap blurredBitmap, float blurRadius) {
        RenderScript rs = RenderScript.create(this);
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation input = Allocation.createFromBitmap(rs, originalBitmap);
        Allocation output = Allocation.createFromBitmap(rs, blurredBitmap);
        script.setRadius(blurRadius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(blurredBitmap);
    }

    private void handleRotateImage() {
        BitmapDrawable drawable = (BitmapDrawable) imgViewEdit.getDrawable();
        Bitmap originalBitmap = drawable.getBitmap();
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
        imgViewEdit.setImageBitmap(rotatedBitmap);
        //originalBitmap.recycle();

    }

    private void handleFlipImageHorizontal() {
        BitmapDrawable drawable = (BitmapDrawable) imgViewEdit.getDrawable();
        Bitmap originalBitmap = drawable.getBitmap();
        Matrix matrix = new Matrix();
        matrix.setScale(-1, 1);
        matrix.postTranslate(originalBitmap.getWidth(), 0);

        Bitmap flippedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
        imgViewEdit.setImageBitmap(flippedBitmap);
        //originalBitmap.recycle();

    }

    private void handleFlipImageVertical() {
        BitmapDrawable drawable = (BitmapDrawable) imgViewEdit.getDrawable();
        Bitmap originalBitmap = drawable.getBitmap();
        Matrix matrix = new Matrix();
        matrix.setScale(1, -1);
        matrix.postTranslate(0, originalBitmap.getHeight());
        Bitmap flippedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
        imgViewEdit.setImageBitmap(flippedBitmap);
        //originalBitmap.recycle();

    }

    private void handleCropImage(float firstRatio, float secondRatio) {

        int width = originImage.getWidth();
        int height = originImage.getHeight();

        if (firstRatio == 3f && secondRatio == 4f) {
            int newHeight = (int) (width * firstRatio / secondRatio);

            // Calculate the y-coordinate for the top of the new image
            int y = (height - newHeight) / 2;
            // Create a new bitmap with the desired dimensions
            Bitmap croppedBitmap = Bitmap.createBitmap(originImage, 0, y, width, newHeight);
            imgViewEdit.setImageBitmap(croppedBitmap);
        } else {
            int originalWidth = originImage.getWidth();
            int originalHeight = originImage.getHeight();
            int newWidth = originalWidth;
            int newHeight = originalWidth * 16 / 9;
            if (newHeight > originalHeight) {
                newHeight = originalHeight;
                newWidth = originalHeight * 9 / 16;
            }
            int left = (originalWidth - newWidth) / 2;
            int top = (originalHeight - newHeight) / 2;

            Bitmap croppedImage = Bitmap.createBitmap(originImage, left, top, newWidth, newHeight);
            imgViewEdit.setImageBitmap(croppedImage);

        }
        //originalBitmap.recycle();
    }

    private void handleAddPaintImage() {
        BitmapDrawable drawable = (BitmapDrawable) imgViewEdit.getDrawable();
        Bitmap bitmap = drawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        imgViewEdit.setImageBitmap(bitmap);

        imgViewEdit.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        Canvas canvas = new Canvas(bitmap);
                        Paint paint = new Paint();
                        paint.setColor(adapterColor.getColorChosen());
                        canvas.drawCircle(x, y, 10, paint);
                        imgViewEdit.invalidate();
                        break;
                }
                return true;
            }
        });
    }

    private void handleAddText(String text) {
        //viewTxtAdd.setText(text);

        // Set the text color of the TextView object to the desired color
//        viewTxtAdd.setTextColor(Color.RED);
        // Create a Bitmap object with the same size as the ImageView object
        Bitmap bitmap = Bitmap.createBitmap(imgViewEdit.getWidth(), imgViewEdit.getHeight(), Bitmap.Config.ARGB_8888);

// Create a Canvas object with the Bitmap object as its parameter
        Canvas canvas = new Canvas(bitmap);
// Draw the ImageView object onto the Canvas object
        imgViewEdit.draw(canvas);

// Draw the TextView object onto the Canvas object at the desired location
        // canvas.drawText(text, 100, 100, viewTxtAdd.getPaint());

// Set the ImageView object to the modified Bitmap object
        imgViewEdit.setImageBitmap(bitmap);
    }

    private void handleBrightnessLevel() {
        Bitmap tempBitmap = ((BitmapDrawable) imgViewEdit.getDrawable()).getBitmap();
        seekBarBrightnessLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                // Lấy ảnh từ file hoặc từ ImageView đã hiển thị
                Bitmap originalBitmap = tempBitmap;

                // Tạo một Bitmap mới từ ảnh gốc
                Bitmap newBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), originalBitmap.getConfig());

                // Thay đổi độ sáng của từng pixel trong ảnh
                for (int i = 0; i < originalBitmap.getWidth(); i++) {
                    for (int j = 0; j < originalBitmap.getHeight(); j++) {
                        int pixel = originalBitmap.getPixel(i, j);
                        int alpha = Color.alpha(pixel);
                        int red = Color.red(pixel) + progress;
                        int green = Color.green(pixel) + progress;
                        int blue = Color.blue(pixel) + progress;

                        // Giới hạn giá trị của red, green và blue trong khoảng từ 0 đến 255
                        red = Math.min(255, Math.max(0, red));
                        green = Math.min(255, Math.max(0, green));
                        blue = Math.min(255, Math.max(0, blue));

                        int newPixel = Color.argb(alpha, red, green, blue);
                        newBitmap.setPixel(i, j, newPixel);
                    }
                }

                // Hiển thị ảnh mới trên ImageView
                imgViewEdit.setImageBitmap(newBitmap);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    private void handleContrastLevel() {
        Bitmap tempBitmap = ((BitmapDrawable) imgViewEdit.getDrawable()).getBitmap();
        seekBarContrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Lấy ảnh từ file hoặc từ ImageView đã hiển thị
                Bitmap originalBitmap = tempBitmap;

                // Tạo một Bitmap mới từ ảnh gốc
                Bitmap newBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), originalBitmap.getConfig());

                // Thay đổi độ tương phản của từng pixel trong ảnh
                float contrast = (float) progress / 100.0f;
                contrast = contrast * contrast;
                for (int i = 0; i < originalBitmap.getWidth(); i++) {
                    for (int j = 0; j < originalBitmap.getHeight(); j++) {
                        int pixel = originalBitmap.getPixel(i, j);
                        int alpha = Color.alpha(pixel);
                        int red = Color.red(pixel);
                        int green = Color.green(pixel);
                        int blue = Color.blue(pixel);

                        // Thực hiện tính toán độ tương phản mới cho red, green và blue
                        red = (int) ((((red / 255.0f) - 0.5f) * contrast + 0.5f) * 255.0f);
                        green = (int) ((((green / 255.0f) - 0.5f) * contrast + 0.5f) * 255.0f);
                        blue = (int) ((((blue / 255.0f) - 0.5f) * contrast + 0.5f) * 255.0f);

                        // Giới hạn giá trị của red, green và blue trong khoảng từ 0 đến 255
                        red = Math.min(255, Math.max(0, red));
                        green = Math.min(255, Math.max(0, green));
                        blue = Math.min(255, Math.max(0, blue));
                        // Tạo một pixel mới từ các giá trị màu sắc đã được thay đổi
                        int newPixel = Color.argb(alpha, red, green, blue);

                        // Đặt pixel mới vào Bitmap mới
                        newBitmap.setPixel(i, j, newPixel);
                    }
                }

                // Hiển thị Bitmap mới lên ImageView hoặc lưu Bitmap mới vào file
                imgViewEdit.setImageBitmap(newBitmap);
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

    //    private void handleShadowLevel(){
//        seekBarShadow.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                BitmapDrawable drawable = (BitmapDrawable) imgViewEdit.getDrawable();
//                Bitmap originalBitmap = drawable.getBitmap();
//                imgViewEdit.setImageBitmap(addShadow(originalBitmap, progress));
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {}
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {}
//        });
//
//    }
    private Bitmap addShadow(Bitmap bitmap, int shadowValue) {
        Log.d("Shadow:", String.valueOf(shadowValue));
        // Tạo một Bitmap mới để chứa ảnh đã đổ bóng
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // Tạo một Canvas mới để vẽ ảnh đã đổ bóng lên Bitmap mới
        Canvas canvas = new Canvas(output);

        // Tạo một Paint mới để vẽ đổ bóng
        Paint shadowPaint = new Paint();
        shadowPaint.setColor(Color.BLACK);
        shadowPaint.setAlpha(shadowValue);

        // Tính toán độ mờ dựa trên giá trị từ seekbar
        int blurRadius = (int) (shadowValue / 2.55);

        // Tạo một đổ bóng bằng cách tạo một mảnh hình chữ nhật bo tròn với một màu đen và độ mờ
        RectF shadowRect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight() + 20);
        canvas.drawRoundRect(shadowRect, 20, 20, shadowPaint);

        // Tạo một đối tượng BlurMaskFilter để áp dụng hiệu ứng blur
        BlurMaskFilter blurMaskFilter = new BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL);

        // Áp dụng hiệu ứng blur vào Paint
        shadowPaint.setMaskFilter(blurMaskFilter);

        // Vẽ ảnh

        canvas.drawBitmap(bitmap, 0, 0, null);

        // Trả về Bitmap mới chứa ảnh và đổ bóng
        return output;

    }

    private void handleAddStickerImage() {
    }

    private void handleAddTextImage() {

    }

    private void initViews() {
        parent_list_color = findViewById(R.id.parent_list_color);
        imgViewEdit = findViewById(R.id.imgViewEdit);
        Intent intent = getIntent();
        path_image = intent.getStringExtra("path_image");
        Bitmap bitmap = BitmapFactory.decodeFile(path_image);
        imgViewEdit.setImageBitmap(bitmap);
        BitmapDrawable drawable = (BitmapDrawable) imgViewEdit.getDrawable();
        originImage = drawable.getBitmap();

//        viewTxtAdd = findViewById(R.id.viewTxtAdd);


        emoteOption = findViewById(R.id.emoteOption);
        cropOption = findViewById(R.id.cropOption);
        filterOption = findViewById(R.id.filterOption);
        brightnessOption = findViewById(R.id.brightnessOption);


        nav_edit_view = findViewById(R.id.navigation_edit_view);
        nav_crop_option = findViewById(R.id.nav_crop_option);
        nav_emote_option = findViewById(R.id.nav_emote_option);
        nav_brightness_option = findViewById(R.id.nav_brightness_option);


        filterRecView = findViewById(R.id.filterRecView);
        setFilter(FilterImageKey.warm);

        //set adapter to imgRecView
        filterItems = new ArrayList<>();
        setAllFilters();

        adapterFilter = new FilterRecViewAdapter(this, originImage);
        adapterFilter.setFilters(filterItems);
        filterRecView.setAdapter(adapterFilter);
        filterRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        colorRecView = findViewById(R.id.colorRecView);
        //set adapter to imgRecView
        colors = new ArrayList<>();
        colors.add(new ColorClass("RED", Color.RED));
        colors.add(new ColorClass("GREEN", Color.GREEN));
        colors.add(new ColorClass("BLUE", Color.BLUE));
        colors.add(new ColorClass("YELLOW", Color.YELLOW));
        colors.add(new ColorClass("GRAY", Color.GRAY));
        colors.add(new ColorClass("BLACK", Color.BLACK));
        colors.add(new ColorClass("WHITE", Color.WHITE));

        adapterColor = new ColorRecViewAdapter(this);
        adapterColor.setColors(colors);
        colorRecView.setAdapter(adapterColor);
        colorRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        seekBarContrast = findViewById(R.id.seekBarContrast);
        seekBarBrightnessLevel = findViewById(R.id.seekBarBrightnessLevel);
        seekBarBlur = findViewById(R.id.seekBarBlur);
    }

    private void setAllFilters() {
        filterItems.add(setFilter(""));
        filterItems.add(setFilter(FilterImageKey.warm));
        filterItems.add(setFilter(FilterImageKey.cool));
        filterItems.add(setFilter(FilterImageKey.fade));
        filterItems.add(setFilter(FilterImageKey.nacreous));
        filterItems.add(setFilter(FilterImageKey.soft));
        filterItems.add(setFilter(FilterImageKey.flower));
        filterItems.add(setFilter(FilterImageKey.faded));
        filterItems.add(setFilter(FilterImageKey.gray));
    }

    private FilterItem setFilter(String filter) {
        FilterItem filterItem = new FilterItem();

        if (filter.equals(FilterImageKey.warm)) {
            float[] colorMatrixWarm = {
                    1.2f, 0.0f, 0.0f, 0.0f, 0.0f,
                    0.0f, 1.1f, 0.0f, 0.0f, 0.0f,
                    0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                    0.0f, 0.0f, 0.0f, 1.0f, 0.0f
            };
            filterItem.setColorMatrix(colorMatrixWarm);
            filterItem.setName(getResources().getString(R.string.filter_warm));
        } else if (filter.equals(FilterImageKey.cool)) {
            float[] colorMatrixCool = {
                    0.8f, 0.0f, 0.0f, 0.0f, 0.0f,
                    0.0f, 1.1f, 0.0f, 0.0f, 0.0f,
                    0.0f, 0.0f, 1.2f, 0.0f, 0.0f,
                    0.0f, 0.0f, 0.0f, 1.0f, 0.0f
            };
            filterItem.setColorMatrix(colorMatrixCool);
            filterItem.setName(getResources().getString(R.string.filter_cool));
        } else if (filter.equals(FilterImageKey.soft)) {
            float[] colorMatrixSoft = {
                    1.1f, 0.0f, 0.0f, 0.0f, -20f,
                    0.0f, 1.1f, 0.0f, 0.0f, -20f,
                    0.0f, 0.0f, 1.1f, 0.0f, -20f,
                    0.0f, 0.0f, 0.0f, 1.0f, 0.0f
            };
            filterItem.setColorMatrix(colorMatrixSoft);
            filterItem.setName(getResources().getString(R.string.filter_soft));
        } else if (filter.equals(FilterImageKey.flower)) {
            float[] colorMatrixFlower = {
                    2.0f, 0.0f, 0.0f, 0.0f, -25f,
                    0.0f, 1.8f, 0.0f, 0.0f, -25f,
                    0.0f, 0.0f, 1.5f, 0.0f, -25f,
                    0.0f, 0.0f, 0.0f, 1.0f, 0.0f
            };
            filterItem.setColorMatrix(colorMatrixFlower);
            filterItem.setName(getResources().getString(R.string.filter_flower));
        } else if (filter.equals(FilterImageKey.gray)) {
            float[] colorMatrixGray = {
                    0.33f, 0.33f, 0.33f, 0.0f, 0.0f,
                    0.33f, 0.33f, 0.33f, 0.0f, 0.0f,
                    0.33f, 0.33f, 0.33f, 0.0f, 0.0f,
                    0.0f, 0.0f, 0.0f, 1.0f, 0.0f
            };
            filterItem.setColorMatrix(colorMatrixGray);
            filterItem.setName(getResources().getString(R.string.filter_gray));
        } else if (filter.equals(FilterImageKey.fade)) {
            float[] colorMatrixFade = {
                    0.5f, 0.5f, 0.5f, 0.0f, 30.0f,
                    0.5f, 0.5f, 0.5f, 0.0f, 30.0f,
                    0.5f, 0.5f, 0.5f, 0.0f, 30.0f,
                    0.0f, 0.0f, 0.0f, 1.0f, 0.0f
            };
            filterItem.setColorMatrix(colorMatrixFade);
            filterItem.setName(getResources().getString(R.string.filter_fade));
        } else if (filter.equals(FilterImageKey.nacreous)) {

            float[] colorMatrixNacreous = {
                    0.5f, 0.5f, 0.5f, 0.0f, 40.0f,
                    0.5f, 0.5f, 0.5f, 0.0f, 40.0f,
                    0.5f, 0.5f, 0.5f, 0.0f, 40.0f,
                    0.0f, 0.0f, 0.0f, 1.0f, 0.0f
            };
            filterItem.setColorMatrix(colorMatrixNacreous);
            filterItem.setName(getResources().getString(R.string.filter_nacreous));
        } else if (filter.equals(FilterImageKey.faded)) {
            float[] colorMatrixFade = {
                    0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
                    0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
                    0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
                    0.0f, 0.0f, 0.0f, 1.0f, 0.0f
            };
            filterItem.setColorMatrix(colorMatrixFade);
            filterItem.setName(getResources().getString(R.string.filter_faded));
        } else {
            float[] colorMatrixWarm = {
                    1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                    0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
                    0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                    0.0f, 0.0f, 0.0f, 1.0f, 0.0f
            };
            return new FilterItem(getResources().getString(R.string.filter_default), colorMatrixWarm, true);
        }


        return filterItem;
    }

    public void setOriginImage(Bitmap bitmap) {
        originImage = bitmap;
    }

    public void setImageViewEdit(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        imgViewEdit.setImageBitmap(bitmap);
    }

}