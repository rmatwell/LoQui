package edu.odu.cs411.loqui;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
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
    TextView textView;
    boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_story);
        getWindow().setFormat(PixelFormat.UNKNOWN);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            Toast.makeText(this, "Grant Permission and restart app", Toast.LENGTH_SHORT).show();
        }
        else {
            video = (VideoView) findViewById(R.id.videoView);
            chronometer = findViewById(R.id.chronometer);
            String path = "android.resource://edu.odu.cs411.loqui/" + R.raw.threelilpigs;
            Uri uri = Uri.parse(path);
            video.setVideoURI(uri);
            video.requestFocus();
            video.start();
            chronometer.start();
            isRunning = true;
            createCameraSource();
        }
        clickOnButton();
    }

    //EyeContactTracker Class uses the Google Vision API to detect eye contact.
    private class EyeContactTracker extends Tracker<Face>{
        private final float THRESHOLD = 15.0f;

        private EyeContactTracker(){}

        @Override
        public void onUpdate(Detector.Detections<Face> detections, Face face){
            if(face.getEulerY() > THRESHOLD || face.getEulerY() < -THRESHOLD){
                showStatus("Eye Contact Not Detected");
                if(isRunning){
                    chronometer.stop();
                }
            }
            else{
                showStatus("Eye Contact Detected");
                if(!isRunning){
                    chronometer.start();
                }
            }
        }

        @Override
        public void onMissing(Detector.Detections<Face> detections){
            super.onMissing(detections);
            showStatus("No face deteceted");
            if(isRunning){
                chronometer.stop();
            }
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
    }
    
    private void showStatus(final String status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(status);
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

}
