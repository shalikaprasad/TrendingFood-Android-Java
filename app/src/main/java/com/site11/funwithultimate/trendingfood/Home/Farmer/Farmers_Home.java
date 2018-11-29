package com.site11.funwithultimate.trendingfood.Home.Farmer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.site11.funwithultimate.trendingfood.Profile_Activity;
import com.site11.funwithultimate.trendingfood.R;

public class Farmers_Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Home_Farmer_Fragment home_farmer_fragment;
    private UP_Farmer_Fragment up_farmer_fragment;
    private Down_Farmer_Fragment down_farmer_fragment;

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmers__home);

        //add tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");


        //add navigation bar
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
        ImageView headpro_image = headerview.findViewById(R.id.pro_imagef);

        //set Email
        TextView nav_txtpro = headerview.findViewById(R.id.twuseremailf);
        nav_txtpro.setText(getIntent().getStringExtra("email"));

        //set Profile
        headpro_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(Farmers_Home.this,Profile_Activity.class);
                startActivity(ii);
            }
        });


        //////////////////////////////////////////////////////////////////
        //////////////////////--Bottom Navigation View--//////////////////
        /////////////////////////////////////////////////////////////////

        home_farmer_fragment = new Home_Farmer_Fragment();
        up_farmer_fragment = new UP_Farmer_Fragment();
        down_farmer_fragment = new Down_Farmer_Fragment();

        frameLayout = (FrameLayout) findViewById(R.id.frame_layoutf);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_barf);

        setFragment(home_farmer_fragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_fhome:
                        bottomNavigationView.setBackgroundResource(R.color.colorPrimary);
                        setFragment(home_farmer_fragment);
                        return true;

                    case R.id.nav_fstore:
                        bottomNavigationView.setBackgroundResource(R.color.colorAccent);
                        setFragment(down_farmer_fragment);
                        return true;

                    case R.id.nav_fsell:
                        bottomNavigationView.setBackgroundResource(R.color.colorPrimaryDark);
                        setFragment(up_farmer_fragment);
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
        getMenuInflater().inflate(R.menu.farmers__home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.faction_summery) {
            return true;
        }else if (id == R.id.faction_settings) {
            return true;
        }else if (id == R.id.faction_help) {
            return true;
        }else if (id == R.id.faction_logout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navside_addnewpost_farmer) {
            // Handle the camera action
        } else if (id == R.id.navside_profile_farmer) {

        }else if (id == R.id.navside_friends_farmer) {

        }else if (id == R.id.navside_home_farmer) {

        }else if (id == R.id.navside_friends_farmer) {

        }else if (id == R.id.navside_message_farmer) {

        } else if (id == R.id.navside_notification_farmer) {

        }else if (id == R.id.navside_setting_farmer) {

        }else if (id == R.id.navside_logout_farmer) {

        } else if (id == R.id.navside_saved_farmer) {

        } else if (id == R.id.navside_sell_farmer) {

        }else if (id == R.id.navside_bid_farmer) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layoutf,fragment);
        fragmentTransaction.commit();
    }

}
