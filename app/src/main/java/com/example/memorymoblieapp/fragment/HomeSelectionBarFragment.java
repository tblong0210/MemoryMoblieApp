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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.GalleryAdapter;
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

public class HomeSelectionBarFragment extends Fragment {
    BottomNavigationView bottomNavigationView;
    ArrayList<String> listSelect;
    ArrayList<String> albumsName;

    @SuppressLint("NonConstantResourceId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View selectionFeaturesBarFragment = inflater.inflate(R.layout.home_selection_bar, container, false);
        bottomNavigationView = selectionFeaturesBarFragment.findViewById(R.id.navigation_view);
        Context context = selectionFeaturesBarFragment.getContext();
        albumsName = new ArrayList<>(DataLocalManager.getSetList(KeyData.ALBUM_NAME_LIST.getKey()));

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            listSelect = new ArrayList<>(GalleryAdapter.getListSelect());

            switch (item.getItemId()) {
                case R.id.add2album:
                    if (albumsName.isEmpty()) {
                        Toast.makeText(context, "Không có album nào trong danh sách. Vui lòng tạo ít nhất một album!", Toast.LENGTH_SHORT).show();
                    } else {
                        final int[] albumChosenPos = new int[1];
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Chọn album cần thêm");

                        Spinner spinner = new Spinner(context);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, albumsName);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(arrayAdapter);

                        final LinearLayout ll = new LinearLayout(getContext());
                        ll.removeAllViews();
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(50, 30, 50, 30);
                        spinner.setLayoutParams(params);
                        ll.addView(spinner);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                                albumChosenPos[0] = pos;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                albumChosenPos[0] = -1;
                            }
                        });

                        builder.setView(ll);
                        builder.setPositiveButton("Đồng ý", null);
                        builder.setNegativeButton("Hủy", null);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                            if (albumChosenPos[0] >= 0 && !albumsName.isEmpty()) {
                                for (String imageSelected : GalleryAdapter.getListSelect())
                                    MainActivity.albumList.get(albumChosenPos[0]).insertNewImage(imageSelected);
                                DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
                                Toast.makeText(context, "Đã thêm ảnh vào album '" + albumsName.get(albumChosenPos[0]) + "'", Toast.LENGTH_SHORT).show();

                                // Refresh and exit choose image mode
                                refresh(context);

                                dialog.dismiss();
                            }
                        });
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog.cancel());
                    }

                    return true;

                case R.id.love:
                    DialogInterface.OnClickListener dialogClickListener2 = (dialog, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                move2Love(listSelect);
                                refresh(context);

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    };
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                    builder2.setMessage("Bạn có chắc muốn xóa các ảnh vừa chọn không?").setPositiveButton("Đồng ý", dialogClickListener2)
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
                    popupMenu.inflate(R.menu.home_selection_bar_more_menu);
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
                System.out.println("File duplicated: " + newFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void move2TrashBin(@NonNull ArrayList<String> filePaths) {
        MainActivity.deletedImageList.addAll(filePaths);
        MainActivity.deletedImageList = removeDuplicates(MainActivity.deletedImageList);
        DataLocalManager.saveData(KeyData.TRASH_LIST.getKey(), MainActivity.deletedImageList);

        MainActivity.lovedImageList.removeAll(filePaths);
        DataLocalManager.saveData(KeyData.FAVORITE_LIST.getKey(), MainActivity.lovedImageList);

        for (Album album : MainActivity.albumList)
            for (String filePath : filePaths)
                album.deleteImage(filePath);

        DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
    }

    void move2Love(@NonNull ArrayList<String> filePaths) {
        MainActivity.lovedImageList.addAll(filePaths);
        MainActivity.lovedImageList = removeDuplicates(MainActivity.lovedImageList);
        DataLocalManager.saveData(KeyData.FAVORITE_LIST.getKey(), MainActivity.lovedImageList);
    }

    void refresh(Context context) {
        // Refresh and exit choose image mode
        ImageFragment imageFragment = new ImageFragment(MainActivity.getNewImage(), MainActivity.getImageDates());
        AppCompatActivity activity = (AppCompatActivity) context;
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content, imageFragment).commit();
        MainActivity.getFrameLayoutSelectionFeaturesBar().removeAllViews();
        MainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
        GalleryAdapter.clearListSelect();
    }

//    void delete(@NonNull ArrayList<String> filePaths, Context context) {
//        for (String filePath : filePaths) {
//            File sourceFile = new File(filePath);
//            if (sourceFile.delete()) {
//                Toast.makeText(context, "Đã xóa " + sourceFile.getName(), Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(context, "Không thể xóa " + sourceFile.getName(), Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    @NonNull
    @Contract("_ -> param1")
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {
        Set<T> set = new LinkedHashSet<>(list);
        list.clear();
        list.addAll(set);
        return list;
    }
}