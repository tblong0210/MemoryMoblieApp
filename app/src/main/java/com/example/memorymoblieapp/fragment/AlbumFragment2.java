package com.example.memorymoblieapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.AlbumAdapter;
import com.example.memorymoblieapp.obj.Album;

import java.util.ArrayList;

public class AlbumFragment2 extends Fragment {
    public static ArrayList<Album> albumList;
    @SuppressLint("StaticFieldLeak")
    static AlbumAdapter adapter;
    private Context context;

    public AlbumFragment2(ArrayList<Album> albumList) {
        AlbumFragment2.albumList = albumList;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View albumsFragment = inflater.inflate(R.layout.album_fragment, container, false);
        RecyclerView recycler = albumsFragment.findViewById(R.id.albumRecView);

        // LayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recycler.setLayoutManager(gridLayoutManager);

        if (albumList.isEmpty() || !albumList.get(0).getName().isBlank())
            albumList.add(0, new Album("", new ArrayList<>(), R.mipmap.ic_add_album));

        adapter = new AlbumAdapter(albumList, context);
        recycler.setAdapter(adapter);

        return albumsFragment;
    }

    @SuppressLint("NotifyDataSetChanged")
    static public void updateItem(int position) {
        adapter.notifyItemChanged(position);
    }
}