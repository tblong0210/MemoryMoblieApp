package com.example.memorymoblieapp.activity.page_image.control_image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.memorymoblieapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {
    private static final int MAX_CACHE_SIZE = 16;

    private Context context;
    private ArrayList<String> pictureFiles;
    private LayoutInflater mLayoutInflater;
    private ZoomableImageView imageView;


    static HashMap<String, Drawable> drawableCache = new HashMap<String, Drawable>();

    static private Drawable getDrawable(String key) {

        //Clear data when the memory is too large
        if (drawableCache.size() >= MAX_CACHE_SIZE) {
            drawableCache.clear();
        }

        //If there isn't the drawable exists => store it
        if (!drawableCache.containsKey(key)) {
            drawableCache.put(key, Drawable.createFromPath(key));
        }

        return drawableCache.get(key);
    }

    public ZoomableImageView getImageView() {
        return imageView;
    }

    // Viewpager Constructor
    public ViewPagerAdapter(Context context, ArrayList<String> pictureFiles) {
        this.context = context;
        this.pictureFiles = pictureFiles;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return pictureFiles.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }

    public boolean checkItem(int position){
        return pictureFiles.get(position).equals(" ");
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        while(pictureFiles.get(position).equals(" "))
//            position++;

        // Inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.control_image_full, container, false);

        // Referencing the image view from the item.xml file
        ZoomableImageView view = itemView.findViewById(R.id.largePictureFull);

        // Set the image in the imageView
        view.setImageDrawable(getDrawable(pictureFiles.get(position)));

        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
//        while(pictureFiles.get(position).equals(" "))
//            position++;
        imageView = ((View) object).findViewById(R.id.largePictureFull);
        imageView.setImageDrawable(getDrawable(pictureFiles.get(position)));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

}