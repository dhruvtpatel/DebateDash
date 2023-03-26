package com.noorsy.lightbulb.debatetimer;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by Noor Syed on 1/29/2017.
 */
public class TimerUtil {

    public static int seconds;

    public void DisplayMinutes(TextView t, int seconds){
        Log.d("TAG", "DISPLAY MINUTES CALLED");
        if (seconds % 60 < 10) {
            t.setText(Long.toString(TimeUnit.MINUTES.convert(seconds, TimeUnit.SECONDS))+":0"+(seconds%60));
        }
        else {
            t.setText(Long.toString(TimeUnit.MINUTES.convert(seconds, TimeUnit.SECONDS)) + ":" + (seconds % 60));
        }
    }





}
