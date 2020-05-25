package edu.odu.cs411.loqui;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class Story extends AppCompatActivity {
    private Button next, back, sam, sarah;
    CameraSource camera;
    VideoView video;
    Chronometer chronometer;
    // Planning to create another Chronometer variable to determine the story time when it's both finished over time and interrupted
    // Chronometer chronometer2;
    ImageView faceDetectStatus;
    boolean isRunning;
    //Creating new Boolean Variable to determine if video is Playing
    boolean isPlaying;
    boolean faceDetected;
    long timeStopped = 0;
    int duration;
    int eyeContactTime;
    double score;
    FirestoreWorker dbWorker = new FirestoreWorker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_story);
        getWindow().setFormat(PixelFormat.UNKNOWN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            Toast.makeText(this, "Camera permission required for game", Toast.LENGTH_SHORT).show();
        }
        else {
            video = (VideoView) findViewById(R.id.videoView);
            chronometer = findViewById(R.id.chronometer);
            faceDetectStatus = findViewById(R.id.imageView);
            String path = "android.resource://edu.odu.cs411.loqui/" + R.raw.threelilpigs;
            Uri uri = Uri.parse(path);
            chronometer.setBase(SystemClock.elapsedRealtime());

            video.setVideoURI(uri);
            video.requestFocus();
            video.start();
            chronometer.start();
            //chronometer2.start();
            //Setting isPlaying variable to true because the video has started
            isPlaying = true;
            isRunning = true;
            faceDetected = true;
            createCameraSource();
        }
        clickOnButton();
        getDuration();
        videoEndListener();
    }

    //EyeContactTracker Class uses the Google Vision API to detect eye contact.
    private class EyeContactTracker extends Tracker<Face>{
        private final float THRESHOLD = 15.0f;

        private EyeContactTracker(){}

        @Override
        public void onUpdate(Detector.Detections<Face> detections, Face face){
            if(face.getEulerY() > THRESHOLD || face.getEulerY() < -THRESHOLD){
                //Face detected, but eye contact not detected
                if(isRunning){
                    timeStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                    chronometer.stop();
                    //If video is playing when it shouldn't be, Video Will Pause
                    if(isPlaying){video.pause(); isPlaying = false;}
                    isRunning = false;
                    faceDetected = false;
                }
                showStatus();
            }
            else{
                //Eye contact detected
                if(!isRunning){
                    chronometer.setBase(SystemClock.elapsedRealtime() + timeStopped);
                    chronometer.start();
                    //If video is not playing when it should be. Video will Resume from Pause Point
                    if (!isPlaying){video.start();isPlaying=true;}
                    isRunning = true;
                    faceDetected = true;
                }
                showStatus();
            }
        }

        @Override
        public void onMissing(Detector.Detections<Face> detections){
            super.onMissing(detections);
            //No face detected
            if(isRunning){
                timeStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                chronometer.stop();
                //If video is playing when it shouldn't be, Video Will Pause
                if(isPlaying){video.pause(); isPlaying=false;}
                isRunning = false;
                faceDetected = false;
            }
            showStatus();
        }

        @Override
        public void onDone(){
            super.onDone();
        }
    }
    private class FaceTrackerFactory implements MultiProcessor.Factory<Face> {

        private FaceTrackerFactory() {

        }

        @Override
        public Tracker<Face> create(Face face) {
            return new EyeContactTracker();
        }
    }

    public void createCameraSource() {
        FaceDetector detector = new FaceDetector.Builder(this)
                .setTrackingEnabled(true)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        detector.setProcessor(new MultiProcessor.Builder(new FaceTrackerFactory()).build());

        camera = new CameraSource.Builder(this, detector)
                .setRequestedPreviewSize(1024, 768)
                .setFacing(camera.CAMERA_FACING_FRONT)
                .setRequestedFps(30.0f)
                .build();

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            camera.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera != null) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                camera.start();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera!=null) {
            camera.stop();
        }
        if (video.isPlaying()) {
            video.pause();
        }
        if(isRunning){
            timeStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
            chronometer.stop();
        }
    }
    
    private void showStatus() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(faceDetected){
                    faceDetectStatus.setImageResource(R.drawable.checkmark);
                }
                else{
                    faceDetectStatus.setImageResource(R.drawable.ic_xmark);
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera!=null) {
            camera.release();
        }
    }

    private void clickOnButton() {
       back = (Button) findViewById(R.id.back);

       back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Story.this, StoryBook.class);
                startActivity(it);
            }

        });

    }
    private void getDuration(){
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer media){
                duration = video.getDuration() / 1000;
                //Toast.makeText(getApplicationContext(), Integer.toString(duration), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void videoEndListener(){
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer media){
                chronometer.stop();
                //chronometer2.stop();
                //MIGHT COMMENT OUT GetDuration Method(),  Wait for Tyler's consent If I figure it's necessary
                //Setting duration variable here
                //storyTime = getSecondsFromChronometer(Chronometer Parameter)  MIGHT ALSO EDIT getSecondsFromChronoMeter() method but also wait for Tyler's Consent
                eyeContactTime = getSecondsFromChronometer();
                //Toast.makeText(getApplicationContext(), Integer.toString(eyeContactTime), Toast.LENGTH_LONG).show();
                calculateScores(duration, eyeContactTime);

                //Progress Tracking and Scoring section
                double totalRewardScore = 5.0 * (score / 100.0);
                dbWorker.addToRewardScore(totalRewardScore);
                dbWorker.addEyeScore(score / 100.0);
                dbWorker.addToGoalCount(2, Math.round((int)score));
            }
            /*
            @Override
            public void OnBackPressed(MediaPlayer media){
                chronometer.stop();
                chronometer2.stop;
                eyeContactTime = getSecondsFromChronometer();
                //Toast.makeText(getApplicationContext(), Integer.toString(eyeContactTime), Toast.LENGTH_LONG).show();
                calculateScores(duration, eyeContactTime);

                //Progress Tracking and Scoring section
                double totalRewardScore = 5.0 * (score / 100.0);
                dbWorker.addToRewardScore(totalRewardScore);
                dbWorker.addEyeScore(score / 100.0);
                dbWorker.addToGoalCount(2, Math.round((int)score));
            }
             */
        });
    }

    private void calculateScores(int videoDur, int contactTime){
        score = ((double)contactTime / (double) videoDur) * 100;
        String scoreStr = String.format("%.0f", score);
        String scoreMessage = "Your score is: " + scoreStr + "%";
        Toast.makeText(getApplicationContext(), scoreMessage, Toast.LENGTH_LONG).show();
    }

    private int getSecondsFromChronometer(){
        String time = chronometer.getText().toString();

        String [] hourMin = time.split(":");

        int seconds;
        int minutes;

        seconds = Integer.parseInt(hourMin[1]);
        minutes = Integer.parseInt(hourMin[0]);

        return seconds + (minutes * 60);
    }
}
