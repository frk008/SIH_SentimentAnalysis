package com.example.stressbuster;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

public class FirstTimeInitialization extends AppCompatActivity {

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_initialization);

        setStatusBarGradientOk.setStatusBarGradient(this);

        final TextView onlyTextHere = findViewById(R.id.loginHeadingText);
        final ImageView onlyImageView = findViewById(R.id.lolImage);
        final MaterialButton nextBtn = findViewById(R.id.introActivityBtn);
        MaterialButton skipBtn = findViewById(R.id.skipActivityBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count == 0) {
                    onlyTextHere.setText(R.string.permissions_needed);
                    onlyImageView.setImageDrawable(null);

                    ActivityCompat.requestPermissions(FirstTimeInitialization.this,
                                    new String[]{Manifest.permission.RECORD_AUDIO},
                                    1);



                    count++;
                } else if(count == 1) {
                    onlyImageView.setImageDrawable(null);
                    String apple = "Explore the app, find out stuff and get used to it " + " :)";
                    onlyTextHere.setText(apple);
                    count++;
                } else if(count == 2) {
                    startActivity(new Intent(FirstTimeInitialization.this, UserDashboard.class));
                }
                if(count == 2) {
                    nextBtn.setText(R.string.first_time_init_doneBtn);
                }

            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(FirstTimeInitialization.this,
                        Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(FirstTimeInitialization.this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            1);

                } else {

                    startActivity(new Intent(FirstTimeInitialization.this, UserDashboard.class));
                }
            }
        });



    }

}
