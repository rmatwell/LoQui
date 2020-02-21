package edu.odu.cs411.loqui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;


public class SettingsActivity extends AppCompatActivity {

    private CardView progressCard, goalsCard, rewardsCard, musicCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_settings);

        progressCard = (CardView) findViewById(R.id.progress_settings);
        goalsCard = (CardView) findViewById(R.id.goal_settings);
        rewardsCard = (CardView) findViewById(R.id.rewards_settings);
        musicCard = (CardView) findViewById(R.id.music_settings);

        progressCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SettingsActivity.this, ProgressActivity.class);
                startActivity(it);
            }
        });

        goalsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SettingsActivity.this, GoalsActivity.class);
                startActivity(it);
            }
        });

        rewardsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SettingsActivity.this, Rewards.class);
                startActivity(it);
            }
        });

        musicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SettingsActivity.this, MusicActivity.class);
                startActivity(it);
            }
        });



    }


}
