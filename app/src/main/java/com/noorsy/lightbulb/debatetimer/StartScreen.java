package com.noorsy.lightbulb.debatetimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        }

    public void StartLD(View view) {

        Log.d("TAG", "START LD BUTTON PRESSED");

        Intent intent = new Intent(StartScreen.this,  LDActivity.class);
        startActivity(intent);

    }

    public void StartPF(View view) {
        Log.d("TAG", "START PF BUTTON PRESSED");
    }
    }


