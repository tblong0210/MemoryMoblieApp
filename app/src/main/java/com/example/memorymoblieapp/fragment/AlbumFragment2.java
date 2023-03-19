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
import com.example.memorymoblieapp.obj.Image;

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
        albumList.add(new Album("", new ArrayList<Image>(), R.mipmap.ic_add_album));
        addAlbumList();
        adapter = new AlbumAdapter(albumList, context);
        recycler.setAdapter(adapter);

        return albumsFragment;
    }

    private void addAlbumList() {
        ArrayList<Image> imgList = new ArrayList<Image>();
        imgList.add(new Image("image1.png", "9.27 KB", "20/1/2023", "512 x 512", "TP.HCM", R.drawable.image1));
        albumList.add(new Album("Album1", new ArrayList<Image>(), R.drawable.image1));
        albumList.add(new Album("Album2", imgList, R.drawable.image1));
        albumList.add(new Album("Album3", imgList, R.drawable.image1));
        albumList.add(new Album("Album4", imgList, R.drawable.image1));
        albumList.add(new Album("Album5", imgList, R.drawable.image1));
        albumList.add(new Album("Album6", imgList, R.drawable.image1));
        albumList.add(new Album("Album7", imgList, R.drawable.image1));
        albumList.add(new Album("Album8", imgList, R.drawable.image1));
        albumList.add(new Album("Album9", imgList, R.drawable.image1));
        albumList.add(new Album("Album10", imgList, R.drawable.image1));
    }
}