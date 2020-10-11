<<<<<<< HEAD
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
=======
version https://git-lfs.github.com/spec/v1
oid sha256:032cf5ac98c2a19c7e5677163b19f9c99324b89c6da9f48bc2ad67151f028348
size 1813
>>>>>>> 69b172438de57648718503ac42d98caad0ea5cf8
