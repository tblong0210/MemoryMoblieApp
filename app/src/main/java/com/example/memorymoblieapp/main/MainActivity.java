package com.example.memorymoblieapp.main;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.memorymoblieapp.DownloadImageFromURL;
import com.example.memorymoblieapp.ImagesGallery;
import com.example.memorymoblieapp.R;

import com.example.memorymoblieapp.adapter.GalleryAdapter;
import com.example.memorymoblieapp.adapter.ImageListAdapter;
import com.example.memorymoblieapp.databinding.ActivityMainBinding;
import com.example.memorymoblieapp.fragment.AlbumFragment;
import com.example.memorymoblieapp.fragment.ImageFragment;
import com.example.memorymoblieapp.fragment.ImageListFragment;

import com.example.memorymoblieapp.fragment.UrlDialog;
import com.example.memorymoblieapp.fragment.ZipFileFragment;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.example.memorymoblieapp.fragment.SettingsFragment;
import com.example.memorymoblieapp.obj.Album;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ImageFragment imageFragment;
    FragmentTransaction fragmentTransaction;
    public static boolean detailed; // view option of image fragment
    public static ArrayList<Album> albumList;
    public static ArrayList<String> lovedImageList;
    public static ArrayList<String> deletedImageList;
    static BottomNavigationView bottomNavigationView;
    private static final int PERMISSION_REQUEST_CODE = 200;
    //    private ArrayList<String> imagePaths = new ArrayList<String>();
    private RecyclerView recyclerView;
    private static List<String> imageDates;
    static ArrayList<String> images;
    static ArrayList<String> newImage;
    static ArrayList<String> trashListImage;
    GalleryAdapter galleryAdapter;
    boolean isPermission = false;
    private static final int MY_READ_PERMISSION_CODE = 101;
    public static boolean isVerify = false; // Status of album blocking
    @SuppressLint("StaticFieldLeak")
    static FrameLayout frame_layout_selection_features_bar;
    public static ArrayList<String> zipList;
    public static String zipPath;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Boolean isThemeDark = DataLocalManager.getBooleanData(KeyData.DARK_MODE.getKey());
        isThemeDark = isThemeDark != null && isThemeDark;

        setTheme(isThemeDark ? R.style.ThemeDark_MemoryMobileApp : R.style.Theme_MemoryMobileApp);

        String lang = "vi";
        if (DataLocalManager.getBooleanData(KeyData.LANGUAGE_CURRENT.getKey()) == true) {
            lang = "en";
        }

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        String[] permissionList = new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.REQUEST_INSTALL_PACKAGES,
                Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.SET_WALLPAPER};

        // Go to settings to turn on all files access permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        }

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

    public static void updateData(Context context) {
        imageDates = new ArrayList<>();

        trashListImage = DataLocalManager.getStringList(KeyData.TRASH_LIST.getKey());

        images = ImagesGallery.listOfImages(context);
        newImage = handleSortListImageView();
        ArrayList<String> picturePath = new ArrayList<>(newImage);
        picturePath.removeAll(Collections.singleton(" "));

        DataLocalManager.saveData(KeyData.IMAGE_PATH_VIEW_LIST.getKey(), newImage);
        DataLocalManager.saveData(KeyData.IMAGE_PATH_LIST.getKey(), picturePath);
    }

    public static void updateZipList() {
        zipPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/MemoryZip";
        File directory = new File(zipPath);
        if (!directory.exists())
            directory.mkdirs();
        zipList = getZipList(zipPath);
    }

    @SuppressLint("NonConstantResourceId")
    private void initiateApp() {
        imageDates = new ArrayList<>();
        zipList = new ArrayList<>();

        trashListImage = DataLocalManager.getStringList(KeyData.TRASH_LIST.getKey());

        images = ImagesGallery.listOfImages(this);
        newImage = handleSortListImageView();
        ArrayList<String> picturePath = new ArrayList<>(newImage);

        picturePath.removeAll(Collections.singleton(" "));

        zipPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/MemoryZip";
        File directory = new File(zipPath);
        if (!directory.exists())
            directory.mkdirs();
        zipList = getZipList(zipPath);

        DataLocalManager.saveData(KeyData.IMAGE_PATH_VIEW_LIST.getKey(), newImage);
        DataLocalManager.saveData(KeyData.IMAGE_PATH_LIST.getKey(), picturePath);

        detailed = false;
        albumList = new ArrayList<>(DataLocalManager.getObjectList(KeyData.ALBUM_DATA_LIST.getKey(), Album.class));
        lovedImageList = DataLocalManager.getStringList(KeyData.FAVORITE_LIST.getKey());
        lovedImageList = lovedImageList == null ? new ArrayList<>() : lovedImageList;
        deletedImageList = DataLocalManager.getStringList(KeyData.TRASH_LIST.getKey());
        deletedImageList = deletedImageList == null ? new ArrayList<>() : deletedImageList;

        frame_layout_selection_features_bar = findViewById(R.id.frame_layout_selection_features_bar);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        imageFragment = new ImageFragment(newImage, imageDates);

        if (!set2FragmentLayout()) {
            fragmentTransaction.replace(R.id.frame_layout_content, imageFragment).commit();
            fragmentTransaction.addToBackStack("image");
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.image:
                    newImage.clear();
                    imageDates.clear();
                    newImage = handleSortListImageView();
                    imageFragment = new ImageFragment(newImage, imageDates);
                    fragmentTransaction.replace(R.id.frame_layout_content, imageFragment).commit();
                    fragmentTransaction.addToBackStack("image");
                    return true;


                case R.id.album:
                    AlbumFragment albumFragment = new AlbumFragment(albumList);
                    fragmentTransaction.replace(R.id.frame_layout_content, albumFragment).commit();
                    fragmentTransaction.addToBackStack("album");
                    return true;

                case R.id.love:
                    ImageListFragment loveImageFragment = new ImageListFragment(lovedImageList, getString(R.string.bottom_navigation_love), "Love");
                    fragmentTransaction.replace(R.id.frame_layout_content, loveImageFragment).commit();
                    fragmentTransaction.addToBackStack("love");
                    return true;

                case R.id.more:
                    PopupMenu popupMenu = new PopupMenu(MainActivity.this, bottomNavigationView, Gravity.END);
                    popupMenu.inflate(R.menu.bottom_navigation_more_menu);

                    popupMenu.setOnMenuItemClickListener(menuItem -> {

                        int itemId = menuItem.getItemId();

                        if (R.id.recycleBin == itemId) {
                            ImageListFragment deletedImageFragment = new ImageListFragment(deletedImageList, getString(R.string.bottom_navigation_recycle_bin), "TrashBin");
                            fragmentTransaction.replace(R.id.frame_layout_content, deletedImageFragment).commit();
                        } else if (R.id.URL == itemId) {
                            new UrlDialog().show(getSupportFragmentManager(), UrlDialog.Tag);
                        } else if (R.id.settings == itemId) {
                            SettingsFragment settingsFragment = new SettingsFragment();
                            fragmentTransaction.replace(R.id.frame_layout_content, settingsFragment).commit();
                        } else if (R.id.zip == itemId) {
                            ZipFileFragment zipFileFragment = new ZipFileFragment(zipList);
                            fragmentTransaction.replace(R.id.frame_layout_content, zipFileFragment).commit();
                        }

                        return true;
                    });

                    popupMenu.show();
                    fragmentTransaction.addToBackStack("more");

                    return true;
            }
            return false;
        });
    }

    @SuppressLint("SimpleDateFormat")
    public static ArrayList<String> handleSortListImageView() {
        ArrayList<String> newImage = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
        trashListImage = DataLocalManager.getStringList(KeyData.TRASH_LIST.getKey());

        int flag = 0;

        for (String imagePath : images) {
            if (imagePath != null && (trashListImage == null || !trashListImage.contains(imagePath))) {
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

    public boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            Toast.makeText(MainActivity.this, "No network is currently active!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!networkInfo.isConnected()) {
            Toast.makeText(MainActivity.this, "Network is not connected!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!networkInfo.isAvailable()) {
            Toast.makeText(MainActivity.this, "Network is not available!", Toast.LENGTH_SHORT).show();
            return false;
        }

        Toast.makeText(MainActivity.this, "Network is OK!", Toast.LENGTH_SHORT).show();
        return true;
    }

    public void onMsgFromFragToMain(String request) {
        boolean network = checkInternetConnection();
        if (!network)
            return;
        DownloadImageFromURL task = new DownloadImageFromURL();
        task.execute(request);
        try {
            Bitmap bitmap = task.get();
            //  picturesFragment.onMsgFromMainToFrag(bitmap);
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "myImageFolder");

            // Create the directory if it doesn't exist
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String imageName = UUID.randomUUID().toString() + ".jpg";
            // Create a file to save the image
            File file = new File(directory, imageName);
            OutputStream outputStream = new FileOutputStream(file);

            // Compress the bitmap and write it to the output stream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            // Flush and close the output stream
            outputStream.flush();
            outputStream.close();

            // Add the image to the gallery so it can be viewed in other apps
            MediaScannerConnection.scanFile(MainActivity.this, new String[]{file.getAbsolutePath()}, null, null);
            MediaScannerConnection.scanFile(MainActivity.this, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override
                public void onMediaScannerConnected() {
                    // Do nothing
                }

                @Override
                public void onScanCompleted(String path, Uri uri) {
                    newImage.clear();
                    imageDates.clear();
//                    newImage = handleSortListImageView();
                    File imageFile = new File(path);
                    Date imageDate = new Date(imageFile.lastModified());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");

                    Log.d("Taggg", path + dateFormat.format(imageDate));
                }
            });
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "No result", Toast.LENGTH_SHORT).show();
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
        } else finish();

        if (fragmentManager.getBackStackEntryCount() > 0) fragmentManager.popBackStack();
        else super.onBackPressed();

        ImageFragment.turnOffselectMode();
        ImageListAdapter.ViewHolder.turnOffSelectMode();
    }

    public Boolean set2FragmentLayout() {
        Intent intent = getIntent();
        int idFragment = intent.getIntExtra(KeyData.CURRENT_FRAGMENT.getKey(), -1);
        String data = intent.getStringExtra("data");
        data = data == null ? "" : data;

        if (idFragment == R.string.view_album) {
            if (data.equals(""))
                return false;

            int pos = 0;
            for (Album a : albumList) {
                if (!a.getAlbumName().equals(data))
                    pos++;
                else break;
            }

            ImageListFragment imageFragment = new ImageListFragment(albumList.get(pos).getPathImages(), albumList.get(pos).getAlbumName(), "Album");
            fragmentTransaction.replace(R.id.frame_layout_content, imageFragment).commit();
            bottomNavigationView.setSelectedItemId(R.id.album);

            return true;
        } else if (idFragment == R.string.settings) {
            SettingsFragment settingsFragment = new SettingsFragment();
            fragmentTransaction.replace(R.id.frame_layout_content, settingsFragment).commit();
            bottomNavigationView.setSelectedItemId(R.id.more);

            return true;
        }
        return false;
    }

    public static BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public static FrameLayout getFrameLayoutSelectionFeaturesBar() {
        return frame_layout_selection_features_bar;
    }

    public static ArrayList<String> getNewImage() {
        return newImage;
    }

    public static List<String> getImageDates() {
        return imageDates;
    }

    public static ArrayList<String> getImages() {
        return images;
    }

    static ArrayList<String> getZipList(String folderPath) {
        File directory = new File(folderPath);
        File[] files = directory.listFiles();
        ArrayList<String> zipFilePaths = new ArrayList<>();

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".zip")) {
                zipFilePaths.add(file.getAbsolutePath());
            }
        }

        return zipFilePaths;
    }
}
