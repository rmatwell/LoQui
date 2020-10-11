<<<<<<< HEAD
package edu.odu.cs411.loqui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

public class Homepage extends AppCompatActivity {

    private Button go1, go2, go3,button2;
    private Button welcomeMessage;
    Button btnahead, btnback;
    static int count = 0;
    MyProgressBar step_progress_bar;
    private static final String TAG = "Homepage";
    final Handler handler = new Handler();
    Timer timer;
    TimerTask timerTask;
    FirestoreWorker dbWorker = new FirestoreWorker();
    IntegerRef countRef = new IntegerRef();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_homepage);
        //welcomeMessage = findViewById(R.id.button3);
        step_progress_bar = findViewById(R.id.step_progress_bar);

        dbWorker.getRewardScore(Homepage.this, countRef);
        count = countRef.intRef - 1;

        Goals currentGoals = new Goals();
        currentGoals.checkForGoalCompletion(Homepage.this);

        dbWorker.setHomepageBanner(Homepage.this);

        new CountDownTimer(10000, 1000) {
            public void onFinish()
            {
                timer.cancel();
            }
            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();

        clickOnButton();
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
        timer.schedule(timerTask, 2000, 2000); //
    }

    public void initializeTimerTask()
    {
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        dbWorker.getRewardScore(Homepage.this, countRef);
                        count = countRef.intRef - 1;
                        if (count > -1)
                        {
                            step_progress_bar.updateProgress(count);
                        }
                        else
                        {
                            step_progress_bar.updateProgress(-1);
                        }
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
                timer.cancel();
                count = 0;
                startActivity(it);
            }
        });
        go2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Homepage.this, SpeechGame.class);
                it.putExtra("content", "Let's improve your speech!");
                it.putExtra("task", "Sarah Says");
                it.putExtra("pic_id", R.drawable.task2_intro);
                timer.cancel();
                count = 0;
                startActivity(it);
            }
        });
        go3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Homepage.this, Task3_question.class);
                it.putExtra("content", "Let's quiz your understanding of emotions!");
                timer.cancel();
                count = 0;
                startActivity(it);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Homepage.this, ParentPortalActivity.class);
                timer.cancel();
                count = 0;
                startActivity(it);
            }
        });

    }


}

=======
version https://git-lfs.github.com/spec/v1
oid sha256:15000db2113b0c3ef19a0acd52c3f2b95d846e821a08dd94eeb7c4ec7402ec53
size 4551
>>>>>>> 69b172438de57648718503ac42d98caad0ea5cf8
