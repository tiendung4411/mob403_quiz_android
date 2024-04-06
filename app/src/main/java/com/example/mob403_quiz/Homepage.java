package com.example.mob403_quiz;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.mob403_quiz.Adapter.CategoryAdapter;
import com.example.mob403_quiz.Adapter.UserAdapter;
import com.example.mob403_quiz.Models.Categories;
import com.example.mob403_quiz.Models.Users;
import com.example.mob403_quiz.Services.APIService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Homepage extends AppCompatActivity {

    private static final int NAV_ITEM_HOME = R.id.nav_item1;
    private static final int NAV_ITEM_SEARCH = R.id.nav_item2;
    private static final int NAV_ITEM_CART = R.id.nav_item3;
    private static final int NAV_ITEM_PROFILE = R.id.nav_item4;
    ImageView large_image,back_icon;

    private NavController navController;
    private ArrayList<Users> usersList;
    private ArrayList<Categories> categoriesList;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isNightMode = currentNightMode == Configuration.UI_MODE_NIGHT_YES;

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        back_icon = findViewById(R.id.back_icon);
        // Inside onCreate() method

        // Set the background drawable based on night mode
        if (isNightMode) {
            bottomNavigationView.setBackgroundResource(R.drawable.corners_dark_bg);
            int color = ContextCompat.getColor(this, R.color.white);
            back_icon.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        } else {
            bottomNavigationView.setBackgroundResource(R.drawable.corners_light_bg);
            int color = ContextCompat.getColor(this, R.color.black);
            back_icon.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_item1) {
                    // If already on the homepage activity, do nothing
                    if (!isHomepageActivity()) {
                        // Finish the current activity to return to the previous one (likely the previous activity)
                        finish();
                    }
                } else if (item.getItemId() == R.id.nav_item2) {
                    // Navigate to Collections fragment
                    navController.navigate(R.id.collectionsFragment);
                } else if (item.getItemId() == R.id.nav_item3) {
                    // Navigate to Ranking fragment
                    navController.navigate(R.id.rankingFragment);
                } else if (item.getItemId() == R.id.nav_item4) {
                    // Navigate to Profile fragment
                    navController.navigate(R.id.profileFragment);
                }
                return true;
            }
        });

// Method to check if the current activity is the Homepage activity




        recyclerView = findViewById(R.id.rcv_category);
        userAdapter = new UserAdapter(usersList, this);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_view_spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
//        StaggeredGridLayoutManager linearLayoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 6, GridLayoutManager.VERTICAL, false); // Set to 2 for two items per row


        CustomGridLayoutManager layoutManager = new CustomGridLayoutManager(this, 6); // 6 because it's the period of your desired pattern
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(userAdapter);

//        fetchUsers();
        fetchCategories();

        large_image = findViewById(R.id.large_image);
        loadImageWithCorners(this,"https://i.imgur.com/v655VqZ.png",large_image);
    }

    public void loadImageWithCorners(Context context ,String url, ImageView view) {

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


    public void fetchCategories(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.base_link)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


    }
    public void fetchUsers() {
        // Fetch users from the server
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.base_link)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);

        service.getUsers().enqueue(new Callback<ArrayList<Users>>() {
            @Override
            public void onResponse(Call<ArrayList<Users>> call, Response<ArrayList<Users>> response) {
                if (response.isSuccessful()) {
                    usersList = new ArrayList<>(response.body());
                    userAdapter = new UserAdapter(usersList, Homepage.this);
                    recyclerView.setAdapter(userAdapter);
                    Log.d("Homepage", "Fetched users successfully");
                    Log.d("Homepage", "Number of users: " + usersList.size());
                } else {
                    // Handle error
                    Log.d("Homepage", "Failed to fetch users");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Users>> call, Throwable t) {
                // Handle failure
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

    private boolean isHomepageActivity() {
        return getIntent().resolveActivity(getPackageManager()) != null && getIntent().resolveActivity(getPackageManager()).getClassName().equals(Homepage.class.getName());
    }
}
