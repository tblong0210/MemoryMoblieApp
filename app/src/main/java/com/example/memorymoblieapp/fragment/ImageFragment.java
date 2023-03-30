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

import java.util.ArrayList;


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
    public ImageFragment(ArrayList<String>images) {
        this.images=images;
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        recyclerview = view.findViewById(R.id.recyclerview_gallery_images);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setHasFixedSize(true);
        GalleryAdapter galleryAdapter = new GalleryAdapter(getContext(), images);
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
