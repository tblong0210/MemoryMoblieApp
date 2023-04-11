package com.example.memorymoblieapp.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.GalleryAdapter;
import com.example.memorymoblieapp.adapter.ImageAdapter;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.example.memorymoblieapp.main.MainActivity;
import com.example.memorymoblieapp.obj.Album;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class AlbumSelectionBarFragment extends Fragment {
    BottomNavigationView bottomNavigationView;
    ArrayList<String> listSelect;
    ArrayList<String> albumsName;
    int albumPos;

    public AlbumSelectionBarFragment(int albumPos) {
        this.albumPos = albumPos;
    }

    @SuppressLint("NonConstantResourceId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View selectionFeaturesBarFragment = inflater.inflate(R.layout.album_selection_bar, container, false);
        bottomNavigationView = selectionFeaturesBarFragment.findViewById(R.id.navigation_view);
        Context context = selectionFeaturesBarFragment.getContext();
        albumsName = new ArrayList<>(DataLocalManager.getSetList(KeyData.ALBUM_NAME_LIST.getKey()));

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            listSelect = new ArrayList<>(ImageAdapter.getListSelect());

            switch (item.getItemId()) {
                case R.id.removeFromAlbum:
                    DialogInterface.OnClickListener dialogClickListener4 = (dialog, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                MainActivity.albumList.get(albumPos).removeImageArray(listSelect);
                                DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);

                                // Refresh and exit choose image mode
                                refresh(context);

                                Toast.makeText(context, "Đã gỡ các ảnh được chọn khỏi album", Toast.LENGTH_SHORT).show();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    };
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(context);
                    builder4.setMessage("Bạn có chắc muốn gỡ các ảnh được chọn khỏi album không?").setPositiveButton("Đồng ý", dialogClickListener4)
                            .setNegativeButton("Hủy", dialogClickListener4).show();


                    return true;

                case R.id.love:
                    DialogInterface.OnClickListener dialogClickListener2 = (dialog, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                MainActivity.lovedImageList.addAll(listSelect);
                                MainActivity.lovedImageList = removeDuplicates(MainActivity.lovedImageList);
                                DataLocalManager.saveData(KeyData.FAVORITE_LIST.getKey(), MainActivity.lovedImageList);

                                refresh(context);
                                Toast.makeText(context, "Đã thêm các ảnh được chọn vào mục yêu thích", Toast.LENGTH_SHORT).show();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    };
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                    builder2.setMessage("Bạn có chắc muốn đưa các ảnh vừa chọn vào mục yêu thích không?").setPositiveButton("Đồng ý", dialogClickListener2)
                            .setNegativeButton("Hủy", dialogClickListener2).show();

                    return true;

                case R.id.delete:
                    DialogInterface.OnClickListener dialogClickListener1 = (dialog, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                move2TrashBin(listSelect);
                                MainActivity.updateData(context);

                                // Refresh and exit choose image mode
                                refresh(context);

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    };
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("Bạn có chắc muốn xóa các ảnh vừa chọn không?").setPositiveButton("Đồng ý", dialogClickListener1)
                            .setNegativeButton("Hủy", dialogClickListener1).show();

                    return true;

                case R.id.more:
                    PopupMenu popupMenu = new PopupMenu(context, bottomNavigationView, Gravity.END);
                    popupMenu.inflate(R.menu.love_selection_bar_more_menu);
                    popupMenu.setOnMenuItemClickListener(menuItem -> {
                        int itemId = menuItem.getItemId();

                        if (R.id.share == itemId) {
                            Toast.makeText(context, "Chia sẻ", Toast.LENGTH_SHORT).show();
                        }

                        else if (R.id.duplicate == itemId) {
                            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        duplicate(listSelect);
                                        MainActivity.updateData(context);

                                        // Refresh and exit choose image mode
                                        refresh(context);

                                        Toast.makeText(context, "Tạo bản sao thành công", Toast.LENGTH_SHORT).show();

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        dialog.cancel();
                                        break;
                                }
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Bạn có chắc muốn tạo bản sao các ảnh vừa chọn không?").setPositiveButton("Đồng ý", dialogClickListener)
                                    .setNegativeButton("Hủy", dialogClickListener).show();
                        }
                        return true;
                    });
                    popupMenu.show();
                    return true;
            }

            return false;
        });

        return selectionFeaturesBarFragment;
    }

    void duplicate(@NonNull ArrayList<String> filePaths) {
        for (String filePath : filePaths) {
            File sourceFile = new File(filePath);
            String fileName = sourceFile.getName();
            String fileExtension = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = fileName.substring(dotIndex);
                fileName = fileName.substring(0, dotIndex);
            }
            int count = 1;
            String newFilePath = sourceFile.getParent() + File.separator + fileName + " (" + count + ")" + fileExtension;
            while (new File(newFilePath).exists()) {
                count++;
                newFilePath = sourceFile.getParent() + File.separator + fileName + " (" + count + ")" + fileExtension;
            }
            try {
                Files.copy(sourceFile.toPath(), new File(newFilePath).toPath());
                MainActivity.albumList.get(albumPos).insertNewImage(newFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
    }

    void move2TrashBin(@NonNull ArrayList<String> filePaths) {
        MainActivity.deletedImageList.addAll(filePaths);
        MainActivity.deletedImageList = removeDuplicates(MainActivity.deletedImageList);
        DataLocalManager.saveData(KeyData.TRASH_LIST.getKey(), MainActivity.deletedImageList);

        MainActivity.lovedImageList.removeAll(filePaths);
        DataLocalManager.saveData(KeyData.FAVORITE_LIST.getKey(), MainActivity.lovedImageList);

        for (Album album : MainActivity.albumList)
            for (String filePath : filePaths)
                album.removeImage(filePath);

        DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
    }

    void refresh(Context context) {
        // Refresh and exit choose image mode
        ImageFragment2 imageFragment = new ImageFragment2(MainActivity.albumList.get(albumPos).getPathImages(), MainActivity.albumList.get(albumPos).getAlbumName(), "Album", albumPos);
        AppCompatActivity activity = (AppCompatActivity) context;
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content, imageFragment).commit();

        MainActivity.getFrameLayoutSelectionFeaturesBar().removeAllViews();
        MainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
        GalleryAdapter.clearListSelect();
    }

    @NonNull
    @Contract("_ -> param1")
    <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {
        Set<T> set = new LinkedHashSet<>(list);
        list.clear();
        list.addAll(set);
        return list;
    }
}
