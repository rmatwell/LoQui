package edu.odu.cs411.loqui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ProgressActivity extends AppCompatActivity {

    private ImageView progress_back_btn, progress_back_to_loqui;
    private CardView emotionProgress, speechProgress, eye_contactProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_progress);

        progress_back_btn = findViewById(R.id.progress_back_btn);
        progress_back_to_loqui = findViewById(R.id.progress_back_to_loqui);
        emotionProgress = findViewById(R.id.EmotionGameProgress);
        speechProgress = findViewById(R.id.SpeechGameProgress);
        eye_contactProgress = findViewById(R.id.EyeContactGameProgress);

        progress_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ProgressActivity.this, ParentPortalActivity.class);
                startActivity(it);
            }
        });

        progress_back_to_loqui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ProgressActivity.this, Homepage.class);
                startActivity(it);
            }
        });

        emotionProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ProgressActivity.this, MonthlyProgressActivity.class);
                startActivity(it);
            }
        });

        speechProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ProgressActivity.this, MonthlySpeechProgressActivity.class);
                startActivity(it);
            }
        });

        eye_contactProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ProgressActivity.this, MonthlyEyeProgressActivity.class);
                startActivity(it);
            }
        });

    }
}
