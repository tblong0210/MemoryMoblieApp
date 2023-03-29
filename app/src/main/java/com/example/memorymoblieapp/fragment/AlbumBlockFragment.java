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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.memorymoblieapp.R;

public class AlbumBlockFragment extends Fragment {
//    @SuppressLint("UseSwitchCompatOrMaterialCode")
//    Switch blockAlbumSwitch;
    TextView txtSetupPassword;
    TextView txtChangePassword;
    TextView txtDeletePassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View albumBlockFragment = inflater.inflate(R.layout.album_block_fragment, container, false);
//        blockAlbumSwitch = albumBlockFragment.findViewById(R.id.blockAlbumSwitch);
        txtSetupPassword = albumBlockFragment.findViewById(R.id.txtSetupPassword);
        txtChangePassword = albumBlockFragment.findViewById(R.id.txtChangePassword);
        txtDeletePassword = albumBlockFragment.findViewById(R.id.txtDeletePassword);

        txtSetupPassword.setOnClickListener(itemView -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Khởi tạo mật khẩu");

            Context context = itemView.getContext();
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText passwordInput = new EditText(context);
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordInput.setHint("Nhập mật khẩu");
            layout.addView(passwordInput);

            final EditText rePasswordInput = new EditText(context);
            rePasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            rePasswordInput.setHint("Nhập lại mật khẩu");
            layout.addView(rePasswordInput);

            builder.setView(layout);

            builder.setPositiveButton("Đồng ý", (dialog, which) -> {
                String password = passwordInput.getText().toString();
                String rePassword = rePasswordInput.getText().toString();
            });
            builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        return albumBlockFragment;
    }
}
