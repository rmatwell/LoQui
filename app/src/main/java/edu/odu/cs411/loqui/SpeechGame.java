package edu.odu.cs411.loqui;


import android.content.Intent;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SpeechGame extends AppCompatActivity {

    private Handler mHandler = new Handler();
    private static final int REQUEST_CODE_SPEECH_INPUT = 100;
    TextView textSpeech;
    Button speechButton;
    ImageView speechImage;
    ImageButton homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_game);

        textSpeech = findViewById(R.id.textViewSpeech);
        speechButton = findViewById(R.id.speechButton);
        speechImage = findViewById(R.id.imageViewSpeech);
        homeButton = findViewById(R.id.homeButton);

        speechImage.setImageResource(R.drawable.frog);

        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                speak();

            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SpeechGame.this,Homepage.class));

            }
        });

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

                    if(textSpeech.getText().toString().equalsIgnoreCase("frog")) {
                        speechImage.setImageResource(R.drawable.checkmark);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                speechImage.setImageResource(R.drawable.frog);
                            }
                        }, 2000);

                    }

           }
    }


}
