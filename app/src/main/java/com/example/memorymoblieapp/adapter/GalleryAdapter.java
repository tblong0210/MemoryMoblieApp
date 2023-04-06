package com.example.memorymoblieapp.adapter;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.memorymoblieapp.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context context;
    private List<String> images;
//    protected PhotoListener photoListener;
    List<String> imageDates;
    protected PhotoListener photoListener;

    private  boolean isLongClick =false;
    private Vector<String> listSelect = new Vector<String>();

    public GalleryAdapter(Context context, List<String> images, List<String> imageDates,  PhotoListener photoListener)
    {
        this.context = context;
        this.images = images;
        this.imageDates = imageDates;
//        , PhotoListener photoListener
//        this.photoListener = photoListener;
          this.photoListener = photoListener;
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
    public void onBindViewHolder( ViewHolder holder, int position) {

        String image = images.get(position);

        Glide.with(context).load(image)
//                .placeholder(R.drawable.image1)
//                .error(R.drawable.image3)
                .into(holder.image);

        holder.textDate.setVisibility(View.VISIBLE);
        if(position==0 || imageDates.get(position-1).equals(" ")==true)
            holder.textDate.setText(imageDates.get(position));
        else
            holder.textDate.setText(" ");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // width
                LinearLayout.LayoutParams.WRAP_CONTENT // height
        );
        holder.textDate.setLayoutParams(layoutParams);

              if( imageDates.get(position).equals(" ")==false) {

            if (isLongClick) {
                
                if (listSelect.contains(image)) {
                    Drawable drawable = context.getDrawable(R.drawable.circle_shape);
                    holder.iconLongSelect.setBackground(drawable);
                }
                holder.iconLongSelect.setVisibility(View.VISIBLE);
            }
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
                }

                return false;
            }
        });

//        formatter.format(imageDates.get(position))

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                photoListener.onPhotoClick(image);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView textDate;
        ToggleButton iconLongSelect;
        Button buttonCancelSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageGallery);
            textDate = itemView.findViewById(R.id.headDate);
            iconLongSelect = itemView.findViewById(R.id.toggleButton);

        }
    }

        public interface PhotoListener {
            void onPhotoClick(String path);

            void onPhotoLongClick(String path);
        }

}
