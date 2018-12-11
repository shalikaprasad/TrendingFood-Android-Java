package com.site11.funwithultimate.trendingfood.Home.Farmer;

import android.content.Context;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.site11.funwithultimate.trendingfood.ClickPostActivity;
import com.site11.funwithultimate.trendingfood.LoginActivity;
import com.site11.funwithultimate.trendingfood.PostActivity;
import com.site11.funwithultimate.trendingfood.Posts;
import com.site11.funwithultimate.trendingfood.Profile_Activity;
import com.site11.funwithultimate.trendingfood.R;
import com.site11.funwithultimate.trendingfood.SettingActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Farmers_Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Home_Farmer_Fragment home_farmer_fragment;
    private UP_Farmer_Fragment up_farmer_fragment;
    private Down_Farmer_Fragment down_farmer_fragment;

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, PostsRef;
    String currentUserID;

    TextView nav_usernamepro;
    ImageView headpro_image;

    private RecyclerView postList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmers__home);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

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

        //set Email in Google Signin
        final TextView nav_txtpro = headerview.findViewById(R.id.twuseremailf);
        nav_txtpro.setText(getIntent().getStringExtra("email"));
        //////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////


        //set Username and Profile Image
        nav_usernamepro = headerview.findViewById(R.id.twusernamef);
        headpro_image = headerview.findViewById(R.id.pro_imagef);

        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("fullname"))
                    {
                        String fullname = dataSnapshot.child("fullname").getValue().toString();
                        nav_usernamepro.setText(fullname);
                    }
                    if(dataSnapshot.hasChild("character"))
                    {
                        String character = dataSnapshot.child("character").getValue().toString();
                        nav_txtpro.setText(character);
                    }
                    if(dataSnapshot.hasChild("profileimage"))
                    {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(headpro_image);
                        //Picasso.with(Farmers_Home.this).load(image).placeholder(R.drawable.profile).into(headpro_image);
                    }
                    else
                    {
                        Toast.makeText(Farmers_Home.this, "Profile name do not exists...", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

        //////////////////////////////////////////////////////////////////
        //////////////////////--Post View--///////////////////////////////
        /////////////////////////////////////////////////////////////////
        postList = (RecyclerView) findViewById(R.id.all_users_post_list);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

        DisplayAllUsersPosts();
    }

    private void DisplayAllUsersPosts()
    {
        FirebaseRecyclerAdapter<Posts, PostsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Posts, PostsViewHolder>
                        (
                                Posts.class,
                                R.layout.all_posts_layout,
                                PostsViewHolder.class,
                                PostsRef
                        )
                {
                    @Override
                    protected void populateViewHolder(PostsViewHolder viewHolder, Posts model, int position)
                    {
                        final String PostKey = getRef(position).getKey();
                        viewHolder.setFullname(model.getFullname());
                        viewHolder.setTime(model.getTime());
                        viewHolder.setDate(model.getDate());
                        viewHolder.setDescription(model.getDescription());
                        viewHolder.setProfileimage(getApplicationContext(), model.getProfileimage());
                        viewHolder.setPostimage(getApplicationContext(), model.getPostimage());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent clickPostIntent = new Intent(Farmers_Home.this,ClickPostActivity.class);
                                clickPostIntent.putExtra("PostKey", PostKey);
                                startActivity(clickPostIntent);
                            }
                        });
                    }
                };
        postList.setAdapter(firebaseRecyclerAdapter);
    }



    public static class PostsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public PostsViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
        }

        public void setFullname(String fullname)
        {
            TextView username = (TextView) mView.findViewById(R.id.post_user_name);
            username.setText(fullname);
        }

        public void setProfileimage(Context ctx, String profileimage)
        {
            CircleImageView image = (CircleImageView) mView.findViewById(R.id.post_profile_image);
            Picasso.get().load(profileimage).placeholder(R.drawable.profile).into(image);
        }

        public void setTime(String time)
        {
            TextView PostTime = (TextView) mView.findViewById(R.id.post_time);
            PostTime.setText("    " + time);
        }

        public void setDate(String date)
        {
            TextView PostDate = (TextView) mView.findViewById(R.id.post_date);
            PostDate.setText("    " + date);
        }

        public void setDescription(String description)
        {
            TextView PostDescription = (TextView) mView.findViewById(R.id.post_description);
            PostDescription.setText(description);
        }

        public void setPostimage(Context ctx1,  String postimage)
        {
            ImageView PostImage = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.get().load(postimage).placeholder(R.drawable.profile).into(PostImage);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null)
        {
            SendUserToLoginActivity();
        }
        else
        {
            CheckUserExistence();
        }
    }



    private void CheckUserExistence()
    {
        final String current_user_id = mAuth.getCurrentUser().getUid();

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(!dataSnapshot.hasChild(current_user_id))
                {
                    SendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToSetupActivity()
    {
        Intent setupIntent = new Intent(Farmers_Home.this, Profile_Activity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }
    /*
    //Firebase check current user is be or null
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null)
        {
            SendUserToLoginActivity();
        }
        else
        {
            CheckUserExistence();
        }
    }
*/
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
        if(id == R.id.faction_new_post){
            SendUserToPostActivity();
        }
        else if (id == R.id.faction_summery) {
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

    private void SendUserToPostActivity() {
        Intent addNewPostIntent = new Intent(Farmers_Home.this, PostActivity.class);
        startActivity(addNewPostIntent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navside_addnewpost_farmer) {
            SendUserToPostActivity();
        } else if (id == R.id.navside_profile_farmer) {

        }else if (id == R.id.navside_friends_farmer) {

        }else if (id == R.id.navside_home_farmer) {

        }else if (id == R.id.navside_friends_farmer) {

        }else if (id == R.id.navside_message_farmer) {

        } else if (id == R.id.navside_notification_farmer) {

        }else if (id == R.id.navside_setting_farmer) {
            SendUserToSettingActivity();

        }else if (id == R.id.navside_logout_farmer) {
            mAuth.signOut();
            SendUserToLoginActivity();
            Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.navside_saved_farmer) {

        } else if (id == R.id.navside_sell_farmer) {

        }else if (id == R.id.navside_bid_farmer) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(Farmers_Home.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void SendUserToSettingActivity()
    {
        Intent setupIntent = new Intent(Farmers_Home.this, SettingActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    /*
      private void CheckUserExistence()
      {
          final String current_user_id = mAuth.getCurrentUser().getUid();

          UsersRef.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot)
              {
                  if(!dataSnapshot.hasChild(current_user_id))
                  {
                      SendUserToSetupActivity();
                  }
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });
      }


      private void SendUserToSetupActivity()
      {
          Intent setupIntent = new Intent(Farmers_Home.this, Profile_Activity.class);
          setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(setupIntent);
          finish();
      }
  */
    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layoutf,fragment);
        fragmentTransaction.commit();
    }



}
