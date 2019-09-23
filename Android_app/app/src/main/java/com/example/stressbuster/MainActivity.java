package com.example.stressbuster;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextView loaderDataPlacer;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBarGradientOk.setStatusBarGradient(this);

        createNotificationChannel();


        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);



        if(!Python.isStarted()) {
            Python.start(new AndroidPlatform(getApplicationContext()));

            if(sharedPref.getString("InstagramuserID", "") == "" || sharedPref.getString("InstagramUserPassword", "") == "") {
                Toast.makeText(this, "No Instagram credentials added. Please add to comtinue", Toast.LENGTH_LONG).show();
            } else {
                InstagramScraperStuff instagramScraperStuff = new InstagramScraperStuff(sharedPref.getString("InstagramUserID",""), sharedPref.getString("InstagramUserPassword",""));
                instagramScraperStuff.getStuffDone();
            }

        }

        Intent intent = new Intent(this, DataScraper.class);
        startService(intent);

        firebaseAuth = FirebaseAuth.getInstance();
        loaderDataPlacer = findViewById(R.id.loadingStatus);

        final Context context = this;
        final Handler handler = new Handler();

        final View view = findViewById(android.R.id.content);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(!NetworkConnectivity()) {
                    loaderDataPlacer.setText(R.string.internet_connectivity_not_found);

                    new AlertDialog.Builder(context)
                            .setTitle("No Internet connection")
                            .setMessage("Please make sure the internet connectivity is available.")
                            .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                    Snackbar.make(view, "Close and reopen app after enabling Internet", Snackbar.LENGTH_INDEFINITE).show();

                                }
                            })
                            .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                                }
                            })
                            .setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                    System.exit(0);
                                }
                            })
                            .setIcon(R.drawable.ic_signal_wifi_off_black_24dp)
                            .show();
                } else {

                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if(firebaseUser != null) {
                        startActivity(new Intent(MainActivity.this, UserDashboard.class));
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginRegisterActivity.class));
                    }
                }

            }
        }, 2500);


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.common_google_play_services_notification_channel_name);
            String description = getString(R.string.common_google_play_services_notification_channel_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_SERVICE, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void onStart() {
        super.onStart();
    }

    private boolean NetworkConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        return connectivityManager.getActiveNetworkInfo()!= null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
