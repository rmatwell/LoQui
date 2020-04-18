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
                int amount = Integer.parseInt(t_amount.getText().toString());

                //time value in seconds
                EditText t_time = (EditText) findViewById(R.id.time);
                int time = Integer.parseInt(t_time.getText().toString());

                long timestamp = new Date().getTime()/1000;

                dbWorker.addGoal(game, goal, amount, time, timestamp);
                /*
                Goals g = new Goals(game, goal, amount, time, timestamp);
                goals.add(g);
                 */
            }
        });

        RadioButton streak = findViewById(R.id.correct_streak);
        RadioButton overallTime = findViewById(R.id.correct_overall_time);
        RadioButton percentTime = findViewById(R.id.correct_percent_time);
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
        });
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
       /*
        Goals g = goals.get(getIndexByGameID(game));

        switch(g.goal)
        {
            case 0:
                OverallCorrect(g.game, g.count, g.amount);
                break;
            case 1:
                StreakCorrect(g.game, g.count, g.amount);
                break;
            case 2:
                TimeCorrect(g.game, g.time, g.timestamp);
                break;
            case 3:
                TimePercent(g.game, g.time, g.timestamp, g.count, g.countw, g.amount);
                break;
        }
        */
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
                            goalAlert(context);
                            dbWorker.removeGoal(overallCorrectList.goals.get(i).goalID);

                        }
                    }
                }

                if (streakCorrectList.goals.size() != 0)
                {
                    for (int i = 0; i < streakCorrectList.goals.size(); i++)
                    {
                        if (StreakCorrect(streakCorrectList.goals.get(i).game, streakCorrectList.goals.get(i).count, streakCorrectList.goals.get(i).amount))
                        {
                            goalAlert(context);
                            dbWorker.removeGoal(streakCorrectList.goals.get(i).goalID);
                        }
                    }
                }

                if (timeCorrectList.goals.size() != 0)
                {
                    for (int i = 0; i < timeCorrectList.goals.size(); i++)
                    {
                        if (TimeCorrect(timeCorrectList.goals.get(i).game, timeCorrectList.goals.get(i).time, timeCorrectList.goals.get(i).timestamp))
                        {
                            goalAlert(context);
                            dbWorker.removeGoal(timeCorrectList.goals.get(i).goalID);
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
                            goalAlert(context);
                            dbWorker.removeGoal(timePercentList.goals.get(i).goalID);
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
            //int index = getIndexByGameID(game);
            //goals.remove(index); //goal is complete, need to remove
            dbWorker.addToRewardScore(5); //bonus points
            //Reward();
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
            //int index = getIndexByGameID(game);
            //goals.remove(index); //goal is complete, need to remove
            dbWorker.addToRewardScore(5); //bonus points
            //Reward();
            return true;
        }

        return false;
    }

    public boolean TimeCorrect(int game, int time, long timestamp)
    {
        Log.d("TimeCorrect", "Entering TimeCorrect");
        long now = new Date().getTime()/1000;
        long elapsed = now - timestamp;
        if (((elapsed >= time) && (elapsed <= time + 10)) && (time != 0))
        {
            Log.d("TimeCorrect", "Goal completed. Updating rewardScore");
            //int index = getIndexByGameID(game);
            //goals.remove(index); //goal is complete, need to remove
            dbWorker.addToRewardScore(5); //bonus points
            //Reward();
            return true;
        }

        return false;
    }

    public boolean TimePercent(int game, int time, long timestamp, int count, int countw, int amount)
    {
        Log.d("TimePercent", "Entering TimePercent");

        long now = new Date().getTime()/1000;
        long elapsed = now - timestamp;
        int percent = Math.round(count / (count + countw));
        if(((elapsed >= time) && (elapsed <= time + 10)) && percent >= amount)
        {
            Log.d("TimePercent", "Goal completed. Updating rewardScore");
            //int index = getIndexByGameID(game);
            //goals.remove(index); //goal is complete, need to remove
            dbWorker.addToRewardScore(5); //bonus points
            //Reward();
            return true;
        }

        return false;
    }

    public void Reward(Context context)
    {
        Log.d(TAGreward, "Entering the reward method.");

        /*
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.reward, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //sets reward text
        ((TextView)popupWindow.getContentView().findViewById(R.id.rewardwindow)).setText(reward);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
         */

        /*
        new AlertDialog.Builder(this.getApplicationContext())
                .setTitle("You've earned a reward!")
                .setMessage("Pizza for dinner!")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

         */

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("You've earned a reward!");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Accept",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void goalAlert(Context context)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("You've earned 5 points for completing a goal!");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Accept",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }



    private int getIndexByGameID(int gameID)
    {
        for (int i = 0; i < goals.size(); i++) {
            if (goals.get(i) != null && (goals.get(i).game == gameID))
            {
                return i;
            }
        }
        return 0;// not here
    }
}