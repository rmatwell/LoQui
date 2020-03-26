package edu.odu.cs411.loqui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.Date;

public class Goals extends AppCompatActivity
{
    private ImageView goals_backbtn;
    public int game;
    public int goal;
    public int amount;
    public int count;
    public int countw;
    public int time;
    public long timestamp;
    public View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_goals);

        Intent intent = getIntent();
        game = intent.getIntExtra("game", 0);
        goal = intent.getIntExtra("goal", 0);
        amount = intent.getIntExtra("amount", 0);
        time = intent.getIntExtra("time", 0);
        timestamp = intent.getLongExtra("timestamp", 0);
        count = 0;

        //add this goal to an array
        switch(game)
        {
            case 0:
                Task3_question g0 = new Task3_question();
                g0.goals.add(this);
                break;
            case 1:
                Task_4 g1 = new Task_4();
                g1.goals.add(this);
                break;
            case 2:
                Task2_2 g2 = new Task2_2();
                g2.goals.add(this);
                break;
        }

        goals_backbtn = findViewById(R.id.goals_back_btn);

        goals_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Goals.this, ParentPortalActivity.class);
                startActivity(it);
            }
        });
    }

    //4 types of goals
    //1: X correct answers overall (no streaks)
    //2: X correct answers in a row
    //3: X correct answers in a certain amount of time
    //4: Percentage of correct answers in an amount of time

    Goals(){}

    public void setView(View v)
    {
        view = v;
    }

    public void Check(int game)
    {
        switch(game)
        {
            case 0:
                Task3_question g0 = new Task3_question();
                for (int i = 0; i < g0.goals.size(); i++)
                {
                   switch(g0.goals.get(i).goal)
                   {
                       case 0:
                           OverallCorrect(count, amount);
                           break;
                       case 1:
                           StreakCorrect(count, amount);
                           break;
                       case 2:
                           TimeCorrect(time, timestamp);
                           break;
                       case 3:
                           TimePercent(time, timestamp, count, countw, amount);
                           break;
                   }
                }
                break;
            case 1:
                Task_4 g1 = new Task_4();
                for (int i = 0; i < g1.goals.size(); i++)
                {
                    switch(g1.goals.get(i).goal)
                    {
                        case 0:
                            OverallCorrect(count, amount);
                            break;
                        case 1:
                            StreakCorrect(count, amount);
                            break;
                        case 2:
                            TimeCorrect(time, timestamp);
                            break;
                        case 3:
                            TimePercent(time, timestamp, count, countw, amount);
                            break;
                    }
                }
                break;
            case 2:
                Task2_2 g2 = new Task2_2();
                for (int i = 0; i < g2.goals.size(); i++)
                {
                    switch(g2.goals.get(i).goal)
                    {
                        case 0:
                            OverallCorrect(count, amount);
                            break;
                        case 1:
                            StreakCorrect(count, amount);
                            break;
                        case 2:
                            TimeCorrect(time, timestamp);
                            break;
                        case 3:
                            TimePercent(time, timestamp, count, countw, amount);
                            break;
                    }
                }
                break;
        }
    }

    public void OverallCorrect(int count, int amount)
    {
        if(count >= amount)
        {
            Reward();
        }
    }

    public void StreakCorrect(int count, int amount)
    {
        if(count >= amount)
        {
            Reward();
        }
    }

    public void TimeCorrect(int time, long timestamp)
    {
        long now = new Date().getTime()/1000;
        long elapsed = now - timestamp;
        if (((elapsed >= time) && (elapsed <= time + 10)) && (time != 0))
        {
            Reward();
        }
    }

    public void TimePercent(int time, long timestamp, int count, int countw, int amount)
    {
        long now = new Date().getTime()/1000;
        long elapsed = now - timestamp;
        int percent = Math.round(count / (count + countw));
        if(((elapsed >= time) && (elapsed <= time + 10)) && percent >= amount)
        {
            Reward();
        }
    }

    public void Reward()
    {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.reward, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}