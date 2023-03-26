package com.noorsy.lightbulb.debatetimer;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class LDActivity extends AppCompatActivity implements TimerFragment.buttonListener {

    TimerUtil timerUtil = new TimerUtil();
    ViewPager pager;
    RelativeLayout rLayout;
    ValueAnimator flash;
    boolean startShowing,pauseShowing, resetShowing;

    public FloatingActionButton start, pause, reset;
    public Animation button_appear, button_dissapear, button_fly_in, button_fly_out;

    List<WeakReference<TimerFragment>> fragList = new ArrayList<WeakReference<TimerFragment>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_ld);

        start = (FloatingActionButton)findViewById(R.id.button_play);
        startShowing=true;

        pause = (FloatingActionButton)findViewById(R.id.button_pause);
        pauseShowing= false;

        reset = (FloatingActionButton)findViewById(R.id.button_reset);
        resetShowing= false;

        button_appear = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_appear);
        button_dissapear = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_disappear);
        button_fly_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_fly_in);
        rLayout = (RelativeLayout) findViewById(R.id.container);
        rLayout.setBackgroundColor(Color.WHITE);
        if(rLayout!=null){
            Log.d("TAG", "RLAYOUT IS NOT NULL");
        }
        pager = (ViewPager) findViewById(R.id.speech_pager);
        pager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));
        pager.setOffscreenPageLimit(8);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                Log.d("TAG", "ON PAGE SELECTED CALLED:  " + TimerUtil.seconds);
                int tfNum = pager.getCurrentItem();
                TimerFragment CurrentTimerFrag = getFragmentByPosition(tfNum);
                int a=0;
                if(tfNum!=0){
                    a = -1;
                }
                TimerFragment PreviousTf = getFragmentByPosition(tfNum-1);
                /*
    TIMERSTATE:
    0 = NEVER STARTED
    1 = CURRENTLY PLAYING
    2 = PAUSED
    3 = FINISHED
     */
                switch (CurrentTimerFrag.timerState){
                    case 0:
                       showStartButton();
                        break;
                    case 1:
                        PreviousTf.pauseTimer();
                        showPauseButton();
                        break;
                    case 2:
                        showStartButton();
                        showResetButton();
                        break;
                    case 3:
                        showResetButton();
                        break;
                }
                    stopFlashScreen(CurrentTimerFrag);
                    CurrentTimerFrag.isFlashing = false;
                    //rLayout.clearAnimation();

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("TAG", "BACK BUTTON PRESSED");
        AlertDialog alertDialog = new AlertDialog.Builder(LDActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Are you sure you want to leave this debate?");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "LEAVE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "STAY",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        //
    }

    @Override
    public void onAttachFragment (Fragment fragment) {
        Log.i("TAG", "onAttachFragment: " + fragment);
        if (fragment.getClass() == TimerFragment.class) {
            fragList.add(new WeakReference<TimerFragment>((TimerFragment) fragment));
        }
    }

    public TimerFragment getFragmentByPosition(int position) {

        TimerFragment ret = null;
        for (WeakReference<TimerFragment> ref : fragList) {
            TimerFragment f = ref.get();
            if (f != null) {
                //Log.d("TAG", "um hullo:  "+f.getPosition());
                if (f.getPosition() == position) {
                    ret = f;
                }
            } else { //delete from list
                fragList.remove(f);
            }

        }
        if (ret==null){
            Log.d("TAG", "RETURNING NULL FRAGMENT");
        }
        else {
            Log.d("TAG", "RETURNING FRAGMENT");
        }

        return ret;
    }

    public void onStartClick(View v){
        showPauseButton();
        //showStartButton();

        Log.d("TAG", "ONCLICK, MAIN LD ACTIVITY");
        //TimerFragment timerFrag = (TimerFragment)
                //getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.speech_pager+":1");
        //TimerFragment timerFrag = (TimerFragment)allFragments.get(R.id.speech_pager);
        //timerFrag.Timer();
        String currentItem = Integer.toString(pager.getCurrentItem());
        Log.d("TAG", "CURRENT ITEM:  " + currentItem);
        TimerFragment timerFrag = getFragmentByPosition(pager.getCurrentItem());

        timerFrag.Timer();
    }

    public void onPauseClick(View v){
        String currentItem = Integer.toString(pager.getCurrentItem());
        Log.d("TAG", "RESART BUTTON CLICKED");
        TimerFragment timerFrag = getFragmentByPosition(pager.getCurrentItem());
        timerFrag.pauseTimer();
        showStartButton();
        stopFlashScreen(timerFrag);
    }

    public void onResetClick(View v){
        Log.d("TAG", "RESET BUTTON PRESSED");
    }

    public void showStartButton(){
        Log.d("TAG", "SHOWING START BUTTON");
        start.setClickable(true);
        if(startShowing==false) {
            start.startAnimation(button_appear);
        }
        startShowing = true;

        Log.d("TAG", "HIDING PAUSE BUTTON");
        //reset.setClickable(false);

        if(pauseShowing==true) {
            pause.startAnimation(button_dissapear);
        }
        pauseShowing = false;
    }

    public void showPauseButton(){
        Log.d("TAG", "SHOWING PAUSE BUTTON");
        //reset.setClickable(true);
        if(pauseShowing==false) {
            pause.startAnimation(button_appear);
        }
        pauseShowing = true;

        Log.d("TAG", "HIDING START BUTTON");
        start.setClickable(false);

        if(startShowing==true) {
            start.startAnimation(button_dissapear);
        }
        startShowing = false;
    }

    void showResetButton(){
        Log.d("TAG", "SHOWING RESET BUTTON BUTTON");
    }

    public void goToNextSpeech(){
        int currentItem = pager.getCurrentItem();
        if(currentItem<7) {
            pager.setCurrentItem(currentItem + 1);
        }
    }

    void flashScreen(){
        Log.d("TAG", "FLASHING SCREEN");
        TimerFragment timerFrag = getFragmentByPosition(pager.getCurrentItem());

        flash = new ValueAnimator();
        int pink = Color.rgb(244, 66, 125);
        flash.setIntValues(Color.WHITE, pink);
        flash.setEvaluator(new ArgbEvaluator());
        flash.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Log.d("TAG", "UPDATING BACKGROUND COLOR");
                rLayout.setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
            }
        });

        flash.setDuration(500);
        flash.setRepeatCount(ValueAnimator.INFINITE);
        flash.setRepeatMode(ValueAnimator.REVERSE);
        flash.start();
        //flash.cancel();

    }

    void stopFlashScreen(TimerFragment f){
        if(flash!=null&& f.currentTime>10) {
            Log.d("TAG", "ATTEMPTING TO CANCEL ANIMATION");
            while(flash.isRunning()||flash.isStarted()) {
                flash.cancel();
                flash.end();
            }
            rLayout.setBackgroundColor(Color.WHITE);
        }
    }
}

