package edu.odu.cs411.loqui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Emotion;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.VerifyResult;
import com.microsoft.projectoxford.face.rest.ClientException;

import com.microsoft.projectoxford.face.*;
import com.microsoft.projectoxford.face.contract.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.*;
import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.graphics.*;
import android.widget.*;
import android.provider.*;


public class EmotionRecogTestClass extends AppCompatActivity {

    private Button process,switch_picture;
    private ImageView filler;
    private Bitmap bitmap;

    private final int PICK_IMAGE = 1;
    private ProgressDialog detectionProgressDialog;
    private final String apiKey = "af26f2c69b9746448828141ef152aeba";
    private final String apiEndpoint = "https://westus.api.cognitive.microsoft.com/face/v1.0/";
    private final FaceServiceClient faceServiceClient = new FaceServiceRestClient(apiEndpoint,apiKey);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emotionrecog_test);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){e.printStackTrace();}
        switch_picture = findViewById(R.id.switch_image);
        process = findViewById(R.id.process);
        filler = findViewById(R.id.emo_filler);
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sad_baby);
        filler.setImageBitmap(bitmap);
        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectAndFrame(bitmap);
            }
        });

        switch_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.happy_anaya);
                filler.setImageBitmap(bitmap);
            }
        });

        detectionProgressDialog = new ProgressDialog(this);
    }

//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK &&
//                data != null && data.getData() != null) {
//            Uri uri = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
//                        getContentResolver(), uri);
//                ImageView imageView = findViewById(R.id.emo_filler);
//                imageView.setImageBitmap(bitmap);
//
//                // Comment out for tutorial
//                detectAndFrame(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private void detectAndFrame(final Bitmap imageBitmap) {

        filler = (ImageView) findViewById(R.id.emo_filler);
        filler.setImageBitmap(imageBitmap);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG,100, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream((outputStream.toByteArray()));

        final FaceServiceClient.FaceAttributeType[] requiredFaceAttributes = new FaceServiceClient.FaceAttributeType[1];
        requiredFaceAttributes[0] = FaceServiceClient.FaceAttributeType.Emotion;

        AsyncTask<InputStream,String, Face[]> detectTask =  new AsyncTask<InputStream, String, Face[]>() {

            String exceptionMessage = "";
            @Override
            protected Face[] doInBackground(InputStream... inputStreams) {

                try {
                    publishProgress("Evaluating...");
                    Face[] result = faceServiceClient.detect(
                            inputStreams[0],
                            true,         // returnFaceId
                            false,        // returnFaceLandmarks
                            requiredFaceAttributes          // returnFaceAttributes:
                                /* new FaceServiceClient.FaceAttributeType[] {
                                    FaceServiceClient.FaceAttributeType.Age,
                                    FaceServiceClient.FaceAttributeType.Gender }
                                */
                    );
                    if (result == null) {
                        publishProgress("Evaluation Finished, sadly nothing detected");
                        return null;
                    }
                    publishProgress(String.format("Detection finished. %d face(s) detected", result.length));
                    return result;


                }
                catch (Exception e) {
                    exceptionMessage = String.format(
                            "Detection failed: %s", e.getMessage());
                    return null;

                }

            }

            @Override
            protected void onPreExecute() {
                //TODO: show progress dialog
                detectionProgressDialog.show();
            }
            @Override
            protected void onProgressUpdate(String... progress) {
                //TODO: update progress
                detectionProgressDialog.setMessage(progress[0]);
            }
            @Override
            protected void onPostExecute(Face[] result) {
                //TODO: update face frames
                detectionProgressDialog.dismiss();

                if(!exceptionMessage.equals("")){
                    showError(exceptionMessage);
                }
                if (result == null) return;

                String emotion = getEmotion(result);
                ImageView imageView = findViewById(R.id.emo_filler);
                imageView.setImageBitmap(
                        drawFaceRectanglesOnBitmap(imageBitmap, result,emotion));
                imageBitmap.recycle();
                Toast.makeText(EmotionRecogTestClass.this,emotion, Toast.LENGTH_SHORT).show();
            }
        };
        detectTask.execute(inputStream);
    }





    private void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }})
                .create().show();
    }

    private static Bitmap drawFaceRectanglesOnBitmap(
            Bitmap originalBitmap, Face[] faces,String emotion) {
        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        if (faces != null) {
            for (Face face : faces) {
                FaceRectangle faceRectangle = face.faceRectangle;
                canvas.drawRect(
                        faceRectangle.left,
                        faceRectangle.top,
                        faceRectangle.left + faceRectangle.width,
                        faceRectangle.top + faceRectangle.height,
                        paint);
            }
        }

        return bitmap;
    }

    private static String getEmotion(Face[] v_res) {

        FaceAttribute faceAttributes = v_res[0].faceAttributes;
        Emotion cur_face_emotion = faceAttributes.emotion;

        List<Double> emo_scores = new ArrayList<>();
        emo_scores.add(cur_face_emotion.sadness);
        emo_scores.add(cur_face_emotion.happiness);
        emo_scores.add(cur_face_emotion.anger);
        emo_scores.add(cur_face_emotion.surprise);
        emo_scores.add(cur_face_emotion.fear);
        emo_scores.add(cur_face_emotion.neutral);
        emo_scores.add(cur_face_emotion.disgust);
        emo_scores.add(cur_face_emotion.contempt);

        Collections.sort(emo_scores);
        double expressed_emotion = emo_scores.get(emo_scores.size() - 1);

        if (expressed_emotion == cur_face_emotion.sadness) {
            return "Sadness";
        } else if (expressed_emotion == cur_face_emotion.happiness) {
            return "Happy";
        } else if (expressed_emotion == cur_face_emotion.anger) {
            return "Angry";
        } else if (expressed_emotion == cur_face_emotion.surprise) {
            return "Surprised";
        } else if (expressed_emotion == cur_face_emotion.fear) {
            return "Scared";
        } else if (expressed_emotion == cur_face_emotion.disgust) {
            return "Disgusted";
        } else if (expressed_emotion == cur_face_emotion.contempt) {
            return "Upset";
        } else if (expressed_emotion == cur_face_emotion.neutral) {
            return "No emotion, neutral!";
        }
        return "Could not determine emotion";
    }

}
