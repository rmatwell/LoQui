package edu.odu.cs411.loqui;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;



public class MusicActivity extends AppCompatActivity {

    MediaPlayer player;
    private ImageView audio_backbtn;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        audio_backbtn = findViewById(R.id.audio_back_btn);


        audio_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MusicActivity.this, SettingsActivity.class);
                startActivity(it);
            }
        });
    }

        public void play(View v){
            if (player == null){
                player = MediaPlayer.create(this, R.raw.inspiring_kids);
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        stopPlayer();
                    }
                });
            }

            player.start();
        }

        public void stop(View v) {
            stopPlayer();
        }

        private void stopPlayer() {
            if (player != null){
                player.release();
                player = null;
            }
        }

        @Override
        protected void onStop() {
            super.onStop();
            stopPlayer();
        }



}
