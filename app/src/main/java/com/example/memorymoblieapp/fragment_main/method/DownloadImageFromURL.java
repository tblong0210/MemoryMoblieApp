package com.example.memorymoblieapp.fragment_main.method;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.activity.MainActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class DownloadImageFromURL extends AsyncTask<String, Integer, String> {
    private Context context;
    private PowerManager.WakeLock wakeLock;
    private ProgressDialog progressDialog;

    public DownloadImageFromURL(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... urls) {
        String url = urls[0];
        int count;
        try {
            URL downloadUrl = new URL(url);
            URLConnection connection = downloadUrl.openConnection();
            connection.connect();

            // Lấy kích thước file để tính toán tiến trình download
            int fileLength = connection.getContentLength();

            // Tạo stream để đọc dữ liệu từ URL
            InputStream input = new BufferedInputStream(downloadUrl.openStream(), 8192);

            // Tạo file để lưu dữ liệu
            File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "myImageFolder");
            String imageName = UUID.randomUUID().toString() + ".jpg";
            File file = new File(outputFile, imageName);
            OutputStream output = new FileOutputStream(file);

            byte[] data = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();

            MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, null);
            MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override
                public void onMediaScannerConnected() {
                    // Do nothing
                }

                @SuppressLint("SimpleDateFormat")
                @Override
                public void onScanCompleted(String path, Uri uri) {
                    Toast.makeText(context, context.getString(R.string.notif_download_OK), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
        return "Download completed";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Hiển thị ProgressDialog trước khi bắt đầu download
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Downloading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
        progressDialog.show();

        // Giữ màn hình luôn sáng trong quá trình download
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        wakeLock.acquire();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // Cập nhật tiến trình download trên ProgressDialog
        progressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // Tắt ProgressDialog sau khi download hoàn thành
        progressDialog.dismiss();

        // Giải phóng WakeLock
        wakeLock.release();

        MainActivity.updateData(context);
    }
}
