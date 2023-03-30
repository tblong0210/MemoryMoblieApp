package com.example.memorymoblieapp.view;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.memorymoblieapp.controlI_mage.ViewPagerAdapter;
import com.example.memorymoblieapp.controlI_mage.ZoomableViewPager;
import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ViewImage extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    ZoomableViewPager mViewPaper;
    ViewPagerAdapter mViewPaperAdapter;
    private WallpaperManager wallpaperManager;
    private boolean isFavorite = false;
    ArrayList<String> picturePaths;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_image_container);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initViews();
        initActions();
    }

    private void initViews() {
        Intent intent = getIntent();
        picturePaths = DataLocalManager.getStringList(KeyData.IMAGE_PATH_LIST.getKey());

        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        bottomNavigationView = findViewById(R.id.navSetting);
        mViewPaper = findViewById(R.id.viewPaperMain);
        mViewPaperAdapter = new ViewPagerAdapter(this, picturePaths);
        mViewPaper.setAdapter(mViewPaperAdapter);
        mViewPaper.setCurrentItem(200);
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
                    shareImage(picturePaths.get(mViewPaper.getCurrentItem()));
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
                    intent.putExtra("path", picturePaths.get(mViewPaper.getCurrentItem()));
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
