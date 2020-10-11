<<<<<<< HEAD
package edu.odu.cs411.loqui;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Task1 extends AppCompatActivity {

    private Button back, letsGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_task1);
        clickOnButton();
    }

    private void clickOnButton(){
        back  = (Button) findViewById(R.id.b1_1);
        letsGo = (Button) findViewById(R.id.b1_2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task1.this, Homepage.class);
                startActivity(it);
            }
        });
        letsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task1.this, Task1_1.class);
                startActivity(it);
            }
        });
    }
}
=======
version https://git-lfs.github.com/spec/v1
oid sha256:9cccb159354b2a17a54d4e41afe08e26c5cb72c1f5d2670583df9ca67ba96039
size 1243
>>>>>>> 69b172438de57648718503ac42d98caad0ea5cf8
