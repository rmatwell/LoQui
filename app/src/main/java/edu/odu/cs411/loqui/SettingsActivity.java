package edu.odu.cs411.loqui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;


public class SettingsActivity extends AppCompatActivity {

    private CardView accountCard, tutorialCard, audioCard, aboutCard;
    private ImageView settings_backbtn;

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
        //aboutCard = (CardView) findViewById(R.id.settings_about);
        settings_backbtn = findViewById(R.id.settings_back_btn);


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
