package edu.odu.cs411.loqui;


import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class StoryBook extends AppCompatActivity {
    private Button next, back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_storybook);
        Button play = (Button) findViewById(R.id.button);
        getWindow().setFormat(PixelFormat.UNKNOWN);
        VideoView video = (VideoView) findViewById(R.id.videoView);
        String path = "android.resource://edu.odu.cs411.loqui/" + R.raw.welcometostorygirl;
        Uri uri = Uri.parse(path);
        video.setVideoURI(uri);
        video.requestFocus();
        video.start();
        clickOnButton();
    }

    private void clickOnButton() {
        next = (Button) findViewById(R.id.sarah);
        back = (Button) findViewById(R.id.sam);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(StoryBook.this, Story.class);
                startActivity(it);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(StoryBook.this, Homepage.class);
                startActivity(it);
            }

        });

    }

}
