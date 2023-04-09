package com.example.memorymoblieapp.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.memorymoblieapp.ImagesGallery;
import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.GalleryAdapter;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.example.memorymoblieapp.main.MainActivity;
import com.example.memorymoblieapp.obj.Album;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.ArrayList;

public class SelectionFeaturesBarFragment extends Fragment {
    BottomNavigationView bottomNavigationView;
    ArrayList<String> listSelect;
    String fragmentName;
    ArrayList<String> albumsName;

    public SelectionFeaturesBarFragment(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    @SuppressLint("NonConstantResourceId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View selectionFeaturesBarFragment = inflater.inflate(R.layout.selection_features_bar, container, false);
        bottomNavigationView = selectionFeaturesBarFragment.findViewById(R.id.navigation_view);
        Context context = selectionFeaturesBarFragment.getContext();
        albumsName = new ArrayList<>(DataLocalManager.getSetList(KeyData.ALBUM_NAME_LIST.getKey()));

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (fragmentName) {
                case "Home":
                    listSelect = new ArrayList<>(GalleryAdapter.getListSelect());
                    break;
                case "Love":
                    //
                    break;
                case "Trash":
                    //
                    break;
            }

            switch (item.getItemId()) {
                case R.id.add2album:
                    if (albumsName.isEmpty()) {
                        Toast.makeText(context, "Không có album nào trong danh sách. Vui lòng tạo ít nhất một album!", Toast.LENGTH_SHORT).show();
                    } else {
                        final int[] albumChosenPos = new int[1];
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Chọn album cần thêm");

                        Spinner spinner = new Spinner(context);
                        ArrayAdapter arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, albumsName);
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
                                dialog.dismiss();
                            }
                        });
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog.cancel());
                    }

                    return true;

                case R.id.duplicate:

                    DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                duplicate(listSelect);
                                MainActivity.updateData(context);
                                ImageFragment imageFragment = new ImageFragment(MainActivity.getNewImage(), MainActivity.getImageDates());
                                AppCompatActivity activity = (AppCompatActivity) context;
                                FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame_layout_content, imageFragment).commit();

                                MainActivity.getFrameLayoutSelectionFeaturesBar().removeAllViews();
                                MainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
                                GalleryAdapter.clearListSelect();

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

                    return true;

                case R.id.delete:
                    Toast.makeText(selectionFeaturesBarFragment.getContext(), "Xóa", Toast.LENGTH_LONG).show();
                    return true;

                case R.id.share:
                    Toast.makeText(selectionFeaturesBarFragment.getContext(), "Chia sẻ", Toast.LENGTH_LONG).show();
                    return true;
            }
            return false;
        });

        return selectionFeaturesBarFragment;
    }

    public void duplicate(@NonNull ArrayList<String> filePaths) {
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
}