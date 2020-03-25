package edu.odu.cs411.loqui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.*;
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
                final String inputEmail = email.getText().toString().trim();
                final String inputChildName = childName.getText().toString().trim();


                    if(validateInput(inputName, inputPw, inputEmail, inputChildName)) {
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

    private void registerUser(final String inputName, final String inputPw, String inputEmail, String childName) {

        progressDialog.setMessage("Creating Your Account...");
        progressDialog.show();


            firebaseAuth.createUserWithEmailAndPassword(inputEmail,inputPw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserData(inputName, inputEmail, childName);
                        Toast.makeText(RegistrationActivity.this,"You've been registered successfully.",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrationActivity.this,Avatar.class));
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

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userID = user.getUid();

        Map<String,Object> userData = new HashMap<>();
        userData.put("email",inputEmail);
        userData.put("username",inputUsername);
        userData.put("childFirstName",childName);
        userData.put("accountCreationDate",FieldValue.serverTimestamp());
        db.collection("users").document(userID)
                .set(userData);

        //This code is a template for adding score data to the DB for user in the games when they are created
        /*
        Map<String,Object> emotionData = new HashMap<>();
        emotionData.put(inputUsername + "EmotionScore",50);
        emotionData.put("EmotionScoreTimestamp",FieldValue.serverTimestamp());

        db.collection("users").document(inputUsername).collection("EmotionScores").add(emotionData);
        */
    }

    private boolean validateInput(String inName, String inPw, String inEmail, String inChildName){

        if(inName.isEmpty()){
            username.setError("Username is empty.");
            return false;
        }

        validatePassword(inPw);

        if(inEmail.isEmpty()){
            email.setError("Email is empty.");
            return false;
        }

        if(inChildName.isEmpty())
        {
            childName.setError("First name is empty.");
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
        }
        else if (numberFlag == false)
        {
            password.setError("Password must contain at least 1 number.");
        }

        return true;
    }


}
