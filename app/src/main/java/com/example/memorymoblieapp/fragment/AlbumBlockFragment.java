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

public class AlbumBlockFragment extends Fragment {
//    @SuppressLint("UseSwitchCompatOrMaterialCode")
//    Switch blockAlbumSwitch;
    TextView txtSetupPassword;
    TextView txtChangePassword;
    TextView txtDeletePassword;
    private AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View albumBlockFragment = inflater.inflate(R.layout.album_block_fragment, container, false);
        super.onViewCreated(albumBlockFragment, savedInstanceState);
//        blockAlbumSwitch = albumBlockFragment.findViewById(R.id.blockAlbumSwitch);
        txtSetupPassword = albumBlockFragment.findViewById(R.id.txtSetupPassword);
        txtChangePassword = albumBlockFragment.findViewById(R.id.txtChangePassword);
        txtDeletePassword = albumBlockFragment.findViewById(R.id.txtDeletePassword);

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
                }

                else if (rePassword.isEmpty()) {
                    Toast.makeText(context, "Vui lòng xác nhận mật khẩu!", Toast.LENGTH_SHORT).show();
                    rePasswordInput.setError(HtmlCompat.fromHtml("<font>Vui lòng xác nhận mật khẩu!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                    rePasswordInput.requestFocus();
                }

                else if (!password.equals(rePassword)) {
                    Toast.makeText(context, "Mật khẩu xác nhận không trùng khớp!", Toast.LENGTH_SHORT).show();
                    rePasswordInput.setError(HtmlCompat.fromHtml("<font>Mật khẩu xác nhận không trùng khớp!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                    rePasswordInput.requestFocus();
                }

                else {
                    Toast.makeText(context, "Khởi tạo mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view -> dialog.cancel());
        });

        return albumBlockFragment;
    }
}
