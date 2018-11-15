package com.site11.funwithultimate.trendingfood;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

public class ForgotActivity extends AppCompatActivity {

    RelativeLayout frellay1;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            frellay1.setVisibility(View.VISIBLE);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_layout);

        frellay1 = (RelativeLayout) findViewById(R.id.frellay1);


        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash
    }

    public void signupbtn(View view) {
        Intent i = new Intent(ForgotActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
