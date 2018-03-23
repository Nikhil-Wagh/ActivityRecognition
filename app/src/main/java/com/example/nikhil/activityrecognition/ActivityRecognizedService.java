package com.example.nikhil.activityrecognition;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

/**
 * Created by nikhil on 28/2/18.
 */

public class ActivityRecognizedService extends IntentService {

    public static final String ACTION_LOCATION_BROADCAST = ActivityRecognizedService.class.getName() + "locationBroadcast";
    public static final String UPDATED_ACTIVITY = "updatedGyroValues";

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities( result.getProbableActivities() );
        }
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        for( DetectedActivity activity : probableActivities ) {
            String a = "";
            switch (activity.getType()) {
                case DetectedActivity.IN_VEHICLE: {
                    Log.e("ActivityRecogition", "In Vehicle: " + activity.getConfidence());
                    //a = "In Vehicle";
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    Log.e("ActivityRecogition", "On Bicycle: " + activity.getConfidence());
                    //a = "On Bicycle";
                    break;
                }
                case DetectedActivity.ON_FOOT: {
                    Log.e("ActivityRecogition", "On Foot: " + activity.getConfidence());
                    if (activity.getConfidence() >= 30) {
                        buildNotification("On Foot");
                        a = "On Foot";
                    }

                    break;
                }
                case DetectedActivity.RUNNING: {
                    Log.e("ActivityRecogition", "Running: " + activity.getConfidence());
                    if (activity.getConfidence() >= 30) {
                        buildNotification("Running");
                        a = "Running";
                    }

                    break;
                }
                case DetectedActivity.STILL: {
                    Log.e("ActivityRecogition", "Still: " + activity.getConfidence());
                    if (activity.getConfidence() >= 30) {
                        buildNotification("Still");
                        a = "Still";
                    }

                    break;
                }
                case DetectedActivity.TILTING: {
                    Log.e("ActivityRecogition", "Tilting: " + activity.getConfidence());
                    //a = "Tilting";
                    break;
                }
                case DetectedActivity.WALKING: {
                    Log.e("ActivityRecogition", "Walking: " + activity.getConfidence());
                    if (activity.getConfidence() >= 30) {
                        buildNotification("Walking");
                        a = "Walking";
                    }

                    break;
                }
                case DetectedActivity.UNKNOWN: {
                    Log.e("ActivityRecogition", "Unknown: " + activity.getConfidence());
                    if (activity.getConfidence() >= 30) {
                        buildNotification("Unknown");
                        a = "Unknown";
                    }

                    break;
                }
            }
            sendLog(a);
        }
    }

    private void buildNotification(String s){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentText(s);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(getString(R.string.app_name));
        NotificationManagerCompat.from(this).notify(0, builder.build());
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    private void sendLog(String a){
        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        if (a != null){
            intent.putExtra(UPDATED_ACTIVITY, a);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            Toast.makeText(this, a, Toast.LENGTH_LONG).show();
        }
    }
}
