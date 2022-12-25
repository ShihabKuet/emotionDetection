package com.example.imagepro;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.opencv.android.OpenCVLoader;

public class Home_activity extends AppCompatActivity {

    static {
        if(OpenCVLoader.initDebug()){
            Log.d("HomeActivity: ","Opencv is loaded");
        }
        else {
            Log.d("HomeActivity: ","Opencv failed to load");
        }
    }

    private Button open_camera;
    private Button open_storage;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

//        LinearLayout linearLayout = findViewById(R.id.home_layout);
//        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
//        animationDrawable.setEnterFadeDuration(2000);
//        animationDrawable.setExitFadeDuration(2000);
//        animationDrawable.start();

        getSupportActionBar().hide();

        builder = new AlertDialog.Builder(this);

        open_camera = findViewById(R.id.open_camera);
        open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Alert!")
                                .setMessage("Are you sure to open your camera?").setCancelable(true)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new Intent(Home_activity.this,CameraActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    }
                                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();


            }
        });

        open_storage = findViewById(R.id.open_storage);
        open_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_activity.this, StorageActivity.class);
                startActivity(intent);
            }
        });
    }
}