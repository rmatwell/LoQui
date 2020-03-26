package edu.odu.cs411.loqui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import android.os.Handler;

public class Homepage extends AppCompatActivity {

    private Button go1, go2, go3,button2;
    Button btnahead, btnback;
    static int count = 0;
    MyProgressBar step_progress_bar;
    private static final String TAG = "Homepage";
    final Handler handler = new Handler();

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

        Log.d(TAG, "Right before starting Firestore");

        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();

        Log.d(TAG, "Firebase Auth initialized");

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userID = user.getEmail();

        Log.d(TAG, "User ID pulled");

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();

        Log.d(TAG, "Firestore initialized");

        DocumentReference userRef = db.collection("users").document(userID);

        userRef.get().addOnCompleteListener(new OnCompleteListener< DocumentSnapshot >() {
            @Override
            public void onComplete(@NonNull Task< DocumentSnapshot > task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot userData = task.getResult();

                    if(userData.exists())
                    {
                        Log.d(TAG, "DocumentSnapshot data: " + userData.getData());
                    }
                    else {
                        Log.d(TAG, "No such document userID = " + userID);
                    }
                    double dbScore = userData.getDouble("rewardScore");
                    count = (int)dbScore - 1;
                    Log.d(TAG, "count = " + count);
                }
                else
                {
                    Log.d(TAG, "get fail with ", task.getException());
                }
            }
        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        Log.d(TAG, "count = " + count);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                step_progress_bar.updateProgress(count);
            }
        }, 1000);
        //step_progress_bar.addedBarToView();
        //step_progress_bar.updateProgress(count);

        clickOnButton();


        btnahead = (Button) findViewById(R.id.complete);
        btnahead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step_progress_bar.updateProgress(count);
            }
        });


    }

    private void clickOnButton(){
        go1 = (Button) findViewById(R.id.go1);
        go2 = (Button) findViewById(R.id.go2);
        go3 = (Button) findViewById(R.id.go3);
        button2 = (Button) findViewById(R.id.button2);

        go1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Homepage.this, Story.class);
                it.putExtra("content", "Story Time");
                it.putExtra("task", "Story Time");
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

