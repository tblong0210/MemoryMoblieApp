package com.example.memorymoblieapp.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.main.MainActivity;
import com.example.memorymoblieapp.obj.Album;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    static ArrayList<Album> albums;
    Context context;
    TextView txtImgQuantity;
    @SuppressLint("StaticFieldLeak")
    static ImageView ivMore;
    @SuppressLint("StaticFieldLeak")
    static ImageView ivAlbum;

    public AlbumAdapter(ArrayList<Album> albums, Context context) {
        AlbumAdapter.albums = albums;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.album_item, parent, false);
        txtImgQuantity = (TextView) itemView.findViewById(R.id.txtImgQuantity);
        ivMore = (ImageView) itemView.findViewById(R.id.ivMore);
        ivAlbum = (ImageView) itemView.findViewById(R.id.ivAlbum);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(albums.get(position).getName());

        if (position == 0) {
            holder.name.setText("Album mới");
            txtImgQuantity.setVisibility(View.GONE);
            ivMore.setVisibility(View.GONE);
        }

        holder.quantity.setText(Integer.toString(albums.get(position).getImgList().size()) + " ảnh");

        holder.img.setImageResource(Integer.parseInt(Integer.toString(albums.get(position).getImg())));
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView quantity;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txtAlbumName);
            quantity = (TextView) itemView.findViewById(R.id.txtImgQuantity);
            img = (ImageView) itemView.findViewById(R.id.ivAlbum);

            ivMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), albums.get(getAdapterPosition()).getName() + " more", Toast.LENGTH_LONG).show();
                }
            });

            ivAlbum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getAdapterPosition() == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                        builder.setTitle("Nhập tên album");

                        final EditText input = new EditText(itemView.getContext());

                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);

                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newAlbumName = input.getText().toString();
                                Toast.makeText(view.getContext(), newAlbumName, Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }

                    else {
                        Toast.makeText(view.getContext(), albums.get(getAdapterPosition()).getName(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
