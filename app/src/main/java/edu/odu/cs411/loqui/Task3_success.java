package edu.odu.cs411.loqui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.graphics.PixelFormat;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.VideoView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

public class Task3_success extends AppCompatActivity {

    private Button home, next;
    private Integer first, second, third, fourth;
    private ImageView first_img, second_img, third_img, fourth_img;
    private TextView def_text, hint;
    private String emotion;
    private String def_json_path = "emotions_def.json";
    private Handler mHandler = new Handler();
    private static final String TAG = "EmotionActivity";
    static int count = 0;
    IntegerRef countRef = new IntegerRef();
    MyProgressBar step_progress_bar;
    final Handler handler = new Handler();
    Timer timer;
    TimerTask timerTask;
    FirestoreWorker dbWorker = new FirestoreWorker();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_task3_success);
        step_progress_bar = findViewById(R.id.step_progress_bar);
        dbWorker.getRewardScore(Task3_success.this, countRef);
        count = countRef.intRef - 1;

        new CountDownTimer(10000, 1000) {
            public void onFinish()
            {
                timer.cancel();
            }
            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
        Button play = (Button) findViewById(R.id.button);
        getWindow().setFormat(PixelFormat.UNKNOWN);
        VideoView video = (VideoView) findViewById(R.id.videoView2);
        String path = "android.resource://edu.odu.cs411.loqui/" + R.raw.welldonefinal;
        Uri uri = Uri.parse(path);
        video.setVideoURI(uri);
        video.requestFocus();
        video.start();
        FirestoreWorker dbWorker = new FirestoreWorker();

        Intent intent = getIntent();
        fourth = intent.getIntExtra("fourth", 0);
        emotion = intent.getStringExtra("emotion");

        String def = readFromJSON(def_json_path);

        if (fourth != 0) {
            first_img = findViewById(R.id.img_4);
            first_img.setImageResource(fourth);
        }


        if (def != null) {
            def_text = findViewById(R.id.msg_def);
            def_text.setText(def);

            hint = findViewById(R.id.hint);
            hint.setText("The " + emotion  + " face is:");
        }

        dbWorker.addToRewardScore(1);

        clickOnButton();
    }


    private void clickOnButton(){
        home  = (Button) findViewById(R.id.home);
        next = (Button) findViewById(R.id.next);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task3_success.this, Homepage.class);
                startActivity(it);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task3_success.this, Task3_question.class);
                startActivity(it);
            }
        });
    }
    
    private String readFromJSON(String filepath) {
        try {
            InputStream is = this.getAssets().open(filepath);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            try {
                JSONArray list = new JSONArray(new String(buffer, "UTF-8"));
                for (int i = 0; i < list.length(); i++) {
                    JSONObject item = list.getJSONObject(i);

                    String name = item.getString("picture");
                    String def = item.getString("content");

                    if (name.equals(emotion)) {
                        return def;
                    }
                }
            } catch (JSONException e) {
                Log.e("json", "error while reading emotions.json " + e.toString());
            }
        } catch (IOException e) {
            Log.e("json", "error reading json file " + e);
        }
        return null;
    }
}