package com.bijoy.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView timeTV;
    LinearLayout playPauseLL;
    LinearLayout resetLL;
    ImageView iconIV;
    TextView titleTV;

    boolean running = false;
    long second = 0;
    String timeString = "";

    State state;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putLong("seconds", second);
        savedInstanceState.putSerializable("state", state);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showToast("onCreate");

        timeTV = findViewById(R.id.timeTV);
        playPauseLL = findViewById(R.id.playPauseLL);
        resetLL = findViewById(R.id.resetLL);
        iconIV = findViewById(R.id.iconIV);
        titleTV = findViewById(R.id.titleTV);
        state = State.INIT;

        if (savedInstanceState != null) {
            state = (State) savedInstanceState.get("state");
            second = savedInstanceState.getLong("seconds", 0);
            running = savedInstanceState.getBoolean("running", false);
        }

        updateUI(state);

        runTimer();


        playPauseLL.setOnClickListener(view -> {

            switch (state) {
                case INIT:
                case PAUSE:
                    running = true;
                    state = State.PLAY;
                    break;
                case PLAY:
                    running = false;
                    state = State.PAUSE;
                    break;
            }

            updateUI(state);


        });

        resetLL.setOnClickListener(view -> {
            state = State.INIT;
            running = false;
            second = 0;
            updateUI(state);


        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        showToast("onStart");
        if (running){
            state = State.PLAY;
            updateUI(state);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showToast("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        showToast("onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        showToast("onStop");
        if (running){
            state = State.PAUSE;
            running = false;
            updateUI(state);
        }
    }

    @Override
    protected void onDestroy() {
        state = State.INIT;
        running = false;
        second = 0;
        super.onDestroy();
        showToast("onDestroy");
    }


    private void runTimer() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                setStopWatchValue();
                handler.postDelayed(this, 10);

            }
        });
    }

    private void updateUI(State state) {

        switch (state) {
            case INIT:
                titleTV.setText("Start");
                iconIV.setImageResource(R.drawable.baseline_play_arrow_24);
                break;
            case PLAY:
                titleTV.setText("Pause");
                iconIV.setImageResource(R.drawable.baseline_pause_24);
                break;
            case PAUSE:
                titleTV.setText("Resume");
                iconIV.setImageResource(R.drawable.baseline_replay_24);
                break;
        }


    }

    private void setStopWatchValue() {
//        int hour = second / 3600;
//        int minutes = (second % 3600) / 60;
//        int secondS = second % 60;

        DateFormat format = new SimpleDateFormat("mm:ss:SS", Locale.getDefault());
        timeString = format.format(second);

//        timeString = String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minutes, secondS);

        timeTV.setText(timeString);
        if (running) {
            // second++;
            second += 10;
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}