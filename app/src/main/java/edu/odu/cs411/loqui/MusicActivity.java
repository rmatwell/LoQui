<<<<<<< HEAD
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


=======
version https://git-lfs.github.com/spec/v1
oid sha256:8be60ae1e03773c921e5295228714d8ab066e78926048057c341e52d460c1b88
size 1769
>>>>>>> 69b172438de57648718503ac42d98caad0ea5cf8
