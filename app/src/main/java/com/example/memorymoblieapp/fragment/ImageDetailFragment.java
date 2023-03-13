package com.example.memorymoblieapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.ImageDetailAdapter;
import com.example.memorymoblieapp.obj.Image;

import java.util.ArrayList;

public class ImageDetailFragment extends Fragment {
    ArrayList<Image> ImageList;
    ImageDetailAdapter adapter;
    private Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View imagesFragment = inflater.inflate(R.layout.image_fragment, container, false);
        RecyclerView recycler = imagesFragment.findViewById(R.id.imageRecView);

        // LayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recycler.setLayoutManager(gridLayoutManager);

        // Divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler.getContext(), gridLayoutManager.getOrientation());
        recycler.addItemDecoration(dividerItemDecoration);

        ImageList = new ArrayList<Image>();
        addImageList();
        adapter = new ImageDetailAdapter(ImageList, context);
        recycler.setAdapter(adapter);

        return imagesFragment;
    }

    private void addImageList() {
        ImageList.add(new Image("Test.png", "9.27 KB", "20/1/2023","512 x 512", "TP.HCM", R.drawable.image1));
        ImageList.add(new Image("Test.png", "9.27 KB", "20/1/2023","512 x 512", "TP.HCM", R.drawable.image1));
        ImageList.add(new Image("Test.png", "9.27 KB", "20/1/2023","512 x 512", "TP.HCM", R.drawable.image1));
        ImageList.add(new Image("Test.png", "9.27 KB", "20/1/2023","512 x 512", "TP.HCM", R.drawable.image1));
        ImageList.add(new Image("Test.png", "9.27 KB", "20/1/2023","512 x 512", "TP.HCM", R.drawable.image1));
        ImageList.add(new Image("Test.png", "9.27 KB", "20/1/2023","512 x 512", "TP.HCM", R.drawable.image1));
        ImageList.add(new Image("Test.png", "9.27 KB", "20/1/2023","512 x 512", "TP.HCM", R.drawable.image1));
        ImageList.add(new Image("Test.png", "9.27 KB", "20/1/2023","512 x 512", "TP.HCM", R.drawable.image1));
        ImageList.add(new Image("Test.png", "9.27 KB", "20/1/2023","512 x 512", "TP.HCM", R.drawable.image1));
        ImageList.add(new Image("Test.png", "9.27 KB", "20/1/2023","512 x 512", "TP.HCM", R.drawable.image1));
        ImageList.add(new Image("Test.png", "9.27 KB", "20/1/2023","512 x 512", "TP.HCM", R.drawable.image1));
        ImageList.add(new Image("Test.png", "9.27 KB", "20/1/2023","512 x 512", "TP.HCM", R.drawable.image1));
        ImageList.add(new Image("Test.png", "9.27 KB", "20/1/2023","512 x 512", "TP.HCM", R.drawable.image1));
    }
}
