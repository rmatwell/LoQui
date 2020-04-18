package edu.odu.cs411.loqui;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class FirestoreWorker
{
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userID;
    FirebaseFirestore db;
    String TAG = "Firestore Worker";
    String TAGreward = "Reward";
    static boolean rewardFlag;
    static double rewardScore;
    static int avatarNumber, numScores, numCorrect;

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
                            //double difference = (dbScore + amount) - 20;
                            //dbScore = dbScore + difference;
                            userRef.update("rewardScore", 20);
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

    public int getRewardScore(Context context)
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

                    if (rewardScore >= 20)
                    {
                        Goals reward = new Goals();
                        reward.Reward(context);
                        userRef.update("rewardScore",0);
                    }
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

        db.collection("users").document(userID)
                .collection(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + "EmotionScores")
                .add(emotionData);
    }

    public void addEmotionScore(int emotionScore, int day, int month, int year)
    {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Map<String,Object> emotionData = new HashMap<>();
        emotionData.put("EmotionScore",emotionScore);
        emotionData.put("scoreMonth",month);
        emotionData.put("scoreDay",day);
        emotionData.put("scoreYear",year);

        db.collection("users").document(userID)
                .collection(cal.getDisplayName(month, Calendar.LONG, Locale.ENGLISH) + "EmotionScores")
                .add(emotionData);
    }

    public void addSpeechScore(int speechScore)
    {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Map<String,Object> speechData = new HashMap<>();
        speechData.put("SpeechScore",speechScore);
        speechData.put("scoreMonth",month);
        speechData.put("scoreDay",day);
        speechData.put("scoreYear",year);

        db.collection("users").document(userID)
                .collection(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + "SpeechScores")
                .add(speechData);
    }

    public void addEyeScore(double contactPercent)
    {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Map<String,Object> eyeData = new HashMap<>();
        eyeData.put("EyeContactScore",contactPercent);
        eyeData.put("scoreMonth",month);
        eyeData.put("scoreDay",day);
        eyeData.put("scoreYear",year);

        db.collection("users").document(userID)
                .collection(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + "EyeContactScores")
                .add(eyeData);
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

    public int getNumberOfScores(String month)
    {
        numScores = 0;

        db.collection("users").document(userID).collection(month + "EmotionScores")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                numScores++;
                            }

                            Log.d(TAG, "Total number of scores in " + month + ": " + numScores);


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return numScores;
    }

    public int getNumberOfCorrectScores(String month)
    {
        numCorrect = 0;

        db.collection("users").document(userID).collection(month + "EmotionScores")
                .whereEqualTo("EmotionScore", 1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                numCorrect++;
                            }

                            Log.d(TAG, "Total number of correct scores in " + month + ": " + numCorrect);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



        return numCorrect;
    }

    public void getMonthlyScores(MonthlyReport monthlyScores, String month, int gameType)
    {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        switch (gameType)
        {
            case 1:
                for (int i = 0; i < daysInMonth; i++)
                {
                    Log.d(TAG, "getMonthlyScore i = " + i);
                    DailyReport newReport = new DailyReport();
                    setDailyEmotionReport(newReport, month, i);
                    monthlyScores.monthlyReport.add(newReport);
                }
                break;

            case 2:
                for (int i = 0; i < daysInMonth; i++)
                {
                    Log.d(TAG, "getMonthlyScore i = " + i);
                    DailyReport newReport = new DailyReport();
                    setDailySpeechReport(newReport, month, i);
                    monthlyScores.monthlyReport.add(newReport);
                }
                break;

            case 3:
                for (int i = 0; i < daysInMonth; i++)
                {
                    Log.d(TAG, "getMonthlyScore i = " + i);
                    DailyReport newReport = new DailyReport();
                    setDailyEyeReport(newReport, month, i);
                    monthlyScores.monthlyReport.add(newReport);
                }
                break;
        }

    }

    public void setDailyEmotionReport (DailyReport newReport, String month, int day)
    {
        db.collection("users").document(userID).collection(month + "EmotionScores")
                .whereEqualTo("scoreDay", day)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                DailyData newData = new DailyData();
                                double emotionScore = document.getDouble("EmotionScore");
                                double scoreDay = document.getDouble("scoreDay");
                                double scoreMonth = document.getDouble("scoreMonth");
                                double scoreYear = document.getDouble("scoreYear");
                                newData.setScore((int)emotionScore);
                                newData.setScoreDay((int)scoreDay);
                                newData.setScoreMonth((int)scoreMonth);
                                newData.setScoreYear((int)scoreYear);
                                Log.d(TAG, "newData = " + newData.getScore() + " " + newData.getScoreDay());
                                newReport.dailyList.add(newData);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void setDailySpeechReport (DailyReport newReport, String month, int day)
    {
        db.collection("users").document(userID).collection(month + "SpeechScores")
                .whereEqualTo("scoreDay", day)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                DailyData newData = new DailyData();
                                double speechScore = document.getDouble("SpeechScore");
                                double scoreDay = document.getDouble("scoreDay");
                                double scoreMonth = document.getDouble("scoreMonth");
                                double scoreYear = document.getDouble("scoreYear");
                                newData.setScore(speechScore);
                                newData.setScoreDay((int)scoreDay);
                                newData.setScoreMonth((int)scoreMonth);
                                newData.setScoreYear((int)scoreYear);
                                Log.d(TAG, "newData = " + newData.getScore() + " " + newData.getScoreDay());
                                newReport.dailyList.add(newData);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void setDailyEyeReport (DailyReport newReport, String month, int day)
    {
        db.collection("users").document(userID).collection(month + "EyeContactScores")
                .whereEqualTo("scoreDay", day)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                DailyData newData = new DailyData();
                                double eyeScore = document.getDouble("EyeContactScore");
                                double scoreDay = document.getDouble("scoreDay");
                                double scoreMonth = document.getDouble("scoreMonth");
                                double scoreYear = document.getDouble("scoreYear");
                                newData.setScore(eyeScore);
                                newData.setScoreDay((int)scoreDay);
                                newData.setScoreMonth((int)scoreMonth);
                                newData.setScoreYear((int)scoreYear);
                                Log.d(TAG, "newData = " + newData.getScore() + " " + newData.getScoreDay());
                                newReport.dailyList.add(newData);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void addGoal(int game, int goal, int amount, int time, long timestamp)
    {
        Map<String,Object> goalData = new HashMap<>();
        goalData.put("goal",goal);
        goalData.put("game",game);
        goalData.put("time",time);
        goalData.put("amount",amount);
        goalData.put("timestamp",timestamp);
        goalData.put("count",0);
        goalData.put("countw",0);

        db.collection("users").document(userID)
                .collection("Goals")
                .add(goalData);
    }

    public Goals getOverallCorrectGoals()
    {
        Log.d("TAG", "Entering getOverallCorrectGoals");

        Goals goalList = new Goals();

        db.collection("users").document(userID).collection("Goals")
                .whereEqualTo("goal", 0)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "getOverallCorrectGoals" + document.getId() + " => " + document.getData());
                                Goals newGoal = new Goals();
                                double goal = document.getDouble("goal");
                                double game = document.getDouble("game");
                                double time = document.getDouble("time");
                                double amount = document.getDouble("amount");
                                double timestamp = document.getDouble("timestamp");
                                double count = document.getDouble("count");
                                double countw = document.getDouble("countw");
                                String goalID = document.getId();
                                newGoal.goal = (int)goal;
                                newGoal.game = (int)game;
                                newGoal.time = (int)time;
                                newGoal.amount = (int)amount;
                                newGoal.timestamp = (long)timestamp;
                                newGoal.count = (int)count;
                                newGoal.countw = (int)countw;
                                newGoal.goalID = goalID;
                                Log.d(TAG, "newGoal = " + newGoal.goal + " " + newGoal.game + " " +
                                        newGoal.time + " " + newGoal.amount + " " + newGoal.timestamp);
                                goalList.goals.add(newGoal);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return goalList;
    }

    public Goals getStreakCorrectGoals()
    {
        Goals goalList = new Goals();

        db.collection("users").document(userID).collection("Goals")
                .whereEqualTo("goal", 1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Goals newGoal = new Goals();
                                double goal = document.getDouble("goal");
                                double game = document.getDouble("game");
                                double time = document.getDouble("time");
                                double amount = document.getDouble("amount");
                                double timestamp = document.getDouble("timestamp");
                                double count = document.getDouble("count");
                                double countw = document.getDouble("countw");
                                String goalID = document.getId();
                                newGoal.goal = (int)goal;
                                newGoal.game = (int)game;
                                newGoal.time = (int)time;
                                newGoal.amount = (int)amount;
                                newGoal.timestamp = (long)timestamp;
                                newGoal.count = (int)count;
                                newGoal.countw = (int)countw;
                                newGoal.goalID = goalID;
                                Log.d(TAG, "newGoal = " + newGoal.goal + " " + newGoal.game + " " +
                                        newGoal.time + " " + newGoal.amount + " " + newGoal.timestamp);
                                goalList.goals.add(newGoal);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return goalList;
    }

    public Goals getTimeCorrectGoals()
    {
        Goals goalList = new Goals();

        db.collection("users").document(userID).collection("Goals")
                .whereEqualTo("goal", 2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Goals newGoal = new Goals();
                                double goal = document.getDouble("goal");
                                double game = document.getDouble("game");
                                double time = document.getDouble("time");
                                double amount = document.getDouble("amount");
                                double timestamp = document.getDouble("timestamp");
                                double count = document.getDouble("count");
                                double countw = document.getDouble("countw");
                                String goalID = document.getId();
                                newGoal.goal = (int)goal;
                                newGoal.game = (int)game;
                                newGoal.time = (int)time;
                                newGoal.amount = (int)amount;
                                newGoal.timestamp = (long)timestamp;
                                newGoal.count = (int)count;
                                newGoal.countw = (int)countw;
                                newGoal.goalID = goalID;
                                Log.d(TAG, "newGoal = " + newGoal.goal + " " + newGoal.game + " " +
                                        newGoal.time + " " + newGoal.amount + " " + newGoal.timestamp);
                                goalList.goals.add(newGoal);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return goalList;
    }

    public Goals getTimePercentGoals()
    {
        Goals goalList = new Goals();

        db.collection("users").document(userID).collection("Goals")
                .whereEqualTo("goal", 3)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Goals newGoal = new Goals();
                                double goal = document.getDouble("goal");
                                double game = document.getDouble("game");
                                double time = document.getDouble("time");
                                double amount = document.getDouble("amount");
                                double timestamp = document.getDouble("timestamp");
                                double count = document.getDouble("count");
                                double countw = document.getDouble("countw");
                                String goalID = document.getId();
                                newGoal.goal = (int)goal;
                                newGoal.game = (int)game;
                                newGoal.time = (int)time;
                                newGoal.amount = (int)amount;
                                newGoal.timestamp = (long)timestamp;
                                newGoal.count = (int)count;
                                newGoal.countw = (int)countw;
                                newGoal.goalID = goalID;
                                Log.d(TAG, "newGoal = " + newGoal.goal + " " + newGoal.game + " " +
                                        newGoal.time + " " + newGoal.amount + " " + newGoal.timestamp);
                                goalList.goals.add(newGoal);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return goalList;
    }

    public Goals getAllGoals()
    {
        Goals goalList = new Goals();

        db.collection("users").document(userID).collection("Goals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Goals newGoal = new Goals();
                                double goal = document.getDouble("goal");
                                double game = document.getDouble("game");
                                double time = document.getDouble("time");
                                double amount = document.getDouble("amount");
                                double timestamp = document.getDouble("timestamp");
                                double count = document.getDouble("count");
                                double countw = document.getDouble("countw");
                                String goalID = document.getId();
                                newGoal.goal = (int)goal;
                                newGoal.game = (int)game;
                                newGoal.time = (int)time;
                                newGoal.amount = (int)amount;
                                newGoal.timestamp = (long)timestamp;
                                newGoal.count = (int)count;
                                newGoal.countw = (int)countw;
                                newGoal.goalID = goalID;
                                Log.d(TAG, "newGoal = " + newGoal.goal + " " + newGoal.game + " " +
                                        newGoal.time + " " + newGoal.amount + " " + newGoal.timestamp);
                                goalList.goals.add(newGoal);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return goalList;
    }

    public Goals getEmotionGoals()
    {
        Goals goalList = new Goals();

        db.collection("users").document(userID).collection("Goals")
                .whereEqualTo("game", 0)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Goals newGoal = new Goals();
                                double goal = document.getDouble("goal");
                                double game = document.getDouble("game");
                                double time = document.getDouble("time");
                                double amount = document.getDouble("amount");
                                double timestamp = document.getDouble("timestamp");
                                double count = document.getDouble("count");
                                double countw = document.getDouble("countw");
                                String goalID = document.getId();
                                newGoal.goal = (int)goal;
                                newGoal.game = (int)game;
                                newGoal.time = (int)time;
                                newGoal.amount = (int)amount;
                                newGoal.timestamp = (long)timestamp;
                                newGoal.count = (int)count;
                                newGoal.countw = (int)countw;
                                newGoal.goalID = goalID;
                                Log.d(TAG, "newGoal = " + newGoal.goal + " " + newGoal.game + " " +
                                        newGoal.time + " " + newGoal.amount + " " + newGoal.timestamp);
                                goalList.goals.add(newGoal);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return goalList;
    }

    public Goals getSpeechGoals()
    {
        Goals goalList = new Goals();

        db.collection("users").document(userID).collection("Goals")
                .whereEqualTo("game", 1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Goals newGoal = new Goals();
                                double goal = document.getDouble("goal");
                                double game = document.getDouble("game");
                                double time = document.getDouble("time");
                                double amount = document.getDouble("amount");
                                double timestamp = document.getDouble("timestamp");
                                double count = document.getDouble("count");
                                double countw = document.getDouble("countw");
                                String goalID = document.getId();
                                newGoal.goal = (int)goal;
                                newGoal.game = (int)game;
                                newGoal.time = (int)time;
                                newGoal.amount = (int)amount;
                                newGoal.timestamp = (long)timestamp;
                                newGoal.count = (int)count;
                                newGoal.countw = (int)countw;
                                newGoal.goalID = goalID;
                                Log.d(TAG, "newGoal = " + newGoal.goal + " " + newGoal.game + " " +
                                        newGoal.time + " " + newGoal.amount + " " + newGoal.timestamp);
                                goalList.goals.add(newGoal);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return goalList;
    }

    public Goals getEyeGoals()
    {
        Goals goalList = new Goals();

        db.collection("users").document(userID).collection("Goals")
                .whereEqualTo("game", 2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Goals newGoal = new Goals();
                                double goal = document.getDouble("goal");
                                double game = document.getDouble("game");
                                double time = document.getDouble("time");
                                double amount = document.getDouble("amount");
                                double timestamp = document.getDouble("timestamp");
                                double count = document.getDouble("count");
                                double countw = document.getDouble("countw");
                                String goalID = document.getId();
                                newGoal.goal = (int)goal;
                                newGoal.game = (int)game;
                                newGoal.time = (int)time;
                                newGoal.amount = (int)amount;
                                newGoal.timestamp = (long)timestamp;
                                newGoal.count = (int)count;
                                newGoal.countw = (int)countw;
                                newGoal.goalID = goalID;
                                Log.d(TAG, "newGoal = " + newGoal.goal + " " + newGoal.game + " " +
                                        newGoal.time + " " + newGoal.amount + " " + newGoal.timestamp);
                                goalList.goals.add(newGoal);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return goalList;
    }

    public void removeGoal(String goalID)
    {
        Log.d("removeGoal", "Entering removeGoal");

        db.collection("users").document(userID).collection("Goals").document(goalID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public void addToGoalCount(String goalID, int correctAnswer)
    {
        DocumentReference userRef = db.collection("users").document(userID).collection("Goals").document(goalID);

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

                    if (correctAnswer == 0)
                    {
                        double counter = userData.getDouble("countw");
                        counter++;
                        userRef.update("countw", (int)counter);
                    }
                    else if (correctAnswer == 1)
                    {
                        double counter = userData.getDouble("count");
                        counter++;
                        userRef.update("count", (int)counter);
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
}
