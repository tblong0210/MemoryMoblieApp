package com.example.memorymoblieapp.local_data_storage;

public enum KeyData {
    SHARED_PREFERENCES,
    ALBUM_LIST,
    ALBUM_DATA_LIST,
    IMAGE_PATH_LIST,
    IMAGE_PATH_VIEW_LIST,
    HISTORY_SEARCH,
    FAVORITE_LIST,
    TRASH_LIST;

    public String getKey() {
        switch (this) {
            case SHARED_PREFERENCES:
                return "SHARED_PREFERENCES";
            case ALBUM_LIST:
                return "ALBUM_LIST";
            case ALBUM_DATA_LIST:
                return "ALBUM_DATA_LIST";
            case IMAGE_PATH_LIST:
                return "IMAGE_PATH_LIST";
            case IMAGE_PATH_VIEW_LIST:
                return "IMAGE_PATH_VIEW_LIST";
            case HISTORY_SEARCH:
                return "HISTORY_SEARCH";
            case FAVORITE_LIST:
                return "FAVORITE_LIST";
            case TRASH_LIST:
                return "TRASH_LIST";
            default:
                return null;
        }
    }
}
