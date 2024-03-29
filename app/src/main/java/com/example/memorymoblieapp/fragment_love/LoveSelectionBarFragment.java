package com.example.memorymoblieapp.fragment_love;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.fragment_main.method.ShareImageToMedia;
import com.example.memorymoblieapp.fragment_main.GalleryAdapter;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.example.memorymoblieapp.activity.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class LoveSelectionBarFragment extends Fragment {
    BottomNavigationView bottomNavigationView;
    ArrayList<String> listSelect;
    ArrayList<String> albumsName;

    public LoveSelectionBarFragment() {
        listSelect = new ArrayList<>();
        albumsName = new ArrayList<>();
    }

    @SuppressLint("NonConstantResourceId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View selectionFeaturesBarFragment = inflater.inflate(R.layout.love_selection_bar, container, false);
        bottomNavigationView = selectionFeaturesBarFragment.findViewById(R.id.navigation_view);
        Context context = selectionFeaturesBarFragment.getContext();
        albumsName = new ArrayList<>(DataLocalManager.getSetList(KeyData.ALBUM_NAME_LIST.getKey()));

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            listSelect = new ArrayList<>(ImageListAdapter.getListSelect());

            switch (item.getItemId()) {
                case R.id.add2album:
                    if (albumsName.isEmpty()) {
                        Toast.makeText(context, context.getString(R.string.toast_no_albums), Toast.LENGTH_SHORT).show();
                    } else {
                        final int[] albumChosenPos = new int[1];
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(context.getString(R.string.alert_dialog_select_album));

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
                        builder.setPositiveButton(context.getString(R.string.alert_dialog_confirm), null);
                        builder.setNegativeButton(context.getString(R.string.alert_dialog_cancel), null);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                            if (albumChosenPos[0] >= 0 && !albumsName.isEmpty()) {
                                MainActivity.albumList.get(albumChosenPos[0]).insertNewImageArray(listSelect);
                                DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
                                Toast.makeText(context, context.getString(R.string.toast_add_images_to) + " '" + albumsName.get(albumChosenPos[0]) + "'", Toast.LENGTH_SHORT).show();

                                // Refresh and exit choose image mode
                                refresh(context);

                                dialog.dismiss();
                            }
                        });
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog.cancel());
                    }

                    return true;

                case R.id.unlove:
                    DialogInterface.OnClickListener dialogClickListener2 = (dialog, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                MainActivity.lovedImageList.removeAll(listSelect);
                                DataLocalManager.saveData(KeyData.FAVORITE_LIST.getKey(), MainActivity.lovedImageList);

                                refresh(context);
                                Toast.makeText(context, context.getString(R.string.toast_remove_images_from_loves), Toast.LENGTH_SHORT).show();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    };
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                    builder2.setMessage(context.getString(R.string.alert_dialog_remove_images_from_loves_confirm)).setPositiveButton(context.getString(R.string.alert_dialog_confirm), dialogClickListener2).setNegativeButton(context.getString(R.string.alert_dialog_cancel), dialogClickListener2).show();

                    return true;

                case R.id.duplicate:
                    DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                duplicate(listSelect);
                                MainActivity.updateData(context);

                                // Refresh and exit choose image mode
                                refresh(context);
                                Toast.makeText(context, context.getString(R.string.toast_add_duplicate_successfully), Toast.LENGTH_SHORT).show();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getString(R.string.alert_dialog_duplicate_images_confirm)).setPositiveButton(context.getString(R.string.alert_dialog_confirm), dialogClickListener).setNegativeButton(context.getString(R.string.alert_dialog_cancel), dialogClickListener).show();

                    return true;

                case R.id.share:
                    ShareImageToMedia shareImageToMedia = new ShareImageToMedia(listSelect, context);
                    shareImageToMedia.sharePictures();

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
                MainActivity.lovedImageList.add(newFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        DataLocalManager.saveData(KeyData.FAVORITE_LIST.getKey(), MainActivity.lovedImageList);
    }

    void refresh(@NonNull Context context) {
        // Refresh and exit choose image mode
        ImageListFragment imageFragment = new ImageListFragment(MainActivity.lovedImageList, context.getString(R.string.title_loves), "Love");
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