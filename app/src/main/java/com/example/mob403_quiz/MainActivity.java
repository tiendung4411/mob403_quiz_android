package com.example.mob403_quiz;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.mob403_quiz.Fragments.HomeFragment;
import com.example.mob403_quiz.Fragments.CollectionsFragment;
import com.example.mob403_quiz.Fragments.RankingFragment;
import com.example.mob403_quiz.Fragments.ProfileFragment;
import com.example.mob403_quiz.R;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        // To ensure the home fragment is loaded on startup
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                new HomeFragment()).commit();
    }

    private NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    if (item.getItemId() == R.id.nav_item1) {
                        selectedFragment = new HomeFragment();
                    } else if (item.getItemId() == R.id.nav_item2) {
                        selectedFragment = new CollectionsFragment();
                    } else if (item.getItemId() == R.id.nav_item3) {
                        selectedFragment = new RankingFragment();
                    } else if (item.getItemId() == R.id.nav_item4) {
                        selectedFragment = new ProfileFragment();
                    } else {
                        // Handle cases where the item ID is not recognized (optional)
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                selectedFragment).commit();
                    }

                    return true; // true to display the item as the selected item
                }
            };
}
