package com.example.memorymoblieapp.view;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import com.example.memorymoblieapp.ControlImage.ViewPagerAdapter;
import com.example.memorymoblieapp.ControlImage.ZoomableViewPager;
import com.example.memorymoblieapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class ViewImage extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    ZoomableViewPager mViewPaper;
    ViewPagerAdapter mViewPaperAdapter;

    private WallpaperManager wallpaperManager;
    private boolean isFavorite = false;
    File[] pictureFiles;
    File pictureFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_image_container);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        initActions();
    }

    private void initViews() {
        Intent intent = getIntent();

        ActivityCompat.requestPermissions(ViewImage.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA, Manifest.permission.INTERNET}, 1);

        pictureFile = new File("/storage/emulated/0/Pictures/Zalo");
        // Create an array contains all files in the folder above
        pictureFiles = pictureFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.toLowerCase(Locale.ROOT).endsWith("png") || s.toLowerCase(Locale.ROOT).endsWith("jpg");
            }
        });

        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        bottomNavigationView = findViewById(R.id.navSetting);
        mViewPaper = findViewById(R.id.viewPaperMain);
        mViewPaperAdapter = new ViewPagerAdapter(this, pictureFiles);
        mViewPaper.setAdapter(mViewPaperAdapter);
        mViewPaper.setCurrentItem(27);
    }

    private void initActions() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.favorite:
                    // Đánh dấu trạng thái yêu thích
                    isFavorite = !isFavorite;
                    if (isFavorite) {
                        Toast.makeText(ViewImage.this, "Add favorite", Toast.LENGTH_SHORT).show();
                        item.setIcon(R.mipmap.ic_heart_like);
                        item.setTitle(R.string.action_favorite);
                    } else {
                        Toast.makeText(ViewImage.this, "Remove favorite", Toast.LENGTH_SHORT).show();
                        item.setIcon(R.mipmap.ic_heart);
                        item.setTitle(R.string.action_unfavorite);
                    }
                    break;
                case R.id.edit:
                    Toast.makeText(ViewImage.this, "edit", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.share:
                    Toast.makeText(ViewImage.this, "share", Toast.LENGTH_SHORT).show();
                    shareImage(pictureFiles[mViewPaper.getCurrentItem()].getAbsolutePath());
                    break;
                case R.id.trash:
                    Toast.makeText(ViewImage.this, "trah", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.more:
                    Toast.makeText(ViewImage.this, "more", Toast.LENGTH_SHORT).show();
                    createMenuPopup(bottomNavigationView, R.menu.more_setting_image);
                    break;
            }
            return true;
        });
    }

    private void shareImage(String path) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) mViewPaperAdapter.getImageView().getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        Uri bmpUri;
        String textToShare = "Share image";
        bmpUri = saveImage(bitmap, getApplicationContext(), path);
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.putExtra(Intent.EXTRA_STREAM, bmpUri);
        share.putExtra(Intent.EXTRA_SUBJECT, "Memory");
        share.putExtra(Intent.EXTRA_TEXT, textToShare);
        startActivity(Intent.createChooser(share, "Share Content"));
    }

    private static Uri saveImage(Bitmap image, Context context, String path) {
        File file = new File(path);
        Uri uri = null;

        try {
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            stream.flush();
            stream.close();

            uri = FileProvider.getUriForFile(Objects.requireNonNull(context.getApplicationContext()),
                    "com.example.memorymoblieapp", file);
        } catch (IOException e) {
            Log.d("TAG", "Exception " + e.getMessage());
        }
        return uri;
    }

    private void createMenuPopup(View item, int menuItem) {
        PopupMenu popupMenu = new PopupMenu(item.getContext(), item);
        popupMenu.inflate(menuItem);
        popupMenu.setGravity(Gravity.END);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int item = menuItem.getItemId();
                ImageView largeImage = mViewPaperAdapter.getImageView();

                if (item == R.id.viewDetails) {
                    Toast.makeText(ViewImage.this, "View details", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ViewImage.this, ViewDetails.class);
                    intent.putExtra("path", pictureFiles[mViewPaper.getCurrentItem()].getAbsolutePath());
                    startActivity(intent);
                } else if (item == R.id.setWallpaper) {
                    Toast.makeText(ViewImage.this, "Set Wallpaper", Toast.LENGTH_SHORT).show();

                    try {
                        wallpaperManager.setBitmap(viewToBitmap(largeImage, largeImage.getWidth(), largeImage.getHeight()));
                    } catch (IOException e) {
                        Log.e("Error set as wallpaper: ", e.getMessage());
                    }
                } else if (item == R.id.setLockscreen) {
                    Toast.makeText(ViewImage.this, "Set Lockscreen", Toast.LENGTH_SHORT).show();

                    try {
                        wallpaperManager.setBitmap(viewToBitmap(largeImage, largeImage.getWidth(), largeImage.getHeight()), null, true, WallpaperManager.FLAG_LOCK);
                    } catch (IOException e) {
                        Log.e("Error set as lockscreen: ", e.getMessage());
                    }
                }
                return true;
            }
        });
        popupMenu.show();
    }

    public static Bitmap viewToBitmap(View view, int width, int height) {
        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        view.draw(canvas);
        return bm;
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
