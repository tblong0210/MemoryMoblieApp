package com.example.memorymoblieapp.fragment;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.memorymoblieapp.R;

public class TitleContentContainerFragment extends Fragment {
    private boolean detailed = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.title_content_container, container, false);
        ImageButton imgBtnChangeView = (ImageButton) view.findViewById(R.id.imgBtnChangeView);
        imgBtnChangeView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if (detailed) {
                    setDetailed(false);
                    imgBtnChangeView.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.ic_view_grid));
                } else {
                    setDetailed(true);
                    imgBtnChangeView.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.ic_view_detail));
                }
            }
        });

        return view;
    }

    public void setDetailed(boolean detailed) {
        this.detailed = detailed;
    }
}
