package com.example.memorymoblieapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.GalleryAdapter;
import com.example.memorymoblieapp.view.ViewSearch;

import java.io.File;
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
    private ImageView searchBtn ;
    private ImageView sortBtn;
    private TextView cancelLongClick;
    private  int numberCol = 3;
    private GalleryAdapter galleryAdapter;

    public ImageFragment(){
        images = new ArrayList<>();
        imageDates = new ArrayList<>();
    }

    public ImageFragment(ArrayList<String>images, List<String> imageDates ) {
        this.images=images;
        // Required empty public constructor
        this.imageDates = imageDates;
    }

    public void setListImagesAndDates(  ArrayList<String>images, List<String> imageDates){

        this.images=images;
        // Required empty public constructor
        this.imageDates = imageDates;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        searchBtn = view.findViewById(R.id.imgBtnSearch);
        sortBtn = view.findViewById(R.id.sort_image_button);
        cancelLongClick = view.findViewById(R.id.button_cancel_select);
        recyclerview = view.findViewById(R.id.recyclerview_gallery_images);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), numberCol);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setHasFixedSize(true);

        galleryAdapter = new GalleryAdapter(getContext(), images, imageDates, new GalleryAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(String path) {
                Log.d("tag",path);
            }
            @Override
            public  void onPhotoLongClick(String path) {
                Log.d("tag","Long" +path);
                cancelLongClick.setVisibility(View.VISIBLE);

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ViewSearch.class);
                startActivity(intent);
            }
        });
        cancelLongClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelLongClick.setVisibility(View.INVISIBLE);

                galleryAdapter.setIsLongClick(false);
                galleryAdapter.setListSelect();
                galleryAdapter.notifyDataSetChanged();
            }
        });
        recyclerview.setAdapter(galleryAdapter);
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag","Clicked sort button");

                ArrayList<String> newImageDateFlag = new ArrayList<>();;
                ArrayList<String> newImagesFlag = new   ArrayList<String>();
                ArrayList<String> newImageDate = new ArrayList<>();;
                ArrayList<String> newImages = new   ArrayList<String>();
                int flag =0;

                for (int i=0;i<images.size();i++) {
                    if(images.get(i)!=" ") {
                        newImageDateFlag.add( imageDates.get(i));
                        newImagesFlag.add(images.get(i));
             /*           newImageDateFlag.add( Integer.toString(i));
                        newImagesFlag.add(Integer.toString(i));*/

                    }
                }
                String  temp;
                int n = newImagesFlag.size() ;
                for (int i = 0; i < n/ 2; i++) {
                    temp = newImagesFlag.get(i);
                    newImagesFlag.set(i, newImagesFlag.get(n - 1 - i));
                    newImagesFlag.set(n - 1 - i,temp)  ;

                    temp = newImageDateFlag.get(i);
                    newImageDateFlag.set(i, newImageDateFlag.get(n - 1 - i));
                    newImageDateFlag.set(n - 1 - i,temp)  ;
                }

                for(int i=0; i<newImagesFlag.size();i++)
                {
                    Log.d("Imagess",newImagesFlag.get(i) + " - " + newImageDateFlag.get(i));
                }

                Log.d("Tagggg",newImageDateFlag.size()+ "  -  "+newImageDateFlag.size());

                for (int i=0;i<newImagesFlag.size();i++) {
                    if(newImagesFlag.get(i)!=" ") {
                        if(i!=0 && newImageDateFlag.get(i).equals(newImageDateFlag.get(i-1))==false )
                        {

                            if(flag%3==2)
                            {
                                newImages.add(" ");
                                newImageDate.add(" ");
                            }
                            if(flag%3==1)
                            {
                                newImages.add(" ");
                                newImages.add(" ");
                                newImageDate.add(" ");
                                newImageDate.add(" ");
                            }
                            flag=0;
                        }
                        newImages.add(newImagesFlag.get(i));
                        newImageDate.add(newImageDateFlag.get(i));
                        flag++;

                    }
                }
                images = newImages;
                imageDates = newImageDate;
                galleryAdapter.setImages(newImages);
                galleryAdapter.setImageDates(newImageDate);
                galleryAdapter.notifyDataSetChanged();
            }
        });

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
