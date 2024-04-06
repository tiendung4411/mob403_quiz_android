package com.example.mob403_quiz.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.mob403_quiz.Adapter.CategoryAdapter;
import com.example.mob403_quiz.Login_Activity;
import com.example.mob403_quiz.MainActivity;
import com.example.mob403_quiz.Models.Categories;
import com.example.mob403_quiz.PlayQuizActivity;
import com.example.mob403_quiz.R;
import com.example.mob403_quiz.Services.APIService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private ArrayList<Categories> categoriesList;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = rootView.findViewById(R.id.rcv_category);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_view_spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        CustomGridLayoutManager layoutManager = new CustomGridLayoutManager(requireContext(), 6);
        recyclerView.setLayoutManager(layoutManager);

        fetchCategories();

        ImageView large_image = rootView.findViewById(R.id.large_image);
        loadImageWithCorners(requireContext(), "https://i.imgur.com/v655VqZ.png", large_image);

        return rootView;
    }

    public void loadImageWithCorners(Context context, String url, ImageView view) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.color.black)
                .error(R.color.black)
                .diskCacheStrategy(DiskCacheStrategy.DATA); // Use DATA instead of SOURCE

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transform(new MultiTransformation<Bitmap>(new CenterCrop(), new RoundedCorners(32)))
                .into(view);
    }

    public void fetchCategories() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.base_link)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService service = retrofit.create(APIService.class);
        service.getCategories().enqueue(new Callback<ArrayList<Categories>>() {
            @Override
            public void onResponse(Call<ArrayList<Categories>> call, Response<ArrayList<Categories>> response) {
                if (response.isSuccessful() && isAdded()) { // Check if fragment is still added
                    categoriesList = response.body(); // Update the global variable
                    CategoryAdapter categoryAdapter = new CategoryAdapter(requireContext(), categoriesList, category -> {
                        // Handle item click here, for example:
                        // Open a dialog with category details
                        openDialog(category);
                        Log.d("HomeFragment", "Clicked on category: " + category.getName());
                    });
                    recyclerView.setAdapter(categoryAdapter);
                    Log.d("HomeFragment", "Fetched categories successfully");
                } else {
                    Log.d("HomeFragment", "Failed to fetch categories or fragment not added.");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Categories>> call, Throwable t) {
                if (isAdded()) { // Check if fragment is still added
                    // Handle failure
                    Log.d("HomeFragment", "Network request failed or fragment not added.");
                }
            }
        });
    }

    public class CustomGridLayoutManager extends GridLayoutManager {

        public CustomGridLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
            setSpanSizeLookup(new SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (position % 6) { // 6 because it's the period of your desired pattern (2, 1, 3, 2, 1, 3)
                        case 0:
                            return 6;

                        // 2 cards in a row
                        case 1:
                            // 1 big card in a row
                        case 2:
                            return 3;
                        case 3:
                        case 4:
                        case 5:
                            return 2; // 3 small cards in a row
                        default:
                            return 6;
                    }
                }
            });
        }
    }

    public void openDialog(Categories category) {
        // Create a dialog with category details
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.play_quiz_dialog);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.corners_light_bg);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(layoutParams);

            Button btnCancel = dialog.findViewById(R.id.btnCancel);
            // Find the button inside the dialog's layout

            if (btnCancel != null) {
                // Set OnClickListener for the button
                btnCancel.setOnClickListener(view -> dialog.dismiss());
            };

            Button btnStart = dialog.findViewById(R.id.btn_start);

            if (btnStart != null) {
                // Set OnClickListener for the button
                btnStart.setOnClickListener(view -> {
                    Intent intent = new Intent(requireContext(), PlayQuizActivity.class);
                    intent.putExtra("category_id", category.getId());
                    startActivity(intent);
                    Log.d("Clicked category", "id: "+ category.getId());
                    dialog.dismiss();
                });
            }
        }

        dialog.show();
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first row
            if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1) {
                outRect.top = space;
            }
        }
    }
}
