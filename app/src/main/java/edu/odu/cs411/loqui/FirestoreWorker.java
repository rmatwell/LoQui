package edu.odu.cs411.loqui;


import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.SetOptions;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirestoreWorker
{
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userID;
    FirebaseFirestore db;
    String TAG = "Firestore Worker";
    static boolean rewardFlag;
    static double rewardScore;
    static int avatarNumber;

    FirestoreWorker()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        userID = user.getEmail();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    public void checkForReward()
    {
        DocumentReference userRef = db.collection("users").document(userID);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot userData = task.getResult();

                    if(userData.exists())
                    {
                        Log.d(TAG, "DocumentSnapshot data: " + userData.getData());
                    }
                    else {
                        Log.d(TAG, "No such document userID = " + userID);
                    }
                    double dbScore = userData.getDouble("rewardScore");

                    if (dbScore >= 20)
                    {
                        rewardFlag = true;
                    }
                    else
                    {
                        rewardFlag = false;
                    }
                }
                else
                {
                    Log.d(TAG, "get fail with ", task.getException());
                }
            }
        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    public void addToRewardScore(int amount)
    {
        DocumentReference userRef = db.collection("users").document(userID);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot userData = task.getResult();

                    if(userData.exists())
                    {
                        Log.d(TAG, "DocumentSnapshot data: " + userData.getData());
                    }
                    else {
                        Log.d(TAG, "No such document userID = " + userID);
                    }
                    checkForReward();

                    if(rewardFlag == true)
                    {
                        //do not increment over 20
                    }
                    else
                    {
                        double dbScore = userData.getDouble("rewardScore");

                        //If the amount to be added would make rewardScore 20 or more
                        if(dbScore + amount >= 20)
                        {
                            double difference = (dbScore + amount) - 20;
                            dbScore = dbScore + difference;
                            userRef.update("rewardScore", dbScore);
                        }
                        else
                        {
                            dbScore = dbScore + amount;
                            userRef.update("rewardScore", dbScore);
                        }
                    }
                }
                else
                {
                    Log.d(TAG, "get fail with ", task.getException());
                }
            }
        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    public int getRewardScore()
    {
        DocumentReference userRef = db.collection("users").document(userID);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userData = task.getResult();

                    if (userData.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + userData.getData());
                    } else {
                        Log.d(TAG, "No such document userID = " + userID);
                    }
                    double dbScore = userData.getDouble("rewardScore");
                    rewardScore = dbScore;
                } else {
                    Log.d(TAG, "get fail with ", task.getException());
                }
            }
        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        return (int) rewardScore;
    }

    public void addEmotionScore(int emotionScore)
    {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Map<String,Object> emotionData = new HashMap<>();
        emotionData.put("EmotionScore",emotionScore);
        emotionData.put("scoreMonth",month);
        emotionData.put("scoreDay",day);
        emotionData.put("scoreYear",year);

        db.collection("users").document(userID).collection("EmotionScores").add(emotionData);
    }

    public void addSpeechScore(int speechScore)
    {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Map<String,Object> emotionData = new HashMap<>();
        emotionData.put("EmotionScore",speechScore);
        emotionData.put("scoreMonth",month);
        emotionData.put("scoreDay",day);
        emotionData.put("scoreYear",year);

        db.collection("users").document(userID).collection("SpeechScores").add(emotionData);
    }

    public void addEyeScore(double contactPercent)
    {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Map<String,Object> emotionData = new HashMap<>();
        emotionData.put("EmotionScore",contactPercent);
        emotionData.put("scoreMonth",month);
        emotionData.put("scoreDay",day);
        emotionData.put("scoreYear",year);

        db.collection("users").document(userID).collection("EyeContactScores").add(emotionData);
    }

    public void setAvatar(int avatar)
    {
        Map<String,Object> data = new HashMap<>();
        data.put("Avatar",avatar);

        db.collection("users").document(userID).set(data, SetOptions.merge());
    }
    public int getAvatar()
    {
        DocumentReference userRef = db.collection("users").document(userID);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userData = task.getResult();

                    if (userData.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + userData.getData());
                    } else {
                        Log.d(TAG, "No such document userID = " + userID);
                    }
                    double dbAvatar = userData.getDouble("Avatar");
                    avatarNumber = (int)dbAvatar;
                } else {
                    Log.d(TAG, "get fail with ", task.getException());
                }
            }
        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        return avatarNumber;
    }
}
