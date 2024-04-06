package com.example.mob403_quiz.Services;

import com.example.mob403_quiz.Models.Categories;
import com.example.mob403_quiz.Models.ProfileResponse;
import com.example.mob403_quiz.Models.Question;
import com.example.mob403_quiz.Models.User;
import com.example.mob403_quiz.Models.Users;
import com.example.mob403_quiz.Models.UsersResult;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {
    String base_link = "http://172.16.70.168/mob403_quiz/";

//    @GET ("api_get.php")
//    Call<List<Users>> getUsers();


    @Headers("Content-Type: application/json")
    @POST("api_login.php")
    Call<JsonObject> login(@Body JsonObject body);

    @GET ("api_get.php")
    Call<ArrayList<Users>> getUsers();

    @GET("api_get.php")
    Call<ArrayList<Users>> getUsersById(@Query("userId") int userId);
    @GET("api_getCategory.php")
    Call<ArrayList<Categories>> getCategories();
    @GET("api_getDetailQuestions.php")
    Call<List<Question>> getDetailQuestions(@Query("category_id") int categoryId);


    @Headers("Content-Type: application/json")
    @POST("api_update.php")
    Call<JsonObject> updateProfile(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("api_submitQuizResult.php")
    Call<JsonObject> submitQuizResult(@Body UsersResult usersResult);
    @GET("profile_info.php") // Replace with your actual endpoint after "base_url/"
    Call<ProfileResponse> fetchProfileData(@Query("user_id") int userId);

    @GET("api_getTopThree.php")
    Call<List<User>> getTopThreeUsers();

    @GET("api_getProfilePic.php")
    Call<JsonObject> getProfilePic(@Query("user_id") Integer user_id);
    @GET("api_getRanking.php")
    Call<List<UsersResult>> getUserResults(@Query("id") Integer id);

    @GET("api_get.php")
    Call<User> getUserInfo(@Query("user_id") int userId);
    @DELETE("api_deleteCategory.php")
    Call<JsonObject>deleteCategory(@Query("id") int id);
}
