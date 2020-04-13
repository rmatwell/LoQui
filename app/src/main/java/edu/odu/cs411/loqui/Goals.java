package edu.odu.cs411.loqui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Goals extends AppCompatActivity
{
    private ImageView goals_backbtn;
    public int game; //which game the goal belongs to
    public int goal; //which goal type
    public List<Goals> goals = new ArrayList<Goals>();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {this.getSupportActionBar().hide();}
        catch (NullPointerException e) { System.out.print("it's fucked"); }

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
                Goals g = new Goals(game, goal, amount, time, timestamp);
                goals.add(g);
            }
        });
    }

    public Goals () {}

    public Goals(int game_, int goal_, int amount_, int time_, long timestamp_)
    {
        game = game_;
        goal = goal_;
        amount = amount_;
        time = time_;
        timestamp = timestamp_;
        count = 0;
        countw = 0;
    }

    public void setView(View v)
    {
        view = v;
    }

    public void Check(int game)
    {
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
    }

    public void OverallCorrect(int game, int count, int amount)
    {
        if(count >= amount)
        {
            int index = getIndexByGameID(game);
            goals.remove(index); //goal is complete, need to remove
            Reward();
        }
    }

    public void StreakCorrect(int game, int count, int amount)
    {
        if(count >= amount)
        {
            int index = getIndexByGameID(game);
            goals.remove(index); //goal is complete, need to remove
            Reward();
        }
    }

    public void TimeCorrect(int game, int time, long timestamp)
    {
        long now = new Date().getTime()/1000;
        long elapsed = now - timestamp;
        if (((elapsed >= time) && (elapsed <= time + 10)) && (time != 0))
        {
            int index = getIndexByGameID(game);
            goals.remove(index); //goal is complete, need to remove
            Reward();
        }
    }

    public void TimePercent(int game, int time, long timestamp, int count, int countw, int amount)
    {
        long now = new Date().getTime()/1000;
        long elapsed = now - timestamp;
        int percent = Math.round(count / (count + countw));
        if(((elapsed >= time) && (elapsed <= time + 10)) && percent >= amount)
        {
            int index = getIndexByGameID(game);
            goals.remove(index); //goal is complete, need to remove
            Reward();
        }
    }

    public void Reward()
    {
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
    }

    public void GlobalGoalAdd()
    {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference userRef = db.collection("users").document(userID);

        //don't know how to retrieve user's points
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