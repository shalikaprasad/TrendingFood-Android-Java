package com.site11.funwithultimate.trendingfood.Home.Retailers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.site11.funwithultimate.trendingfood.Profile_Activity;
import com.site11.funwithultimate.trendingfood.R;

public class Retails_Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Home_Retail_Fragment home_retail_fragment;
    private Up_Retail_Fragment up_retail_fragment;
    private Down_Retail_Fragment down_retail_fragment;

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retailshome_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ///////////////////////////////////////////////////////////
        //////////////////////--Navigation View--//////////////////
        ///////////////////////////////////////////////////////////

        View headerview = navigationView.getHeaderView(0);
        ImageView headpro_image = headerview.findViewById(R.id.pro_imager);

        //set Email
        TextView nav_txtpro = headerview.findViewById(R.id.twuseremailr);
        nav_txtpro.setText(getIntent().getStringExtra("email"));

        //set Profile
        headpro_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Retails_Home.this,Profile_Activity.class);
                startActivity(ii);
            }
        });


        //////////////////////////////////////////////////////////////////
        //////////////////////--Bottom Navigation View--//////////////////
        /////////////////////////////////////////////////////////////////

        home_retail_fragment = new Home_Retail_Fragment();
        up_retail_fragment = new Up_Retail_Fragment();
        down_retail_fragment = new Down_Retail_Fragment();

        frameLayout = (FrameLayout) findViewById(R.id.frame_layoutr);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_barr);

        setFragment(home_retail_fragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_rhome:
                        bottomNavigationView.setBackgroundResource(R.color.colorPrimary);
                        setFragment(home_retail_fragment);
                        return true;

                    case R.id.nav_rstore:
                        bottomNavigationView.setBackgroundResource(R.color.colorAccent);
                        setFragment(down_retail_fragment);
                        return true;

                    case R.id.nav_rsell:
                        bottomNavigationView.setBackgroundResource(R.color.colorPrimaryDark);
                        setFragment(up_retail_fragment);
                        return true;

                    default:
                        return false;
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.retails__home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.raction_summery) {
            return true;
        }else if (id == R.id.raction_settings) {
            return true;
        }else if (id == R.id.raction_help) {
            return true;
        }else if (id == R.id.raction_logout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navside_home_retailer) {
            // Handle the camera action
        } else if (id == R.id.navside_friends_retailer) {

        } else if (id == R.id.navside_message_retailer) {

        } else if (id == R.id.navside_notification_retailer) {

        } else if (id == R.id.navside_saved_retailer) {

        } else if (id == R.id.navside_sell_retailer) {

        }else if (id == R.id.navside_bid_retailer) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layoutr,fragment);
        fragmentTransaction.commit();
    }

}
