package edu.odu.cs411.loqui;

import android.content.Intent;
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


/**
 * Created by Richard Atwell on 3/21/2020
 * Old Dominion University
 * ratwe002@odu.edu
 */
public class SpeechGame extends AppCompatActivity {

    private Handler mHandler = new Handler();
    private static final int REQUEST_CODE_SPEECH_INPUT = 100;
    TextView textSpeech;
    Button speechButton;
    ImageView speechImage;
    ImageButton homeButton;
    Images images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_game);

        textSpeech = findViewById(R.id.textViewSpeech);
        speechButton = findViewById(R.id.speechButton);
        speechImage = findViewById(R.id.imageViewSpeech);
        homeButton = findViewById(R.id.homeButton);

        images = new Images(this);

        textSpeech.setText("Say "+ "\"" + images.getName() + "\"");
        textSpeech.setVisibility(View.VISIBLE);

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

                animateButton();
                startActivity(new Intent(SpeechGame.this,Homepage.class));

            }
        });

    }

    private void animateButton(){homeButton.animate().rotationX(homeButton.getRotationX()+360).
            rotationY(homeButton.getRotationY()+360).setDuration(3500);
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

                textSpeech.setText(result.get(0));

            }

            if(textSpeech.getText().toString().equalsIgnoreCase(images.getName())) {
                speechImage.setImageResource(R.drawable.checkmark);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        images.randomizeArray();
                        nextImage();
                    }
                }, 2000);

            }

        }
    }

    private void nextImage(){
        textSpeech.setText("Say "+ "\"" + images.getName() + "\"");
        textSpeech.setVisibility(View.VISIBLE);
        speechImage.setImageResource(images.getImages().getResourceId(images.getIndex(),0));
    }


}
