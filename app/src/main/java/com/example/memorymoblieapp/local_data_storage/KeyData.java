package com.example.memorymoblieapp.local_data_storage;

public enum KeyData {
    SHARED_PREFERENCES,
    ALBUM_LIST,
    ALBUM_DATA_LIST,
    IMAGE_PATH_LIST;

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
            default:
                return null;
        }
    }
}
