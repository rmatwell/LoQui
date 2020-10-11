<<<<<<< HEAD
package edu.odu.cs411.loqui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.ImageView;

public class ParentPortalActivity extends AppCompatActivity {

    private CardView goalsCard, progressCard, rewardsCard, audioCard;
    private ImageView portal_backbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_parentportal);

        goalsCard = (CardView) findViewById(R.id.portal_goals);
        progressCard = (CardView) findViewById(R.id.portal_progress);
        rewardsCard = (CardView) findViewById(R.id.portal_rewards);
        audioCard = (CardView) findViewById(R.id.portal_audio);
        portal_backbtn = findViewById(R.id.portal_back_btn);

        goalsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ParentPortalActivity.this, Goals.class);
                startActivity(it);
            }
        });

        progressCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ParentPortalActivity.this, ProgressActivity.class);
                startActivity(it);
            }
        });

        rewardsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ParentPortalActivity.this, RewardsActivity.class);
                startActivity(it);
            }
        });

        audioCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ParentPortalActivity.this, MusicActivity.class);
                startActivity(it);
            }
        });

        portal_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ParentPortalActivity.this, Homepage.class);
                startActivity(it);
            }
        });



    }


}
=======
version https://git-lfs.github.com/spec/v1
oid sha256:cdef30e1df29cdaf7ec49edb3916cb0cc983a3b2157ce2430fae9e7c142de3eb
size 2455
>>>>>>> 69b172438de57648718503ac42d98caad0ea5cf8
