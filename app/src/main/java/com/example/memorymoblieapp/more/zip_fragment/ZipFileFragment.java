package com.example.memorymoblieapp.more.zip_fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;

import java.util.ArrayList;

public class ZipFileFragment extends Fragment {
    ArrayList<String> zipList;
    @SuppressLint("StaticFieldLeak")
    static ZipFileAdapter adapter;
    private Context context;

    public ZipFileFragment(){
        this.context = null;
        this.zipList = new ArrayList<>();
    }

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
