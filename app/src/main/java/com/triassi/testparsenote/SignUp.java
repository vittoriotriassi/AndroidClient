package com.triassi.testparsenote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
    }

    public void onSignUpClick(View view)
    {
        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();

        Log.i("Username content", username);
        Log.i("Password content", password);

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null)
                {
                    Log.i("Success Info", "Let's use the app now");
                    Toast.makeText(getApplicationContext(), "Registrazione effettuata con successo!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), AfterSignUp.class);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Registrazione fallita!", Toast.LENGTH_SHORT).show();
                    Log.i("Fail Info", "SignUp failed!");
                }
            }
        });
    }
}
