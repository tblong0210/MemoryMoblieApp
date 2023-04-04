package com.example.memorymoblieapp.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.example.memorymoblieapp.main.MainActivity;
import com.example.memorymoblieapp.obj.Album;

public class AlbumBlockFragment extends Fragment {
    //    @SuppressLint("UseSwitchCompatOrMaterialCode")
//    Switch blockAlbumSwitch;
    TextView txtSetupPassword;
    TextView txtChangePassword;
    TextView txtDeletePassword;
    String albumPassword;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View albumBlockFragment = inflater.inflate(R.layout.album_block_fragment, container, false);
        super.onViewCreated(albumBlockFragment, savedInstanceState);
//        blockAlbumSwitch = albumBlockFragment.findViewById(R.id.blockAlbumSwitch);
        txtSetupPassword = albumBlockFragment.findViewById(R.id.txtSetupPassword);
        txtChangePassword = albumBlockFragment.findViewById(R.id.txtChangePassword);
        txtDeletePassword = albumBlockFragment.findViewById(R.id.txtDeletePassword);

        albumPassword = DataLocalManager.getStringData(KeyData.ALBUM_PASSWORD.getKey());
        albumPassword = albumPassword == null ? null : albumPassword.substring(1, albumPassword.length() - 1);


        txtSetupPassword.setOnClickListener(itemView -> {
            Context context = itemView.getContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(albumBlockFragment.getContext());
            builder.setTitle("Khởi tạo mật khẩu");
            builder.setView(albumBlockFragment);

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText passwordInput = new EditText(context);
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordInput.setHint("Nhập mật khẩu");
            layout.addView(passwordInput);

            final EditText rePasswordInput = new EditText(context);
            rePasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            rePasswordInput.setHint("Xác nhận mật khẩu");
            layout.addView(rePasswordInput);

            builder.setView(layout);
            passwordInput.requestFocus();

            builder.setPositiveButton("Đồng ý", null);
            builder.setNegativeButton("Hủy", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                String password = passwordInput.getText().toString();
                String rePassword = rePasswordInput.getText().toString();

                if (password.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>Vui lòng nhập mật khẩu!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                    passwordInput.requestFocus();
                } else if (rePassword.isEmpty()) {
                    Toast.makeText(context, "Vui lòng xác nhận mật khẩu!", Toast.LENGTH_SHORT).show();
                    rePasswordInput.setError(HtmlCompat.fromHtml("<font>Vui lòng xác nhận mật khẩu!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                    rePasswordInput.requestFocus();
                } else if (!password.equals(rePassword)) {
                    Toast.makeText(context, "Mật khẩu xác nhận không trùng khớp!", Toast.LENGTH_SHORT).show();
                    rePasswordInput.setError(HtmlCompat.fromHtml("<font>Mật khẩu xác nhận không trùng khớp!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                    rePasswordInput.requestFocus();
                } else {
                    Toast.makeText(context, "Khởi tạo mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    DataLocalManager.saveData(KeyData.ALBUM_PASSWORD.getKey(), password);
                    albumPassword = password;
                    txtSetupPassword.setClickable(false);
                    txtChangePassword.setClickable(true);
                    txtDeletePassword.setClickable(true);
                    dialog.dismiss();
                }
            });

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view -> dialog.cancel());
        });

        txtChangePassword.setOnClickListener(itemView -> {
            Context context = itemView.getContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(albumBlockFragment.getContext());
            builder.setTitle("Khởi tạo mật khẩu");
            builder.setView(albumBlockFragment);

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText oldPasswordInput = new EditText(context);
            oldPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            oldPasswordInput.setHint("Nhập mật khẩu cũ");
            layout.addView(oldPasswordInput);

            final EditText newPasswordInput = new EditText(context);
            newPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            newPasswordInput.setHint("Nhập mật khẩu mới");
            layout.addView(newPasswordInput);

            final EditText reNewPasswordInput = new EditText(context);
            reNewPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            reNewPasswordInput.setHint("Xác nhận mật khẩu mới");
            layout.addView(reNewPasswordInput);

            builder.setView(layout);
            newPasswordInput.requestFocus();

            builder.setPositiveButton("Đồng ý", null);
            builder.setNegativeButton("Hủy", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                String oldPassword = oldPasswordInput.getText().toString();
                String newPassword = newPasswordInput.getText().toString();
                String reNewPassword = reNewPasswordInput.getText().toString();

                if (oldPassword.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập mật khẩu cũ!", Toast.LENGTH_SHORT).show();
                    oldPasswordInput.setError(HtmlCompat.fromHtml("<font>Vui lòng nhập mật khẩu cũ!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                    oldPasswordInput.requestFocus();
                } else if (newPassword.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập mật khẩu mới!", Toast.LENGTH_SHORT).show();
                    newPasswordInput.setError(HtmlCompat.fromHtml("<font>Vui lòng nhập mật khẩu mới!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                    newPasswordInput.requestFocus();
                } else if (reNewPassword.isEmpty()) {
                    Toast.makeText(context, "Vui lòng xác nhận mật khẩu mới!", Toast.LENGTH_SHORT).show();
                    reNewPasswordInput.setError(HtmlCompat.fromHtml("<font>Vui lòng xác nhận mật khẩu mới!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                    reNewPasswordInput.requestFocus();
                } else if (!newPassword.equals(reNewPassword)) {
                    Toast.makeText(context, "Mật khẩu xác nhận không trùng khớp!", Toast.LENGTH_SHORT).show();
                    reNewPasswordInput.setError(HtmlCompat.fromHtml("<font>Mật khẩu xác nhận không trùng khớp!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                    reNewPasswordInput.requestFocus();
                } else if (!oldPassword.equals(albumPassword)) {
                    Toast.makeText(context, "Mật khẩu cũ không chính xác!", Toast.LENGTH_SHORT).show();
                    oldPasswordInput.setError(HtmlCompat.fromHtml("<font>Mật khẩu cũ không chính xác!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                    oldPasswordInput.requestFocus();
                } else {
                    Toast.makeText(context, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    DataLocalManager.saveData(KeyData.ALBUM_PASSWORD.getKey(), newPassword);
                    albumPassword = newPassword;
                    dialog.dismiss();
                }
            });

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view -> dialog.cancel());
        });

        txtDeletePassword.setOnClickListener(itemView -> {
            Context context = itemView.getContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Xác nhận xóa mật khẩu");

            final EditText passwordInput = new EditText(itemView.getContext());
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT);
            passwordInput.setHint("Nhập mật khẩu");
            builder.setView(passwordInput);
            passwordInput.requestFocus();

            builder.setPositiveButton("Đồng ý", null);
            builder.setNegativeButton("Hủy", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                String password = passwordInput.getText().toString();
                if (password.isBlank()) {
                    Toast.makeText(context, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>Vui lòng nhập mật khẩu!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                }

                else if (!password.equals(albumPassword)) {
                    Toast.makeText(context, "Mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>Mật khẩu không chính xác!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                }

                else {
                    Toast.makeText(context, "Xóa mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    DataLocalManager.remove(KeyData.ALBUM_PASSWORD.getKey());
                    albumPassword = null;
                    txtSetupPassword.setClickable(true);
                    txtChangePassword.setClickable(false);
                    txtDeletePassword.setClickable(false);
                    dialog.dismiss();
                }
            });

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog.cancel());
        });

        if (albumPassword == null) {
            txtSetupPassword.setClickable(true);
            txtChangePassword.setClickable(false);
            txtDeletePassword.setClickable(false);
        } else {
            txtSetupPassword.setClickable(false);
            txtChangePassword.setClickable(true);
            txtDeletePassword.setClickable(true);
        }

        return albumBlockFragment;
    }
}