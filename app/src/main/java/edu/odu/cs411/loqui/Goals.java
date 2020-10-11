<<<<<<< HEAD
package edu.odu.cs411.loqui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Goals extends AppCompatActivity
{
    private ImageView goals_backbtn;
    public int game; //which game the goal belongs to
    public int goal; //which goal type
    public List<Goals> goals = new ArrayList<Goals>();
    String goalID;
    FirestoreWorker dbWorker;
    static boolean goalFlag;

    //4 types of goals
    //1: X correct answers overall (no streaks)
    //2: X correct answers in a row
    //3: X correct answers in a certain amount of time
    //4: Percentage of correct answers in an amount of time

    public int amount; //amount of points or percentage needed to achieve goal
    public int count; //points collected so far
    public int countw; //wrong answers collected, important for percentages
    public int time; //time limit for certain goals, in seconds
    public long timestamp; //shows when a goal was created, so we know when time runs out
    public View view; //need this so we know where to display notification
    public String reward;
    private Button createGoals_btn;
    public String TAG = "Goal";
    public String TAGreward = "Reward";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbWorker = new FirestoreWorker();

        try {this.getSupportActionBar().hide();}
        catch (NullPointerException e) { System.out.print("oh no"); }

        setContentView(R.layout.activity_goals);

        goals_backbtn = findViewById(R.id.goals_back_btn);
        createGoals_btn = findViewById(R.id.create_goal);

        goals_backbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(Goals.this, ParentPortalActivity.class);
                startActivity(it);
            }
        });

        createGoals_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* BEGGINING OF JOSH'S IDEA OF CODE
                // Color Set of Buttons
                ColorStateList colorStateList = new ColorStateList(new int[][]{
                        new int[]{android.R.attr.state_enabled},
                        new int[]{-android.R.attr.state_enabled}},
                        new int[]{Color.BLACK, Color.GRAY});

                g_overall.setButtonTintList(colorStateList);
                g_streak.setButtonTintList(colorStateList);
                g_overallt.setButtonTintList(colorStateList);
                g_percent.setButtonTintList(colorStateList);

                g_overall.setEnabled(false);
                if (g_emotion.isSelected()){g_overall.setEnabled(true);}
                 */


                //game type
                int game = 0;
                RadioButton g_emotion = (RadioButton) findViewById(R.id.emotion_game);
                if (g_emotion.isChecked()) {game = 0;}
                RadioButton g_speech = (RadioButton) findViewById(R.id.speech_training);
                if (g_speech.isChecked()) {game = 1;}
                RadioButton g_eye = (RadioButton) findViewById(R.id.eye_contact_game);
                if (g_eye.isChecked()) {game = 2;}

                //goal type
                int goal = 0;
                RadioButton g_overall = (RadioButton) findViewById(R.id.correct_overall);
                if (g_overall.isChecked()) {goal = 0;}
                RadioButton g_streak = (RadioButton) findViewById(R.id.correct_streak);
                if (g_streak.isChecked()) {goal = 1;}
                RadioButton g_overallt = (RadioButton) findViewById(R.id.correct_overall_time);
                if (g_overallt.isChecked()) {goal = 2;}
                RadioButton g_percent = (RadioButton) findViewById(R.id.correct_percent_time);
                if (g_percent.isChecked()) {goal = 3;}

                //amount or percent value
                EditText t_amount = (EditText) findViewById(R.id.amount_correct);
                int amount;

                if(!t_amount.getText().toString().equals("")) {
                    amount = Integer.parseInt(t_amount.getText().toString());
                }
                else {
                    amount = 0;
                }

                int time;
                //time value in seconds
                EditText t_time = (EditText) findViewById(R.id.time);
                if (!t_time.getText().toString().equals(""))
                {
                    time = Integer.parseInt(t_time.getText().toString());
                }
                else
                {
                    time = 0;
                }

                long timestamp = new Date().getTime()/1000;

                dbWorker.addGoal(game, goal, amount, time, timestamp);

                Toast.makeText(Goals.this,"You've successfully created a new Goal",Toast.LENGTH_SHORT).show();
            }
        });
