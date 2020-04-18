package edu.odu.cs411.loqui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.material.*;
//import com.google.android.material.transition.MaterialFadeThrough;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Richard Atwell on 3/21/2020
 * Old Dominion University
 * ratwe002@odu.edu
 */
public class SpeechGame extends AppCompatActivity {

    private Handler mHandler = new Handler();
    private static final int REQUEST_CODE_SPEECH_INPUT = 100;
    MyProgressBar step_progress_bar;
    TextView textSpeech;
    Button speechButton;
    ImageView speechImage;
    ImageButton homeButton;
    Images images;
    MediaPlayer mediaPlayer;
    MediaPlayer successPlayer;
    MediaPlayer incorrectPlayer;
    MediaPlayer homePlayer;
    boolean correct = false;
    int questionCounter = 0;
    FirestoreWorker worker = new FirestoreWorker();
    private Timer timer;
    private TimerTask timerTask;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_game);
        step_progress_bar = findViewById(R.id.step_progress_bar);

        textSpeech = findViewById(R.id.textViewSpeech);
        speechButton = findViewById(R.id.speechButton);
        speechImage = findViewById(R.id.imageViewSpeech);
        homeButton = findViewById(R.id.homeButton);

        images = new Images(this);

        textSpeech.setText("Say "+ "\"" + images.getName() + "\"");
        textSpeech.setVisibility(View.VISIBLE);

        int resID = getResources().getIdentifier(images.getName(), "raw", getPackageName());
        mediaPlayer = MediaPlayer.create(getBaseContext(), resID);
        successPlayer = MediaPlayer.create(getBaseContext(), R.raw.success0);
        incorrectPlayer = MediaPlayer.create(getBaseContext(), R.raw.wrong0);
        homePlayer = MediaPlayer.create(getBaseContext(), R.raw.home);
        mediaPlayer.start();

        speechImage.setImageResource(images.getImages().getResourceId(images.getIndex(),0));

        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    speak();
            }
        });



        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homePlayer.start();
                animateButton();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mediaPlayer.release();
                        successPlayer.release();
                        incorrectPlayer.release();
                        homePlayer.release();
                        startActivity(new Intent(SpeechGame.this,Homepage.class));
                                            }
                }, 1500);


            }
        });

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        startTimer();
    }

    public void startTimer()
    {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void initializeTimerTask()
    {
        timerTask = new TimerTask() {
            public void run() {
                mHandler.post(new Runnable() {
                    public void run() {
                        FirestoreWorker dbWorker = new FirestoreWorker();
                        count = dbWorker.getRewardScore(SpeechGame.this) - 1;
                        step_progress_bar.updateProgress(count);
                    }
                });
            }
        };
    }

    private void animateButton(){homeButton.animate().rotationX(homeButton.getRotationX()+360).
            rotationY(homeButton.getRotationY()+360).setDuration(1500);
    }

    private void speak() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_SPEECH_INPUT){
            if(resultCode == RESULT_OK && null != data){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                textSpeech.setVisibility(View.INVISIBLE);
                textSpeech.setText(result.get(0));

            }

            if(textSpeech.getText().toString().equalsIgnoreCase(images.getName())) {
                correctAnswer();
            }

            else{
                questionCounter++;
                if(questionCounter < 3) {
                    incorrectPlayer.start();
                    textSpeech.setText("Try Again, You Can Do It!");
                    textSpeech.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mediaPlayer.start();
                            textSpeech.setText("Say " + "\"" + images.getName() + "\"");
                        }
                    }, 3000);

                }
                else{
                    nextImage();
                }
            }

        }
    }

    private void correctAnswer(){
        worker.addToRewardScore(1);
        worker.addSpeechScore(1);
        textSpeech.setText("Amazing!");
        textSpeech.setVisibility(View.VISIBLE);
        successPlayer.start();
        speechImage.setImageResource(R.drawable.checkmark);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextImage();
            }
        }, 3000);
    }

    private void nextImage(){
        questionCounter = 0;
        mediaPlayer.release();
        mediaPlayer = null;
        images.randomizeArray();
        textSpeech.setText("Say "+ "\"" + images.getName() + "\"");
        textSpeech.setVisibility(View.VISIBLE);
        speechImage.setImageResource(images.getImages().getResourceId(images.getIndex(),0));
        int resID = getResources().getIdentifier(images.getName(), "raw", getPackageName());
        mediaPlayer = MediaPlayer.create(getBaseContext(), resID);
        mediaPlayer.start();
    }


}
