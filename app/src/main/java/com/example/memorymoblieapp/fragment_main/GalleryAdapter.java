package com.example.memorymoblieapp.fragment_main;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.memorymoblieapp.R;

import com.example.memorymoblieapp.activity.MainActivity;
import com.example.memorymoblieapp.activity.page_image.ViewImage;
import com.example.memorymoblieapp.fragment_love.ImageListAdapter;
import com.example.memorymoblieapp.local_data_storage.KeyData;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context context;
    private List<String> images;
    protected PhotoListener photoListener;
    List<String> imageDates;
    private boolean isLongClick = false;
    Button cancelLongClickButton;
    private static Vector<String> listSelect = new Vector<String>();
    public static ArrayList<ViewHolder> holders;

    public GalleryAdapter() {
        context = null;
        images = new ArrayList<>();
        imageDates = new ArrayList<>();
        holders = new ArrayList<>();
    }

    public GalleryAdapter(Context context, List<String> images, List<String> imageDates, PhotoListener photoListener) {
        this.context = context;
        this.images = images;
        this.imageDates = imageDates;
        this.photoListener = photoListener;
        holders = new ArrayList<>();
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public boolean getisLongClick() {
        return isLongClick;
    }

    public void setIsLongClick(boolean isLongClick) {
        this.isLongClick = isLongClick;
    }

    public void setListSelect() {
        listSelect.clear();
        listSelect.clear();
    }

    public void setImageDates(List<String> imageDates) {
        this.imageDates = imageDates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String image = images.get(position);
        Glide.with(context).load(image)
                .into(holder.image);

        // set text ngày tạo ảnh
        holder.textDate.setVisibility(View.VISIBLE);
        if (position == 0 || imageDates.get(position - 1).equals(" ") || (!imageDates.get(position - 1).equals(imageDates.get(position)))) {
            holder.textDate.setText(imageDates.get(position));
        } else {
            holder.textDate.setText(" ");
        }

        if (position == 0
                || position == 1 || position == 2 ||
                ((!imageDates.get(position).equals(" ")) && (!imageDates.get(position - 1).equals(imageDates.get(position))
                        || (!imageDates.get(position - 2).equals(imageDates.get(position)))
                        || (!imageDates.get(position - 3).equals(imageDates.get(position)))))) {
            //set size text date
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, // width
                    LinearLayout.LayoutParams.WRAP_CONTENT // height
            );

            holder.textDate.setLayoutParams(layoutParams);
            ViewGroup.MarginLayoutParams layoutParamss =
                    (ViewGroup.MarginLayoutParams) holder.textDate.getLayoutParams();
            layoutParamss.setMargins(30, 50, 16, 10);
            holder.textDate.setLayoutParams(layoutParamss);
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    0, 0
            );
            holder.textDate.setLayoutParams(layoutParams);
        }

        Log.d("sizelist", String.valueOf(listSelect.size()));

        if (!image.equals(" ")) {

            if (isLongClick) {

                if (listSelect.contains(image)) {

                    Drawable drawable = context.getDrawable(R.drawable.checked);
                    holder.iconLongSelect.setForeground(drawable);
                    Drawable shadow = context.getDrawable(R.drawable.shadow);
                    holder.iconLongSelect.setBackground(shadow);
                    changeBrightness(holder.image, 0.6f);
                } else {
                    Drawable drawable = context.getDrawable(R.drawable.circle_unfill);
                    holder.iconLongSelect.setForeground(drawable);
                    Drawable shadow = context.getDrawable(R.drawable.shadow);
                    holder.iconLongSelect.setBackground(shadow);
                    changeBrightness(holder.image, 1f);
                }
                int sizeInDp = 25;
                int sizeInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sizeInDp, context.getResources().getDisplayMetrics());
                LinearLayout.LayoutParams layoutParamSelect = new LinearLayout.LayoutParams(sizeInPixels, sizeInPixels);
                holder.iconLongSelect.setLayoutParams(layoutParamSelect);

                holder.iconLongSelect.setVisibility(View.VISIBLE);

            } else {
                LinearLayout.LayoutParams layoutParamSelect = new LinearLayout.LayoutParams(0, 0);
                holder.iconLongSelect.setLayoutParams(layoutParamSelect);
                holder.iconLongSelect.setVisibility(View.INVISIBLE);
            }
        } else {
            LinearLayout.LayoutParams layoutParamSelect = new LinearLayout.LayoutParams(0, 0);
            holder.iconLongSelect.setLayoutParams(layoutParamSelect);
            holder.iconLongSelect.setVisibility(View.INVISIBLE);
        }

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLongClick) {
                    if (listSelect.contains(image)) {

                        Drawable drawable = context.getDrawable(R.drawable.circle_unfill);
                        holder.iconLongSelect.setForeground(drawable);
                        Drawable shadow = context.getDrawable(R.drawable.shadow);
                        holder.iconLongSelect.setBackground(shadow);
                        changeBrightness(holder.image, 1f);
                        listSelect.remove(image);
                    } else {

                        Drawable drawable = context.getDrawable(R.drawable.checked);
                        holder.iconLongSelect.setForeground(drawable);
                        Drawable shadow = context.getDrawable(R.drawable.shadow);
                        holder.iconLongSelect.setBackground(shadow);
                        changeBrightness(holder.image, 0.6f);
                        listSelect.add(image);
                    }
                    notifyDataSetChanged();
                } else {
                    if (!images.get(position).equals(" ")) {
                        Intent intent = new Intent(context, ViewImage.class);
                        intent.putExtra(KeyData.PATH_CURRENT_IMAGE_VIEW.getKey(), images.get(position));
                        context.startActivity(intent);
                        photoListener.onPhotoClick(image);
                    }
                }
            }


        });

        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                photoListener.onPhotoLongClick(image);
                if (isLongClick == false) {
                    listSelect.add(image);
                    isLongClick = true;
                    notifyDataSetChanged();

                    // Display selection features bar, hide bottom navigation bar
                    HomeSelectionBarFragment homeSelectionBarFragment = new HomeSelectionBarFragment();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout_selection_features_bar, homeSelectionBarFragment).commit();
                    fragmentTransaction.addToBackStack("selectImage");

                    MainActivity.getBottomNavigationView().setVisibility(View.GONE);
                    MainActivity.getFrameLayoutSelectionFeaturesBar().setVisibility(View.VISIBLE);
                }

                return false;
            }
        });
        holders.add(holder);
    }

    public static void hideSelectionBar() {
        MainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
        MainActivity.getFrameLayoutSelectionFeaturesBar().setVisibility(View.GONE);
        MainActivity.getFrameLayoutSelectionFeaturesBar().removeAllViews();
        clearListSelect();
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView textDate;
        ToggleButton iconLongSelect;
//        Button buttonCancelSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageGallery);
            textDate = itemView.findViewById(R.id.headDate);
            iconLongSelect = itemView.findViewById(R.id.toggleButton);
//            buttonCancelSelect = itemView.findViewById(R.id.cancel_long_click_button);
        }
    }

    public interface PhotoListener {
        void onPhotoClick(String path);

        void onPhotoLongClick(String path);
    }

    public static Vector<String> getListSelect() {
        listSelect.removeAll(Collections.singleton(" "));
        return listSelect;
    }

    public static void clearListSelect() {
        listSelect.clear();
    }

    static void changeBrightness(@NonNull ImageView iv, float scale) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        colorMatrix.setScale(scale, scale, scale, 1f);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        iv.setColorFilter(colorFilter);
    }

    public static void restoreBrightnessAll() {
        for (ViewHolder holder : holders) {
            changeBrightness(holder.image, 1f);
        }
    }
}
