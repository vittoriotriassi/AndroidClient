package com.triassi.testparsenote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    TextView signUpTextView;
    TextView logInTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        logInTextView = (TextView) findViewById(R.id.logInTextView);

        ParseUser currentUser = ParseUser.getCurrentUser();

        SharedPreferences sharedPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        boolean isAlreadyInQueue = sharedPrefs.getBoolean("isAlreadyInQueue", false);


        if(currentUser != null && !isAlreadyInQueue)
        {
            Intent intent = new Intent(getApplicationContext(), AfterLogIn.class);
            intent.putExtra("username", currentUser.getUsername());
            startActivity(intent);
        }
        else if(currentUser != null && isAlreadyInQueue)
        {
            Intent intent = new Intent(getApplicationContext(), Queue.class);
            startActivity(intent);
        }
    }

    public void onSignUpClick(View view)
    {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void onLogInClick(View view)
    {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }
}
