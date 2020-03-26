package edu.odu.cs411.loqui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

public class ParentPortalActivity extends AppCompatActivity {

    private CardView goalsCard, progressCard, rewardsCard, settingsCard;
    private ImageView portal_backbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_parentportal);

        goalsCard = (CardView) findViewById(R.id.portal_goals);
        progressCard = (CardView) findViewById(R.id.portal_progress);
        rewardsCard = (CardView) findViewById(R.id.portal_rewards);
        settingsCard = (CardView) findViewById(R.id.portal_settings);
        portal_backbtn = findViewById(R.id.portal_back_btn);

        goalsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ParentPortalActivity.this, GoalsActivity.class);
                startActivity(it);
            }
        });

        progressCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ParentPortalActivity.this, ProgressActivity.class);
                startActivity(it);
            }
        });

        rewardsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ParentPortalActivity.this, Rewards.class);
                startActivity(it);
            }
        });

        settingsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ParentPortalActivity.this, SettingsActivity.class);
                startActivity(it);
            }
        });

        portal_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ParentPortalActivity.this, Homepage.class);
                startActivity(it);
            }
        });



    }


}
