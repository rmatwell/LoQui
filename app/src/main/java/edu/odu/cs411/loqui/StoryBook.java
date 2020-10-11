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
        String path = "android.resource://edu.odu.cs411.loqui/" + R.raw.storytimefinal;
        Uri uri = Uri.parse(path);
        video.setVideoURI(uri);
        video.requestFocus();
        video.start();
        clickOnButton();
    }

    private void clickOnButton() {
        back = (Button) findViewById(R.id.back);
        next = (Button) findViewById(R.id.next);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(StoryBook.this, Homepage.class);
                startActivity(it);
            }

        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(StoryBook.this, Story.class);
                startActivity(it);
            }

        });

    }
}
=======
version https://git-lfs.github.com/spec/v1
oid sha256:b91698055d5a89e7fedf984d848ba1a58c126f6d3ada5e3b0931f53411331ed5
size 1729
>>>>>>> 69b172438de57648718503ac42d98caad0ea5cf8
