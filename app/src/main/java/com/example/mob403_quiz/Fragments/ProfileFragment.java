package com.example.mob403_quiz.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mob403_quiz.EditProfileActivity;
import com.example.mob403_quiz.Login_Activity;
import com.example.mob403_quiz.Models.ProfileResponse;
import com.example.mob403_quiz.Models.Users;
import com.example.mob403_quiz.R;
import com.example.mob403_quiz.Services.APIService;
import com.example.mob403_quiz.Services.RetrofitClient;
import com.example.mob403_quiz.Services.SharedPreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private TextView txtScore, txtQuizzes, txtRank, txtLevel, profileUsername, usernameLabel;
    private Users user;

    private Button btnEditProfile, btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views and set click listener for the edit profile button
        initViews(view);

        // Load user data
        loadUserData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload user data when the fragment becomes visible again
        loadUserData();
    }

    private void initViews(View view) {
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        txtScore = view.findViewById(R.id.txt_score);
        txtQuizzes = view.findViewById(R.id.txt_quizzes);
        txtRank = view.findViewById(R.id.txt_rank);
        txtLevel = view.findViewById(R.id.txt_level);
        profileUsername = view.findViewById(R.id.profile_username);
        usernameLabel = view.findViewById(R.id.username_label);



        btnEditProfile.setOnClickListener(v -> {
            // Open the EditProfileActivity
            Intent intent = new Intent(requireContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> {
            // Clear user data from SharedPreferences
            SharedPreferencesManager.clearUser(requireContext());
            // Redirect to the login screen
            Intent intent = new Intent(requireContext(), Login_Activity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    private void loadUserData() {
        // Load user data from SharedPreferences
        user = SharedPreferencesManager.getUser(requireContext());
        if (user != null) {
            profileUsername.setText(user.getName());
            usernameLabel.setText(user.getEmail());
            fetchProfileData(user.getId());
        }
    }

    private void fetchProfileData(int userId) {
        APIService apiService = RetrofitClient.getClient().create(APIService.class);
        Call<ProfileResponse> call = apiService.fetchProfileData(userId);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    txtScore.setText(String.valueOf(response.body().getScore()));
                    txtQuizzes.setText(String.valueOf(response.body().getQuizzes()));
                    txtRank.setText(String.valueOf(response.body().getRank()));
                    // For level, you might want to convert int level to a String representation
                    switch (response.body().getLevel()) {
                        case 1:
                            txtLevel.setText("Beginner");
                            break;
                        case 2:
                            txtLevel.setText("Amateur");
                            break;
                        case 3:
                            txtLevel.setText("Advanced");
                            break;
                        default:
                            txtLevel.setText("Unknown");
                    }
                } else {
                    // Handle case where response is not successful
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                // Handle failure (e.g., network error, no internet)
            }
        });
    }
}
