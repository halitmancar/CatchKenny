package com.halitman.catchkenny;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView timeText;
    TextView scoreText;
    TextView highScoreText;
    int score = 0;
    ImageView kenny1;
    ImageView kenny2;
    ImageView kenny3;
    ImageView kenny4;
    ImageView kenny5;
    ImageView kenny6;
    ImageView kenny7;
    ImageView kenny8;
    ImageView kenny9;
    Random random = new Random();
    ImageView[] imageViews;
    Handler handler;
    Runnable runnable;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeText = findViewById(R.id.timeText);
        scoreText = findViewById(R.id.scoreText);
        highScoreText = findViewById(R.id.highScore);
        kenny1 = findViewById(R.id.imageView);
        kenny2 = findViewById(R.id.imageView2);
        kenny3 = findViewById(R.id.imageView3);
        kenny4 = findViewById(R.id.imageView4);
        kenny5 = findViewById(R.id.imageView5);
        kenny6 = findViewById(R.id.imageView6);
        kenny7 = findViewById(R.id.imageView7);
        kenny8 = findViewById(R.id.imageView8);
        kenny9 = findViewById(R.id.imageView9);


        sharedPreferences = this.getSharedPreferences("com.halitman.catchkenny", Context.MODE_PRIVATE);
        int highScoreInt = sharedPreferences.getInt("highScore", 0);
        if (highScoreInt!=0){
            highScoreText.setText("High Score: " + highScoreInt);
        }
        changeKenny();

        imageViews = new ImageView[] {kenny1, kenny2, kenny3, kenny4, kenny5, kenny6, kenny7, kenny8, kenny9};

        new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long l) {
                timeText.setText("Time: " + l/1000);
            }

            @Override
            public void onFinish() {
                updateHighScore();
                handler.removeCallbacks(runnable);
                for (ImageView image: imageViews) {
                    image.setVisibility(View.INVISIBLE);
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Restart?");
                alert.setMessage("Your score is: " + score + "\nDo you want to play again?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                        highScoreText.setText("High Score: " + sharedPreferences.getInt("highScore",0));
                    }
                });
                alert.show();
            }
        }.start();
    }
    public void increaseScore(View view){
        score++;
        scoreText.setText("Score: " + score);
    }
    public void changeKenny(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                for (ImageView image: imageViews) {
                    image.setVisibility(View.INVISIBLE);
                }

                int n= random.nextInt(9);
                imageViews[n].setVisibility(View.VISIBLE);

                handler.postDelayed(this, 250);
            }
        };

        handler.post(runnable);

    }
    public void updateHighScore(){
        if (score>sharedPreferences.getInt("highScore",0)){
            sharedPreferences.edit().putInt("highScore", score).apply();
        }
    }
}