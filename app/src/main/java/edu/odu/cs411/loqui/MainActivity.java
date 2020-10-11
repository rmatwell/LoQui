package edu.odu.cs411.loqui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.MotionEvent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ViewGroup mainContentView;
    private Button btnLogout;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //btnLogout = findViewById(R.id.btnLogout);
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user;
        user = firebaseAuth.getCurrentUser();

        mainContentView = (ViewGroup) findViewById(R.id.main_view);
        if (mainContentView == null) throw new AssertionError();
        mainContentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    fullScreenMode();
                }
                return false;
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        fullScreenMode();
    }


    /**
     *  Sets the app to full screen
     */
    private void fullScreenMode() {

        mainContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }
}