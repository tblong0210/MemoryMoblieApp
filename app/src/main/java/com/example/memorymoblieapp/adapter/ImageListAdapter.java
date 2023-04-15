package com.example.memorymoblieapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.fragment.AlbumSelectionBarFragment;
import com.example.memorymoblieapp.fragment.ImageListFragment;
import com.example.memorymoblieapp.fragment.LoveSelectionBarFragment;
import com.example.memorymoblieapp.fragment.TrashBinSelectionBarFragment;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.example.memorymoblieapp.main.MainActivity;
import com.example.memorymoblieapp.view.ViewImage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {
    static ArrayList<String> images;
    Context context;
    static boolean detailed;
    static Vector<String> listSelect;
    public static boolean selectMode;
    static String type;
    static ArrayList<ViewHolder> holders;
    static int albumPos;

    public ImageListAdapter(ArrayList<String> images, Context context, boolean detailed, String type) {
        ImageListAdapter.images = images;
        this.context = context;
        ImageListAdapter.detailed = detailed;
        holders = new ArrayList<>();
        ImageListAdapter.type = type;
    }

    public ImageListAdapter(ArrayList<String> images, Context context, boolean detailed, String type, int albumPos) {
        ImageListAdapter.images = images;
        this.context = context;
        ImageListAdapter.detailed = detailed;
        holders = new ArrayList<>();
        ImageListAdapter.type = type;
        ImageListAdapter.albumPos = albumPos;
    }

    @NonNull
    @Override
    public ImageListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        listSelect = new Vector<>();
        selectMode = false;

        if (detailed)
            itemView = layoutInflater.inflate(R.layout.image_detail_item, parent, false);
        else
            itemView = layoutInflater.inflate(R.layout.image_item, parent, false);

        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ImageListAdapter.ViewHolder holder, int position) {
        File currentFile = new File(images.get(position));

        if (currentFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(currentFile.getAbsolutePath());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT);

            long fileSizeNumber = Math.round(currentFile.length() * 1.0 / 1000);
            String fileSizeResult;
            if (fileSizeNumber > 2000)
                fileSizeResult = String.format(Locale.ROOT, "%.2f MB", fileSizeNumber * 1.0 / 1000);
            else
                fileSizeResult = String.format(Locale.ROOT, "%d KB", fileSizeNumber);

            if (detailed) {
                holder.name.setText(context.getString(R.string.text_name) + ": " + currentFile.getName());
                holder.createdDate.setText(context.getString(R.string.text_created_date) + ": " + sdf.format(currentFile.lastModified()));
                holder.size.setText(context.getString(R.string.text_size) + ": " + fileSizeResult);
                if (bitmap != null)
                    holder.dimensions.setText(context.getString(R.string.text_dimensions) + ": " + bitmap.getWidth() + "x" + bitmap.getHeight());
                holder.location.setText(context.getString(R.string.text_location) + ": " + currentFile.getParent());
            }
            holder.img.setImageBitmap(bitmap);
        }
        holders.add(holder);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView createdDate;
        TextView size;
        TextView dimensions;
        TextView location;
        ImageView img;
        ImageView ivSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (detailed) {
                name = itemView.findViewById(R.id.txtImageName);
                createdDate = itemView.findViewById(R.id.txtCreatedDate);
                size = itemView.findViewById(R.id.txtSize);
                dimensions = itemView.findViewById(R.id.txtDimensions);
                location = itemView.findViewById(R.id.txtLocation);
            }
            img = itemView.findViewById(R.id.ivImage);
            ivSelect = itemView.findViewById(R.id.ivSelect);
            ivSelect.setVisibility(View.GONE);

            itemView.setOnClickListener(view -> {
                if (selectMode) {
                    String imagePath = images.get(getAdapterPosition());
                    if (listSelect.contains(imagePath)) {
                        listSelect.remove(imagePath);
                        ivSelect.setImageResource(R.drawable.circle_unfill);
                    } else {
                        listSelect.add(imagePath);
                        ivSelect.setImageResource(R.drawable.checked);
                    }
                    if (listSelect.isEmpty())
                        turnOffSelectMode();
                } else {
                    if (!type.equals("TrashBin")) {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, ViewImage.class);
                        intent.putStringArrayListExtra(KeyData.LIST_IMAGE_VIEW.getKey(), images);
                        intent.putExtra(KeyData.PATH_CURRENT_IMAGE_VIEW.getKey(), images.get(getAdapterPosition()));
                        context.startActivity(intent);
                    }
                }
            });

            itemView.setOnLongClickListener(view -> {
                turnOnSelectMode(view);
                return false;
            });
        }

        public static void turnOnSelectMode(View view) {
            if (listSelect != null) {
                selectMode = true;
                listSelect.clear();
                for (ViewHolder holder : holders) {
                    holder.ivSelect.setVisibility(View.VISIBLE);
                    holder.ivSelect.setImageResource(R.drawable.circle_unfill);
                }

                // Display selection features bar, hide bottom navigation bar
                Fragment selectionFragment;
                if (type.equals("Love"))
                    selectionFragment = new LoveSelectionBarFragment();
                else if (type.equals("TrashBin"))
                    selectionFragment = new TrashBinSelectionBarFragment();
                else
                    selectionFragment = new AlbumSelectionBarFragment(albumPos);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout_selection_features_bar, selectionFragment).commit();
                fragmentTransaction.addToBackStack("");

                ImageListFragment.imgBtnChangeViewContainer.setVisibility(View.GONE);
                ImageListFragment.imgBtnBackContainer.setVisibility(View.VISIBLE);

                MainActivity.getBottomNavigationView().setVisibility(View.GONE);
                MainActivity.getFrameLayoutSelectionFeaturesBar().setVisibility(View.VISIBLE);
            }
        }

        public static void turnOffSelectMode() {
            if (listSelect != null) {
                selectMode = false;
                listSelect.clear();
                for (ViewHolder holder : holders)
                    holder.ivSelect.setVisibility(View.GONE);

                ImageListFragment.imgBtnChangeViewContainer.setVisibility(View.VISIBLE);
                ImageListFragment.imgBtnBackContainer.setVisibility(View.GONE);

                // Display bottom navigation bar, hide selection features bar
                MainActivity.getFrameLayoutSelectionFeaturesBar().removeAllViews();
                MainActivity.getFrameLayoutSelectionFeaturesBar().setVisibility(View.GONE);
                MainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
            }
        }
    }

    public static Vector<String> getListSelect() {
        return listSelect;
    }
}