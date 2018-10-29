package com.triassi.testparsenote;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import static com.triassi.testparsenote.Notifications.CHANNEL_1_ID;

public class Queue extends AppCompatActivity {

    private View view;
    private String serviceName;
    private TextView serviceNameTextView;
    private TextView myNumberTextView;
    private TextView currentNumberTextView;
    private int myNumber;
    private int currentNumber;
    private boolean isAlreadyInQueue = false;

    private NotificationManagerCompat notificationManager;

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        serviceNameTextView = findViewById(R.id.serviceNameTextView);
        myNumberTextView = findViewById(R.id.myNumberTextView);
        currentNumberTextView = findViewById(R.id.currentNumberTextView);

        notificationManager = NotificationManagerCompat.from(this);

        if (!isAlreadyInQueue) {
            Intent intent = getIntent();
            serviceName = intent.getStringExtra("service_tapped");
            checkIfQueueHasBeenReset();
            checkTurn();
        }

        serviceNameTextView.setText(serviceName);
        myNumberTextView.setText(String.valueOf(myNumber));
    }

    @Override
    protected void onResume() {
        super.onResume();

        sharedPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        checkTurn();

        serviceNameTextView.setText(sharedPrefs.getString("serviceNameLabel", ""));
        myNumberTextView.setText(String.valueOf(sharedPrefs.getInt("myTurn", 0)));
    }

    private void checkIfQueueHasBeenReset() {
        sharedPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Service");

        query.whereEqualTo("SERVICE_NAME", serviceName);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> services, ParseException e) {
                if (e == null) {
                    if (services.size() > 0) {
                        for (ParseObject service : services) {
                            myNumber = service.getInt("MY_NUMBER");
                            currentNumber = service.getInt("CURRENT_NUMBER");
                            Log.i("SERVICE NAME LABEL", serviceName);
                            Log.i("CURRENT NUMBER", String.valueOf(currentNumber));
                            Log.i("MY NUMBER", String.valueOf(myNumber));
                            if (currentNumber == -1 && myNumber == 0) /* If the queue is closed */ {
                                onLogOutClick(view);
                            } else /* If the queue is open */ {
                                gettingNumber();
                            }
                        }
                    }
                }
            }
        });
    }

    private void checkTurn()
    {
        sharedPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run()
            {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Service");
                query.whereEqualTo("SERVICE_NAME", sharedPrefs.getString("serviceNameLabel", ""));
                query.findInBackground(new FindCallback<ParseObject>()
                {
                    public void done(List<ParseObject> services, ParseException e)
                    {
                        if (e == null)
                        {
                            if(services.size() > 0)
                            {
                                for (ParseObject service : services)
                                {
                                    currentNumber = service.getInt("CURRENT_NUMBER");

                                    if(currentNumber == sharedPrefs.getInt("myTurn", 0))
                                    {
                                        currentNumberTextView.setText("E' il tuo turno!");

                                        /* Notification popup here */
                                        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                                                .setSmallIcon(R.drawable.ic_done)
                                                .setContentTitle(sharedPrefs.getString("serviceNameLabel", ""))
                                                .setContentText("E' il tuo turno!")
                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                                .setOngoing(false)
                                                .build();

                                        notificationManager.notify(1, notification);

                                    }
                                    else if(currentNumber == sharedPrefs.getInt("myTurn", 0) + 1)
                                    {
                                        handler.removeCallbacksAndMessages(null);
                                        onLogOutClick(view);
                                        finish();
                                    }
                                    else
                                    {
                                        currentNumberTextView.setText("Il numero corrente è " + String.valueOf(currentNumber));
                                    }
                                }
                            }
                        }
                    }
                });

                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(runnable, 1000);
    }

    private void gettingNumber()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Service");
        query.whereEqualTo("SERVICE_NAME", serviceName);
        query.findInBackground(new FindCallback<ParseObject>()
        {
            public void done(List<ParseObject> services, ParseException e)
            {
                if (e == null)
                {
                    if(services.size() > 0)
                    {
                        for (ParseObject service : services)
                        {
                            service.increment("MY_NUMBER");
                            service.saveInBackground();

                            myNumber = service.getInt("MY_NUMBER");

                            sharedPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                            editor = sharedPrefs.edit();
                            editor.putInt("myTurn", myNumber);
                            isAlreadyInQueue = true;
                            editor.putBoolean("isAlreadyInQueue", isAlreadyInQueue);
                            editor.putString("serviceNameLabel", serviceName);
                            editor.apply();

                            serviceNameTextView.setText(service.getString("SERVICE_NAME"));

                            myNumberTextView.setText(String.valueOf(myNumber));

                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed()
    {

    }

    public void onLogOutClick(View view)
    {
        ParseUser.logOut();

        sharedPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        isAlreadyInQueue = false;
        editor.putBoolean("isAlreadyInQueue", isAlreadyInQueue);
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
