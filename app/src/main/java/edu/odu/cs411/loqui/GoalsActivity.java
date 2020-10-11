<<<<<<< HEAD
package edu.odu.cs411.loqui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class GoalsActivity extends AppCompatActivity {

    private ImageView goals_backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_goals);

        goals_backbtn = findViewById(R.id.goals_back_btn);

        goals_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(GoalsActivity.this, ParentPortalActivity.class);
                startActivity(it);
            }
        });
    }
}
=======
version https://git-lfs.github.com/spec/v1
oid sha256:25220e77f3700319e5d66672e14c79a4c9b9ab49a87526edab3503bc8377956a
size 922
>>>>>>> 69b172438de57648718503ac42d98caad0ea5cf8
