package com.example.memorymoblieapp.fragment_love;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.activity.MainActivity;
import com.example.memorymoblieapp.fragment_album.AlbumFragment;

import java.io.File;
import java.util.ArrayList;

public class ImageListFragment extends Fragment {
    ArrayList<String> imageList;
    static ImageListAdapter adapter;
    private Context context;
    private final String title;
    String type;
    int albumPos;

    public static CardView imgBtnBackContainer;
    public static CardView imgBtnChangeViewContainer;

    public ImageListFragment() {
        imageList = new ArrayList<>();
        title = "";
        type = "";
        albumPos = -1;
    }

    public ImageListFragment(ArrayList<String> imageList, String title, String type) {
        this.imageList = imageList;
        this.title = title;
        this.type = type;
        this.albumPos = -1;
    }

    public ImageListFragment(ArrayList<String> imageList, String title, String type, int albumPos) {
        this.imageList = imageList;
        this.title = title;
        this.type = type;
        this.albumPos = albumPos;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View imagesFragment;
        imagesFragment = inflater.inflate(R.layout.image_list_fragment, container, false);

        for (String image : imageList) {
            File file = new File(image);
            if (!file.exists())
                imageList.remove(image);
        }

        RecyclerView recycler = imagesFragment.findViewById(R.id.imageRecView);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        if (MainActivity.detailed)
            params.gravity = Gravity.START;
        else
            params.gravity = Gravity.CENTER_HORIZONTAL;
        recycler.setLayoutParams(params);

        TextView txtTitle = imagesFragment.findViewById(R.id.txtTitle);
        txtTitle.setText(title);

        ImageButton imgBtnChangeView = imagesFragment.findViewById(R.id.imgBtnChangeView);
        ImageButton imgBtnCancelSelect = imagesFragment.findViewById(R.id.imgBtnCancelSelect);
        ImageView ivBack = imagesFragment.findViewById(R.id.ivBack);

        imgBtnBackContainer = imagesFragment.findViewById(R.id.imgBtnBackContainer);
        imgBtnBackContainer.setVisibility(View.GONE);
        imgBtnChangeViewContainer = imagesFragment.findViewById(R.id.imgBtnChangeViewContainer);
        imgBtnChangeViewContainer.setVisibility(View.VISIBLE);

        if (MainActivity.detailed)
            imgBtnChangeView.setImageDrawable(imagesFragment.getContext().getResources().getDrawable(R.drawable.ic_view_detail));
        else
            imgBtnChangeView.setImageDrawable(imagesFragment.getContext().getResources().getDrawable(R.drawable.ic_view_grid));

        if (!type.equals("Album"))
            ivBack.setVisibility(View.GONE);
        ivBack.setOnClickListener(view -> {
            requireActivity().onBackPressed();
        });

        imgBtnCancelSelect.setOnClickListener(view -> ImageListAdapter.ViewHolder.turnOffSelectMode());

        imgBtnChangeView.setOnClickListener(view -> {
            MainActivity.detailed = !MainActivity.detailed;
            ImageListFragment imageFragment = new ImageListFragment(imageList, title, type);
            ImageListAdapter.ViewHolder.turnOffSelectMode();

            @SuppressLint("CommitTransaction") FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout_content, imageFragment).commit();
            fragmentTransaction.addToBackStack("image");

            assert getFragmentManager() != null;
            getFragmentManager().popBackStack();
        });

        if (MainActivity.detailed) {
            // LayoutManager
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recycler.setLayoutManager(gridLayoutManager);
            recycler.addItemDecoration(new GridSpacingItemDecoration(5, 1));

            // Divider
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler.getContext(), gridLayoutManager.getOrientation());
            recycler.addItemDecoration(dividerItemDecoration);
        } else {
            // LayoutManager
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recycler.setLayoutManager(gridLayoutManager);
            recycler.addItemDecoration(new GridSpacingItemDecoration(5, 3));
        }

        adapter = new ImageListAdapter(imageList, imagesFragment.getContext(), MainActivity.detailed, type, albumPos);
        recycler.setAdapter(adapter);

        return imagesFragment;
    }

    @SuppressLint("NotifyDataSetChanged")
    static public void updateItem() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }
}

class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private final int spacing;
    private final int spanCount;

    public GridSpacingItemDecoration(int spacing, int spanCount) {
        this.spacing = spacing;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, View view, @NonNull RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;

        outRect.left = spacing - column * spacing / spanCount;
        outRect.right = (column + 1) * spacing / spanCount;
        if (position < spanCount) {
            outRect.top = spacing;
        }
        outRect.bottom = spacing;
    }
}