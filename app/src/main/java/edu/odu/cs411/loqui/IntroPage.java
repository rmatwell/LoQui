package edu.odu.cs411.loqui;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class IntroPage extends AppCompatActivity {

    String cont;
    Integer pic_id;
    Button letsgo, back;
    String task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_intro_page);
        Intent intent = getIntent();
        cont = intent.getStringExtra("content");
        pic_id = intent.getIntExtra("pic_id", 0);
        task = intent.getStringExtra("task");

        TextView title = findViewById(R.id.textView);
        title.setText(task);

        ImageView image = findViewById(R.id.imageView);
        image.setImageResource(pic_id);

        TextView text = findViewById(R.id.text0);
        text.setText(cont);
        clickOnButton();
    }

    private void clickOnButton(){
        letsgo = (Button) findViewById(R.id.sarah);
        back = (Button) findViewById(R.id.sam);

        letsgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               
                if(task.equals("Sammie Says")){
                    Intent it = new Intent(IntroPage.this, Task_4.class);
                    startActivity(it);
                }
                else if (task.equals("Emotion Quest")){
                    Intent it = new Intent(IntroPage.this, Task3_question.class);
                    startActivity(it);
                }
                else if(task.equals("SettingsActivity")){
                    Intent it = new Intent(IntroPage.this, SettingsActivity.class);
                    startActivity(it);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(IntroPage.this, Homepage.class);
                startActivity(it);
            }
        });
    }
}
