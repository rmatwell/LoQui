package edu.odu.cs411.loqui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class ProgressActivity extends AppCompatActivity {

    private ImageView progress_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_progress);

        progress_back_btn = findViewById(R.id.progress_back_btn);

        progress_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ProgressActivity.this, ParentPortalActivity.class);
                startActivity(it);
            }
        });
    }
}
