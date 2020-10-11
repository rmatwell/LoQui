<<<<<<< HEAD
package edu.odu.cs411.loqui;


import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class Avatars extends AppCompatActivity {
    private Button next, back, sam, sarah;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_avatars);
        Button play = (Button) findViewById(R.id.button);
        getWindow().setFormat(PixelFormat.UNKNOWN);
        VideoView video = (VideoView) findViewById(R.id.videoView);
        String path = "android.resource://edu.odu.cs411.loqui/" + R.raw.avatars;
        Uri uri = Uri.parse(path);
        video.setVideoURI(uri);
        video.start();
        video.requestFocus();
        video.setOnPreparedListener(mp -> mp.setLooping(true));
        clickOnButton();
    }

    private void clickOnButton() {
        sam = (Button) findViewById(R.id.sam);
        sarah = (Button) findViewById(R.id.sarah);

        sarah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirestoreWorker dbWorker = new FirestoreWorker();
                dbWorker.setAvatar(1);
                Intent it = new Intent(Avatars.this, Homepage.class);
                startActivity(it);
            }
        });
        sam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirestoreWorker dbWorker = new FirestoreWorker();
                dbWorker.setAvatar(2);
                Intent it = new Intent(Avatars.this, Homepage.class);
                startActivity(it);
            }

        });

    }

}
=======
version https://git-lfs.github.com/spec/v1
oid sha256:f5ff7a509a639e51197d070bfbf8124fdaa3fd207a2b85f0048b406f5f3f110b
size 2004
>>>>>>> 69b172438de57648718503ac42d98caad0ea5cf8
