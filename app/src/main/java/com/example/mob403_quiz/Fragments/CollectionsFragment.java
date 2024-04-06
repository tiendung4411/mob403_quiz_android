package com.example.mob403_quiz.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.mob403_quiz.Adapter.CategoryAdapter;
import com.example.mob403_quiz.Models.Categories;
import com.example.mob403_quiz.PlayQuizActivity;
import com.example.mob403_quiz.R;
import com.example.mob403_quiz.Services.APIService;
import com.example.mob403_quiz.Services.RetrofitClient;
import com.example.mob403_quiz.Services.SharedPreferencesManager;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CollectionsFragment extends Fragment {

    private ArrayList<Categories> categoriesList;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_collections, container, false);

        recyclerView = rootView.findViewById(R.id.rcv_collections);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_view_spacing_linear);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        fetchCategories();


        return rootView;
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
                        String userRole = SharedPreferencesManager.getUserRole(requireContext());
                        if(userRole.equals("admin")) {
                            openDialog(category);
                        } else {
                            Intent intent = new Intent(requireContext(), PlayQuizActivity.class);
                            intent.putExtra("category_id", category.getId());
                            startActivity(intent);
                        }

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

    public void openDialog(Categories category) {
        // Create a dialog with category details
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_dialog);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.corners_light_bg);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(layoutParams);
            Button btnCancel = dialog.findViewById(R.id.btn_cancel);
            // Find the button inside the dialog's layout

            if (btnCancel != null) {
                // Set OnClickListener for the button
                btnCancel.setOnClickListener(view -> dialog.dismiss());
            };

            Button btnDelete = dialog.findViewById(R.id.btn_delete);

            if (btnDelete != null) {
                // Set OnClickListener for the button
                btnDelete.setOnClickListener(view -> {
                    int id = category.getId();
                   APIService apiService = RetrofitClient.getClient().create(APIService.class);
                   Call<JsonObject> call = apiService.deleteCategory(id);
                     call.enqueue(new Callback<JsonObject>() {
                          @Override
                          public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                 Log.d("HomeFragment", "Category deleted successfully");
                                 fetchCategories();
                                 dialog.dismiss();
                            } else {
                                 Log.d("HomeFragment", "Failed to delete category");
                            }
                          }

                          @Override
                          public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.d("HomeFragment", "Network request failed");
                          }
                     });
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
            if (parent.getChildLayoutPosition(view) == 0 ) {
                outRect.top = space;
            }
        }
    }
}