/*
        ColorStateList colorStateList = new ColorStateList(new int[][]{
                        new int[]{android.R.attr.state_enabled},
                        new int[]{-android.R.attr.state_enabled}},
                        new int[]{Color.BLACK, Color.GRAY});

        RadioButton emotion = findViewById(R.id.emotion_game);
        RadioButton eyeContact = findViewById(R.id.eye_contact_game);
        RadioButton speechTraining = findViewById(R.id.speech_training);

        RadioButton overall = findViewById(R.id.correct_overall);
        RadioButton streak = findViewById(R.id.correct_streak);
        RadioButton overallTime = findViewById(R.id.correct_overall_time);
        RadioButton percentTime = findViewById(R.id.correct_percent_time);

        overall.setEnabled(false);
        overall.setButtonTintList(colorStateList);
        streak.setEnabled(false);
        overallTime.setEnabled(false);
        percentTime.setEnabled(false);


        if (emotion.isPressed()){overall.setEnabled(true); overall.setClickable(true);}


       RadioButton all = findViewById(R.id.all_games);

        all.setOnTouchListener(new View.OnTouchListener()
        {
           @Override
           public boolean onTouch(View view, MotionEvent mtnevent) {
               ColorStateList colorStateList = new ColorStateList(new int[][]{
                       new int[]{-android.R.attr.state_enabled},
                       new int[]{android.R.attr.state_enabled}},
                       new int[]{Color.BLACK, Color.BLUE});

               if (mtnevent.getAction() == MotionEvent.ACTION_UP) {
                   streak.setButtonTintList(colorStateList);
                   overallTime.setButtonTintList(colorStateList);
                   percentTime.setButtonTintList(colorStateList);
                   streak.setEnabled(false);
                   overallTime.setEnabled(false);
                   percentTime.setEnabled(false);
                   return true;
               }
               return false;
           }
        });*/

    }

    public Goals () {
        dbWorker = new FirestoreWorker();
    }

    public Goals(int game_, int goal_, int amount_, int time_, long timestamp_)
    {
        game = game_;
        goal = goal_;
        amount = amount_;
        time = time_;
        timestamp = timestamp_;
        count = 0;
        countw = 0;
        goalID = "";
    }

    public void setView(View v)
    {
        view = v;
    }

    public boolean checkForGoalCompletion(Context context)
    {
       goalFlag = false;

       Goals overallCorrectList = dbWorker.getOverallCorrectGoals();
       Goals streakCorrectList = dbWorker.getStreakCorrectGoals();
       Goals timeCorrectList = dbWorker.getTimeCorrectGoals();
       Goals timePercentList = dbWorker.getTimePercentGoals();

        new CountDownTimer(2000, 1000) {
            public void onFinish()
            {
                if (overallCorrectList.goals.size() != 0)
                {
                    for (int i = 0; i < overallCorrectList.goals.size(); i++)
                    {
                        if (OverallCorrect(overallCorrectList.goals.get(i).game, overallCorrectList.goals.get(i).count, overallCorrectList.goals.get(i).amount))
                        {
                            Toast.makeText(context,"You've earned 5 points for completing a Goal! Way to go!",Toast.LENGTH_SHORT).show();
                            dbWorker.removeGoal(overallCorrectList.goals.get(i).goalID);
                            dbWorker.addToRewardScore(5); //bonus points

                        }
                    }
                }

                if (streakCorrectList.goals.size() != 0)
                {
                    for (int i = 0; i < streakCorrectList.goals.size(); i++)
                    {
                        if (StreakCorrect(streakCorrectList.goals.get(i).game, streakCorrectList.goals.get(i).count, streakCorrectList.goals.get(i).amount))
                        {
                            Toast.makeText(context,"You've earned 5 points for completing a Goal! Way to go!",Toast.LENGTH_SHORT).show();
                            dbWorker.removeGoal(streakCorrectList.goals.get(i).goalID);
                            dbWorker.addToRewardScore(5); //bonus points
                        }
                    }
                }

                if (timeCorrectList.goals.size() != 0)
                {
                    for (int i = 0; i < timeCorrectList.goals.size(); i++)
                    {
                        if (TimeCorrect(timeCorrectList.goals.get(i).game, timeCorrectList.goals.get(i).time, timeCorrectList.goals.get(i).timestamp, timeCorrectList.goals.get(i).count, timeCorrectList.goals.get(i).amount))
                        {
                            Toast.makeText(context,"You've earned 5 points for completing a Goal! Way to go!",Toast.LENGTH_SHORT).show();
                            dbWorker.removeGoal(timeCorrectList.goals.get(i).goalID);
                            dbWorker.addToRewardScore(5); //bonus points
                        }
                    }
                }

                if (timePercentList.goals.size() != 0)
                {
                    for (int i = 0; i < timePercentList.goals.size(); i++)
                    {
                        if (TimePercent(timePercentList.goals.get(i).game, timePercentList.goals.get(i).time, timePercentList.goals.get(i).timestamp,
                                timePercentList.goals.get(i).count, timePercentList.goals.get(i).countw, timePercentList.goals.get(i).amount))
                        {
                            Toast.makeText(context,"You've earned 5 points for completing a Goal! Way to go!",Toast.LENGTH_SHORT).show();
                            dbWorker.removeGoal(timePercentList.goals.get(i).goalID);
                            dbWorker.addToRewardScore(5); //bonus points
                        }
                    }
                }
            }
            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();

        Log.d("checkForGoalCompletion","Goal Flag = " + goalFlag);
        return goalFlag;
    }

    public boolean OverallCorrect(int game, int count, int amount)
    {
        Log.d("OverallCorrect", "Entering OverallCorrect");
        if(count >= amount)
        {
            Log.d("OverallCorrect", "Goal completed. Updating rewardScore");
            return true;
        }

        return false;
    }

    public boolean StreakCorrect(int game, int count, int amount)
    {
        Log.d("StreakCorrect", "Entering StreakCorrect");
        if(count >= amount)
        {
            Log.d("StreakCorrect","Goal completed. Updating rewardScore");
            return true;
        }

        return false;
    }

    public boolean TimeCorrect(int game, int time, long timestamp, int count, int amount)
    {
        Log.d("TimeCorrect", "Entering TimeCorrect");
        long now = new Date().getTime()/1000;
        long elapsed = now - timestamp;

        Log.d("TimeCorrect", "elapsed = " + elapsed + " and time = " + time);

        if ((elapsed <= time && (time != 0)) && count >= amount)
        {
            Log.d("TimeCorrect", "Goal completed. Updating rewardScore");
            return true;
        }

        return false;
    }

    public boolean TimePercent(int game, int time, long timestamp, int count, int countw, int amount)
    {
        Log.d("TimePercent", "Entering TimePercent");

        int percent;

        if (game == 0 || game == 1)
        {
            long now = new Date().getTime()/1000;
            long elapsed = now - timestamp;

            Log.d("TimeCorrect", "elapsed = " + elapsed + " and time = " + time);

            if (count >= 3)
            {
                percent = Math.round(count / (count + countw));

                if(elapsed <= time && percent >= (amount / 100))
                {
                    Log.d("TimePercent", "Goal completed. Updating rewardScore");
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
        else if (game == 2)
        {
            long now = new Date().getTime()/1000;
            long elapsed = now - timestamp;
            percent = count;
            if(elapsed <= time && percent >= amount)
            {
                Log.d("TimePercent", "Goal completed. Updating rewardScore");
                return true;
            }
        }


        return false;
    }
}
=======
version https://git-lfs.github.com/spec/v1
oid sha256:b9039af97e3d38fc26bbc96df5f5199ecb3a8d946b236ce34b0110571c26b8e9
size 14336
>>>>>>> 69b172438de57648718503ac42d98caad0ea5cf8
