package com.example.memorymoblieapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.GalleryAdapter;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ImageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerview;
    private ArrayList<String> images;
    List<String> imageDates = new ArrayList<>();
    private  int numberCol = 3;
    public ImageFragment(ArrayList<String>images, List<String> imageDates ) {
        this.images=images;
        // Required empty public constructor
        this.imageDates = imageDates;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        images = DataLocalManager.getStringList(KeyData.IMAGE_PATH_VIEW_LIST.getKey());

        recyclerview = view.findViewById(R.id.recyclerview_gallery_images);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), numberCol);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setHasFixedSize(true);
        GalleryAdapter galleryAdapter = new GalleryAdapter(getContext(), images, imageDates, new GalleryAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(String path) {
                Log.d("tag",path);
            }
            @Override
            public  void onPhotoLongClick(String path) {
                Log.d("tag","Long" +path);
            }
        });
        recyclerview.setAdapter(galleryAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        recyclerview = view.findViewById(R.id.recyclerview_gallery_images);
//        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
//        recyclerview.setLayoutManager(layoutManager);
//        recyclerview.setHasFixedSize(true);
//        GalleryAdapter galleryAdapter = new GalleryAdapter(getContext(), images);
//        recyclerview.setAdapter(galleryAdapter);


    }


}
