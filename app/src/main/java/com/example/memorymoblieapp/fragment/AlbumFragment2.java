package com.example.memorymoblieapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.AlbumAdapter;
import com.example.memorymoblieapp.obj.Album;

import java.util.ArrayList;

public class AlbumFragment2 extends Fragment {
    ArrayList<Album> albumList;
    AlbumAdapter adapter;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View albumsFragment = inflater.inflate(R.layout.album_fragment, container, false);
        RecyclerView recycler = albumsFragment.findViewById(R.id.albumRecView);

        // LayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recycler.setLayoutManager(gridLayoutManager);

        albumList = new ArrayList<Album>();
        albumList.add(new Album("", -1, R.mipmap.ic_add_album));
        addAlbumList();
        adapter = new AlbumAdapter(albumList, context);
        recycler.setAdapter(adapter);

        return albumsFragment;
    }

    private void addAlbumList() {
        albumList.add(new Album("Album1", 3, R.drawable.image1));
        albumList.add(new Album("Album2", 3, R.drawable.image1));
        albumList.add(new Album("Album3", 3, R.drawable.image1));
        albumList.add(new Album("Album4", 3, R.drawable.image1));
        albumList.add(new Album("Album5", 3, R.drawable.image1));
        albumList.add(new Album("Album6", 3, R.drawable.image1));
        albumList.add(new Album("Album7", 3, R.drawable.image1));
        albumList.add(new Album("Album8", 3, R.drawable.image1));
        albumList.add(new Album("Album9", 3, R.drawable.image1));
        albumList.add(new Album("Album10", 3, R.drawable.image1));
    }
}