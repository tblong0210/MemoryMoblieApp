package com.example.memorymoblieapp.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
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
import androidx.core.content.FileProvider;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.fragment.AlbumFragment;
import com.example.memorymoblieapp.fragment.ImageListFragment;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.example.memorymoblieapp.main.MainActivity;
import com.example.memorymoblieapp.obj.Album;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
                    } else if (R.id.export == itemId) {
                        try {
                            exportAlbum(view, getAdapterPosition());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return true;
                });
                popupMenu.show();
            });

            ivAlbum.setOnClickListener(view -> {
                if (getAdapterPosition() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(context.getString(R.string.action_new_album));

                    final EditText input = new EditText(context);

                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setHint(context.getString(R.string.alert_dialog_enter_album_name_hint));
                    input.requestFocus();

                    final LinearLayout ll = new LinearLayout(context);
                    ll.removeAllViews();
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(50, 10, 50, 10);
                    input.setLayoutParams(params);
                    ll.addView(input);

                    builder.setView(ll);
                    builder.setPositiveButton(context.getString(R.string.alert_dialog_confirm), null);
                    builder.setNegativeButton(context.getString(R.string.alert_dialog_cancel), null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                        String newAlbumName = input.getText().toString();
                        if (newAlbumName.isBlank()) {
                            Toast.makeText(context, context.getString(R.string.alert_dialog_blank_album_name_notification), Toast.LENGTH_SHORT).show();
                            input.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_blank_album_name_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                        } else if (Album.getAlbumNameArrayList(AlbumFragment.albumList).contains(newAlbumName)) {
                            Toast.makeText(context, context.getString(R.string.alert_dialog_existed_album_name_notification), Toast.LENGTH_SHORT).show();
                            input.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_existed_album_name_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                        } else {
                            Toast.makeText(context, context.getString(R.string.alert_dialog_create_album_successfully_notification), Toast.LENGTH_SHORT).show();
                            AlbumFragment.albumList.add(new Album(newAlbumName));
                            AlbumFragment.updateItem();
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
                        builder.setTitle(context.getString(R.string.alert_dialog_blocked_album));

                        final EditText passwordInput = new EditText(itemView.getContext());
                        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        passwordInput.setHint(context.getString(R.string.alert_dialog_enter_password_hint));
                        passwordInput.requestFocus();

                        final LinearLayout ll = new LinearLayout(context);
                        ll.removeAllViews();
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(50, 10, 50, 10);
                        passwordInput.setLayoutParams(params);
                        ll.addView(passwordInput);

                        builder.setView(ll);
                        builder.setPositiveButton(context.getString(R.string.alert_dialog_confirm), null);
                        builder.setNegativeButton(context.getString(R.string.alert_dialog_cancel), null);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                            String password = passwordInput.getText().toString();
                            if (password.isBlank()) {
                                Toast.makeText(context, context.getString(R.string.alert_dialog_blank_password_notification), Toast.LENGTH_SHORT).show();
                                passwordInput.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_blank_password_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                            } else if (!BCrypt.checkpw(password, albumPassword)) {
                                Toast.makeText(context, context.getString(R.string.alert_dialog_wrong_password_notification), Toast.LENGTH_SHORT).show();
                                passwordInput.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_wrong_password_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                            } else {
                                MainActivity.isVerify = true;
                                Toast.makeText(context, context.getString(R.string.alert_dialog_unblock_successfully_notification), Toast.LENGTH_SHORT).show();

                                int pos = getAdapterPosition();
                                ImageListFragment imageFragment = new ImageListFragment(albums.get(pos).getPathImages(), albums.get(pos).getAlbumName(), "Album", pos - 1);
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame_layout_content, imageFragment).commit();
                                fragmentTransaction.addToBackStack("album");

                                dialog.dismiss();
                            }
                        });
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog.cancel());
                    } else {
                        int pos = getAdapterPosition();
                        ImageListFragment imageFragment = new ImageListFragment(albums.get(pos).getPathImages(), albums.get(pos).getAlbumName(), "Album", pos - 1);
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
            builder.setTitle(context.getString(R.string.alert_dialog_change_album_name));

            final EditText passwordInput = new EditText(context);
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordInput.setHint(context.getString(R.string.alert_dialog_enter_password_hint));
            passwordInput.requestFocus();

            final LinearLayout ll = new LinearLayout(context);
            ll.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50, 10, 50, 10);
            passwordInput.setLayoutParams(params);
            ll.addView(passwordInput);

            builder.setView(ll);
            builder.setPositiveButton(context.getString(R.string.alert_dialog_confirm), null);
            builder.setNegativeButton(context.getString(R.string.alert_dialog_cancel), null);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                String password = passwordInput.getText().toString();
                if (password.isBlank()) {
                    Toast.makeText(context, context.getString(R.string.alert_dialog_blank_password_notification), Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_blank_password_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else if (!BCrypt.checkpw(password, albumPassword)) {
                    Toast.makeText(context, context.getString(R.string.alert_dialog_wrong_password_notification), Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_wrong_password_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else {
                    dialog.dismiss();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setTitle(context.getString(R.string.alert_dialog_change_album_name));

                    final EditText input = new EditText(context);

                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setHint(context.getString(R.string.alert_dialog_enter_new_name_hint));
                    input.requestFocus();

                    final LinearLayout ll1 = new LinearLayout(context);
                    ll1.removeAllViews();
                    input.setLayoutParams(params);
                    ll1.addView(input);

                    builder1.setView(ll1);
                    builder1.setPositiveButton(context.getString(R.string.alert_dialog_confirm), null);
                    builder1.setNegativeButton(context.getString(R.string.alert_dialog_cancel), null);

                    AlertDialog dialog1 = builder1.create();
                    dialog1.show();
                    dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view12 -> {
                        String newName = input.getText().toString();
                        if (newName.isBlank()) {
                            Toast.makeText(context, context.getString(R.string.alert_dialog_blank_album_name_notification), Toast.LENGTH_SHORT).show();
                            input.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_blank_album_name_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                        } else if (newName.equals(AlbumFragment.albumList.get(position).getAlbumName())) {
                            Toast.makeText(context, context.getString(R.string.alert_dialog_change_album_name_successfully_notification), Toast.LENGTH_SHORT).show();
                            dialog1.dismiss();
                        } else if (Album.getAlbumNameArrayList(AlbumFragment.albumList).contains(newName)) {
                            Toast.makeText(context, context.getString(R.string.alert_dialog_existed_album_name_notification), Toast.LENGTH_SHORT).show();
                            input.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_existed_album_name_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                        } else {
                            AlbumFragment.albumList.get(position).setAlbumName(newName);
                            AlbumFragment.updateItem();
                            MainActivity.albumList.get(position - 1).setAlbumName(newName);
                            DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
                            DataLocalManager.saveSetStringData(KeyData.ALBUM_NAME_LIST.getKey(), Album.getAlbumNameSet(MainActivity.albumList));
                            Toast.makeText(context, context.getString(R.string.alert_dialog_change_album_name_successfully_notification), Toast.LENGTH_SHORT).show();
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
            builder.setTitle(context.getString(R.string.alert_dialog_change_album_name));

            final EditText input = new EditText(context);

            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setHint(context.getString(R.string.alert_dialog_enter_new_name_hint));
            input.requestFocus();

            final LinearLayout ll = new LinearLayout(context);
            ll.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50, 10, 50, 10);
            input.setLayoutParams(params);
            ll.addView(input);

            builder.setView(ll);
            builder.setPositiveButton(context.getString(R.string.alert_dialog_confirm), null);
            builder.setNegativeButton(context.getString(R.string.alert_dialog_cancel), null);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                String newName = input.getText().toString();
                if (newName.isBlank()) {
                    Toast.makeText(context, context.getString(R.string.alert_dialog_blank_album_name_notification), Toast.LENGTH_SHORT).show();
                    input.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_blank_album_name_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else if (newName.equals(AlbumFragment.albumList.get(position).getAlbumName())) {
                    Toast.makeText(context, context.getString(R.string.alert_dialog_change_album_name_successfully_notification), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else if (Album.getAlbumNameArrayList(AlbumFragment.albumList).contains(newName)) {
                    Toast.makeText(context, context.getString(R.string.alert_dialog_existed_album_name_notification), Toast.LENGTH_SHORT).show();
                    input.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_existed_album_name_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else {
                    AlbumFragment.albumList.get(position).setAlbumName(newName);
                    AlbumFragment.updateItem();
                    MainActivity.albumList.get(position - 1).setAlbumName(newName);
                    DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
                    DataLocalManager.saveSetStringData(KeyData.ALBUM_NAME_LIST.getKey(), Album.getAlbumNameSet(MainActivity.albumList));
                    Toast.makeText(context, context.getString(R.string.alert_dialog_change_album_name_successfully_notification), Toast.LENGTH_SHORT).show();
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
            builder.setTitle(context.getString(R.string.alert_dialog_delete_album));

            final EditText passwordInput = new EditText(context);
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordInput.setHint(context.getString(R.string.alert_dialog_enter_password_hint));
            passwordInput.requestFocus();

            final LinearLayout ll = new LinearLayout(context);
            ll.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50, 10, 50, 10);
            passwordInput.setLayoutParams(params);
            ll.addView(passwordInput);

            builder.setView(ll);
            builder.setPositiveButton(context.getString(R.string.alert_dialog_confirm), null);
            builder.setNegativeButton(context.getString(R.string.alert_dialog_cancel), null);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                String password = passwordInput.getText().toString();
                if (password.isBlank()) {
                    Toast.makeText(context, context.getString(R.string.alert_dialog_blank_password_notification), Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_blank_password_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else if (!BCrypt.checkpw(password, albumPassword)) {
                    Toast.makeText(context, context.getString(R.string.alert_dialog_wrong_password_notification), Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_wrong_password_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else {
                    dialog.dismiss();
                    DialogInterface.OnClickListener dialogClickListener = (dialog1, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Toast.makeText(context, context.getString(R.string.alert_dialog_deleted_album) + " '" + albums.get(position).getAlbumName() + "'", Toast.LENGTH_LONG).show();
                                AlbumFragment.albumList.remove(position);
                                AlbumFragment.updateItem();
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
                    builder1.setMessage(context.getString(R.string.alert_dialog_delete_album_confirm) + " '" + AlbumFragment.albumList.get(position).getAlbumName() + "'?").setPositiveButton(context.getString(R.string.alert_dialog_confirm), dialogClickListener)
                            .setNegativeButton(context.getString(R.string.alert_dialog_cancel), dialogClickListener).show();
                }
            });
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog.cancel());
        }

        if (!albums.get(position).getBlock() || albumPassword == null) {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Toast.makeText(context, context.getString(R.string.alert_dialog_deleted_album) + " '" + albums.get(position).getAlbumName() + "'", Toast.LENGTH_LONG).show();
                        AlbumFragment.albumList.remove(position);
                        AlbumFragment.updateItem();
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
            builder.setMessage("Bạn có chắc muốn xóa album '" + AlbumFragment.albumList.get(position).getAlbumName() + "'?").setPositiveButton(context.getString(R.string.alert_dialog_confirm), dialogClickListener)
                    .setNegativeButton(context.getString(R.string.alert_dialog_cancel), dialogClickListener).show();
        }
    }

    private static void blockAlbum(@NonNull View itemView, int position) {
        Context context = itemView.getContext();

        if (albumPassword == null) {
            Toast.makeText(context, context.getString(R.string.alert_dialog_not_setup_password), Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.alert_dialog_block_album));

            final EditText passwordInput = new EditText(context);
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordInput.setHint(context.getString(R.string.alert_dialog_enter_password_hint));
            passwordInput.requestFocus();

            final LinearLayout ll = new LinearLayout(context);
            ll.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50, 10, 50, 10);
            passwordInput.setLayoutParams(params);
            ll.addView(passwordInput);

            builder.setView(ll);
            builder.setPositiveButton(context.getString(R.string.alert_dialog_confirm), null);
            builder.setNegativeButton(context.getString(R.string.alert_dialog_cancel), null);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                String password = passwordInput.getText().toString();
                if (password.isBlank()) {
                    Toast.makeText(context, context.getString(R.string.alert_dialog_blank_password_notification), Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_blank_password_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else if (!BCrypt.checkpw(password, albumPassword)) {
                    Toast.makeText(context, context.getString(R.string.alert_dialog_wrong_password_notification), Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_wrong_password_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else {
                    albums.get(position).setBlock(true);
                    MainActivity.albumList.get(position - 1).setBlock(true);
                    DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
                    DataLocalManager.saveSetStringData(KeyData.ALBUM_NAME_LIST.getKey(), Album.getAlbumNameSet(MainActivity.albumList));
                    Toast.makeText(context, context.getString(R.string.alert_dialog_block_album_successfully_notification), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(context, context.getString(R.string.alert_dialog_remove_block_album_successfully_notification), Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.alert_dialog_remove_block_album));

            final EditText passwordInput = new EditText(itemView.getContext());
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordInput.setHint(context.getString(R.string.alert_dialog_enter_password_hint));
            passwordInput.requestFocus();

            final LinearLayout ll = new LinearLayout(context);
            ll.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50, 10, 50, 10);
            passwordInput.setLayoutParams(params);
            ll.addView(passwordInput);

            builder.setView(ll);
            builder.setPositiveButton(context.getString(R.string.alert_dialog_confirm), null);
            builder.setNegativeButton(context.getString(R.string.alert_dialog_cancel), null);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                String password = passwordInput.getText().toString();
                if (password.isBlank()) {
                    Toast.makeText(context, context.getString(R.string.alert_dialog_blank_password_notification), Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_blank_password_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else if (!BCrypt.checkpw(password, albumPassword)) {
                    Toast.makeText(context, context.getString(R.string.alert_dialog_wrong_password_notification), Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_wrong_password_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else {
                    albums.get(position).setBlock(false);
                    MainActivity.albumList.get(position - 1).setBlock(false);
                    DataLocalManager.saveObjectList(KeyData.ALBUM_DATA_LIST.getKey(), MainActivity.albumList);
                    DataLocalManager.saveSetStringData(KeyData.ALBUM_NAME_LIST.getKey(), Album.getAlbumNameSet(MainActivity.albumList));
                    Toast.makeText(context, context.getString(R.string.alert_dialog_remove_block_album_successfully_notification), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog.cancel());
        }
    }

    private static void exportAlbum(@NonNull View itemView, int position) throws Exception {
        Context context = itemView.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (!MainActivity.isVerify && albums.get(position).getBlock() && albumPassword != null) {
            builder.setTitle(context.getString(R.string.alert_dialog_blocked_album));

            final EditText passwordInput = new EditText(itemView.getContext());
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordInput.setHint(context.getString(R.string.alert_dialog_enter_password_hint));
            passwordInput.requestFocus();

            final LinearLayout ll = new LinearLayout(context);
            ll.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50, 10, 50, 10);
            passwordInput.setLayoutParams(params);
            ll.addView(passwordInput);

            builder.setView(ll);
            builder.setPositiveButton(context.getString(R.string.alert_dialog_confirm), null);
            builder.setNegativeButton(context.getString(R.string.alert_dialog_cancel), null);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                String password = passwordInput.getText().toString();
                if (password.isBlank()) {
                    Toast.makeText(context, context.getString(R.string.alert_dialog_blank_password_notification), Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_blank_password_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else if (!BCrypt.checkpw(password, albumPassword)) {
                    Toast.makeText(context, context.getString(R.string.alert_dialog_wrong_password_notification), Toast.LENGTH_SHORT).show();
                    passwordInput.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_wrong_password_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else {
                    MainActivity.isVerify = true;
                    Toast.makeText(context, context.getString(R.string.alert_dialog_unblock_successfully_notification), Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder5 = new AlertDialog.Builder(context);
                    builder5.setTitle(context.getString(R.string.alert_dialog_export_zip));

                    final EditText folderNameInput = new EditText(context);
                    folderNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
                    folderNameInput.setHint(context.getString(R.string.alert_dialog_enter_file_name_hint));
                    folderNameInput.requestFocus();

                    final LinearLayout ll1 = new LinearLayout(context);
                    ll1.removeAllViews();
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params1.setMargins(50, 10, 50, 10);
                    folderNameInput.setLayoutParams(params1);
                    ll1.addView(folderNameInput);

                    builder5.setView(ll1);
                    builder5.setPositiveButton(context.getString(R.string.alert_dialog_confirm), null);
                    builder5.setNegativeButton(context.getString(R.string.alert_dialog_cancel), null);

                    AlertDialog dialog1 = builder5.create();
                    dialog1.show();
                    dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view2 -> {
                        String folderName = folderNameInput.getText().toString();
                        if (folderName.isBlank()) {
                            Toast.makeText(context, context.getString(R.string.alert_dialog_blank_file_name_notification), Toast.LENGTH_SHORT).show();
                            folderNameInput.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_blank_file_name_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                        } else {
                            File file = new File(MainActivity.zipPath + "/" + folderName + ".zip");
                            if (file.exists()) {
                                DialogInterface.OnClickListener dialogClickListener = (dialog2, which) -> {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            try {
                                                compressFiles(albums.get(position).getPathImages(), MainActivity.zipPath + "/" + folderName + ".zip", folderName);
                                                MainActivity.updateZipList();
                                                Toast.makeText(context, context.getString(R.string.toast_compress_file_successfully), Toast.LENGTH_SHORT).show();
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            dialog2.cancel();
                                            break;
                                    }
                                };
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(itemView.getContext());
                                builder1.setMessage(context.getString(R.string.alert_dialog_overwrite_confirm)).setPositiveButton(context.getString(R.string.alert_dialog_confirm), dialogClickListener)
                                        .setNegativeButton(context.getString(R.string.alert_dialog_cancel), dialogClickListener).show();
                            }

                            else {
                                try {
                                    compressFiles(albums.get(position).getPathImages(), MainActivity.zipPath + "/" + folderName + ".zip", folderName);
                                    MainActivity.updateZipList();
                                    Toast.makeText(context, context.getString(R.string.toast_compress_file_successfully), Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            dialog1.dismiss();
                        }
                    });
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog.cancel());

                    dialog.dismiss();
                }
            });
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog.cancel());
        } else {
            builder.setTitle(context.getString(R.string.alert_dialog_export_zip));

            final EditText folderNameInput = new EditText(context);
            folderNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
            folderNameInput.setHint(context.getString(R.string.alert_dialog_enter_file_name_hint));
            folderNameInput.requestFocus();

            final LinearLayout ll = new LinearLayout(context);
            ll.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50, 10, 50, 10);
            folderNameInput.setLayoutParams(params);
            ll.addView(folderNameInput);

            builder.setView(ll);
            builder.setPositiveButton(context.getString(R.string.alert_dialog_confirm), null);
            builder.setNegativeButton(context.getString(R.string.alert_dialog_cancel), null);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                String folderName = folderNameInput.getText().toString();
                if (folderName.isBlank()) {
                    Toast.makeText(context, context.getString(R.string.alert_dialog_blank_file_name_notification), Toast.LENGTH_SHORT).show();
                    folderNameInput.setError(HtmlCompat.fromHtml("<font>" + context.getString(R.string.alert_dialog_blank_file_name_notification) + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else {
                    File file = new File(MainActivity.zipPath + "/" + folderName + ".zip");
                    if (file.exists()) {
                        DialogInterface.OnClickListener dialogClickListener = (dialog1, which) -> {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    try {
                                        String outputZipFilePath = MainActivity.zipPath + "/" + folderName + ".zip";
                                        compressFiles(albums.get(position).getPathImages(), outputZipFilePath, folderName);
                                        Toast.makeText(context, context.getString(R.string.toast_compress_file_successfully), Toast.LENGTH_SHORT).show();
                                        MainActivity.updateZipList();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog1.cancel();
                                    break;
                            }
                        };
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(itemView.getContext());
                        builder1.setMessage(context.getString(R.string.alert_dialog_overwrite_confirm)).setPositiveButton(context.getString(R.string.alert_dialog_confirm), dialogClickListener)
                                .setNegativeButton(context.getString(R.string.alert_dialog_cancel), dialogClickListener).show();
                    }

                    else {
                        try {
                            compressFiles(albums.get(position).getPathImages(), MainActivity.zipPath + "/" + folderName + ".zip", folderName);
                            Toast.makeText(context, context.getString(R.string.toast_compress_file_successfully), Toast.LENGTH_SHORT).show();
                            MainActivity.updateZipList();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Toast.makeText(context, context.getString(R.string.toast_compress_file_successfully), Toast.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();
                }
            });
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view12 -> dialog.cancel());
        }
    }

    public static void compressFiles(@NonNull ArrayList<String> filePaths, String outputZipFilePath, String folderName) throws IOException {
        // Tạo đối tượng ZipOutputStream
        ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(new FileOutputStream(outputZipFilePath));

        // Duyệt qua danh sách đường dẫn file và thêm chúng vào zip file
        for (String filePath : filePaths) {
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            ZipArchiveEntry zipEntry = new ZipArchiveEntry(folderName + "/" + file.getName());
            zipOutputStream.putArchiveEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileInputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, length);
            }

            fileInputStream.close();
            zipOutputStream.closeArchiveEntry();
        }

        // Đóng ZipOutputStream
        zipOutputStream.close();
    }
}