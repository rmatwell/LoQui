package edu.odu.cs411.loqui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

public class Homepage extends AppCompatActivity {

    private Button go1, go2, go3,button2;
    Button btnahead, btnback;
    static int count = 0;
    MyProgressBar step_progress_bar;
    private static final String TAG = "Homepage";
    final Handler handler = new Handler();
    Timer timer;
    TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_homepage);
        step_progress_bar = findViewById(R.id.step_progress_bar);

        clickOnButton();

        btnahead = (Button) findViewById(R.id.complete);
        btnahead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step_progress_bar.updateProgress(count);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        startTimer();
    }

    public void startTimer()
    {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void initializeTimerTask()
    {
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        FirestoreWorker dbWorker = new FirestoreWorker();
                        count = dbWorker.getRewardScore() - 1;
                        step_progress_bar.updateProgress(count);
                    }
                });
            }
        };
    }

    private void clickOnButton(){
        go1 = (Button) findViewById(R.id.go1);
        go2 = (Button) findViewById(R.id.go2);
        go3 = (Button) findViewById(R.id.go3);
        button2 = (Button) findViewById(R.id.button2);

        go1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Homepage.this, StoryBook.class);
                startActivity(it);
            }
        });
        go2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Homepage.this, SpeechGame.class);
                it.putExtra("content", "Let's improve your speech!");
                it.putExtra("task", "Sammie Says");
                it.putExtra("pic_id", R.drawable.task2_intro);
                startActivity(it);
            }
        });
        go3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Homepage.this, IntroPage.class);
                it.putExtra("content", "Let's quiz your understanding of emotions!");
                it.putExtra("task", "Emotion Quest");
                it.putExtra("pic_id", R.drawable.task3_intro);
                startActivity(it);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Homepage.this, ParentPortalActivity.class);
                startActivity(it);
            }
        });

    }


}

