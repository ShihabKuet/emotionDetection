package com.example.imagepro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    static {
        if(OpenCVLoader.initDebug()){
            Log.d("MainActivity: ","Opencv is loaded");
        }
        else {
            Log.d("MainActivity: ","Opencv failed to load");
        }
    }


    private Button homebtn;
    private TextView loading_txt;
    int counter = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        LinearLayout linearLayout = findViewById(R.id.main_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        homebtn = findViewById(R.id.homebtn);
        loading_txt = findViewById(R.id.loading_txt);
        loading_txt.setVisibility(View.INVISIBLE);







        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading_txt.setVisibility(View.VISIBLE);


                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        counter++;
                        if(counter==20){
                            timer.cancel();
                            Intent intent = new Intent(MainActivity.this, Home_activity.class);
                            startActivity(intent);
                            finish();

                        }
                    }
                };
                timer.schedule(timerTask,100,100);


            }
        });





    }
}