package com.noorsy.lightbulb.debatetimer;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public  class TimerFragment extends Fragment {

    int currentSpeech=0;
    public boolean isFlashing;
    public int currentTime =0;
    /*
    TIMERSTATE:
    0 = NEVER STARTED
    1 = CURRENTLY PLAYING
    2 = PAUSED
    3 = FINISHED
     */
    public int timerState;

    String currentSpeechName;

    TextView timeText;
    TextView speechText;
    CountDownTimer timer;
    static TimerFragment f;
    //Button button;

    TimerUtil timerUtil = new TimerUtil();
    LDActivity ldActivity = new LDActivity();

    public interface buttonListener{

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timer, container, false);
        //View outsideV = inflater.inflate(R.layout.content_ld, container, false);
        //button = (Button) outsideV.findViewById(R.id.Startbutton);
        //button.setOnClickListener(new );

        //TextView debateName = (TextView) v.findViewById(R.id.SpeechName);
        currentSpeech = Integer.parseInt(getArguments().getString("msg"));
        timeText = (TextView)v.findViewById(R.id.TimeView);
        speechText = (TextView)v.findViewById(R.id.SpeechName);

        timerState = 0;
        GetCurrentSpeech();
        return v;
    }

    public static TimerFragment newInstance(String text) {

        f = new TimerFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    void GetCurrentSpeech(){
        switch (currentSpeech) {
            case 0:
                currentSpeechName="1st Affirmative Constructive";
                currentTime = 15;
                //360
                break;
            case 1:
                currentSpeechName="Cross Examination";
                currentTime = 12;
                //180
                break;
            case 2:
                currentSpeechName="1st Negative Constructive";
                currentTime = 420;
                break;
            case 3:
                currentSpeechName = "Cross Examination";
                currentTime = 180;
                break;
            case 4:
                currentSpeechName= "1st Affirmative Rebuttal";
                currentTime = 240;
                break;
            case 5:
                currentSpeechName = "Negative Rebuttal";
                currentTime = 360;
                break;
            case 6:
                currentSpeechName="2nd Affirmative Rebuttal";
                currentTime = 180;
                break;
        }
        DisplayMinutes(timeText, currentTime);
        speechText.setText(currentSpeechName);
        TimerUtil.seconds = currentTime;
    }

    public void Timer(){
        //GetCurrentSpeech();
        Log.d("TAG", "START TIMER METHOD FRAGMENT");
        if(timerState!=1) {
            timerState = 1;
            timer = new CountDownTimer(currentTime * 1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    Log.d("TAG", "TICK  " + currentTime);
                    currentTime -= 1;
                    DisplayMinutes(timeText, currentTime);
                    if (currentTime <= 10) {
                        if (!isFlashing) {
                            ((LDActivity) getActivity()).flashScreen();
                            Log.d("TAG", "TIMER ACTIVITY FLASHING SCREEN");
                            isFlashing = true;
                        }
                    }
                }

                public void onFinish() {
                    Log.d("TAG", "TIMER FINISHED");
                    timeText.setText("0:00");
                    TimerUtil.seconds = currentTime;
                    //((LDActivity) getActivity()).showStartButton();
                    ((LDActivity) getActivity()).goToNextSpeech();
                    ((LDActivity) getActivity()).stopFlashScreen(f);
                    timerState = 3;

                }

            }.start();
        }
    }

    public void pauseTimer(){
        Log.d("TAG", "ATTEMPTING TO CANCEL TIMER");
        this.timer.cancel();
        //resets timer, idk why
        //GetCurrentSpeech();
        timerState = 2;
    }

    public void test(){
        Log.d("TAG", "FUCK YES MOTHER BITCHES WE DOIN THIS");
    }

    public void DisplayMinutes(TextView t, int seconds){
        Log.d("TAG", "DISPLAY MINUTES CALLED");
        if (seconds % 60 < 10) {
            String tx = Long.toString(TimeUnit.MINUTES.convert(seconds, TimeUnit.SECONDS))+":0"+(seconds%60);
            t.setText(tx);

        }
        else {
            String tx = Long.toString(TimeUnit.MINUTES.convert(seconds, TimeUnit.SECONDS)) + ":" + (seconds % 60);
            t.setText(tx);
        }
    }

    public int getPosition(){
    return currentSpeech;
    }

}

