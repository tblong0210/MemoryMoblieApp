package com.example.memorymoblieapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.adapter.GalleryAdapter;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.example.memorymoblieapp.main.MainActivity;
import com.example.memorymoblieapp.view.ViewSearch;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ImageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerview;
    private ArrayList<String> images;
    List<String> imageDates;
    private ImageView searchBtn;
    private ImageView sortBtn;
    private ImageView btnAddImage, btnUrl, btnCamera;
    private Animation menuFABShow, menuFABHide;
    private int CAMERA_CAPTURED = 102;
    private Boolean addIsPressed = false;
    @SuppressLint("StaticFieldLeak")
    private static TextView cancelLongClick;
    private int numberCol = 3;
    private GalleryAdapter galleryAdapter;

    public ImageFragment() {
        images = new ArrayList<>();
        imageDates = new ArrayList<>();
    }

    public ImageFragment(ArrayList<String> images, List<String> imageDates) {
        this.images = images;
        // Required empty public constructor
        this.imageDates = imageDates;
    }

    public void setListImagesAndDates(ArrayList<String> images, List<String> imageDates) {

        this.images = images;
        // Required empty public constructor
        this.imageDates = imageDates;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        searchBtn = view.findViewById(R.id.imgBtnSearch);
        sortBtn = view.findViewById(R.id.sort_image_button);

        btnAddImage = view.findViewById(R.id.btnAddImage);
        btnCamera = view.findViewById(R.id.btnCamera);
        btnUrl = view.findViewById(R.id.btnUrl);

        menuFABShow = AnimationUtils.loadAnimation(view.getContext(), R.anim.menu_button_show);
        menuFABHide = AnimationUtils.loadAnimation(view.getContext(), R.anim.menu_bottom_hide);

        cancelLongClick = view.findViewById(R.id.button_cancel_select);
        recyclerview = view.findViewById(R.id.recyclerview_gallery_images);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), numberCol);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setHasFixedSize(true);

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnimationButton(addIsPressed);
                setVisibilityButton(addIsPressed);
                addIsPressed = !addIsPressed;
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        btnUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                new UrlDialog().show(activity.getSupportFragmentManager(), UrlDialog.Tag);
            }
        });

        galleryAdapter = new GalleryAdapter(getContext(), images, imageDates, new GalleryAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(String path) {
                Log.d("tag", path);
            }

            @Override
            public void onPhotoLongClick(String path) {
                Log.d("tag", "Long" + path);
                cancelLongClick.setVisibility(View.VISIBLE);

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ViewSearch.class);
                startActivity(intent);
            }
        });
        cancelLongClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelLongClick.setVisibility(View.INVISIBLE);

                galleryAdapter.setIsLongClick(false);
                galleryAdapter.setListSelect();
                galleryAdapter.notifyDataSetChanged();
                GalleryAdapter.hideSelectionBar();
            }
        });
        recyclerview.setAdapter(galleryAdapter);
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag", "Clicked sort button");

                ArrayList<String> newImageDateFlag = new ArrayList<>();
                ArrayList<String> newImagesFlag = new ArrayList<String>();
                ArrayList<String> newImageDate = new ArrayList<>();
                ArrayList<String> newImages = new ArrayList<String>();
                int flag = 0;

                for (int i = 0; i < images.size(); i++) {
                    if (!images.get(i).equals(" ")) {
                        newImageDateFlag.add(imageDates.get(i));
                        newImagesFlag.add(images.get(i));
             /*           newImageDateFlag.add( Integer.toString(i));
                        newImagesFlag.add(Integer.toString(i));*/

                    }
                }
                String temp;
                int n = newImagesFlag.size();
                for (int i = 0; i < n / 2; i++) {
                    temp = newImagesFlag.get(i);
                    newImagesFlag.set(i, newImagesFlag.get(n - 1 - i));
                    newImagesFlag.set(n - 1 - i, temp);

                    temp = newImageDateFlag.get(i);
                    newImageDateFlag.set(i, newImageDateFlag.get(n - 1 - i));
                    newImageDateFlag.set(n - 1 - i, temp);
                }

                for (int i = 0; i < newImagesFlag.size(); i++) {
                    Log.d("Imagess", newImagesFlag.get(i) + " - " + newImageDateFlag.get(i));
                }

                Log.d("Tagggg", newImageDateFlag.size() + "  -  " + newImageDateFlag.size());

                for (int i = 0; i < newImagesFlag.size(); i++) {
                    if (!newImagesFlag.get(i).equals(" ")) {
                        if (i != 0 && !newImageDateFlag.get(i).equals(newImageDateFlag.get(i - 1))) {

                            if (flag % 3 == 2) {
                                newImages.add(" ");
                                newImageDate.add(" ");
                            }
                            if (flag % 3 == 1) {
                                newImages.add(" ");
                                newImages.add(" ");
                                newImageDate.add(" ");
                                newImageDate.add(" ");
                            }
                            flag = 0;
                        }
                        newImages.add(newImagesFlag.get(i));
                        newImageDate.add(newImageDateFlag.get(i));
                        flag++;

                    }
                }
                images = newImages;
                imageDates = newImageDate;
                galleryAdapter.setImages(newImages);
                galleryAdapter.setImageDates(newImageDate);
                galleryAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_CAPTURED) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                saveImage(bitmap);
            }
        }
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        if (images == null) return;

        this.images = images;
        galleryAdapter.setImages(images);
        galleryAdapter.notifyDataSetChanged();
    }

    public List<String> getImageDates() {
        return imageDates;
    }

    public void setImageDates(List<String> imageDates) {
        if (imageDates == null) return;

        this.imageDates = imageDates;
        galleryAdapter.setImageDates(imageDates);
        galleryAdapter.notifyDataSetChanged();
    }

    void saveImage(Bitmap bitmap) {
        File pictureFile = new File(getFolderDirectory(), bitmap.toString() + ".jpg");
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
        } catch (Exception e) {
            Log.e("Error to save image! ", e.getMessage());
        }
        MainActivity.updateData(getContext());
    }

    private File getFolderDirectory() {
        String pathFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/Camera";
        File pictureDirectory = new File(pathFolder);
        if (!pictureDirectory.exists())
            pictureDirectory.mkdirs();
        return pictureDirectory;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        recyclerview = view.findViewById(R.id.recyclerview_gallery_images);
//        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
//        recyclerview.setLayoutManager(layoutManager);
//        recyclerview.setHasFixedSize(true);
//        GalleryAdapter galleryAdapter = new GalleryAdapter(getContext(), images);
//        recyclerview.setAdapter(galleryAdapter);
    }

    void setAnimationButton(boolean isPressed) {
        if (isPressed) {
            btnAddImage.setImageResource(R.drawable.ic_add);
            btnCamera.startAnimation(menuFABHide);
            btnUrl.startAnimation(menuFABHide);
        } else {
            btnAddImage.setImageResource(R.drawable.ic_close);
            btnCamera.startAnimation(menuFABShow);
            btnUrl.startAnimation(menuFABShow);
        }
    }

    void setVisibilityButton(boolean isPressed) {
        if (isPressed) {
            btnCamera.setVisibility(FloatingActionButton.INVISIBLE);
            btnUrl.setVisibility(FloatingActionButton.INVISIBLE);
        } else {
            btnCamera.setVisibility(FloatingActionButton.VISIBLE);
            btnUrl.setVisibility(FloatingActionButton.VISIBLE);
        }
    }

    void openCamera() {
        try {
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getActivity().startActivityFromFragment(this, takePhotoIntent, CAMERA_CAPTURED);
        } catch (Exception e) {
            Log.e("Error to open camera! ", e.getMessage());
        }
    }

    public static void turnOffselectMode() {
        cancelLongClick.callOnClick();
    }
}
