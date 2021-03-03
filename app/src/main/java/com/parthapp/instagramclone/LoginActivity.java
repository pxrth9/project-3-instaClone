package com.parthapp.instagramclone;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity  extends AppCompatActivity {


    public static final String TAG = "LoginActivity";
    private EditText userName;
    private EditText userPassword;
    private Button loginBtn;
    private Button signupBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "gets here");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

        userName = findViewById(R.id.userName);
        userPassword = findViewById(R.id.userPassword);
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupBtn);

        //SET BACKGROUND COLORS
        loginBtn.setBackgroundColor(Color.rgb(253,232,182));
        signupBtn.setBackgroundColor(Color.rgb(0,251,0));



        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uN = userName.getText().toString();
                String pW = userPassword.getText().toString();
                makeNewUser(uN, pW);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick login button");
                String uN = userName.getText().toString();
                String pW = userPassword.getText().toString();
                loginUser(uN, pW);
            }
        });

    }

    private void makeNewUser(String uN, String pW) {
        ParseUser user = new ParseUser();
        user.setUsername(uN);
        user.setPassword(pW);
        user.signUpInBackground(new SignUpCallback(){

            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Toast.makeText(LoginActivity.this, "Error, user exists, or invalid input", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(LoginActivity.this, "Successfully Signed Up, Please Log-in", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String uN, String pW) {
        Log.i(TAG, "Attempting to Login" + uN);
        //TODO: naviagte to the main activity if the credentials are correct
        ParseUser.logInInBackground(uN, pW, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(LoginActivity.this, "Issue with login, or user doesn't exist", Toast.LENGTH_SHORT).show();
                    return;
                }
                //NAVIGATE TO MAIN ACTIVITY
                goMainActivity();
                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
