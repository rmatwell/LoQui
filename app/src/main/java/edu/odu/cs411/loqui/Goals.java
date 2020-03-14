package edu.odu.cs411.loqui;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Goals extends AppCompatActivity
{
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