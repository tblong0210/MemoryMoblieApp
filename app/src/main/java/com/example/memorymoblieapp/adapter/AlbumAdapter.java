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
import android.widget.LinearLayout;
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

import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    static ArrayList<Album> albums;
    @SuppressLint("StaticFieldLeak")
    static Context context;
    TextView txtAlbumName;
    TextView txtImgQuantity;
    @SuppressLint("StaticFieldLeak")
    static ImageView ivMore;
    @SuppressLint("StaticFieldLeak")
    static ImageView ivAlbum;
    static String albumPassword;

    public AlbumAdapter(ArrayList<Album> albums, Context context) {
        AlbumAdapter.albums = albums;
        AlbumAdapter.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.album_item, parent, false);
        txtAlbumName = itemView.findViewById(R.id.txtAlbumName);
        txtImgQuantity = itemView.findViewById(R.id.txtImgQuantity);
        ivMore = itemView.findViewById(R.id.ivMore);
        ivAlbum = itemView.findViewById(R.id.ivAlbum);

        albumPassword = DataLocalManager.getStringData(KeyData.ALBUM_PASSWORD.getKey());
        albumPassword = albumPassword == null ? null : albumPassword.substring(1, albumPassword.length() - 1);

        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(albums.get(position).getAlbumName());

        if (position == 0) {
            holder.name.setText("Album mới");
            holder.quantity.setText("");
            holder.more.setImageResource(0);
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

                if (albums.get(getAdapterPosition()).getBlock())
                    popupMenu.getMenu().getItem(1).setVisible(false);
                else
                    popupMenu.getMenu().getItem(2).setVisible(false);

                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    int itemId = menuItem.getItemId();
                    if (R.id.changeName == itemId) {
                        changeAlbumName(view, getAdapterPosition());
                    } else if (R.id.block == itemId) {
                        blockAlbum(view, getAdapterPosition());
                    } else if (R.id.removeBlock == itemId) {
                        removeBlockAlbum(view, getAdapterPosition());
                    } else if (R.id.delete == itemId) {
                        deleteAlbum(view, getAdapterPosition());
                    }
                    return true;
                });
                popupMenu.show();
            });

            ivAlbum.setOnClickListener(view -> {
                if (getAdapterPosition() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Tạo album");

                    final EditText input = new EditText(context);

                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setHint("Nhập tên album");
                    input.requestFocus();

                    final LinearLayout ll = new LinearLayout(context);
                    ll.removeAllViews();
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(55,10,55,10);
                    input.setLayoutParams(params);
                    ll.addView(input);

                    builder.setView(ll);
                    builder.setPositiveButton("Đồng ý", null);
                    builder.setNegativeButton("Hủy", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                        String newAlbumName = input.getText().toString();
                        if (newAlbumName.isBlank()) {
                            Toast.makeText(context, "Vui lòng nhập tên album!", Toast.LENGTH_SHORT).show();
                            input.setError(HtmlCompat.fromHtml("<font>Vui lòng nhập tên album!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                        } else if (Album.getAlbumNameArrayList(AlbumFragment2.albumList).contains(newAlbumName)) {
                            Toast.makeText(context, "Tên album đã tồn tại, vui lòng chọn tên khác!", Toast.LENGTH_SHORT).show();
                            input.setError(HtmlCompat.fromHtml("<font>Tên album đã tồn tại, vui lòng chọn tên khác!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                        } else {
                            Toast.makeText(context, "Tạo album thành công!", Toast.LENGTH_SHORT).show();
                            AlbumFragment2.albumList.add(new Album(newAlbumName));
                            AlbumFragment2.updateItem();
                            MainActivity.albumList.add(new Album(newAlbumName));
                            DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
                            DataLocalManager.saveSetStringData(KeyData.ALBUM_NAME_LIST.getKey(), Album.getAlbumNameSet(MainActivity.albumList));
                            dialog.dismiss();
                        }
                    });

                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog.cancel());

                } else {
                    if (!MainActivity.isVerify && albums.get(getAdapterPosition()).getBlock() && albumPassword != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Album bị khóa. Hãy nhập mật khẩu");

                        final EditText passwordInput = new EditText(itemView.getContext());
                        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        passwordInput.setHint("Nhập mật khẩu");
                        passwordInput.requestFocus();

                        final LinearLayout ll = new LinearLayout(context);
                        ll.removeAllViews();
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(55,10,55,10);
                        passwordInput.setLayoutParams(params);
                        ll.addView(passwordInput);

                        builder.setView(ll);
                        builder.setPositiveButton("Đồng ý", null);
                        builder.setNegativeButton("Hủy", null);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                            String password = passwordInput.getText().toString();
                            if (password.isBlank()) {
                                Toast.makeText(context, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                                passwordInput.setError(HtmlCompat.fromHtml("<font>Vui lòng nhập mật khẩu!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                            } else if (!BCrypt.checkpw(password, albumPassword)) {
                                Toast.makeText(context, "Mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                                passwordInput.setError(HtmlCompat.fromHtml("<font>Mật khẩu không chính xác!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                            } else {
                                MainActivity.isVerify = true;
                                Toast.makeText(context, "Mở khóa thành công!", Toast.LENGTH_SHORT).show();

                                ImageFragment2 imageFragment = new ImageFragment2(albums.get(getAdapterPosition()).getPathImages(), albums.get(getAdapterPosition()).getAlbumName());
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame_layout_content, imageFragment).commit();
                                fragmentTransaction.addToBackStack("album");

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
                }
            });
        }
    }

    private static void changeAlbumName(@NonNull View itemView, int position) {
        Context context = itemView.getContext();

        if (albums.get(position).getBlock() && albumPassword != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Đổi tên album");

            final EditText passwordInput = new EditText(context);
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordInput.setHint("Nhập mật khẩu xác thực");
            passwordInput.requestFocus();

            final LinearLayout ll = new LinearLayout(context);
            ll.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(55,10,55,10);
            passwordInput.setLayoutParams(params);
            ll.addView(passwordInput);

            builder.setView(ll);
            builder.setPositiveButton("Đồng ý", null);
            builder.setNegativeButton("Hủy", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                String password = passwordInput.getText().toString();
                if (password.isBlank()) {
                    Toast.makeText(context, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>Vui lòng nhập mật khẩu!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else if (!BCrypt.checkpw(password, albumPassword)) {
                    Toast.makeText(context, "Mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>Mật khẩu không chính xác!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else {
                    dialog.dismiss();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setTitle("Đổi tên album");

                    final EditText input = new EditText(context);

                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setHint("Nhập tên mới");
                    input.requestFocus();

                    final LinearLayout ll1 = new LinearLayout(context);
                    ll1.removeAllViews();
                    input.setLayoutParams(params);
                    ll1.addView(input);

                    builder1.setView(ll1);
                    builder1.setPositiveButton("Đồng ý", null);
                    builder1.setNegativeButton("Hủy", null);

                    AlertDialog dialog1 = builder1.create();
                    dialog1.show();
                    dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view12 -> {
                        String newName = input.getText().toString();
                        if (newName.isBlank()) {
                            Toast.makeText(context, "Vui lòng nhập tên album!", Toast.LENGTH_SHORT).show();
                            input.setError(HtmlCompat.fromHtml("<font>Vui lòng nhập tên album!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                        } else if (newName.equals(AlbumFragment2.albumList.get(position).getAlbumName())) {
                            Toast.makeText(context, "Đổi tên album thành công!", Toast.LENGTH_SHORT).show();
                            dialog1.dismiss();
                        } else if (Album.getAlbumNameArrayList(AlbumFragment2.albumList).contains(newName)) {
                            Toast.makeText(context, "Tên album đã tồn tại, vui lòng chọn tên khác!", Toast.LENGTH_SHORT).show();
                            input.setError(HtmlCompat.fromHtml("<font>Tên album đã tồn tại, vui lòng chọn tên khác!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                        } else {
                            AlbumFragment2.albumList.get(position).setAlbumName(newName);
                            AlbumFragment2.updateItem();
                            MainActivity.albumList.get(position - 1).setAlbumName(newName);
                            DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
                            DataLocalManager.saveSetStringData(KeyData.ALBUM_NAME_LIST.getKey(), Album.getAlbumNameSet(MainActivity.albumList));
                            Toast.makeText(context, "Đổi tên album thành công!", Toast.LENGTH_SHORT).show();
                            dialog1.dismiss();
                        }
                    });

                    dialog1.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog1.cancel());
                }
            });
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog.cancel());
        }

        if (!albums.get(position).getBlock() || albumPassword == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Đổi tên album");

            final EditText input = new EditText(context);

            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setHint("Nhập tên mới");
            input.requestFocus();

            final LinearLayout ll = new LinearLayout(context);
            ll.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(55,10,55,10);
            input.setLayoutParams(params);
            ll.addView(input);

            builder.setView(ll);
            builder.setPositiveButton("Đồng ý", null);
            builder.setNegativeButton("Hủy", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                String newName = input.getText().toString();
                if (newName.isBlank()) {
                    Toast.makeText(context, "Vui lòng nhập tên album!", Toast.LENGTH_SHORT).show();
                    input.setError(HtmlCompat.fromHtml("<font>Vui lòng nhập tên album!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else if (newName.equals(AlbumFragment2.albumList.get(position).getAlbumName())) {
                    Toast.makeText(context, "Đổi tên album thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else if (Album.getAlbumNameArrayList(AlbumFragment2.albumList).contains(newName)) {
                    Toast.makeText(context, "Tên album đã tồn tại, vui lòng chọn tên khác!", Toast.LENGTH_SHORT).show();
                    input.setError(HtmlCompat.fromHtml("<font>Tên album đã tồn tại, vui lòng chọn tên khác!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else {
                    AlbumFragment2.albumList.get(position).setAlbumName(newName);
                    AlbumFragment2.updateItem();
                    MainActivity.albumList.get(position - 1).setAlbumName(newName);
                    DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
                    DataLocalManager.saveSetStringData(KeyData.ALBUM_NAME_LIST.getKey(), Album.getAlbumNameSet(MainActivity.albumList));
                    Toast.makeText(context, "Đổi tên album thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog.cancel());
        }
    }

    private static void deleteAlbum(@NonNull View itemView, int position) {
        Context context = itemView.getContext();

        if (albums.get(position).getBlock() && albumPassword != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xóa album");

            final EditText passwordInput = new EditText(context);
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordInput.setHint("Nhập mật khẩu xác thực");
            passwordInput.requestFocus();

            final LinearLayout ll = new LinearLayout(context);
            ll.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(55,10,55,10);
            passwordInput.setLayoutParams(params);
            ll.addView(passwordInput);

            builder.setView(ll);
            builder.setPositiveButton("Đồng ý", null);
            builder.setNegativeButton("Hủy", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                String password = passwordInput.getText().toString();
                if (password.isBlank()) {
                    Toast.makeText(context, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>Vui lòng nhập mật khẩu!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else if (!BCrypt.checkpw(password, albumPassword)) {
                    Toast.makeText(context, "Mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>Mật khẩu không chính xác!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else {
                    dialog.dismiss();
                    DialogInterface.OnClickListener dialogClickListener = (dialog1, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Toast.makeText(context, "Đã xóa album '" + albums.get(position).getAlbumName() + "'", Toast.LENGTH_LONG).show();
                                AlbumFragment2.albumList.remove(position);
                                AlbumFragment2.updateItem();
                                MainActivity.albumList.remove(position - 1);
                                DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
                                DataLocalManager.saveSetStringData(KeyData.ALBUM_NAME_LIST.getKey(), Album.getAlbumNameSet(MainActivity.albumList));
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog1.cancel();
                                break;
                        }
                    };
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(itemView.getContext());
                    builder1.setMessage("Bạn có chắc muốn xóa album '" + AlbumFragment2.albumList.get(position).getAlbumName() + "' không?").setPositiveButton("Đồng ý", dialogClickListener)
                            .setNegativeButton("Hủy", dialogClickListener).show();
                }
            });
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog.cancel());
        }

        if (!albums.get(position).getBlock() || albumPassword == null) {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Toast.makeText(context, "Đã xóa album '" + albums.get(position).getAlbumName() + "'", Toast.LENGTH_LONG).show();
                        AlbumFragment2.albumList.remove(position);
                        AlbumFragment2.updateItem();
                        MainActivity.albumList.remove(position - 1);
                        DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
                        DataLocalManager.saveSetStringData(KeyData.ALBUM_NAME_LIST.getKey(), Album.getAlbumNameSet(MainActivity.albumList));
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

    private static void blockAlbum(@NonNull View itemView, int position) {
        Context context = itemView.getContext();

        if (albumPassword == null) {
            Toast.makeText(context, "Mật khẩu album chưa được thiết lập. Vui lòng vào cài đặt để thiết lập.", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Khóa album");

            final EditText passwordInput = new EditText(context);
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordInput.setHint("Nhập mật khẩu xác thực");
            passwordInput.requestFocus();

            final LinearLayout ll = new LinearLayout(context);
            ll.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(55,10,55,10);
            passwordInput.setLayoutParams(params);
            ll.addView(passwordInput);

            builder.setView(ll);
            builder.setPositiveButton("Đồng ý", null);
            builder.setNegativeButton("Hủy", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                String password = passwordInput.getText().toString();
                if (password.isBlank()) {
                    Toast.makeText(context, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>Vui lòng nhập mật khẩu!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else if (!BCrypt.checkpw(password, albumPassword)) {
                    Toast.makeText(context, "Mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>Mật khẩu không chính xác!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else {
                    albums.get(position).setBlock(true);
                    MainActivity.albumList.get(position - 1).setBlock(true);
                    DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
                    DataLocalManager.saveSetStringData(KeyData.ALBUM_NAME_LIST.getKey(), Album.getAlbumNameSet(MainActivity.albumList));
                    Toast.makeText(context, "Khóa album thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog.cancel());
        }
    }

    private static void removeBlockAlbum(@NonNull View itemView, int position) {
        Context context = itemView.getContext();

        if (albumPassword == null) {
            albums.get(position).setBlock(false);
            MainActivity.albumList.get(position - 1).setBlock(false);
            DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
            DataLocalManager.saveSetStringData(KeyData.ALBUM_NAME_LIST.getKey(), Album.getAlbumNameSet(MainActivity.albumList));
            Toast.makeText(context, "Xóa khóa album thành công!", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xóa khóa album");

            final EditText passwordInput = new EditText(itemView.getContext());
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordInput.setHint("Nhập mật khẩu xác thực");
            passwordInput.requestFocus();

            final LinearLayout ll = new LinearLayout(context);
            ll.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(55,10,55,10);
            passwordInput.setLayoutParams(params);
            ll.addView(passwordInput);

            builder.setView(ll);
            builder.setPositiveButton("Đồng ý", null);
            builder.setNegativeButton("Hủy", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                String password = passwordInput.getText().toString();
                if (password.isBlank()) {
                    Toast.makeText(context, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>Vui lòng nhập mật khẩu!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else if (!BCrypt.checkpw(password, albumPassword)) {
                    Toast.makeText(context, "Mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>Mật khẩu không chính xác!</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else {
                    albums.get(position).setBlock(false);
                    MainActivity.albumList.get(position - 1).setBlock(false);
                    DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
                    DataLocalManager.saveSetStringData(KeyData.ALBUM_NAME_LIST.getKey(), Album.getAlbumNameSet(MainActivity.albumList));
                    Toast.makeText(context, "Xóa khóa album thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog.cancel());
        }
    }
}