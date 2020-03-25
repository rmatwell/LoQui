package edu.odu.cs411.loqui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class Goals extends AppCompatActivity
{
    private ImageView goals_backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_goals);

        goals_backbtn = findViewById(R.id.goals_back_btn);

        goals_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Goals.this, ParentPortalActivity.class);
                startActivity(it);
            }
        });
    }

    private int count = 0;
    public int trigger;
    public String game;
    public int reward;
    public View view;

    //4 types of goals
    //1: X correct answers overall (no streaks)
    //2: X correct answers in a row
    //3: X correct answers in a certain amount of time
    //4: Percentage of correct answers in an amount of time

    Goals(int trig, String g, int r, View v)
    {
        trigger = trig;
        game = g;
        reward = r;
        view = v;
    }

    public void Count()
    {
        count++;
        if (count >= trigger)
        {
            switch(reward)
            {
                case 1:
                    OverallCorrect();
                    break;
                case 2:
                    StreakCorrect();
                    break;
                case 3:
                    TimeCorrect();
                    break;
                case 4:
                    TimePercent();
                    break;
            }
        }
        count = 0;
    }

    public void OverallCorrect()
    {

    }

    public void StreakCorrect()
    {

    }

    public void TimeCorrect()
    {

    }

    public void TimePercent()
    {

    }
}