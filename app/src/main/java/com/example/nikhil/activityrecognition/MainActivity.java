package com.example.nikhil.activityrecognition;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    GoogleApiClient mApiClient;
    private EditText username, password;
    private Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        /*mApiClient.connect();*/
        initUI();
        signIn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(Objects.equals(username.getText().toString(), "Group11") && Objects.equals(password.getText().toString(), "hitesh")) {
                    mApiClient.connect();
                    Toast.makeText(MainActivity.this, "Logged In successfully", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(MainActivity.this, "Incorrect Login Details", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initUI() {
        username = findViewById(R.id.username_edittext);
        password = findViewById(R.id.password_edittext);
        //activity = findViewById(R.id.activity_edittext);
        signIn = findViewById(R.id.sigin_button);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Intent intent = new Intent( this, ActivityRecognizedService.class );
        PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( mApiClient, 3000, pendingIntent );
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }
}
