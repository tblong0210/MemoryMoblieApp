package com.example.memorymoblieapp.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import com.example.memorymoblieapp.view.ViewImage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
   // private Button btnViewEdit;
    public static boolean detailed; // view option of image fragment
    public ArrayList<Album> albumList;
    ArrayList<String> lovedImageList;
    ArrayList<String> deletedImageList;
    BottomNavigationView bottomNavigationView;

    private static final int PERMISSION_REQUEST_CODE = 200;
    //    private ArrayList<String> imagePaths = new ArrayList<String>();
    private RecyclerView recyclerView;

    ArrayList<String> images;
    GalleryAdapter galleryAdapter;
    boolean isPermission = false;
    private static final int MY_READ_PERMISSION_CODE = 101;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }

    private void loadImages() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        images = ImagesGallery.listOfImages(this);
        DataLocalManager.saveData(KeyData.IMAGE_PATH_LIST.getKey(), images);

        Log.d("pathImage", images.size() + "//" + images.get(0));
       galleryAdapter = new GalleryAdapter(this, images
//                , new GalleryAdapter.PhotoListener() {
//            @Override
//            public void onPhotoClick(String path) {
//                Toast.makeText(MainActivity.this, "" + path, Toast.LENGTH_SHORT).show();
//
//            }
//        }
        );
        recyclerView.setAdapter(galleryAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // this method is called after permissions has been granted.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // we are checking the permission code.
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions Granted..", Toast.LENGTH_SHORT).show();
                initApp();
                loadImages();
            } else {
                Toast.makeText(this, "Permissions denied, Permissions are required to use the app..", Toast.LENGTH_SHORT).show();
            }
        }
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

        if (fragmentManager.getBackStackEntryCount() > 0)
            fragmentManager.popBackStack();
        else
            super.onBackPressed();
    }

    private void initApp(){

        recyclerView = findViewById(R.id.recyclerview_gallery_images);

        detailed = false;
        albumList = new ArrayList<>();
        addAlbumList();
        lovedImageList = new ArrayList<>();
        addLovedImageList();
        deletedImageList = new ArrayList<>();
        addDeletedImageList();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.image:
                        Toast.makeText(MainActivity.this, "Image", Toast.LENGTH_LONG).show();
//                        ArrayList<String> pathImages = DataLocalManager.getStringList(KeyData.IMAGE_PATH_LIST.getKey());
//                        Toast.makeText(MainActivity.this, pathImages.size() + "", Toast.LENGTH_LONG).show();
                        // fragmentTransaction.replace(...).commit();
                        ImageFragment imageFragment = new ImageFragment(images);
                        fragmentTransaction.replace(R.id.frame_layout_content, imageFragment).commit();
                        fragmentTransaction.addToBackStack("image");
                        return true;

                    case R.id.album:
                        AlbumFragment2 albumFragment = new AlbumFragment2(albumList);
                        fragmentTransaction.replace(R.id.frame_layout_content, albumFragment).commit();
                        fragmentTransaction.addToBackStack("album");
                        return true;

                    case R.id.love:
                        ImageFragment2 loveImageFragment = new ImageFragment2(lovedImageList, "Yêu thích");
                        fragmentTransaction.replace(R.id.frame_layout_content, loveImageFragment).commit();
                        fragmentTransaction.addToBackStack("love");
                        return true;

                    case R.id.more:
                        PopupMenu popupMenu = new PopupMenu(MainActivity.this, bottomNavigationView, Gravity.END);
                        popupMenu.inflate(R.menu.more_menu);
                        popupMenu.setOnMenuItemClickListener(menuItem -> {
                            int itemId = menuItem.getItemId();
                            if (R.id.recycleBin == itemId) {
                                ImageFragment2 deletedImageFragment = new ImageFragment2(deletedImageList, "Thùng rác");
                                fragmentTransaction.replace(R.id.frame_layout_content, deletedImageFragment).commit();
                            } else if (R.id.URL == itemId) {
                                Toast.makeText(MainActivity.this, "Tải ảnh bằng URL", Toast.LENGTH_LONG).show();
                            } else if (R.id.settings == itemId) {
                                SettingsFragment settingsFragment = new SettingsFragment();
                                fragmentTransaction.replace(R.id.frame_layout_content, settingsFragment).commit();
                            }
                            return true;
                        });
                        popupMenu.show();
                        fragmentTransaction.addToBackStack("more");

                        return true;
                }
                return false;
            }
        });

//        btnViewEdit = findViewById(R.id.btnViewEdit);
//
//        btnViewEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, ViewSearch.class);
//                startActivity(intent);
//            }
//        });
    }

    private void addAlbumList() {
        ArrayList<String> imgList = new ArrayList<>();
        imgList.add("/storage/emulated/0/Download/iPhone-14-Purple-wallpaper.png");
        albumList.add(new Album("Album1", new ArrayList<>()));
        albumList.add(new Album("Album2", imgList));
    }

    private void addLovedImageList() {
        lovedImageList.add("/storage/emulated/0/Download/iPhone-14-Purple-wallpaper.png");
    }

    private void addDeletedImageList() {
        deletedImageList.add("/storage/emulated/0/Download/iPhone-14-Purple-wallpaper.png");
    }
}
