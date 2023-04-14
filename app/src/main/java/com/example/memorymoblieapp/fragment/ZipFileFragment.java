package com.example.memorymoblieapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.ImageListAdapter;
import com.example.memorymoblieapp.adapter.ZipFileAdapter;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.example.memorymoblieapp.main.MainActivity;

import java.util.ArrayList;

public class ZipFileFragment extends Fragment {
    ArrayList<String> zipList;
    @SuppressLint("StaticFieldLeak")
    static ZipFileAdapter adapter;
    private Context context;

    public ZipFileFragment(ArrayList<String> zipList) {
        this.zipList = zipList;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View zipFileFragment;
        zipFileFragment = inflater.inflate(R.layout.zip_file_fragment, container, false);

        RecyclerView recycler = zipFileFragment.findViewById(R.id.zipRecView);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );


            // LayoutManager
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recycler.setLayoutManager(gridLayoutManager);

            // Divider
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler.getContext(), gridLayoutManager.getOrientation());
            recycler.addItemDecoration(dividerItemDecoration);

        adapter = new ZipFileAdapter(zipList, zipFileFragment.getContext());
        recycler.setAdapter(adapter);

        return zipFileFragment;
    }

    @SuppressLint("NotifyDataSetChanged")
    static public void updateItem() {
        adapter.notifyDataSetChanged();
    }
}
