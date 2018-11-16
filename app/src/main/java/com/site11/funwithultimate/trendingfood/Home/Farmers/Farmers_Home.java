package com.site11.funwithultimate.trendingfood.Home.Farmers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class Farmers_Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txt_Logged_email;
    private Home_Fragment home_fragment;
    private UP_Food_Fragment up_food_fragment;
    private Down_Food_Fragment down_food_fragment;

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmershome_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //get Email from Login
        if(getIntent() != null){
            txt_Logged_email.setText(getIntent().getStringExtra("email"));

        }




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
        ImageView headpro_image = headerview.findViewById(R.id.pro_image);

        //set Email
        TextView nav_txtpro = headerview.findViewById(R.id.twuseremail);
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
        /*
        home_fragment = new Home_Fragment();
        up_food_fragment = new UP_Food_Fragment();
        down_food_fragment = new Down_Food_Fragment();

        frameLayout = (FrameLayout) findViewById(R.id.frame_layoutf);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_barf);

        setFragment(home_fragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_fhome:
                        bottomNavigationView.setBackgroundResource(R.color.colorPrimary);
                        setFragment(home_fragment);
                        return true;

                    case R.id.nav_fstore:
                        bottomNavigationView.setBackgroundResource(R.color.colorAccent);
                        setFragment(down_food_fragment);
                        return true;

                    case R.id.nav_fsell:
                        bottomNavigationView.setBackgroundResource(R.color.colorPrimaryDark);
                        setFragment(up_food_fragment);
                        return true;

                    default:
                        return false;
                }

            }
        });*/
        ////////////////////////////////////////////////////////////////////

    }

        ///////////////////////////////////////////////////////////
        //////////////////////--Back-Button--//////////////////////
        ///////////////////////////////////////////////////////////

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
