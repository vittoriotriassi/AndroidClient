package com.triassi.testparsenote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

public class AfterLogIn extends AppCompatActivity {

    TextView loggedUserCredentialsTextView;
    Button goToTheAppButton;
    Button logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_log_in);

        loggedUserCredentialsTextView = findViewById(R.id.loggedUserCredentialsTextView);
        goToTheAppButton = findViewById(R.id.goToTheAppButton);
        logOutButton = findViewById(R.id.logOutButton);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        loggedUserCredentialsTextView.setText(username);
    }

    public void onGoToTheAppClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(), UserLocation.class);
        startActivity(intent);
        finish();
    }

    public void onLogOutClick(View view)
    {
        ParseUser.logOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
