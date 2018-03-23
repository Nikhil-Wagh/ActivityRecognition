package com.example.nikhil.activityrecognition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.CalendarView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class logActivity extends AppCompatActivity {

    public static final String ACTION_LOCATION_BROADCAST = ActivityRecognizedService.class.getName() + "locationBroadcast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        final TableLayout tableLayout = findViewById(R.id.table1);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String activity;
                        activity = intent.getStringExtra(ActivityRecognizedService.UPDATED_ACTIVITY);
                        if (activity != null && activity.length() > 0){
                            Toast.makeText(logActivity.this, activity, Toast.LENGTH_LONG).show();

                            TableRow tableRow = new TableRow(getApplicationContext());
                            tableRow.setGravity(Gravity.CENTER_HORIZONTAL);

                            TextView timestampTV = new TextView(getApplicationContext());
                            timestampTV.setText(new Timestamp(System.currentTimeMillis()).toString());
                            timestampTV.setGravity(Gravity.CENTER_HORIZONTAL);
                            TextView activityTV = new TextView(getApplicationContext());
                            activityTV.setText(activity);
                            activityTV.setGravity(Gravity.CENTER_HORIZONTAL);

                            tableRow.addView(timestampTV);
                            tableRow.addView(activityTV);

                            tableLayout.addView(tableRow);
                        }
                    }
                }, new IntentFilter(ActivityRecognizedService.ACTION_LOCATION_BROADCAST)
        );
    }

    private String getCurrentTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = (Date) cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss a");
        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));

        String localTime = date.format(currentLocalTime);
        return localTime;
    }
}
