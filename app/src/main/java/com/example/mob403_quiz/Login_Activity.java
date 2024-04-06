package com.example.mob403_quiz;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mob403_quiz.Models.Users;
import com.example.mob403_quiz.Services.APIService;
import com.example.mob403_quiz.Services.SharedPreferencesManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login_Activity extends AppCompatActivity {

    private TextInputLayout textInputLayout;
    private TextInputLayout textInputLayout2;
    private TextInputEditText edtUsername;
    private TextInputEditText edtPassword;
    private CheckBox rememberMeCheckbox;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "login_pref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER_ME = "remember_me";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btn_login);
        textInputLayout = findViewById(R.id.edt_username_layout);
        textInputLayout2 = findViewById(R.id.edt_password_layout);
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        rememberMeCheckbox = findViewById(R.id.cbo_remember);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        boolean rememberMe = sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
        if (rememberMe) {
            String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
            String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");
            edtUsername.setText(savedUsername);
            edtPassword.setText(savedPassword);
            rememberMeCheckbox.setChecked(true);
        }

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isNightMode = currentNightMode == Configuration.UI_MODE_NIGHT_YES;

        int shapeResource = isNightMode ? R.drawable.corners_dark_bg : R.drawable.corners_light_bg;
        GradientDrawable roundedDrawable = (GradientDrawable) ContextCompat.getDrawable(this, shapeResource);

        // Apply the rounded drawable as the background
        textInputLayout.setBackground(roundedDrawable);
        textInputLayout2.setBackground(roundedDrawable);

        // Login
        btnLogin.setOnClickListener(v -> {
            final String username = edtUsername.getText().toString();
            final String password = edtPassword.getText().toString();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            } else {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(APIService.base_link)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                APIService service = retrofit.create(APIService.class);
                JsonObject body = new JsonObject();
                body.addProperty("username", username);
                body.addProperty("pw", password);

                service.login(body).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject jsonResponse = response.body();
                            if (jsonResponse != null) {
                                int code = jsonResponse.get("code").getAsInt();
                                if (code == 1) {
                                    JsonObject userData = jsonResponse.getAsJsonObject("user");
                                    int id = userData.get("id").getAsInt();
                                    String name = userData.get("name").getAsString();
                                    String email = userData.get("email").getAsString();
                                    String role = userData.get("role").getAsString();
                                    String pw = userData.get("pw").getAsString();
                                    int age = userData.get("age").getAsInt();
                                    String username = userData.get("username").getAsString();
                                    Users user = new Users(id, username, name, email, pw, age, role);
                                    SharedPreferencesManager.saveUser(Login_Activity.this, user, role);
                                    if (rememberMeCheckbox.isChecked()) {
                                        saveLoginCredentials(username, password);
                                    } else {
                                        clearLoginCredentials();
                                    }
                                    showDialog();
                                } else {
                                    showFailedDialog();
                                }
                            } else {
                                Toast.makeText(Login_Activity.this, "Login failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            int responseCode = response.code();
                            String responseMessage = response.message();
                            Log.e("API_ERROR", "Response Code: " + responseCode + ", Message: " + responseMessage);
                            Toast.makeText(Login_Activity.this, "Login failed: " + responseMessage, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e("API_ERROR", "Failure: " + t.getMessage());
                        Toast.makeText(Login_Activity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showFailedDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_failed_dialog);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.corners_light_bg);

            // Set the dialog to the bottom of the screen
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.BOTTOM; // This positions the dialog at the bottom

            // Apply the layout parameters to the dialog window
            dialog.getWindow().setAttributes(layoutParams);
            Button button = dialog.findViewById(R.id.btn_failed);
            button.setOnClickListener(v -> dialog.dismiss());

            // Optional: If you want the dialog to be full-width
        }

        dialog.show();
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_dialog);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.corners_light_bg);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.BOTTOM;
            dialog.getWindow().setAttributes(layoutParams);

            // Find the button inside the dialog's layout
            Button btnStart = dialog.findViewById(R.id.btnStart);
            if (btnStart != null) {
                // Set OnClickListener for the button
                btnStart.setOnClickListener(view -> {
                    Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                });
            }
        }

        dialog.show();
    }

    private void saveLoginCredentials(String username, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putBoolean(KEY_REMEMBER_ME, true);
        editor.apply();
    }

    private void clearLoginCredentials() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_PASSWORD);
        editor.remove(KEY_REMEMBER_ME);
        editor.apply();
    }
}
