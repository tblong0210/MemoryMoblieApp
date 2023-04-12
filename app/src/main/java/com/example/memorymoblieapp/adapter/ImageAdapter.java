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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.fragment.AlbumSelectionBarFragment;
import com.example.memorymoblieapp.fragment.HomeSelectionBarFragment;
import com.example.memorymoblieapp.fragment.LoveSelectionBarFragment;
import com.example.memorymoblieapp.fragment.TrashBinSelectionBarFragment;
import com.example.memorymoblieapp.main.MainActivity;
import com.example.memorymoblieapp.view.ViewImage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    static ArrayList<String> images;
    Context context;
    static boolean detailed;
    static Vector<String> listSelect;
    public static boolean selectMode;
    static String type;
    static ArrayList<ViewHolder> holders;
    static int albumPos;

    public ImageAdapter(ArrayList<String> images, Context context, boolean detailed, String type) {
        ImageAdapter.images = images;
        this.context = context;
        ImageAdapter.detailed = detailed;
        holders = new ArrayList<>();
        ImageAdapter.type = type;
    }

    public ImageAdapter(ArrayList<String> images, Context context, boolean detailed, String type, int albumPos) {
        ImageAdapter.images = images;
        this.context = context;
        ImageAdapter.detailed = detailed;
        holders = new ArrayList<>();
        ImageAdapter.type = type;
        this.albumPos = albumPos;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
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
                holder.name.setText("Tên: " + currentFile.getName());
                holder.createdDate.setText("Ngày tạo: " + sdf.format(currentFile.lastModified()));
                holder.size.setText("Dung lượng: " + fileSizeResult);
                if (bitmap != null)
                    holder.dimensions.setText("Kích thước: " + bitmap.getWidth() + "x" + bitmap.getHeight());
                holder.location.setText("Vị trí: " + currentFile.getParent());
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
                name = (TextView) itemView.findViewById(R.id.txtImageName);
                createdDate = (TextView) itemView.findViewById(R.id.txtCreatedDate);
                size = (TextView) itemView.findViewById(R.id.txtSize);
                dimensions = (TextView) itemView.findViewById(R.id.txtDimensions);
                location = (TextView) itemView.findViewById(R.id.txtLocation);
            }
            img = (ImageView) itemView.findViewById(R.id.ivImage);
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
                        ivSelect.setImageResource(R.drawable.circle_shape);
                    }
                    if (listSelect.isEmpty())
                        turnOffSelectMode();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ViewImage.class);
                    intent.putExtra("path_image", images.get(getAdapterPosition()));
                    context.startActivity(intent);
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