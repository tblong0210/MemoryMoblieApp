package com.example.memorymoblieapp.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.GalleryAdapter;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.example.memorymoblieapp.main.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;


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
                    params.setMargins(50,30,50,30);
                    spinner.setLayoutParams(params);
                    ll.addView(spinner);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            albumChosenPos[0] = i;
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
                        for (String imageSelected : GalleryAdapter.getListSelect())
                            MainActivity.albumList.get(albumChosenPos[0]).insertNewImage(imageSelected);
                        DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
                        Toast.makeText(context, "Đã thêm ảnh vào album '" + albumsName.get(albumChosenPos[0]) + "'", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog.cancel());

                    return true;

                case R.id.duplicate:
                    Toast.makeText(selectionFeaturesBarFragment.getContext(), "Tạo bản sao", Toast.LENGTH_LONG).show();
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
}
