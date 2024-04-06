package com.example.mob403_quiz.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mob403_quiz.Models.User;
import com.example.mob403_quiz.Models.Users;
import com.example.mob403_quiz.Models.UsersResult;
import com.example.mob403_quiz.R;
import com.example.mob403_quiz.Services.APIService;
import com.example.mob403_quiz.Services.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingFragment extends Fragment {
    private List<UsersResult> userResults;

    TextView first_name, second_name, third_name;
    TextView first_score, second_score, third_score;

    ImageView first_image, second_image, third_image;
    public RankingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);

        first_name = view.findViewById(R.id.first_place_name);
        second_name = view.findViewById(R.id.second_place_name);
        third_name = view.findViewById(R.id.third_place_name);

        first_score = view.findViewById(R.id.first_place_score);
        second_score = view.findViewById(R.id.second_place_score);
        third_score = view.findViewById(R.id.third_place_score);

        first_image = view.findViewById(R.id.first_place_img);
        second_image = view.findViewById(R.id.second_place_img);
        third_image = view.findViewById(R.id.third_place_img);

        fetchUserResults();

        return view;
    }

    private void fetchUserResults() {
        APIService apiService = RetrofitClient.getClient().create(APIService.class);
        Call<List<UsersResult>> call = apiService.getUserResults(null); // Pass null to get all results
        call.enqueue(new Callback<List<UsersResult>>() {
            @Override
            public void onResponse(Call<List<UsersResult>> call, Response<List<UsersResult>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userResults = response.body();

                    // Calculate total scores for each user
                    Map<Integer, Integer> userScores = new HashMap<>();
                    for (UsersResult result : userResults) {
                        int userId = result.getUser_id();
                        int score = result.getScore();
                        userScores.put(userId, userScores.getOrDefault(userId, 0) + score);
                    }

                    // Sort users based on total scores
                    List<Map.Entry<Integer, Integer>> sortedScores = new ArrayList<>(userScores.entrySet());
                    Collections.sort(sortedScores, (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

                    displayTopThreeUsers(sortedScores);
                } else {
                    // Handle unsuccessful response
                    Log.e("RankingFragment", "Failed to fetch user results: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<UsersResult>> call, Throwable t) {
                // Handle failure
                Log.e("RankingFragment", "Failed to fetch user results: " + t.getMessage());
            }
        });
    }

    private void fetchUserInfo(int user_id, TextView textView) {
        APIService apiService = RetrofitClient.getClient().create(APIService.class);
        Call<User> call = apiService.getUserInfo(user_id); // Expecting a single User object
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    textView.setText(user.getName());
                } else {
                    // Handle unsuccessful response or empty body
                    Log.e("RankingFragment", "Failed to fetch user info: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle failure
                Log.e("RankingFragment", "Failed to fetch user info: " + t.getMessage());
            }
        });
    }


    private void displayTopThreeUsers(List<Map.Entry<Integer, Integer>> sortedScores) {
        // Ensure sortedScores is not empty and contains at least 3 items
        if (!sortedScores.isEmpty() && sortedScores.size() >= 3) {
            // First place
            Map.Entry<Integer, Integer> firstPlaceEntry = sortedScores.get(0);
            first_score.setText(String.valueOf(firstPlaceEntry.getValue()));
            fetchUserInfo(firstPlaceEntry.getKey(), first_name);

            // Second place
            Map.Entry<Integer, Integer> secondPlaceEntry = sortedScores.get(1);
            second_score.setText(String.valueOf(secondPlaceEntry.getValue()));
            fetchUserInfo(secondPlaceEntry.getKey(), second_name);

            // Third place
            Map.Entry<Integer, Integer> thirdPlaceEntry = sortedScores.get(2);
            third_score.setText(String.valueOf(thirdPlaceEntry.getValue()));
            fetchUserInfo(thirdPlaceEntry.getKey(), third_name);
        }
    }

}
