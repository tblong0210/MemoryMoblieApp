package com.example.memorymoblieapp.fragment_album;

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
import com.example.memorymoblieapp.fragment_love.ImageListFragment;
import com.example.memorymoblieapp.fragment_main.method.ShareImageToMedia;
import com.example.memorymoblieapp.fragment_main.GalleryAdapter;
import com.example.memorymoblieapp.fragment_love.ImageListAdapter;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.example.memorymoblieapp.activity.MainActivity;
import com.example.memorymoblieapp.object.Album;
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
    int albumPos;

    public AlbumSelectionBarFragment() {
        albumPos = 0;
        listSelect = new ArrayList<>();
    }

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

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            listSelect = new ArrayList<>(ImageListAdapter.getListSelect());

            switch (item.getItemId()) {
                case R.id.removeFromAlbum:
                    DialogInterface.OnClickListener dialogClickListener4 = (dialog, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                MainActivity.albumList.get(albumPos).removeImageArray(listSelect);
                                DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);

                                // Refresh and exit choose image mode
                                refresh(context);

                                Toast.makeText(context, context.getString(R.string.toast_remove_images_from_album), Toast.LENGTH_SHORT).show();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    };
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(context);
                    builder4.setMessage(context.getString(R.string.alert_dialog_remove_images_from_album_confirm)).setPositiveButton(context.getString(R.string.alert_dialog_confirm), dialogClickListener4)
                            .setNegativeButton(context.getString(R.string.alert_dialog_cancel), dialogClickListener4).show();


                    return true;

                case R.id.love:
                    DialogInterface.OnClickListener dialogClickListener2 = (dialog, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                MainActivity.lovedImageList.addAll(listSelect);
                                MainActivity.lovedImageList = removeDuplicates(MainActivity.lovedImageList);
                                DataLocalManager.saveData(KeyData.FAVORITE_LIST.getKey(), MainActivity.lovedImageList);

                                refresh(context);
                                Toast.makeText(context, context.getString(R.string.toast_add_images_to_loves), Toast.LENGTH_SHORT).show();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    };
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                    builder2.setMessage(context.getString(R.string.alert_dialog_add_images_to_loves_confirm)).setPositiveButton(context.getString(R.string.alert_dialog_confirm), dialogClickListener2)
                            .setNegativeButton(context.getString(R.string.alert_dialog_cancel), dialogClickListener2).show();

                    return true;

                case R.id.delete:
                    DialogInterface.OnClickListener dialogClickListener1 = (dialog, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                move2TrashBin(listSelect);
                                MainActivity.updateData(context);

                                // Refresh and exit choose image mode
                                refresh(context);
                                Toast.makeText(context, context.getString(R.string.toast_delete_successfully), Toast.LENGTH_SHORT).show();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    };
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage(context.getString(R.string.alert_dialog_delete_images_confirm)).setPositiveButton(context.getString(R.string.alert_dialog_confirm), dialogClickListener1)
                            .setNegativeButton(context.getString(R.string.alert_dialog_cancel), dialogClickListener1).show();

                    return true;

                case R.id.more:
                    PopupMenu popupMenu = new PopupMenu(context, bottomNavigationView, Gravity.END);
                    popupMenu.inflate(R.menu.album_selection_bar_more_menu);
                    popupMenu.setOnMenuItemClickListener(menuItem -> {
                        int itemId = menuItem.getItemId();

                        if (R.id.share == itemId) {
                            ShareImageToMedia shareImageToMedia = new ShareImageToMedia(listSelect, context);
                            shareImageToMedia.sharePictures();
                        } else if (R.id.duplicate == itemId) {
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
                            builder.setMessage(context.getString(R.string.alert_dialog_duplicate_images_confirm)).setPositiveButton(context.getString(R.string.alert_dialog_confirm), dialogClickListener)
                                    .setNegativeButton(context.getString(R.string.alert_dialog_cancel), dialogClickListener).show();
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
        ImageListFragment imageFragment = new ImageListFragment(MainActivity.albumList.get(albumPos).getPathImages(), MainActivity.albumList.get(albumPos).getAlbumName(), "Album", albumPos);
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