package com.example.memorymoblieapp.fragment;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.main.MainActivity;

public class TitleContentContainerFragment extends Fragment {
    private boolean detailed;
    private String title;

    public TitleContentContainerFragment(boolean detailed, String title) {
        this.detailed = detailed;
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.title_content_container, container, false);
        ImageButton imgBtnChangeView = (ImageButton) view.findViewById(R.id.imgBtnChangeView);
        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        txtTitle.setText(title);
        imgBtnChangeView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if (detailed) {
                    setDetailed(false);
                    MainActivity.detailed = false;
                    imgBtnChangeView.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.ic_view_grid));
                } else {
                    setDetailed(true);
                    MainActivity.detailed = true;
                    imgBtnChangeView.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.ic_view_detail));
                }
                ImageFragment2 imageFragment = new ImageFragment2(MainActivity.detailed);
                getParentFragmentManager().beginTransaction().replace(R.id.frame_layout_content, imageFragment).commit();
            }
        });

        return view;
    }

    public void setDetailed(boolean detailed) {
        this.detailed = detailed;
    }
}
