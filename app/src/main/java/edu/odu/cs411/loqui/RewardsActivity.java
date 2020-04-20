package edu.odu.cs411.loqui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class RewardsActivity extends AppCompatActivity {

    private Button createReward_btn;
    private ImageView settings_backbtn;
    FirestoreWorker dbWorker = new FirestoreWorker();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_rewards);

        createReward_btn = findViewById(R.id.create_reward);
        settings_backbtn = findViewById(R.id.settings_back_btn);



        settings_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(RewardsActivity.this, ParentPortalActivity.class);
                startActivity(it);
            }
        });

        createReward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText rewardString = (EditText)findViewById(R.id.rewardStringField);
                String rewardFromText = rewardString.getText().toString();
                dbWorker.setReward(rewardFromText);

                Toast.makeText(RewardsActivity.this,"Reward successfully created.",Toast.LENGTH_SHORT).show();
            }
        });



    }


}
