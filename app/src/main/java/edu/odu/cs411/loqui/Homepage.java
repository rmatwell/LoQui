package edu.odu.cs411.loqui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Homepage extends AppCompatActivity {

    //watches to see if the user presses home button
    HomeWatcher mHomeWatcher;
    private Button go1, go2, go3,button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_homepage);
        clickOnButton();

        //Binds music service to Activity
        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);

        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
        });
        mHomeWatcher.startWatch();
    }

    private void clickOnButton(){
        go1 = (Button) findViewById(R.id.go1);
        go2 = (Button) findViewById(R.id.go2);
        go3 = (Button) findViewById(R.id.go3);
        button2 = (Button) findViewById(R.id.button2);

        go1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Homepage.this, IntroPage.class);
                it.putExtra("content", "Look at the pictures, and learn what they mean!");
                it.putExtra("task", "Learn Your Emotions");
                it.putExtra("pic_id", R.drawable.task1_intro);
                startActivity(it);
            }
        });
        go2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Homepage.this, IntroPage.class);
                it.putExtra("content", "Make your face look like the picture of the character!");
                it.putExtra("task", "Face Matching Game");
                it.putExtra("pic_id", R.drawable.task2_intro);
                startActivity(it);
            }
        });
        go3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Homepage.this, IntroPage.class);
                it.putExtra("content", "Lets see how well you know your emotions!");
                it.putExtra("task", "Emotion Quiz");
                it.putExtra("pic_id", R.drawable.task3_intro);
                startActivity(it);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Homepage.this, SettingsActivity.class);
                startActivity(it);
            }
        });

    }

    //Binds/unbinds music service
    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null) {
            mServ.resumeMusic();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isInteractive = false;
        if (pm != null) {
            isInteractive = pm.isInteractive();
        }

        if (!isInteractive) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        stopService(music);

    }
}

