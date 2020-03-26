package edu.odu.cs411.loqui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import java.util.Date;


public class SettingsActivity extends AppCompatActivity {

    private CardView accountCard, tutorialCard, audioCard, aboutCard;
    private ImageView settings_backbtn;
    private Button goals_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_settings);

        //accountCard = (CardView) findViewById(R.id.settings_account);
        //tutorialCard = (CardView) findViewById(R.id.settings_tutorial);
        audioCard = (CardView) findViewById(R.id.settings_audio);
        goals_btn = (Button) findViewById(R.id.create_goal);
        //aboutCard = (CardView) findViewById(R.id.settings_about);
        settings_backbtn = findViewById(R.id.settings_back_btn);

        goals_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SettingsActivity.this, Goals.class);
                //game type
                int game = 0;
                RadioButton g_emotion = (RadioButton) findViewById(R.id.emotion_game);
                if (g_emotion.isChecked()) {game = 0;}
                RadioButton g_speech = (RadioButton) findViewById(R.id.speech_training);
                if (g_speech.isChecked()) {game = 1;}
                RadioButton g_eye = (RadioButton) findViewById(R.id.eye_contact_game);
                if (g_eye.isChecked()) {game = 2;}
                it.putExtra("game", game);
                //goal type
                int goal = 0;
                RadioButton g_overall = (RadioButton) findViewById(R.id.correct_overall);
                if (g_overall.isChecked()) {goal = 0;}
                RadioButton g_streak = (RadioButton) findViewById(R.id.correct_streak);
                if (g_streak.isChecked()) {goal = 1;}
                RadioButton g_overallt = (RadioButton) findViewById(R.id.correct_overall_time);
                if (g_overallt.isChecked()) {goal = 2;}
                RadioButton g_percent = (RadioButton) findViewById(R.id.correct_percent_time);
                if (g_percent.isChecked()) {goal = 3;}
                it.putExtra("goal", goal);
                //amount or percent value
                EditText t_amount = (EditText) findViewById(R.id.amount_correct);
                int amount = Integer.parseInt(t_amount.getText().toString());
                it.putExtra("amount", amount);
                //time value in seconds
                EditText t_time = (EditText) findViewById(R.id.time);
                int time = Integer.parseInt(t_time.getText().toString());
                it.putExtra("time", time);
                long timestamp = new Date().getTime()/1000;
                it.putExtra("timestamp", timestamp);
                startActivity(it);
            }
        });

        audioCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SettingsActivity.this, MusicActivity.class);
                startActivity(it);
            }
        });

        settings_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SettingsActivity.this, ParentPortalActivity.class);
                startActivity(it);
            }
        });



    }


}
