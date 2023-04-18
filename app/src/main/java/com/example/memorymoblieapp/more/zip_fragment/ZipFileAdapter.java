package com.example.memorymoblieapp.more.zip_fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;

import java.io.File;
import java.util.ArrayList;

public class ZipFileAdapter extends RecyclerView.Adapter<ZipFileAdapter.ViewHolder> {
    static ArrayList<String> zipList;
    @SuppressLint("StaticFieldLeak")
    static Context context;

    public ZipFileAdapter(ArrayList<String> zipList, Context context) {
        ZipFileAdapter.zipList = zipList;
        ZipFileAdapter.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.zip_file_item, parent, false);
        return new ZipFileAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File file = new File(zipList.get(position));
        holder.txtZipName.setText(file.getName());
        holder.txtZipPath.setText(file.getParent());
    }

    @Override
    public int getItemCount() {
        return zipList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtZipName;
        TextView txtZipPath;
        ImageView ivDelete;
        ImageView ivShare;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtZipName = itemView.findViewById(R.id.txtZipName);
            txtZipPath = itemView.findViewById(R.id.txtZipPath);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivShare = itemView.findViewById(R.id.ivShare);

            ivDelete.setOnClickListener(view -> {
                DialogInterface.OnClickListener dialogClickListener = (dialog1, which) -> {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            File sourceFile = new File(zipList.get(getAdapterPosition()));
                            if (sourceFile.delete()) {
                                Toast.makeText(context, context.getString(R.string.toast_deleted) + " '" + sourceFile.getName() + "'", Toast.LENGTH_LONG).show();
                                zipList.remove(zipList.get(getAdapterPosition()));
                                ZipFileFragment.updateItem();
                            } else {
                                Toast.makeText(context, context.getString(R.string.toast_cannot_delete) + " '" + sourceFile.getName() + "'", Toast.LENGTH_LONG).show();
                            }

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog1.cancel();
                            break;
                    }
                };
                AlertDialog.Builder builder1 = new AlertDialog.Builder(itemView.getContext());
                builder1.setMessage(context.getString(R.string.alert_dialog_delete_album_confirm) + " '" + txtZipName.getText() + "'?").setPositiveButton(context.getString(R.string.alert_dialog_confirm), dialogClickListener)
                        .setNegativeButton(context.getString(R.string.alert_dialog_cancel), dialogClickListener).show();
            });

            ivShare.setOnClickListener(view -> shareZip(zipList.get(getAdapterPosition()), itemView.getContext()));
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private static void shareZip(String path, Context context) {
        int REQUEST_CODE_SHARE = 1111;
        File fileImage = new File(path);
        // Khởi tạo đường dẫn zip và nội dung chia sẻ

        Uri mImageUri = FileProvider.getUriForFile(context, "com.example.memorymoblieapp.fileprovider", fileImage);
        String mShareContent = "";

        // Tạo Intent để chia sẻ nội dung
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, mImageUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, mShareContent);

        // Tạo Intent chooser để hiển thị danh sách các ứng dụng cho phép chia sẻ nội dung
        Intent chooserIntent = Intent.createChooser(shareIntent, "Chọn ứng dụng");
        ((AppCompatActivity) context).startActivityForResult(chooserIntent, REQUEST_CODE_SHARE);
    }
}
