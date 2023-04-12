package com.example.memorymoblieapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.SettingsAdapter;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {
    ArrayList<String> settings;
    SettingsAdapter adapter;
    private Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Boolean isThemeDark = DataLocalManager.getBooleanData(KeyData.DARK_MODE.getKey());
        isThemeDark = isThemeDark == null ? false : isThemeDark;

        getActivity().setTheme(isThemeDark ? R.style.ThemeDark_MemoryMobileApp : R.style.Theme_MemoryMobileApp);

        View settingsFragment = inflater.inflate(R.layout.settings_fragment, container, false);
        RecyclerView recycler = settingsFragment.findViewById(R.id.settingsRecView);
        settings = new ArrayList<>();
        addSettings();

        // LayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        // Divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler.getContext(), gridLayoutManager.getOrientation());
        recycler.addItemDecoration(dividerItemDecoration);

        recycler.setLayoutManager(gridLayoutManager);

        adapter = new SettingsAdapter(settings, context);
        recycler.setAdapter(adapter);

        return settingsFragment;
    }

    private void addSettings() {
        settings.add("Ngôn ngữ");
        settings.add("Chế độ tối");
        settings.add("Khóa album");
    }
}
