package com.example.memorymoblieapp.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.fragment.AlbumFragment2;
import com.example.memorymoblieapp.fragment.ImageFragment2;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.example.memorymoblieapp.main.MainActivity;
import com.example.memorymoblieapp.obj.Album;

import java.io.File;
import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    static ArrayList<Album> albums;
    @SuppressLint("StaticFieldLeak")
    static Context context;
    TextView txtImgQuantity;
    @SuppressLint("StaticFieldLeak")
    static ImageView ivMore;
    @SuppressLint("StaticFieldLeak")
    static ImageView ivAlbum;

    public AlbumAdapter(ArrayList<Album> albums, Context context) {
        AlbumAdapter.albums = albums;
        AlbumAdapter.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.album_item, parent, false);
        txtImgQuantity = itemView.findViewById(R.id.txtImgQuantity);
        ivMore = itemView.findViewById(R.id.ivMore);
        ivAlbum = itemView.findViewById(R.id.ivAlbum);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(albums.get(position).getAlbumName());

        if (position == 0) {
            holder.name.setText("Album mới");
            txtImgQuantity.setVisibility(View.GONE);
            ivMore.setVisibility(View.GONE);
            holder.img.setImageResource(R.mipmap.ic_add_album);
        } else {
            holder.quantity.setText(albums.get(position).getPathImages().size() + " ảnh");

            for (int i = 0; i < albums.get(position).getPathImages().size(); i++) {
                File currentFile = new File(albums.get(position).getPathImages().get(0));
                Bitmap bitmap = BitmapFactory.decodeFile(currentFile.getAbsolutePath());
                if (bitmap != null) {
                    holder.img.setImageBitmap(bitmap);
                    break;
                }
            }

            if (holder.img.getDrawable() == null)
                holder.img.setImageResource(R.mipmap.ic_album);
        }
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView quantity;
        ImageView img;
        ImageView more;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtAlbumName);
            quantity = itemView.findViewById(R.id.txtImgQuantity);
            img = itemView.findViewById(R.id.ivAlbum);
            more = itemView.findViewById(R.id.ivMore);
            Context context = itemView.getContext();

            ivMore.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(context, more, Gravity.CENTER);
                popupMenu.inflate(R.menu.album_menu);
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    int itemId = menuItem.getItemId();
                    if (R.id.changeName == itemId) {
                        changeAlbumName(view, getAdapterPosition());
                    } else if (R.id.block == itemId) {
                        Toast.makeText(view.getContext(), "Block " + albums.get(getAdapterPosition()).getAlbumName(), Toast.LENGTH_LONG).show();
                    } else if (R.id.delete == itemId) {
                        deleteAlbum(view, getAdapterPosition());
                        Toast.makeText(view.getContext(), "Đã xóa album '" + albums.get(getAdapterPosition()).getAlbumName() + "'", Toast.LENGTH_LONG).show();
                    }
                    return true;
                });
                popupMenu.show();
            });

            ivAlbum.setOnClickListener(view -> {
                if (getAdapterPosition() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle("Nhập tên album");

                    final EditText input = new EditText(itemView.getContext());

                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);
                    input.requestFocus();

                    builder.setPositiveButton("Đồng ý", null);
                    builder.setNegativeButton("Hủy", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                        String newAlbumName = input.getText().toString();
                        if (newAlbumName.isBlank()) {
                            Toast.makeText(context, "Vui lòng nhập tên album!", Toast.LENGTH_SHORT).show();
                            input.setError(HtmlCompat.fromHtml("<font>Vui lòng nhập tên album!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                        }

                        else if (Album.getAlbumNameArrayList(AlbumFragment2.albumList).contains(newAlbumName)) {
                            Toast.makeText(context, "Tên album đã tồn tại, vui lòng chọn tên khác!", Toast.LENGTH_SHORT).show();
                            input.setError(HtmlCompat.fromHtml("<font>Tên album đã tồn tại, vui lòng chọn tên khác!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                        }

                        else {
                            Toast.makeText(context, "Tạo album thành công!", Toast.LENGTH_SHORT).show();
                            AlbumFragment2.albumList.add(new Album(newAlbumName, new ArrayList<>()));
                            AlbumFragment2.updateItem(AlbumFragment2.albumList.size() - 1);
                            DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
                            dialog.dismiss();
                        }
                    });

                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog.cancel());

                } else {
                    ImageFragment2 imageFragment = new ImageFragment2(albums.get(getAdapterPosition()).getPathImages(), albums.get(getAdapterPosition()).getAlbumName());
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout_content, imageFragment).commit();
                    fragmentTransaction.addToBackStack("album");
                }
            });
        }
    }

    private static void changeAlbumName(@NonNull View itemView, int position) {
        context = itemView.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
        builder.setTitle("Nhập tên mới");

        final EditText input = new EditText(itemView.getContext());

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        input.requestFocus();

        builder.setPositiveButton("Đồng ý", null);
        builder.setNegativeButton("Hủy", null);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
            String newName = input.getText().toString();
            if (newName.isBlank()) {
                Toast.makeText(context, "Vui lòng nhập tên album!", Toast.LENGTH_SHORT).show();
                input.setError(HtmlCompat.fromHtml("<font>Vui lòng nhập tên album!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
            }

            else if (newName.equals(AlbumFragment2.albumList.get(position).getAlbumName())) {
                Toast.makeText(context, "Đổi tên album thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            else if (Album.getAlbumNameArrayList(AlbumFragment2.albumList).contains(newName)) {
                Toast.makeText(context, "Tên album đã tồn tại, vui lòng chọn tên khác!", Toast.LENGTH_SHORT).show();
                input.setError(HtmlCompat.fromHtml("<font>Tên album đã tồn tại, vui lòng chọn tên khác!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
            }

            else {
                AlbumFragment2.albumList.get(position).setAlbumName(newName);
                AlbumFragment2.updateItem(position);
                DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
                Toast.makeText(context, "Đổi tên album thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog.cancel());
    }

    private static void deleteAlbum(@NonNull View itemView, int position) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    AlbumFragment2.albumList.remove(position);
                    AlbumFragment2.updateItem(position);
                    DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.cancel();
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
        builder.setMessage("Bạn có chắc muốn xóa album '" + AlbumFragment2.albumList.get(position).getAlbumName() + "' không?").setPositiveButton("Đồng ý", dialogClickListener)
                .setNegativeButton("Hủy", dialogClickListener).show();
    }
}