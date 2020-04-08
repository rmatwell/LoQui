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

public class FirestoreWorker
{
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userID;
    FirebaseFirestore db;
    String TAG = "Firestore Worker";
    static boolean rewardFlag;

    FirestoreWorker()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        userID = user.getEmail();
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
}
