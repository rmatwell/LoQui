<<<<<<< HEAD
package edu.odu.cs411.loqui;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Homepage2 extends AppCompatActivity {


    private Button go1, go2, go3, button2;
    Button btnahead, btnback;
    static int count = -1;
    MyProgressBar step_progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_homepage);
        step_progress_bar = findViewById(R.id.step_progress_bar);

        clickOnButton();
        btnahead = (Button) findViewById(R.id.go2);
        btnback = (Button) findViewById(R.id.go3);

        btnahead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                step_progress_bar.updateProgress(count);
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                step_progress_bar.updateProgress(count);
            }
        });

    }

    private void clickOnButton(){
        go1 = (Button) findViewById(R.id.go1);
        go2 = (Button) findViewById(R.id.go2);
        go3 = (Button) findViewById(R.id.go3);
        button2 = (Button) findViewById(R.id.button2);


        go2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Homepage2.this, SpeechGame.class);
                it.putExtra("content", "Let's improve your speech!");
                it.putExtra("task", "Sammie Says");
                it.putExtra("pic_id", R.drawable.task2_intro);
                startActivity(it);
            }
        });
        go3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Homepage2.this, IntroPage.class);
                it.putExtra("content", "Let's quiz your understanding of emotions!");
                it.putExtra("task", "Emotion Quest");
                it.putExtra("pic_id", R.drawable.task3_intro);
                startActivity(it);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Homepage2.this, ParentPortalActivity.class);
                startActivity(it);
            }
        });

    }


}

=======
version https://git-lfs.github.com/spec/v1
oid sha256:ffde1b516d8ba737918202e6ea14d57ef52c0493950a21bbe6a4001a22798483
size 2766
>>>>>>> 69b172438de57648718503ac42d98caad0ea5cf8
