package com.example.memorymoblieapp.local_data_storage;

public enum KeyData {
    SHARED_PREFERENCES,
    ALBUM_NAME_LIST,
    ALBUM_DATA_LIST,
    ALBUM_PASSWORD,
    IMAGE_PATH_LIST,
    IMAGE_PATH_VIEW_LIST,
    HISTORY_SEARCH_IMAGE,
    HISTORY_SEARCH_ALBUM,
    FAVORITE_LIST,
    TRASH_LIST,
    UN_AVAILABLE_IMAGE;

    public String getKey() {
        switch (this) {
            case SHARED_PREFERENCES:
                return "SHARED_PREFERENCES";
            case ALBUM_NAME_LIST:
                return "ALBUM_NAME_LIST";
            case ALBUM_DATA_LIST:
                return "ALBUM_DATA_LIST";
            case ALBUM_PASSWORD:
                return "ALBUM_PASSWORD";
            case IMAGE_PATH_LIST:
                return "IMAGE_PATH_LIST";
            case IMAGE_PATH_VIEW_LIST:
                return "IMAGE_PATH_VIEW_LIST";
            case HISTORY_SEARCH_IMAGE:
                return "HISTORY_SEARCH_IMAGE";
            case HISTORY_SEARCH_ALBUM:
                return "HISTORY_SEARCH_ALBUM";
            case FAVORITE_LIST:
                return "FAVORITE_LIST";
            case TRASH_LIST:
                return "TRASH_LIST";
            case UN_AVAILABLE_IMAGE:
                return "UNAVAILABLE_IMAGE";
            default:
                return null;
        }
    }
}
