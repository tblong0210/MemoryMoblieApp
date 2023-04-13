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

public class TrashBinSelectionBarFragment extends Fragment {
    BottomNavigationView bottomNavigationView;
    ArrayList<String> listSelect;

    @SuppressLint("NonConstantResourceId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View selectionFeaturesBarFragment = inflater.inflate(R.layout.trash_bin_selection_bar, container, false);
        bottomNavigationView = selectionFeaturesBarFragment.findViewById(R.id.navigation_view);
        Context context = selectionFeaturesBarFragment.getContext();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            listSelect = new ArrayList<>(ImageAdapter.getListSelect());

            switch (item.getItemId()) {
                case R.id.restore:
                    DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                MainActivity.deletedImageList.removeAll(listSelect);
                                DataLocalManager.saveData(KeyData.TRASH_LIST.getKey(), MainActivity.deletedImageList);

                                MainActivity.updateData(context);

                                // Refresh and exit choose image mode
                                refresh(context);
                                Toast.makeText(context, context.getString(R.string.toast_restore_successfully), Toast.LENGTH_SHORT).show();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getString(R.string.alert_dialog_restore_images_confirm)).setPositiveButton(context.getString(R.string.alert_dialog_confirm), dialogClickListener)
                            .setNegativeButton(context.getString(R.string.alert_dialog_cancel), dialogClickListener).show();

                    return true;

                case R.id.delete:
                    DialogInterface.OnClickListener dialogClickListener1 = (dialog, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                for (String filePath : listSelect) {
                                    File sourceFile = new File(filePath);
                                    if (sourceFile.delete()) {
                                        Toast.makeText(context, context.getString(R.string.toast_deleted) + " '" + sourceFile.getName() + "'", Toast.LENGTH_LONG).show();
                                        MainActivity.deletedImageList.remove(filePath);
                                    } else {
                                        Toast.makeText(context, context.getString(R.string.toast_cannot_delete) + " '" + sourceFile.getName() + "'", Toast.LENGTH_LONG).show();
                                    }
                                }

                                DataLocalManager.saveData(KeyData.TRASH_LIST.getKey(), MainActivity.deletedImageList);
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
                    builder1.setMessage(context.getString(R.string.alert_dialog_permanently_delete_confirm)).setPositiveButton(context.getString(R.string.alert_dialog_confirm), dialogClickListener1)
                            .setNegativeButton(context.getString(R.string.alert_dialog_cancel), dialogClickListener1).show();

                    return true;
            }

            return false;
        });

        return selectionFeaturesBarFragment;
    }

    void refresh(Context context) {
        // Refresh and exit choose image mode
        ImageFragment2 imageFragment = new ImageFragment2(MainActivity.deletedImageList, context.getString(R.string.title_recycle_bin), "TrashBin");
        AppCompatActivity activity = (AppCompatActivity) context;
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content, imageFragment).commit();

        MainActivity.getFrameLayoutSelectionFeaturesBar().removeAllViews();
        MainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
        GalleryAdapter.clearListSelect();
    }
}