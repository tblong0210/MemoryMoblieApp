package com.example.memorymoblieapp.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.memorymoblieapp.ImagesGallery;
import com.example.memorymoblieapp.R;

import com.example.memorymoblieapp.adapter.GalleryAdapter;
import com.example.memorymoblieapp.databinding.ActivityMainBinding;
import com.example.memorymoblieapp.fragment.AlbumFragment2;
import com.example.memorymoblieapp.fragment.ImageFragment;
import com.example.memorymoblieapp.fragment.ImageFragment2;

import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.example.memorymoblieapp.fragment.SettingsFragment;
import com.example.memorymoblieapp.obj.Album;
import com.example.memorymoblieapp.view.ViewSearch;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    public static boolean detailed; // view option of image fragment
    public static ArrayList<Album> albumList;
    public static ArrayList<String> lovedImageList;
    public static ArrayList<String> deletedImageList;
    BottomNavigationView bottomNavigationView;
    private static final int PERMISSION_REQUEST_CODE = 200;
    //    private ArrayList<String> imagePaths = new ArrayList<String>();
    private RecyclerView recyclerView;
    private List<String> imageDates = new ArrayList<>();
    ArrayList<String> images;
    ArrayList<String> newImage;
    ArrayList<String> trashListImage;
    GalleryAdapter galleryAdapter;
    boolean isPermission = false;
    private static final int MY_READ_PERMISSION_CODE = 101;
    public static boolean isVerify = false; // Status of album blocking

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String[] permissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.INTERNET};

        if (!checkPermissionList(permissionList))
            ActivityCompat.requestPermissions(MainActivity.this, permissionList, 1);
        else initiateApp();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            initiateApp();
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

    @SuppressLint("NonConstantResourceId")
    private void initiateApp() {
//        ArrayList<String> arr = new ArrayList<>();
//        arr.addAll(DataLocalManager.getSetList(KeyData.UN_AVAILABLE_IMAGE.getKey()));

        trashListImage = DataLocalManager.getStringList(KeyData.TRASH_LIST.getKey());

        images = ImagesGallery.listOfImages(this);
        newImage = handleSortListImageView();
        ArrayList<String> picturePath = new ArrayList<>(newImage);

        picturePath.removeAll(Collections.singleton(" "));

        DataLocalManager.saveData(KeyData.IMAGE_PATH_VIEW_LIST.getKey(), newImage);
        DataLocalManager.saveData(KeyData.IMAGE_PATH_LIST.getKey(), picturePath);
//        Toast.makeText(this, newImage.size() + " "+ picturePath.size(), Toast.LENGTH_SHORT).show();

        detailed = false;
        albumList = DataLocalManager.getObjectList(KeyData.ALBUM_DATA_LIST.getKey(), Album.class);
        albumList = albumList == null ? new ArrayList<>() : albumList;
        lovedImageList = DataLocalManager.getStringList(KeyData.FAVORITE_LIST.getKey());
        lovedImageList = lovedImageList == null ? new ArrayList<>() : lovedImageList;
        deletedImageList = DataLocalManager.getStringList(KeyData.TRASH_LIST.getKey());
        deletedImageList = deletedImageList == null ? new ArrayList<>() : deletedImageList;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        ImageFragment imageFragment = new ImageFragment(newImage, imageDates);
        fragmentTransaction.replace(R.id.frame_layout_content, imageFragment).commit();
        fragmentTransaction.addToBackStack("image");

        Intent intent = getIntent();
        String request = intent.getStringExtra("request");
        String albumName = intent.getStringExtra("album_name");
        if (request != null && albumName != null) {
            onMsgToMain(albumName, request);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.image:
                    ImageFragment imageFragment1 = new ImageFragment(newImage, imageDates);
                    fragmentTransaction1.replace(R.id.frame_layout_content, imageFragment1).commit();
                    fragmentTransaction1.addToBackStack("image");
                    return true;

                case R.id.album:
                    AlbumFragment2 albumFragment = new AlbumFragment2(albumList);
                    fragmentTransaction1.replace(R.id.frame_layout_content, albumFragment).commit();
                    fragmentTransaction1.addToBackStack("album");
                    return true;

                case R.id.love:
                    ImageFragment2 loveImageFragment = new ImageFragment2(lovedImageList, "Yêu thích");
                    fragmentTransaction1.replace(R.id.frame_layout_content, loveImageFragment).commit();
                    fragmentTransaction1.addToBackStack("love");
                    return true;

                case R.id.more:
                    PopupMenu popupMenu = new PopupMenu(MainActivity.this, bottomNavigationView, Gravity.END);
                    popupMenu.inflate(R.menu.more_menu);
                    popupMenu.setOnMenuItemClickListener(menuItem -> {
                        int itemId = menuItem.getItemId();
                        if (R.id.recycleBin == itemId) {
                            ImageFragment2 deletedImageFragment = new ImageFragment2(deletedImageList, "Thùng rác");
                            fragmentTransaction1.replace(R.id.frame_layout_content, deletedImageFragment).commit();
                        } else if (R.id.URL == itemId) {
                            Toast.makeText(MainActivity.this, "Tải ảnh bằng URL", Toast.LENGTH_LONG).show();
                        } else if (R.id.settings == itemId) {
                            SettingsFragment settingsFragment = new SettingsFragment();
                            fragmentTransaction1.replace(R.id.frame_layout_content, settingsFragment).commit();
                        }
                        return true;
                    });
                    popupMenu.show();
                    fragmentTransaction1.addToBackStack("more");

                    return true;
            }
            return false;
        });
    }

    @SuppressLint("SimpleDateFormat")
    private ArrayList<String> handleSortListImageView() {
        ArrayList<String> newImage = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
        int flag = 0;

        for (String imagePath : images) {
            if (imagePath != null) {

                File imageFile = new File(imagePath);
                Date imageDate = new Date(imageFile.lastModified());
                if (imageDates.size() != 0 && !dateFormat.format(imageDate).equals(imageDates.get(imageDates.size() - 1))) {

                    if (flag % 3 == 2) {
                        newImage.add(" ");
                        imageDates.add(" ");
                    }
                    if (flag % 3 == 1) {
                        newImage.add(" ");
                        newImage.add(" ");
                        imageDates.add(" ");
                        imageDates.add(" ");
                    }
                    flag = 0;
                }
                newImage.add(imagePath);
                imageDates.add(dateFormat.format(imageDate));
                flag++;
//                Log.d("MyTag", dateFormat.format(imageDate) + imagePath);
            }
        }
        return newImage;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionSearch) {
            Intent intent = new Intent(this, ViewSearch.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();

        if (backStackEntryCount >= 2) {
            FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(backStackEntryCount - 2);
            String fragmentName = backStackEntry.getName();
            if (fragmentName != null) {
                switch (fragmentName) {
                    case "image":
                        bottomNavigationView.getMenu().findItem(R.id.image).setChecked(true);
                        break;
                    case "album":
                        bottomNavigationView.getMenu().findItem(R.id.album).setChecked(true);
                        break;
                    case "love":
                        bottomNavigationView.getMenu().findItem(R.id.love).setChecked(true);
                        break;
                    case "more":
                        bottomNavigationView.getMenu().findItem(R.id.more).setChecked(true);
                        break;
                }
            }
        }

        if (fragmentManager.getBackStackEntryCount() > 0) fragmentManager.popBackStack();
        else super.onBackPressed();
    }

    private void addLovedImageList() {
        lovedImageList.add("/storage/emulated/0/Download/iPhone-14-Purple-wallpaper.png");
    }

    private void addDeletedImageList() {
        deletedImageList.add("/storage/emulated/0/Download/iPhone-14-Purple-wallpaper.png");
    }

    public void onMsgToMain(String data, String request) {
        if (request.equals("VIEW_ALBUM_IMAGE")) {
            int pos = 0;
            for (Album a : albumList) {
                if(!a.getAlbumName().equals(data))
                    pos++;
                else break;
            }

            ImageFragment2 imageFragment = new ImageFragment2(albumList.get(pos).getPathImages(), albumList.get(pos).getAlbumName());
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout_content, imageFragment).commit();
            fragmentTransaction.addToBackStack("album");
        }
    }
}