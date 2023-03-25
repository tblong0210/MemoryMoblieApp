package com.example.memorymoblieapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.ImageAdapter;
import com.example.memorymoblieapp.main.MainActivity;
import com.example.memorymoblieapp.obj.Image;

import java.util.ArrayList;

public class ImageFragment2 extends Fragment {
    ArrayList<Image> imageList;
    ImageAdapter adapter;
    private Context context;
    private final String title;

    public ImageFragment2(ArrayList<Image> imageList, String title) {
        this.imageList = imageList;
        this.title = title;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View imagesFragment;
        if (MainActivity.detailed)
            imagesFragment = inflater.inflate(R.layout.image_detail_fragment, container, false);
        else
            imagesFragment = inflater.inflate(R.layout.image_fragment, container, false);

        RecyclerView recycler = imagesFragment.findViewById(R.id.imageRecView);

        TextView txtTitle = (TextView) imagesFragment.findViewById(R.id.txtTitle);
        txtTitle.setText(title);

        ImageButton imgBtnChangeView = (ImageButton) imagesFragment.findViewById(R.id.imgBtnChangeView);

        if (MainActivity.detailed)
            imgBtnChangeView.setImageDrawable(imagesFragment.getContext().getResources().getDrawable(R.drawable.ic_view_detail));
        else
            imgBtnChangeView.setImageDrawable(imagesFragment.getContext().getResources().getDrawable(R.drawable.ic_view_grid));

        imgBtnChangeView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                MainActivity.detailed = !MainActivity.detailed;
                ImageFragment2 imageFragment = new ImageFragment2(imageList, title);

                @SuppressLint("CommitTransaction") FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout_content, imageFragment).commit();
                fragmentTransaction.addToBackStack("image");

                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();
            }
        });

        if (MainActivity.detailed) {
            // LayoutManager
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recycler.setLayoutManager(gridLayoutManager);

            // Divider
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler.getContext(), gridLayoutManager.getOrientation());
            recycler.addItemDecoration(dividerItemDecoration);
        } else {
            // LayoutManager
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recycler.setLayoutManager(gridLayoutManager);
        }

        adapter = new ImageAdapter(imageList, context, MainActivity.detailed);
        recycler.setAdapter(adapter);

        return imagesFragment;
    }
}