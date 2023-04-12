package com.example.memorymoblieapp.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.memorymoblieapp.controI_image.ViewPagerAdapter;
import com.example.memorymoblieapp.controI_image.ZoomableViewPager;
import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.example.memorymoblieapp.main.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ViewImage extends AppCompatActivity {
    private final int REQUEST_CODE_SHARE = 1111;
    private static BottomNavigationView bottomNavigationView;
    private ZoomableViewPager mViewPaper;
    private ViewPagerAdapter mViewPaperAdapter;
    private WallpaperManager wallpaperManager;
    private static RelativeLayout parentHeader;
    private ImageView imgBack;
    private ArrayList<String> picturePaths;
    private ArrayList<String> favorList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Boolean isThemeDark = DataLocalManager.getBooleanData(KeyData.DARK_MODE.getKey());
        isThemeDark = isThemeDark == null ? false : isThemeDark;

        setTheme(isThemeDark ? R.style.ThemeDark_MemoryMobileApp : R.style.Theme_MemoryMobileApp);
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_view_image);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initViews();
        initActions();
    }

    private void initViews() {
        ArrayList<String> getPicturePaths = new ArrayList<>();
        ArrayList<String> getFavorList = new ArrayList<>();
        picturePaths = new ArrayList<>();
        favorList = new ArrayList<>();

        getPicturePaths = DataLocalManager.getStringList(KeyData.IMAGE_PATH_LIST.getKey());
        getFavorList = DataLocalManager.getStringList(KeyData.FAVORITE_LIST.getKey());
        if (getFavorList != null)
            favorList.addAll(getFavorList);

        if (getPicturePaths != null)
            picturePaths.addAll(getPicturePaths);

        parentHeader = findViewById(R.id.parentHeader);
        imgBack = findViewById(R.id.imgBack);

        Intent intent = getIntent();
        int currentPosition = picturePaths.indexOf(intent.getStringExtra("path_image"));

        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

        bottomNavigationView = findViewById(R.id.navSetting);
        mViewPaper = findViewById(R.id.viewPaperMain);

        mViewPaperAdapter = new ViewPagerAdapter(this, picturePaths);
        mViewPaper.setAdapter(mViewPaperAdapter);
        mViewPaper.setCurrentItem(currentPosition);

        loadFavorite();
    }

    public static void setBottomNavigationViewHide(Boolean check) {
        if (check) {
            parentHeader.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.GONE);
        } else {
            parentHeader.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.VISIBLE);
        }

    }

    private void initActions() {

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ViewImage.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });
        mViewPaper.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                loadFavorite();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.favorite:
                    if (item.getTitle().equals(getResources().getString(R.string.action_favorite))) {
                        Toast.makeText(this, R.string.action_unfavorite, Toast.LENGTH_SHORT).show();

                        favorList.remove(picturePaths.get(mViewPaper.getCurrentItem()));
                        MainActivity.lovedImageList.remove(picturePaths.get(mViewPaper.getCurrentItem()));

                        item.setIcon(R.drawable.ic_unfavorite);
                        item.setTitle(R.string.action_unfavorite);
                    } else {
                        Toast.makeText(this, R.string.action_favorite, Toast.LENGTH_SHORT).show();

                        favorList.add(picturePaths.get(mViewPaper.getCurrentItem()));
                        MainActivity.lovedImageList.add(picturePaths.get(mViewPaper.getCurrentItem()));

                        item.setIcon(R.drawable.ic_favorite);
                        item.setTitle(R.string.action_favorite);
                    }
                    DataLocalManager.saveData(KeyData.FAVORITE_LIST.getKey(), favorList);
                    break;
                case R.id.edit:
                    Toast.makeText(ViewImage.this, "edit", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ViewImage.this, ViewEdit.class);
                    intent.putExtra("path_image", picturePaths.get(mViewPaper.getCurrentItem()));
                    startActivity(intent);
                    break;
                case R.id.share:
                    Toast.makeText(ViewImage.this, "share", Toast.LENGTH_SHORT).show();
                    sharePicture(picturePaths.get(mViewPaper.getCurrentItem()));

                    break;
                case R.id.trash:
                    Toast.makeText(ViewImage.this, "trash", Toast.LENGTH_SHORT).show();
                    moveTrash();
                    break;
                case R.id.more:
                    Toast.makeText(ViewImage.this, "more", Toast.LENGTH_SHORT).show();
                    createMenuPopup(bottomNavigationView, R.menu.more_setting_image);
                    break;
            }
            return true;
        });
    }

    private void moveTrash() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_yes_no);

        Window window = dialog.getWindow();
        if (window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtrr = window.getAttributes();
        windowAtrr.gravity = Gravity.CENTER;
        window.setAttributes(windowAtrr);

        dialog.setCancelable(true);
        Button btnBack = dialog.findViewById(R.id.btnBack);
        Button btnMove = dialog.findViewById(R.id.btnMove);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                int currPos = mViewPaper.getCurrentItem();

                ArrayList<String> trashList = DataLocalManager.getStringList(KeyData.TRASH_LIST.getKey());
                if (trashList == null) {
                    trashList = new ArrayList<>();
                }
                trashList.add(picturePaths.get(currPos));
                MainActivity.deletedImageList.add(picturePaths.get(currPos));
                MainActivity.lovedImageList.remove(picturePaths.get(currPos));
                favorList.remove(picturePaths.get(currPos));

                ArrayList<String> viewListImage = DataLocalManager.getStringList(KeyData.IMAGE_PATH_VIEW_LIST.getKey());
                viewListImage.remove(picturePaths.get(currPos));

                picturePaths.remove(picturePaths.get(currPos));

                mViewPaperAdapter = new ViewPagerAdapter(ViewImage.this, picturePaths);
                mViewPaper.setAdapter(mViewPaperAdapter);
                mViewPaper.setCurrentItem(currPos);

                DataLocalManager.saveData(KeyData.IMAGE_PATH_LIST.getKey(), picturePaths);
                DataLocalManager.saveData(KeyData.IMAGE_PATH_VIEW_LIST.getKey(), viewListImage);
                DataLocalManager.saveData(KeyData.TRASH_LIST.getKey(), trashList);
                DataLocalManager.saveData(KeyData.FAVORITE_LIST.getKey(), favorList);
            }
        });

        dialog.show();
    }

    private void loadFavorite() {
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.favorite);
        if (favorList == null || !favorList.contains(picturePaths.get(mViewPaper.getCurrentItem()))) {
            item.setIcon(R.mipmap.ic_heart);
            item.setTitle(R.string.action_unfavorite);
        } else {
            item.setIcon(R.mipmap.ic_heart_like);
            item.setTitle(R.string.action_favorite);
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void sharePicture(String path) {
        File fileImage = new File(path);
        // Khởi tạo đường dẫn ảnh và nội dung chia sẻ

        Uri mImageUri = FileProvider.getUriForFile(this, "com.example.memorymoblieapp.fileprovider", fileImage);
        String mShareContent = "";

        // Tạo Intent để chia sẻ nội dung
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, mImageUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, mShareContent);

        // Tạo Intent chooser để hiển thị danh sách các ứng dụng cho phép chia sẻ nội dung
        Intent chooserIntent = Intent.createChooser(shareIntent, "Chọn ứng dụng");
        startActivityForResult(chooserIntent, REQUEST_CODE_SHARE);
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
                    Toast.makeText(ViewImage.this, "Set wallpaper complete!", Toast.LENGTH_SHORT).show();
                    try {
                        wallpaperManager.setBitmap(viewToBitmap(largeImage, largeImage.getWidth(), largeImage.getHeight()));
                    } catch (IOException e) {
                        Log.e("Error set as wallpaper: ", e.getMessage());
                        Toast.makeText(ViewImage.this, "Set wallpaper error!", Toast.LENGTH_SHORT).show();

                    }
                } else if (item == R.id.setLockscreen) {
                    Toast.makeText(ViewImage.this, "Set lockscreen complete!", Toast.LENGTH_SHORT).show();

                    try {
                        wallpaperManager.setBitmap(viewToBitmap(largeImage, largeImage.getWidth(), largeImage.getHeight()), null, true, WallpaperManager.FLAG_LOCK);
                    } catch (IOException e) {
                        Log.e("Error set as lockscreen: ", e.getMessage());
                        Toast.makeText(ViewImage.this, "Set lockscreen error!", Toast.LENGTH_SHORT).show();
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
}
