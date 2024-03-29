package com.example.memorymoblieapp.fragment_album;

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
import com.example.memorymoblieapp.object.Album;

import java.util.ArrayList;

public class AlbumFragment extends Fragment {
    public static ArrayList<Album> albumList;
    @SuppressLint("StaticFieldLeak")
    static AlbumAdapter adapter;
    private Context context;

    public AlbumFragment() {
        AlbumFragment.albumList = new ArrayList<>();
    }

    public AlbumFragment(ArrayList<Album> albumList) {
        AlbumFragment.albumList = new ArrayList<>(albumList);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View albumsFragment = inflater.inflate(R.layout.album_fragment, container, false);
        RecyclerView recycler = albumsFragment.findViewById(R.id.albumRecView);

        // LayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recycler.setLayoutManager(gridLayoutManager);

        if (albumList.isEmpty() || !albumList.get(0).getAlbumName().isBlank())
            albumList.add(0, new Album(""));

        adapter = new AlbumAdapter(albumList, albumsFragment.getContext());
        recycler.setAdapter(adapter);

        return albumsFragment;
    }

    @SuppressLint("NotifyDataSetChanged")
    static public void updateItem() {
        adapter.notifyDataSetChanged();
    }
}