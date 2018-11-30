package com.site11.funwithultimate.trendingfood;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

public class Profile_Activity extends AppCompatActivity {

    RelativeLayout prellay1;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            prellay1.setVisibility(View.VISIBLE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);


        prellay1 = (RelativeLayout) findViewById(R.id.prellay1);
        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash


    }

}
