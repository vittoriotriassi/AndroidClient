package com.triassi.testparsenote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AfterSignUp extends AppCompatActivity {

    TextView newUserTextView;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_sign_up);

        newUserTextView = findViewById(R.id.newUserTextView);

        Intent intent = getIntent();

        username = intent.getStringExtra("username");

        newUserTextView.setText(username);
    }

    public void onTornaAllaHomeButtonClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
