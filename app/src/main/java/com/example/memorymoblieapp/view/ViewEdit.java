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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.memorymoblieapp.FilterItem;
import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.ColorRecViewAdapter;
import com.example.memorymoblieapp.adapter.FilterRecViewAdapter;
import com.example.memorymoblieapp.adapter.StickerRecViewAdapter;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;

import com.example.memorymoblieapp.obj.FilterImageKey;
import com.example.memorymoblieapp.obj.ColorClass;
import com.example.memorymoblieapp.obj.Sticker;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ViewEdit extends AppCompatActivity {

    private boolean isMouseMoving = false;
    private CardView parent_list_color;

    private ArrayList<String> picturePaths;

    private LinearLayout filterOption;
    private ImageView imgViewEdit;
    private RelativeLayout emoteOption, cropOption, brightnessOption, textOption;
    private RecyclerView filterRecView, colorRecView, colorTxtRecView, stickerRecView;

    private EditText edtTxtInput;

    private String path_image;
    private SeekBar seekBarBrightnessLevel, seekBarContrast, seekBarBlur, seekBarSize, seekBarSticker;

    private ArrayList<FilterItem> filterItems;
    private ArrayList<ColorClass> colors, text_colors;

    private ArrayList<Sticker> stickers;

    private ColorRecViewAdapter adapterColor, adapterTxtColor;
    private FilterRecViewAdapter adapterFilter;

    private Boolean CROPPED_3_4 = false, CROPPED_16_9 = false;

    private StickerRecViewAdapter adapterSticker;
    private Bitmap originImage, mutableBitmap;
    private ArrayList<Bitmap> previousBitmaps;
    BottomNavigationView nav_edit_view, nav_crop_option, nav_emote_option, nav_brightness_option;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Boolean isThemeDark = DataLocalManager.getBooleanData(KeyData.DARK_MODE.getKey());
        isThemeDark = isThemeDark != null && isThemeDark;

        setTheme(isThemeDark ? R.style.ThemeDark_MemoryMobileApp : R.style.Theme_MemoryMobileApp);
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
                refreshPicture();
                break;
            case R.id.saveViewEdit:
                try {
                    savePicture();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case R.id.backViewEdit:
                backPreviousPicture();
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
        mutableBitmap = originImage.copy(Bitmap.Config.ARGB_8888, true);

        seekBarSticker.setProgress(100);
        seekBarSize.setProgress(30);
        edtTxtInput.setText("");
        if (mutableBitmap != null) {
            mutableBitmap.recycle();
            mutableBitmap = originImage.copy(Bitmap.Config.ARGB_8888, true);
        }
        imgViewEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        previousBitmaps.clear();
        imgViewEdit.setImageBitmap(originImage);
    }
    private void backPreviousPicture() {
        if (previousBitmaps.size() > 0) {
            previousBitmaps.remove(previousBitmaps.size() - 1);
            if (previousBitmaps.size() == 0) {
                imgViewEdit.setImageBitmap(originImage);
                previousBitmaps.clear();
            } else {
                imgViewEdit.setImageBitmap(previousBitmaps.get(previousBitmaps.size() - 1));
            }
        }
        else{
            Toast.makeText(this, R.string.disable_back_edit_view, Toast.LENGTH_SHORT).show();
        }
    }

    private void savePicture() throws IOException {
        ArrayList<String> getPicturePaths = new ArrayList<>();
        picturePaths = new ArrayList<>();
        getPicturePaths = DataLocalManager.getStringList(KeyData.IMAGE_PATH_LIST.getKey());

        String new_path = path_image;
        ArrayList<Integer> numberSlag = new ArrayList<>();
        // Tính số thư mục trong đường dẫn chứa ảnh cũ
        for (int i = 0; i < new_path.length(); i++) {
            if (new_path.charAt(i) == '/') {
                numberSlag.add(i);
            }
        }
        // Cắt đường dẫn trong thư mục cuối cùng
        new_path = new_path.substring(0, numberSlag.get(numberSlag.size() - 1));
        //Tạo một tên file mới và gắn tên file đó với đường dẫn đến thư mục cũ
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

        // Điều hướng các chức năng trong edit view
        nav_edit_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cropPic:
                        cropOption.setVisibility(View.VISIBLE);

                        textOption.setVisibility(View.GONE);
                        filterOption.setVisibility(View.GONE);
                        brightnessOption.setVisibility(View.GONE);
                        emoteOption.setVisibility(View.GONE);
                        break;

                    case R.id.filterPic:
                        filterOption.setVisibility(View.VISIBLE);
                        cropOption.setVisibility(View.GONE);
                        brightnessOption.setVisibility(View.GONE);
                        emoteOption.setVisibility(View.GONE);
                        textOption.setVisibility(View.GONE);

                        Drawable drawable = imgViewEdit.getDrawable();
                        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        drawable.draw(canvas);
                        adapterFilter.setImageFilterView(bitmap);
                        previousBitmaps.add(getOriginalBitmap(imgViewEdit));
                        break;
                    case R.id.brightnessPic:
                        brightnessOption.setVisibility(View.VISIBLE);
                        nav_brightness_option.setSelectedItemId(R.id.brightnessLevelPic);

                        textOption.setVisibility(View.GONE);
                        filterOption.setVisibility(View.GONE);
                        cropOption.setVisibility(View.GONE);
                        emoteOption.setVisibility(View.GONE);

                        break;
                    case R.id.emotePic:
                        emoteOption.setVisibility(View.VISIBLE);

                        if (nav_emote_option.getSelectedItemId() == R.id.paintPic) {
                            colorRecView.setVisibility(View.VISIBLE);
                            handleAddPaintImage();
                        } else if (nav_emote_option.getSelectedItemId() == R.id.stickerPic) {
                            handleAddStickerImage();
                        }

                        textOption.setVisibility(View.GONE);
                        filterOption.setVisibility(View.GONE);
                        brightnessOption.setVisibility(View.GONE);
                        cropOption.setVisibility(View.GONE);

                        break;
                    case R.id.textPic:
                        textOption.setVisibility(View.VISIBLE);

                        emoteOption.setVisibility(View.GONE);
                        filterOption.setVisibility(View.GONE);
                        brightnessOption.setVisibility(View.GONE);
                        cropOption.setVisibility(View.GONE);

                        handleAddTextImage();
                    default:
                        break;
                }
                return true;
            }
        });


        // Chọn chức năng trong cắt ảnh
        nav_crop_option.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.rotatePic:
                        handleRotateImage();
                        break;

                    case R.id.flipHorizontalPic:
                        handleFlipImageHorizontal();
                        break;
                    case R.id.flipVerticalPic:
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

        // Chọn chức năng trong emote
        nav_emote_option.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.paintPic:
                        colorRecView.setVisibility(View.VISIBLE);

                        stickerRecView.setVisibility(View.GONE);
                        seekBarSticker.setVisibility(View.GONE);
                        handleAddPaintImage();
                        break;

                    case R.id.stickerPic:
                        colorRecView.setVisibility(View.GONE);

                        stickerRecView.setVisibility(View.VISIBLE);
                        seekBarSticker.setVisibility(View.VISIBLE);
                        handleAddStickerImage();
                        break;

                    default:
                        break;
                }
                return true;
            }
        });

        // Chọn chức năng trong chỉnh độ sáng ảnh
        nav_brightness_option.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.brightnessLevelPic:
                        seekBarContrast.setVisibility(View.GONE);
                        seekBarBlur.setVisibility(View.GONE);
                        seekBarBrightnessLevel.setVisibility(View.VISIBLE);
                        handleBrightnessLevel();
                        break;

                    case R.id.contrastPic:
                        seekBarContrast.setVisibility(View.VISIBLE);
                        seekBarBlur.setVisibility(View.GONE);
                        seekBarBrightnessLevel.setVisibility(View.GONE);
                        handleContrastLevel();

                        break;

                    case R.id.blurPic:
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

    }

    private void handleBlurLevel() {
        Bitmap originalBitmap = getOriginalBitmap(imgViewEdit);
        Bitmap blurredBitmap = originalBitmap.copy(originalBitmap.getConfig(), true);
        imgViewEdit.setImageBitmap(blurredBitmap);

        // Điều chỉnh độ mờ của ảnh dựa trên thanh seekbar
        seekBarBlur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                float blurRadius = (float) progress / 100.0f * 25.0f;
                if (blurredBitmap != null) {
                    blurBitmap(originalBitmap, blurredBitmap, blurRadius);
                    imgViewEdit.setImageBitmap(blurredBitmap);
                    previousBitmaps.add(getOriginalBitmap(imgViewEdit));
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

    // Xoay ảnh
    private void handleRotateImage() {
        Bitmap originalBitmap = getOriginalBitmap(imgViewEdit);
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
        imgViewEdit.setImageBitmap(rotatedBitmap);
        previousBitmaps.add(getOriginalBitmap(imgViewEdit));
    }

    // Lật ảnh theo chiều ngang
    private void handleFlipImageHorizontal() {
        Bitmap originalBitmap = getOriginalBitmap(imgViewEdit);
        Matrix matrix = new Matrix();
        matrix.setScale(-1, 1);
        matrix.postTranslate(originalBitmap.getWidth(), 0);

        Bitmap flippedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
        imgViewEdit.setImageBitmap(flippedBitmap);
        //originalBitmap.recycle();
        previousBitmaps.add(getOriginalBitmap(imgViewEdit));
    }

    // Lật ảnh theo chiều dọc
    private void handleFlipImageVertical() {
        Bitmap originalBitmap = getOriginalBitmap(imgViewEdit);
        Matrix matrix = new Matrix();
        matrix.setScale(1, -1);
        matrix.postTranslate(0, originalBitmap.getHeight());
        Bitmap flippedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
        imgViewEdit.setImageBitmap(flippedBitmap);
        previousBitmaps.add(getOriginalBitmap(imgViewEdit));
    }

    // Cắt ảnh
    private void handleCropImage(float firstRatio, float secondRatio) {
        refreshPicture();
        int width = originImage.getWidth();
        int height = originImage.getHeight();

        //Cắt ảnh 3:4
        if (firstRatio == 3f && secondRatio == 4f) {
            int newHeight = (int) (width * firstRatio / secondRatio);


            int y = (height - newHeight) / 2;
            Bitmap croppedBitmap = Bitmap.createBitmap(originImage, 0, y, width, newHeight);
            imgViewEdit.setImageBitmap(croppedBitmap);

        }
        // Cắt ảnh 16:9
        else {
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
        previousBitmaps.add(getOriginalBitmap(imgViewEdit));
    }

    private void handleBrightnessLevel() {
        Bitmap tempBitmap = getOriginalBitmap(imgViewEdit);

        // Chỉnh độ sáng của ảnh dựa trên thanh seekbar
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
                previousBitmaps.add(getOriginalBitmap(imgViewEdit));
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
        Bitmap tempBitmap = getOriginalBitmap(imgViewEdit);

        //Chỉnh độ tương phản của ảnh
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
                previousBitmaps.add(getOriginalBitmap(imgViewEdit));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    private void handleAddPaintImage() {
        BitmapDrawable drawable = (BitmapDrawable) imgViewEdit.getDrawable();
        Bitmap bitmap = drawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        imgViewEdit.setImageBitmap(bitmap);

        // Vẽ ảnh dựa vào việc bắt sự kiện click chuột
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
                        if (adapterColor.getColorChosen() != 0) {
                            paint.setColor(adapterColor.getColorChosen());
                            canvas.drawCircle(x, y, 10, paint);
                            imgViewEdit.invalidate();
                        } else {
                            Toast.makeText(ViewEdit.this, R.string.choose_color_paint, Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return true;
            }
        });
        previousBitmaps.add(getOriginalBitmap(imgViewEdit));

    }

    private void handleAddTextImage() {

        // Thêm text dựa vào việc bắt sự kiện click chuột
        imgViewEdit.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Bitmap bitmap = ((BitmapDrawable) imgViewEdit.getDrawable()).getBitmap();
                mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        Paint paint = new Paint();
                        int Size = seekBarSize.getProgress();
                        int colorText = adapterTxtColor.getColorChosen();
                        if (colorText != 0) {
                            paint.setColor(colorText);
                            paint.setTextSize(Size);
                            Canvas canvas = new Canvas(mutableBitmap);
                            if (edtTxtInput.getText().length() > 0) {
                                String text = edtTxtInput.getText().toString();
                                canvas.drawText(text, x, y, paint);
                                imgViewEdit.setImageBitmap(mutableBitmap);
                                previousBitmaps.add(getOriginalBitmap(imgViewEdit));
                            } else {
                                Toast.makeText(ViewEdit.this, R.string.provide_text, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ViewEdit.this, R.string.provide_text_color, Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return true;
            }
        });

    }

    private void handleAddStickerImage() {

        // Thêm sticker dựa vào việc bắt sự kiện click chuột
        imgViewEdit.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Bitmap originalBitmap = ((BitmapDrawable) imgViewEdit.getDrawable()).getBitmap();
                mutableBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        System.out.println(mutableBitmap);
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        int Size = seekBarSticker.getProgress();

                        Bitmap originalSticker = adapterSticker.getStickerChosen();
                        if (originalSticker != null) {
                            Bitmap scaledSticker = Bitmap.createScaledBitmap(originalSticker, Size, Size, false);

                            Canvas canvas = new Canvas(mutableBitmap);
                            canvas.drawBitmap(mutableBitmap, 0, 0, null);
                            canvas.drawBitmap(scaledSticker, x, y, null);
                            imgViewEdit.setImageBitmap(mutableBitmap);
                            previousBitmaps.add(getOriginalBitmap(imgViewEdit));
                        }
                        else {
                            Toast.makeText(ViewEdit.this, "Please choose your sticker", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return true;
            }
        });
    }


    private void initViews() {
        previousBitmaps = new ArrayList<Bitmap>();

        parent_list_color = findViewById(R.id.parent_list_color);
        imgViewEdit = findViewById(R.id.imgViewEdit);
        Intent intent = getIntent();
        path_image = intent.getStringExtra("path_image");
        Bitmap bitmap = BitmapFactory.decodeFile(path_image);
        imgViewEdit.setImageBitmap(bitmap);
        BitmapDrawable drawable = (BitmapDrawable) imgViewEdit.getDrawable();
        originImage = drawable.getBitmap();

        edtTxtInput = findViewById(R.id.edtTxtInput);


        emoteOption = findViewById(R.id.emoteOption);
        cropOption = findViewById(R.id.cropOption);
        filterOption = findViewById(R.id.filterOption);
        brightnessOption = findViewById(R.id.brightnessOption);
        textOption = findViewById(R.id.textOption);


        nav_edit_view = findViewById(R.id.navigation_edit_view);
        nav_crop_option = findViewById(R.id.nav_crop_option);
        nav_emote_option = findViewById(R.id.nav_emote_option);
        nav_brightness_option = findViewById(R.id.nav_brightness_option);


        filterRecView = findViewById(R.id.filterRecView);
        setFilterRecView();

        colorRecView = findViewById(R.id.colorRecView);
        setColorRecView();

        colorTxtRecView = findViewById(R.id.colorTextRecView);
        setTextColorRecView();

        stickerRecView = findViewById(R.id.stickerRecView);
        setStickerRecView();

        seekBarContrast = findViewById(R.id.seekBarContrast);
        seekBarBrightnessLevel = findViewById(R.id.seekBarBrightnessLevel);
        seekBarBlur = findViewById(R.id.seekBarBlur);
        seekBarSize = findViewById(R.id.seekBarSize);
        seekBarSticker = findViewById(R.id.seekBarSticker);

    }

    private void setFilterRecView() {
        setFilter(FilterImageKey.warm);

        //set adapter to filterRecView
        filterItems = new ArrayList<>();
        setAllFilters();

        adapterFilter = new FilterRecViewAdapter(this, originImage);
        adapterFilter.setFilters(filterItems);
        filterRecView.setAdapter(adapterFilter);
        filterRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    private void setTextColorRecView() {
        //set adapter to textRecView
        text_colors = new ArrayList<>();
        text_colors.add(new ColorClass("BLACK", Color.BLACK));
        text_colors.add(new ColorClass("WHITE", Color.WHITE));
        text_colors.add(new ColorClass("RED", Color.RED));
        text_colors.add(new ColorClass("GREEN", Color.GREEN));
        text_colors.add(new ColorClass("BLUE", Color.BLUE));
        text_colors.add(new ColorClass("YELLOW", Color.YELLOW));
        text_colors.add(new ColorClass("GRAY", Color.GRAY));


        adapterTxtColor = new ColorRecViewAdapter(this);
        adapterTxtColor.setColors(text_colors);
        colorTxtRecView.setAdapter(adapterTxtColor);
        colorTxtRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    private void setColorRecView() {
        //set adapter to colorRecView
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

    }

    private void setStickerRecView() {
        //set adapter to stickerRecView
        stickers = new ArrayList<>();
        stickers.add(new Sticker("Birthday Cake Sticker", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_birthday_cake)));
        stickers.add(new Sticker("Angry Sticker", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_angry_sticker)));
        stickers.add(new Sticker("Thinking Sticker", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_thinking_sticker)));
        stickers.add(new Sticker("Love Sticker", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_love_sticker)));
        stickers.add(new Sticker("Surprise box Sticker", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_surpise_box_sticker)));
        stickers.add(new Sticker("Mocking Sticker", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_mocking_sticker)));
        stickers.add(new Sticker("Hard work Sticker", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_hard_work_sticker)));
        stickers.add(new Sticker("Calendar Sticker", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_calendar_sticker)));
        stickers.add(new Sticker("Toy gun Sticker", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_toy_gun_sticker)));
        stickers.add(new Sticker("Banana Sticker", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_banana_sticker)));
        stickers.add(new Sticker("Fish Bones Sticker", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_fishbones_sticker)));
        stickers.add(new Sticker("Pie Sticker", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_pie_sticker)));
        stickers.add(new Sticker("Hipster Sticker", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_hipster_sticker)));
        stickers.add(new Sticker("Prank Sticker", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_prank_sticker)));
        stickers.add(new Sticker("Ice cream Sticker", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_ice_cream_sticker)));
        stickers.add(new Sticker("Balloons Sticker", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_balloons_sticker)));
        stickers.add(new Sticker("Banner Sticker", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_banner_sticker)));

        adapterSticker = new StickerRecViewAdapter(this);
        adapterSticker.setStickers(stickers);
        stickerRecView.setAdapter(adapterSticker);
        stickerRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


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

    private Bitmap getOriginalBitmap(ImageView img) {
        return ((BitmapDrawable) img.getDrawable()).getBitmap();
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

    public void setImageViewEdit(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        imgViewEdit.setImageBitmap(bitmap);
    }

}