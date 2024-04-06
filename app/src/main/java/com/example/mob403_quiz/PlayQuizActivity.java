package com.example.mob403_quiz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.mob403_quiz.Adapter.ThemeUtils;
import com.example.mob403_quiz.Models.Answer;
import com.example.mob403_quiz.Models.Question;
import com.example.mob403_quiz.Models.Users;
import com.example.mob403_quiz.Models.UsersResult;
import com.example.mob403_quiz.Services.APIService;
import com.example.mob403_quiz.Services.RetrofitClient;
import com.example.mob403_quiz.Services.SharedPreferencesManager;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayQuizActivity extends AppCompatActivity {
    // UI Components
    private TextView questionTextView;
    private ImageView quizImageView;
    private Button option1Button, option2Button, option3Button ;
    private ImageView btnBack;
    private List<Question> questions;
    ProgressBar progressBar;
    CountDownTimer countDownTimer;

    int score = 0;
    int i = 0;
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_quiz);

        // Initialize UI Components
        questionTextView = findViewById(R.id.question);
        quizImageView = findViewById(R.id.quiz_img);
        option1Button = findViewById(R.id.option1);
        option2Button = findViewById(R.id.option2);
        option3Button = findViewById(R.id.option3);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
        int categoryId = getIntent().getIntExtra("category_id", 0);
        if (categoryId != 0) {
            fetchQuizData(categoryId);
        } else {
            finish(); // Close activity if no category ID is passed
        }

        progressBar = findViewById(R.id.progressbar);
        ThemeUtils.setProgressBarColor(this, progressBar);

    }
    private void startTimer() {
        progressBar.setProgress(0);
        i = 0; // Reset progress

        // Cancel existing timer to avoid multiple timers running
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(15000, 5) { // Update progress every 10 milliseconds
            long startTime = System.currentTimeMillis();

            @Override
            public void onTick(long millisUntilFinished) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                progressBar.setProgress((int) (elapsedTime * 100 / 15000)); // Calculate progress based on elapsed time
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(100);
                moveToNextQuestion(); // Move to the next question when timer finishes
            }
        }.start();

    }
    private void displayQuestion(Question question) {
        questionTextView.setText(question.getText());
        loadImageWithCorners(this, question.getImage(), quizImageView);
        List<Answer> answers = question.getAnswers();
        Button[] buttons = {option1Button, option2Button, option3Button};

        // Reset buttons to avoid displaying old data
        for (Button button : buttons) {
            button.setText(""); // Clear text
            button.setOnClickListener(null); // Remove old click listeners
        }

        for (int i = 0; i < answers.size() && i < buttons.length; i++) {
            int index = i; // Create a final copy of 'i'
            buttons[i].setText(answers.get(i).getText());
            buttons[i].setOnClickListener(view -> {
                countDownTimer.cancel(); // Stop the timer as soon as an answer is selected
                if (answers.get(index).isIs_correct()==1) {
                    Toast.makeText(PlayQuizActivity.this, "Score +25!", Toast.LENGTH_SHORT).show();
                    score+=25;
                } else {
                    Toast.makeText(PlayQuizActivity.this, "Incorrect!", Toast.LENGTH_SHORT).show();
                }
                moveToNextQuestion(); // Move to the next question regardless of answer correctness
            });
        }

        startTimer(); // Start or restart the timer for the new question
    }

    private void moveToNextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            displayQuestion(questions.get(currentQuestionIndex));
            startTimer(); // Restart the timer for the next question
        } else {
            // Here you can handle the quiz completion, e.g., navigate to a results screen
            Users user = SharedPreferencesManager.getUser(this);
            if (user != null) {
                int categoryId = getIntent().getIntExtra("category_id", 0);

                submitQuizResult(user.getId(), categoryId, score);
            }else{
                Toast.makeText(PlayQuizActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void submitQuizResult(int userId, int categoryId, int score) {
        UsersResult usersResult = new UsersResult();
        usersResult.setUser_id(userId);
        usersResult.setCategory_id(categoryId);
        usersResult.setScore(score);
        // Assuming you have a method to format/obtain the completion timestamp, add it here
        usersResult.setCompleted_at("2021-12-31 23:59:59");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userId);
        jsonObject.addProperty("category_id", categoryId);
        jsonObject.addProperty("score", score);
        jsonObject.addProperty("completed_at", "2021-12-31 23:59:59");
        APIService apiService = RetrofitClient.getClient().create(APIService.class);
        Log.d("usersResult", "submitQuizResult body: "+usersResult.getScore() + " " + usersResult.getCategory_id() + " " + usersResult.getUser_id() + " " + usersResult.getCompleted_at());
        Call<JsonObject> call = apiService.submitQuizResult(usersResult);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    // Handle success
                    JsonObject jsonResponse = response.body();
                    showDialog();

                    // You might want to fetch and update the user profile here or inform the user of success
                    Log.d("SubmitQuizResult", jsonResponse.toString());
                } else {
                    // Handle failure
                    Log.d("SubmitQuizResult", "Failed to submit quiz result: "+response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Handle the error scenario
            }
        });
    }


    private void fetchQuizData(int categoryId) {
        APIService apiService = RetrofitClient.getClient().create(APIService.class);
        Call<List<Question>> call = apiService.getDetailQuestions(categoryId);
        call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    questions = response.body();
                    if (!questions.isEmpty()) {
                        displayQuestion(questions.get(0)); // Display first question
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                // Handle failure
            }
        });
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

    public void showDialog() {
        // Create a dialog to show the quiz results
        // You can use a custom layout or a simple AlertDialog
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.quiz_done_dialog);
        dialog.setCancelable(false); // Prevent dialog from being dismissed by tapping outside

        if(dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.corners_light_bg);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(layoutParams);

            // Find the button inside the dialog's layout
            Button btnStart = dialog.findViewById(R.id.btn_continue);
            if (btnStart != null) {
                // Set OnClickListener for the button
                btnStart.setOnClickListener(view -> {
                    Intent intent = new Intent(PlayQuizActivity.this, MainActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                });
            }
        }
        dialog.show();
    }
}

