package com.example.memorymoblieapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;

public class ShareImageToMedia {
    private int REQUEST_CODE_SHARE = 1111;
    private ArrayList<String> paths;
    private Context context;

    public ShareImageToMedia(ArrayList<String> paths, Context context) {
        this.paths = paths;
        this.context = context;
    }

    public ShareImageToMedia() {
        this.paths = new ArrayList<>();
    }

    public ArrayList<String> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<String> paths) {
        this.paths = paths;
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void sharePictures() {
        if (paths.isEmpty()){
            Toast.makeText(context, "Image path list is empty!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Uri> imageUris = new ArrayList<>();

        for (String path : paths) {
            File fileImage = new File(path);
            // Khởi tạo đường dẫn ảnh và nội dung chia sẻ
            Uri mImageUri = FileProvider.getUriForFile(context, "com.example.memorymoblieapp.fileprovider", fileImage);
            imageUris.add(mImageUri);
        }

        // Tạo Intent để chia sẻ nội dung
        Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUris);

        // Tạo Intent chooser để hiển thị danh sách các ứng dụng cho phép chia sẻ nội dung
        Intent chooserIntent = Intent.createChooser(shareIntent, "Chọn ứng dụng");
        ((AppCompatActivity) context).startActivityForResult(chooserIntent, REQUEST_CODE_SHARE);
    }

}
