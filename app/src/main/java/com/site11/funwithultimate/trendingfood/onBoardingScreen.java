package com.site11.funwithultimate.trendingfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class onBoardingScreen extends Activity {

    private ViewPager viewpage;
    private LinearLayout linearLayout;
    private SliderAdapter sliderAdapter;

    private TextView[] dots;

    private Button backbtn;
    private Button nextbtn;

    private int currentPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_boarding_layout);

        viewpage = (ViewPager) findViewById(R.id.pageview);
        linearLayout = (LinearLayout) findViewById(R.id.dots);

        backbtn = (Button) findViewById(R.id.back1);
        nextbtn = (Button) findViewById(R.id.next1);

        sliderAdapter = new SliderAdapter(this);
        viewpage.setAdapter(sliderAdapter);

        addDotIndicator(0);
        viewpage.addOnPageChangeListener(viewlistner);

        //OnClickListner

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpage.setCurrentItem(currentPage + 1);

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpage.setCurrentItem(currentPage - 1);
            }
        });


    }

    public void addDotIndicator(int position) {

        dots = new TextView[3];
        linearLayout.removeAllViews();

        for(int i=0; i < dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.trans_sc));

            linearLayout.addView(dots[i]);


        }
        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.white));
        }
    }

    ViewPager.OnPageChangeListener viewlistner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotIndicator(i);

            currentPage = i;
            if(i == 0){
                nextbtn.setEnabled(true);
                backbtn.setEnabled(false);
                backbtn.setVisibility(View.INVISIBLE);

                nextbtn.setText("Next");
                backbtn.setText("");

            }else if(i == dots.length-1){
                nextbtn.setEnabled(true);
                backbtn.setEnabled(true);
                backbtn.setVisibility(View.VISIBLE);

                nextbtn.setText("Finish");
                backbtn.setText("Back");

                nextbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ii = new Intent(onBoardingScreen.this,LoginActivity.class);
                        startActivity(ii);
                    }
                });


            }else{
                nextbtn.setEnabled(true);
                backbtn.setEnabled(true);
                backbtn.setVisibility(View.VISIBLE);

                nextbtn.setText("Next");
                backbtn.setText("Back");
            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };


}
