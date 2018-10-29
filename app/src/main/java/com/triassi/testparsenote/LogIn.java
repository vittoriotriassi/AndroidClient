package com.triassi.testparsenote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LogIn extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
    }

    public void onLogInClick(View view)
    {
        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();

        Log.i("Username content", username);

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user != null)
                {
                    Log.i("LogIn Info", "The user is logged in.");
                    Toast.makeText(getApplicationContext(), "Accesso effettuato con successo!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), AfterLogIn.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "I dati inseriti non sono corretti!", Toast.LENGTH_SHORT).show();
                    Log.i("LogIn Info", "The user is NOT logged in.");
                }
            }
        });
    }
}
