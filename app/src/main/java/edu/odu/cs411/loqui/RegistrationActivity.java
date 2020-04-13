package edu.odu.cs411.loqui;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private ImageView logo, joinus;
    private AutoCompleteTextView username, email, password, confirmPassword, childName;
    private Button signup;
    private TextView signin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initializeGUI();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.dismiss();

                final String inputName = username.getText().toString().trim();
                final String inputPw = password.getText().toString().trim();
                final String inputConfirmPw = confirmPassword.getText().toString().trim();
                final String inputEmail = email.getText().toString().trim();
                final String inputChildName = childName.getText().toString().trim();


                    if(validateInput(inputName, inputPw, inputConfirmPw, inputEmail, inputChildName)) {
                        registerUser(inputName, inputPw, inputEmail, inputChildName);
                    }
            }
        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });

    }


    private void initializeGUI(){

        email =  findViewById(R.id.atvEmailReg);
        username = findViewById(R.id.atvUsername);
        password = findViewById(R.id.atvPasswordReg);
        confirmPassword = findViewById(R.id.atvConfirmPasswordReg);
        childName = findViewById(R.id.atvFirstName);

        signin = findViewById(R.id.tvSignIn);
        signup = findViewById(R.id.btnSignUp);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void registerUser(final String inputName, final String inputPw, final String inputEmail, final String childName) {

        progressDialog.setMessage("Creating Your Account...");
        progressDialog.show();


            firebaseAuth.createUserWithEmailAndPassword(inputEmail,inputPw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        if (inputName.equals("ProgressTest"))
                        {
                            sendProgressUserData(inputName, inputEmail, childName);
                            Toast.makeText(RegistrationActivity.this,"You've been registered successfully.",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationActivity.this,Avatars.class));
                        }
                        else
                        {
                            sendUserData(inputName, inputEmail, childName);
                            Toast.makeText(RegistrationActivity.this,"You've been registered successfully.",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationActivity.this,Avatars.class));
                        }
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this,"Email already exists.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }


    private void sendUserData(String inputUsername, String inputEmail, String childName){

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        Map<String,Object> userData = new HashMap<>();
        userData.put("email",inputEmail);
        userData.put("username",inputUsername);
        userData.put("childFirstName",childName);
        userData.put("accountCreationDate",FieldValue.serverTimestamp());
        userData.put("rewardScore",0);
        userData.put("rewardLimit", 20);
        db.collection("users").document(inputEmail.toLowerCase())
                .set(userData);

        //This code is a template for adding score data to the DB for user in the games when they are created
        /*
        Map<String,Object> emotionData = new HashMap<>();
        emotionData.put(inputUsername + "EmotionScore",50);
        emotionData.put("EmotionScoreTimestamp",FieldValue.serverTimestamp());

        db.collection("users").document(inputUsername).collection("EmotionScores").add(emotionData);
        */
    }

    private void sendProgressUserData(String inputUsername, String inputEmail, String childName){

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        Map<String,Object> userData = new HashMap<>();
        userData.put("email",inputEmail);
        userData.put("username",inputUsername);
        userData.put("childFirstName",childName);
        userData.put("accountCreationDate",FieldValue.serverTimestamp());
        userData.put("rewardScore",0);
        userData.put("rewardLimit", 20);
        db.collection("users").document(inputEmail.toLowerCase())
                .set(userData);

        //This code is a template for adding score data to the DB for user in the games when they are created
        /*
        Map<String,Object> emotionData = new HashMap<>();
        emotionData.put(inputUsername + "EmotionScore",50);
        emotionData.put("EmotionScoreTimestamp",FieldValue.serverTimestamp());

        db.collection("users").document(inputUsername).collection("EmotionScores").add(emotionData);
        */

        for (int i = 0; i < 30; i++)
        {

            for (int j = 0; j < 5; j++)
            {
                long randomNum = 0;
                randomNum = Math.round(Math.random());
                Date date = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                Map<String,Object> emotionData = new HashMap<>();
                emotionData.put("EmotionScore",(int)randomNum);
                emotionData.put("scoreMonth",3);
                emotionData.put("scoreDay",i);
                emotionData.put("scoreYear",2020);

                db.collection("users").document(inputEmail.toLowerCase())
                        .collection("AprilEmotionScores")
                        .add(emotionData);
            }
        }

    }

    private boolean validateInput(String inName, String inPw, String confirmPw, String inEmail, String inChildName){

        if(inName.isEmpty()){
            username.setError("Username is empty.");
            return false;
        }

        if(inPw.equals(confirmPw) == false)
        {
            confirmPassword.setError("Passwords must be the same in both password fields.");
            return false;
        }

        if(validatePassword(inPw) == false)
        {
            return false;
        }

        if(inEmail.isEmpty()){
            email.setError("Email is empty.");
            return false;
        }

        if(inChildName.isEmpty())
        {
            childName.setError("First name is empty.");
            return false;
        }

        return true;
    }

    private boolean validatePassword(String passIn)
    {
        boolean upperFlag = false;
        boolean lowerFlag = false;
        boolean numberFlag = false;

        int asciiVal = 0;

        if(passIn.isEmpty()){
            password.setError("Password is empty.");
            return false;
        }

        if (passIn.length() > 20 || passIn.length() < 8)
        {
            password.setError("Password must be between 8 and 20 characters long.");
            return false;
        }

        for (int i = 0; i < passIn.length(); i++)
        {
            asciiVal = passIn.charAt(i);

            if (asciiVal >= 97 && asciiVal <= 122)
            {
                lowerFlag = true;
            }
            else if (asciiVal >= 65 && asciiVal <= 90)
            {
                upperFlag = true;
            }
            else if (asciiVal >= 48 && asciiVal <= 57)
            {
                numberFlag = true;
            }
        }

        if (upperFlag == false)
        {
            password.setError("Password must contain at least 1 uppercase letter.");
            return false;
        }
        else if (lowerFlag == false)
        {
            password.setError("Password must contain at least 1 lowercase letter.");
            return false;
        }
        else if (numberFlag == false)
        {
            password.setError("Password must contain at least 1 number.");
            return false;
        }

        return true;
    }


}
