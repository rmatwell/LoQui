package edu.odu.cs411.loqui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

public class MusicActivity extends AppCompatActivity {

    private ImageView audio_backbtn;

    protected static MediaPlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        audio_backbtn = findViewById(R.id.audio_back_btn);

        audio_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MusicActivity.this, ParentPortalActivity.class);
                startActivity(it);
            }
        });


        final RadioButton playButton = (RadioButton) findViewById(R.id.song_on);
        final RadioButton stopButton = (RadioButton) findViewById(R.id.song_off);

        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (player == null) {

                    player = MediaPlayer.create(MusicActivity.this, R.raw.inspiring_kids);
                }
                // Play the music player.
                player.start();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stop the MediaPlayer and release it from memory.
                player.stop();
                player.release();
                player = null;
            }
        });

    }
}


