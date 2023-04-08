package com.example.memorymoblieapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.memorymoblieapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SelectionFeaturesBarFragment extends Fragment {
    BottomNavigationView bottomNavigationView;

    @SuppressLint("NonConstantResourceId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View selectionFeaturesBarFragment = inflater.inflate(R.layout.selection_features_bar, container, false);
        bottomNavigationView = selectionFeaturesBarFragment.findViewById(R.id.navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.add2album:
                    Toast.makeText(selectionFeaturesBarFragment.getContext(), "Thêm vào album", Toast.LENGTH_LONG).show();
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
