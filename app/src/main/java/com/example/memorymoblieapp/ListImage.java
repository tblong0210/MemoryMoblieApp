package com.example.memorymoblieapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

public class ListImage extends BaseAdapter  {

    private Context mContext;

    public int[] imageArray = {
        R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4
    };

    public ListImage(Context mContext)
    {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return imageArray.length;
    }

    @Override
    public Object getItem(int i) {
        return imageArray[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(
                mContext
        );
        imageView.setImageResource( imageArray[i] );
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams( new ViewGroup.LayoutParams(340,350));

        return imageView;
    }
}
