package edu.odu.cs411.loqui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Avatar extends AppCompatActivity {

    private Button  sammie, masie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.settings);
        clickOnButton();
    }

    private void clickOnButton(){
        sammie = (Button) findViewById(R.id.sammie);
        masie = (Button) findViewById(R.id.masie);

        sammie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Avatar.this, Homepage.class);
                startActivity(it);
            }
        });
        masie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Avatar.this, Homepage2.class);
                startActivity(it);
            }
        });

    }
}
