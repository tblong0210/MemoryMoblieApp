//package com.example.memorymoblieapp.main;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.FragmentManager;
//
//import android.app.FragmentTransaction;
//import androidx.fragment.app.Fragment;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import com.example.memorymoblieapp.R;
//
//import android.widget.GridView;
//import android.annotation.SuppressLint;
//import com.example.memorymoblieapp.fragment.ImageFragment;
//import com.example.memorymoblieapp.fragment.TitleContentContainerFragment;
//import com.example.memorymoblieapp.view.ViewEdit;
//
//import com.example.memorymoblieapp.databinding.ActivityMainBinding;
//import com.example.memorymoblieapp.fragment.AlbumFragment;
//import com.example.memorymoblieapp.fragment.ImageFragment;
//import com.example.memorymoblieapp.fragment.LoveFragment;
//
//
//public class MainActivity extends AppCompatActivity {
//    private Button btnViewEdit;
//
//    ActivityMainBinding binding;
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(R.layout.activity_main);
//
//        // FragmentAlbum
////        AlbumFragment albumFragment = new AlbumFragment();
////        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, albumFragment).commit();
//        replaceFragment(new ImageFragment());
//        binding.bottomNavigationView.setOnItemSelectedListener(item ->{
//
//            switch(item.getItemId()) {
//                case R.id.image:
//                    replaceFragment(new ImageFragment());
//                    break;
//                case R.id.album:
//                    replaceFragment(new AlbumFragment());
//                    break;
//                case R.id.love:
//                    replaceFragment(new LoveFragment());
//                    break;
//            }
//            return true;
//        });
//
//        ImageFragment imageFragment = new ImageFragment(true);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, imageFragment).commit();
//
//        TitleContentContainerFragment titleContentContainerFragment = new TitleContentContainerFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_title_content_container, titleContentContainerFragment).commit();
//
//
//        btnViewEdit = findViewById(R.id.btnViewEdit);
//
//        btnViewEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, ViewEdit.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private void replaceFragment(Fragment fragment)
//    {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction  =  fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_layout_navigation, fragment);
//        fragmentTransaction.commit();
//
//    }
//}


// ***********************************************


package com.example.memorymoblieapp.main;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.memorymoblieapp.fragment.ImageFragment2;

import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.example.memorymoblieapp.fragment.SettingsFragment;
import com.example.memorymoblieapp.obj.Album;
import com.example.memorymoblieapp.obj.Image;
import com.example.memorymoblieapp.view.ViewEdit;
import com.example.memorymoblieapp.view.ViewImage;
import com.example.memorymoblieapp.view.ViewSearch;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private Button btnViewEdit;
    public static boolean detailed; // view option of image fragment
    public ArrayList<Album> albumList;
    ArrayList<Image> lovedImageList;
    ArrayList<Image> deletedImageList;
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

        images = ImagesGallery.listOfImages(this);
        DataLocalManager.saveData(KeyData.IMAGE_PATH_LIST.getKey(), images);
        Log.d("pathImage", images.size() + "//" + images.get(0));
//        loadImages();

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
                        // fragmentTransaction.replace(...).commit();
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

        btnViewEdit = findViewById(R.id.btnViewEdit);

        btnViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewSearch.class);
                startActivity(intent);
            }
        });
    }

    private void loadImages() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        images = ImagesGallery.listOfImages(this);
        galleryAdapter = new GalleryAdapter(this, images, new GalleryAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(String path) {
                Toast.makeText(MainActivity.this, "" + path, Toast.LENGTH_SHORT).show();

            }
        });
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

    private void addAlbumList() {
        ArrayList<Image> imgList = new ArrayList<Image>();
        imgList.add(new Image("image1.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imgList.add(new Image("image1.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imgList.add(new Image("image1.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imgList.add(new Image("image1.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        imgList.add(new Image("image1.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        albumList.add(new Album("Album1", new ArrayList<Image>(), R.drawable.image1));
        albumList.add(new Album("Album2", imgList, R.drawable.image1));
    }

    private void addLovedImageList() {
        lovedImageList.add(new Image("image1.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        lovedImageList.add(new Image("image2.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        lovedImageList.add(new Image("image3.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        lovedImageList.add(new Image("image4.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        lovedImageList.add(new Image("image5.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        lovedImageList.add(new Image("image6.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        lovedImageList.add(new Image("image7.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        lovedImageList.add(new Image("image8.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        lovedImageList.add(new Image("image9.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        lovedImageList.add(new Image("image10.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        lovedImageList.add(new Image("image11.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        lovedImageList.add(new Image("image12.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        lovedImageList.add(new Image("image13.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
    }

    private void addDeletedImageList() {
        deletedImageList.add(new Image("image1.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        deletedImageList.add(new Image("image2.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        deletedImageList.add(new Image("image3.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        deletedImageList.add(new Image("image4.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        deletedImageList.add(new Image("image5.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        deletedImageList.add(new Image("image6.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
    }
}
