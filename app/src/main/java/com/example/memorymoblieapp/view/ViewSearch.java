package com.example.memorymoblieapp.view;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.SearchItemAdapter;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ViewSearch extends AppCompatActivity {
    private ImageView btnTypeFilter;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ArrayList<String> pathImages;
    private ArrayList<String> pathAlbums;
    private List<String> resultSearch;
    private int typeSearch;
    private ArrayList<String> historyImage;

    public ViewSearch() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Boolean isThemeDark = DataLocalManager.getBooleanData(KeyData.DARK_MODE.getKey());
        isThemeDark = isThemeDark != null && isThemeDark;

        setTheme(isThemeDark ? R.style.ThemeDark_MemoryMobileApp : R.style.Theme_MemoryMobileApp);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_picture);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initViews();
        initActions();
    }

    private void initViews() {
        pathImages = new ArrayList<>();
        pathAlbums = new ArrayList<>();
        historyImage = new ArrayList<>();

        ArrayList<String> getPathImages = DataLocalManager.getStringList(KeyData.IMAGE_PATH_LIST.getKey());
        Set<String> getPathAlbums = DataLocalManager.getSetList(KeyData.ALBUM_NAME_LIST.getKey());
        Set<String> getPathHistoryImage = DataLocalManager.getSetList(KeyData.HISTORY_SEARCH_IMAGE.getKey());

        if (getPathImages != null)
            pathImages.addAll(getPathImages);
        if (getPathAlbums != null)
            pathAlbums.addAll(getPathAlbums);
        if (getPathHistoryImage != null)
            historyImage.addAll(getPathHistoryImage);

        btnTypeFilter = findViewById(R.id.typeFilter);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        typeSearch = R.id.name;

        resetHistory(historyImage);
    }

    private void initActions() {
        btnTypeFilter.setOnClickListener(view -> createMenuPopup(btnTypeFilter, R.menu.search_type));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> history = new ArrayList<>();
                ArrayList<String> paths = new ArrayList<>();

                if (typeSearch == R.id.name) {
                    history.addAll(DataLocalManager.getSetList(KeyData.HISTORY_SEARCH_IMAGE.getKey()));
                    paths.addAll(pathImages);
                } else {
                    history.addAll(DataLocalManager.getSetList(KeyData.HISTORY_SEARCH_ALBUM.getKey()));
                    paths.addAll(pathAlbums);
                }
                if (!paths.isEmpty()) {
                    if (newText.equals("")) {
                        resetHistory(history);
                    } else {
                        String name = newText.toLowerCase();
                        resultSearch = paths.stream()
                                .filter(str -> str.substring(
                                                str.lastIndexOf('/') + 1)
                                        .toLowerCase()
                                        .contains(name))
                                .collect(Collectors.toList());
                        resetHistory((ArrayList<String>) resultSearch);
                    }
                }
                return false;
            }
        });
    }

    private void resetHistory(ArrayList<String> paths) {
        SearchItemAdapter adapter = new SearchItemAdapter(typeSearch);
        adapter.setElement(paths);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void createMenuPopup(View item, int menuItem) {
        PopupMenu popupMenu = new PopupMenu(item.getContext(), item);
        popupMenu.inflate(menuItem);
        popupMenu.setGravity(Gravity.END);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                typeSearch = menuItem.getItemId();
                ArrayList<String> history = new ArrayList<>();
                if (typeSearch == R.id.name) {
                    Toast.makeText(ViewSearch.this, "Find by name", Toast.LENGTH_SHORT).show();

                    history.addAll(DataLocalManager.getSetList(KeyData.HISTORY_SEARCH_IMAGE.getKey()));
                    searchView.setQueryHint(getString(R.string.query_hint_name));

                } else if (typeSearch == R.id.album) {
                    Toast.makeText(ViewSearch.this, "Find by album", Toast.LENGTH_SHORT).show();

                    history.addAll(DataLocalManager.getSetList(KeyData.HISTORY_SEARCH_ALBUM.getKey()));
                    searchView.setQueryHint(getString(R.string.query_hint_album));

                }
                resetHistory(history);
                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
