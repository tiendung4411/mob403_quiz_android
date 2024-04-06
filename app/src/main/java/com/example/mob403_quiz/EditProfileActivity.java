package com.example.mob403_quiz;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mob403_quiz.Models.Users;
import com.example.mob403_quiz.Services.APIService;
import com.example.mob403_quiz.Services.RetrofitClient;
import com.example.mob403_quiz.Services.SharedPreferencesManager;
import com.google.gson.JsonObject;
import com.skydoves.powerspinner.PowerSpinnerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    EditText edtName, edtEmail;
    PowerSpinnerView edtAge;
    Button btnSave;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        edtName = findViewById(R.id.edit_name);
        edtEmail = findViewById(R.id.edit_email);
        btnSave = findViewById(R.id.btn_save);
        btnBack = findViewById(R.id.back_icon);



        btnBack.setOnClickListener(v -> {
            finish();
        });

        // Load the user's profile data
        edtName.setText(SharedPreferencesManager.getUser(this).getName());
        edtEmail.setText(SharedPreferencesManager.getUser(this).getEmail());
        // Set the age spinner to the user's age
        String[] ages = getResources().getStringArray(R.array.age_array);


        btnSave.setOnClickListener(v -> {
            // Save the updated profile data
            int id = SharedPreferencesManager.getUser(this).getId();
            if (id == 0) {
                return;
            }
            String name = edtName.getText().toString();
            String email = edtEmail.getText().toString();
            // Validate the data
            if (name.isEmpty()) {
                edtName.setError("Name is required");
                edtName.requestFocus();
                return;
            }
            if (email.isEmpty()) {
                edtEmail.setError("Email is required");
                edtEmail.requestFocus();
                return;
            }


            // Create a JSON object with the updated profile data
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", id);
            jsonObject.addProperty("name", name);
            jsonObject.addProperty("email", email);
            jsonObject.addProperty("age", SharedPreferencesManager.getUser(this).getAge());
            // Call the API to update the user's profile
            updateProfile(jsonObject);
        });

    }

    private void updateProfile(JsonObject jsonObject) {
        APIService apiService = RetrofitClient.getClient().create(APIService.class);
        Call<JsonObject> call = apiService.updateProfile(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
//                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    // You can update SharedPreferences with the new data if needed
                    Users user = SharedPreferencesManager.getUser(EditProfileActivity.this);
                    user.setName(jsonObject.get("name").getAsString());
                    user.setEmail(jsonObject.get("email").getAsString());
                    String userRole = SharedPreferencesManager.getUser(EditProfileActivity.this).getRole();
                     SharedPreferencesManager.saveUser(EditProfileActivity.this, user,userRole);
                     String test = SharedPreferencesManager.getUser(EditProfileActivity.this).getName();
                    Log.d("test:  @@", test);
                    finish(); // Close the activity after successful update
                } else {
                    Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
