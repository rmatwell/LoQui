package edu.odu.cs411.loqui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;


public class SettingsActivity extends AppCompatActivity {

    private CardView rewardsCard, audioCard;
    private ImageView settings_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_settings);


        //accountCard = (CardView) findViewById();
        rewardsCard = (CardView) findViewById(R.id.settings_rewards);
        audioCard = (CardView) findViewById(R.id.settings_audio);
        //aboutCard = (CardView) findViewById();
        settings_back_btn = findViewById(R.id.settings_back_btn);


        rewardsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SettingsActivity.this, Rewards.class);
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

        settings_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SettingsActivity.this, ParentPortalActivity.class);
                startActivity(it);
            }
        });


    }


}
