<<<<<<< HEAD
package edu.odu.cs411.loqui;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Task1_emotion extends AppCompatActivity {

    String cont;
    Integer pic_id;
    ImageView photo;
    TextView text;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_task1_emotion);
        Intent intent = getIntent();
        cont = intent.getStringExtra("content");
        pic_id = intent.getIntExtra("pic_id", 0);
        photo = findViewById(R.id.imageView);
        photo.setImageResource(pic_id);


        text = findViewById(R.id.text0);
        text.setText(cont);
        clickOnButton();
    }

    private void clickOnButton(){
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Task1_emotion.this, Task1_1.class);
                startActivity(it);
            }
        });
    }
}
=======
version https://git-lfs.github.com/spec/v1
oid sha256:addf69ff3224bafc45c9a2514cfe05740f6b17fd78f41c188c955f7d4b6d5402
size 1385
>>>>>>> 69b172438de57648718503ac42d98caad0ea5cf8
