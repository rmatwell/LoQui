package edu.odu.cs411.loqui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import edu.odu.cs411.loqui.utils.SoundHelper;



/**
 * Created by Richard Atwell on 3/21/2020
 * Old Dominion University
 * ratwe002@odu.edu
 */
public class SpeechGame extends AppCompatActivity {

    private SoundHelper mySoundHelper;
    private AudioManager mAudioManager;
    private static final String TAG = "SpeechGame";
    private Handler mHandler = new Handler();
    private MyProgressBar step_progress_bar;
    private TextView textSpeech;
    private Button speechButton;
    private ImageView speechImage;
    private ImageButton homeButton;
    private Images images;
    private MediaPlayer mediaPlayer;
    private MediaPlayer correctAnswerPlayer;
    private int mStreamVolume;
    int questionCounter = 0;
    int resID;
    private FirestoreWorker worker = new FirestoreWorker();
    private TimerTask timerTask;
    private int count = 0;
    private SpeechRecognizer speechRecognizer;
    private ProgressBar myProgressBar;

    AnimatedVectorDrawable checkAVD;
    AnimatedVectorDrawableCompat checkAVDCompat;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_game);
        step_progress_bar = findViewById(R.id.step_progress_bar);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        textSpeech = findViewById(R.id.textViewSpeech);
        speechButton = findViewById(R.id.speechButton);
        speechImage = findViewById(R.id.imageViewSpeech);
        homeButton = findViewById(R.id.homeButton);
        myProgressBar = findViewById(R.id.progressBar);

        myProgressBar.setVisibility(View.INVISIBLE);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new CustomerListener());

        images = new Images(this);

        textSpeech.setText("Say " + "\"" + images.getName() + "\"");
        textSpeech.setVisibility(View.VISIBLE);

        resID = getResources().getIdentifier(images.getName(), "raw", getPackageName());

        mediaPlayer = MediaPlayer.create(getBaseContext(), resID);
        mediaPlayer.start();

        correctAnswerPlayer = MediaPlayer.create(this, R.raw.correctsound);
        correctAnswerPlayer.setVolume(.6f, .6f);

        mySoundHelper = new SoundHelper(this);

        speechImage.setImageResource(images.getImages().getResourceId(images.getIndex(), 0));

        speechButton.setOnClickListener(v -> speak());

        homeButton.setOnClickListener(v -> {
            mySoundHelper.playHomeSound();
            animateButton();
            mHandler.postDelayed(() -> {

               mediaPlayer.release();
                startActivity(new Intent(SpeechGame.this, Homepage.class));
            }, 1500);

        });

    }

    class CustomerListener implements RecognitionListener {

        @Override
        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "onReadyForSpeech");
            mHandler.postDelayed(() -> {
                speechButton.setText("");
                myProgressBar.setVisibility(View.VISIBLE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mStreamVolume, 0);
                mySoundHelper.playStartListeningSound();
            }, 300);

        }

        @Override
        public void onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech");
        }

        @Override
        public void onRmsChanged(float rms) {
            Log.d(TAG, "onRmsChanged");
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {
            Log.d(TAG, "onEndOfSpeech");
        }

        @Override
        public void onError(int error) {
            Log.d(TAG, "Error " + error);
            speechButton.setText("Speech");
            myProgressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onResults(Bundle results) {

            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);

            mHandler.postDelayed(() -> {

                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mStreamVolume, 0);
                myProgressBar.setVisibility(View.INVISIBLE);
                speechButton.setText("Speech");
                ArrayList<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                textSpeech.setVisibility(View.INVISIBLE);
                if (result == null) throw new AssertionError();
                textSpeech.setText(result.get(0));

                if (textSpeech.getText().toString().equalsIgnoreCase(images.getName())) {
                    correctAnswer();
                }
                else {
                    if (questionCounter < 3) {
                        questionCounter++;
                        mySoundHelper.playIncorrectSound();
                        textSpeech.setText("Try Again");
                        textSpeech.setVisibility(View.VISIBLE);
                        mHandler.postDelayed(() -> {

                            mediaPlayer.start();
                            textSpeech.setText("Say " + "\"" + images.getName() + "\"");
                        }, 3000);

                    } else {
                        nextImage();
                    }
                }

            }, 400);

        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, "onPartialResults");
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent " + eventType);
        }
    }


    protected void onResume() {
        super.onResume();
        startTimer();
    }

    public void startTimer() {
        Timer timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                mHandler.post(() -> {
                    FirestoreWorker dbWorker = new FirestoreWorker();
                    count = dbWorker.getRewardScore() - 1;
                    step_progress_bar.updateProgress(count);
                });
            }
        };
    }

    private void animateButton() {
        homeButton.animate().rotationX(homeButton.getRotationX() + 360).
                rotationY(homeButton.getRotationY() + 360).setDuration(1500);
    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());

        mStreamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        speechRecognizer.startListening(intent);


    }



    private void correctAnswer() {
        worker.addToRewardScore(1);
        worker.addSpeechScore(1);
        textSpeech.setText("Amazing!");
        textSpeech.setVisibility(View.VISIBLE);
        mySoundHelper.playCorrectSound();
        correctAnswerPlayer.start();
        speechImage.setImageResource(R.drawable.check);

        Drawable drawable = speechImage.getDrawable();

        if(drawable instanceof AnimatedVectorDrawable){
            checkAVD = (AnimatedVectorDrawable) drawable;
            checkAVD.start();
        }
        else if(drawable instanceof AnimatedVectorDrawableCompat){
            checkAVDCompat = (AnimatedVectorDrawableCompat) drawable;
            startTimer();
        }

        nextImage();
    }
    private void nextImage() {
        mHandler.postDelayed(() -> {
            questionCounter = 0;
            mediaPlayer.release();
            mediaPlayer = null;
            images.randomizeArray();
            textSpeech.setText("Say " + "\"" + images.getName() + "\"");
            textSpeech.setVisibility(View.VISIBLE);
            speechImage.setImageResource(images.getImages().getResourceId(images.getIndex(), 0));
            resID = getResources().getIdentifier(images.getName(), "raw", getPackageName());

            mediaPlayer = MediaPlayer.create(getBaseContext(), resID);
            mediaPlayer.start();
        }, 3000);

    }

}