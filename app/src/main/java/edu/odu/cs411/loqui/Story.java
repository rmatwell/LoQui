package edu.odu.cs411.loqui;


import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;


public class Story extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_story);
        Button play = (Button) findViewById(R.id.button);
        getWindow().setFormat(PixelFormat.UNKNOWN);
        VideoView video = (VideoView) findViewById(R.id.videoView);
        String path = "android.resource://edu.odu.cs411.loqui/" + R.raw.avatar1;
        Uri uri = Uri.parse(path);
        video.setVideoURI(uri);
        video.requestFocus();
        video.start();
    }

}
