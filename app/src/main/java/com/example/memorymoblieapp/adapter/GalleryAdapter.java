package com.example.memorymoblieapp.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.fragment.ImageFragment2;
import com.example.memorymoblieapp.fragment.SelectionFeaturesBarFragment;
import com.example.memorymoblieapp.main.MainActivity;
import com.example.memorymoblieapp.view.ViewImage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context context;
    private List<String> images;
    protected PhotoListener photoListener;
    List<String> imageDates;
    private  boolean isLongClick =false;
    private static Vector<String> listSelect = new Vector<String>();
    public GalleryAdapter(Context context, List<String> images, List<String> imageDates, PhotoListener photoListener)
    {
        this.context = context;
        this.images = images;
        this.imageDates = imageDates;

//        , PhotoListener photoListener
        this.photoListener = photoListener;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setImageDates(List<String> imageDates) {
        this.imageDates = imageDates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
//        return new ViewHolder(
//                LayoutInflater.from(context).inflate(R.layout.gallery_item,parent,false)
//        );

        View v =LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String image = images.get(position);
        Glide.with(context).load(image)
//                .placeholder(R.drawable.image1)
//                .error(R.drawable.image3)
                .into(holder.image);

        holder.textDate.setVisibility(View.VISIBLE);
        if(position==0 || imageDates.get(position-1).equals(" ")==true || (position >0 &&imageDates.get(position-1).equals(imageDates.get(position))==false) )
        {
            holder.textDate.setText(imageDates.get(position));
        }
        else
        {
            holder.textDate.setText(" ");
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // width
                LinearLayout.LayoutParams.WRAP_CONTENT // height
        );
        holder.textDate.setLayoutParams(layoutParams);
        if (position==0
                || position==1 || position==2 ||  (position >0 &&imageDates.get(position-1).equals(imageDates.get(position))==false)
                || (position >1 &&imageDates.get(position-2).equals(imageDates.get(position))==false)
                || (position >2 &&imageDates.get(position-3).equals(imageDates.get(position))==false))
        {
            ViewGroup.MarginLayoutParams layoutParamss =
                    (ViewGroup.MarginLayoutParams) holder.textDate.getLayoutParams();
            layoutParamss.setMargins(30, 50, 16, 10);
            holder.textDate.setLayoutParams(layoutParamss);
        }

//        formatter.format(imageDates.get(position))
        if(image.equals(" ")==false) {

            if (isLongClick) {
                
                if (listSelect.contains(image)) {
                    Drawable drawable = context.getDrawable(R.drawable.circle_shape);
                    holder.iconLongSelect.setBackground(drawable);
                }
                int sizeInDp = 25;
                int sizeInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sizeInDp,context.getResources().getDisplayMetrics());
                LinearLayout.LayoutParams layoutParamSelect = new LinearLayout.LayoutParams(sizeInPixels, sizeInPixels);
                holder.iconLongSelect.setLayoutParams(layoutParamSelect);

                holder.iconLongSelect.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            holder.iconLongSelect.setVisibility(View.INVISIBLE);
        }


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLongClick)
                {
                    if(listSelect.contains(image))
                    {
                        Drawable drawable = context.getDrawable(R.drawable.circle_unfill);
                        holder.iconLongSelect.setBackground(drawable);

                        listSelect.remove(image);
                    }
                    else {
                        Drawable drawable = context.getDrawable(R.drawable.circle_shape);
                        holder.iconLongSelect.setBackground(drawable);
                        listSelect.add(image);
                    }
                }
                else {
                    Intent intent = new Intent(context, ViewImage.class);
                    intent.putExtra("path_image", images.get(position));
                    context.startActivity(intent);
                    photoListener.onPhotoClick(image);
                }
            }


        });

        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                photoListener.onPhotoLongClick(image);
                if(isLongClick==false)
                {
                    listSelect.add(image);
                    isLongClick=true;
                    notifyDataSetChanged();

                    // Display selection features bar, hide bottom navigation bar
                    SelectionFeaturesBarFragment selectionFeaturesBarFragment = new SelectionFeaturesBarFragment("Home");
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout_selection_features_bar, selectionFeaturesBarFragment).commit();
                    fragmentTransaction.addToBackStack("selectImage");
                    MainActivity.getBottomNavigationView().setVisibility(View.GONE);
                }

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image ;
        TextView textDate;
        ToggleButton iconLongSelect;
        Button buttonCancelSelect;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.imageGallery);
            textDate = itemView.findViewById(R.id.headDate);
            iconLongSelect = itemView.findViewById(R.id.toggleButton);
            buttonCancelSelect = itemView.findViewById(R.id.button_cancel_select);
            
        }
    }

    public interface  PhotoListener{
        void onPhotoClick(String path);
        void onPhotoLongClick(String path);
    }

    public static Vector<String> getListSelect() {
        return listSelect;
    }
}
