package com.example.memorymoblieapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.main.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewImage extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ImageView imageView;
    private boolean isFavorite = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting_image);
        initViews();
        initActions();
    }

    private void initViews(){
        bottomNavigationView = findViewById(R.id.navSetting);
        imageView = findViewById(R.id.imgView);
    }

    private void initActions(){
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.favorite:
                    // Đánh dấu trạng thái yêu thích
                    isFavorite = !isFavorite;
                    if (isFavorite) {
                        Toast.makeText(ViewImage.this, "Add favorite", Toast.LENGTH_SHORT).show();
                        item.setIcon(R.mipmap.ic_heart_like);
                        item.setTitle(R.string.action_favorite);
                    } else {
                        Toast.makeText(ViewImage.this, "Remove favorite", Toast.LENGTH_SHORT).show();
                        item.setIcon(R.mipmap.ic_heart);
                        item.setTitle(R.string.action_unfavorite);
                    }
                    break;
                case R.id.edit:
                    Toast.makeText(ViewImage.this, "edit", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.share:
                    Toast.makeText(ViewImage.this, "share", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.trash:
                    Toast.makeText(ViewImage.this, "trah", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.more:
                    Toast.makeText(ViewImage.this, "more", Toast.LENGTH_SHORT).show();
                    createMenuPopup(bottomNavigationView, R.menu.more_setting_image);
                    break;
            }
            return true;
        });
    }

    private void createMenuPopup(View item, int menuItem){
        PopupMenu popupMenu = new PopupMenu(item.getContext(), item);
        popupMenu.inflate(menuItem);
        popupMenu.setGravity(Gravity.END);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int item = menuItem.getItemId();
                if(item == R.id.viewDetails){
                    Toast.makeText(ViewImage.this, "View details", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ViewImage.this, ViewDetails.class);
                    startActivity(intent);
                }
                else if(item == R.id.setWallpaper){
                    Toast.makeText(ViewImage.this, "Set Wallpaper", Toast.LENGTH_SHORT).show();
                }
                else if(item == R.id.setLockscreen){
                    Toast.makeText(ViewImage.this, "Set Lockscreen", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        popupMenu.show();
    }
}
