package com.site11.funwithultimate.trendingfood;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.site11.funwithultimate.trendingfood.Home.Consumers.Consumer_Home;
import com.site11.funwithultimate.trendingfood.Home.Farmer.Farmers_Home;
import com.site11.funwithultimate.trendingfood.Home.Retailers.Retails_Home;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    //private static final int RC_SIGN_IN = 1;
    //private GoogleApiClient mGoogleSignInClient;
    private static final String TAG = "LoginActivity";


//    private ImageView googleSignInButton;
    FirebaseAuth firebaseAuth;

    //Create Dialog Box
    AlertDialog waitingDialog;
    RelativeLayout rellay1, rellay2;
//    Spinner category;
    int category_no = -1;
    boolean connected;

    EditText loguser,logpass,shopname;
    private ProgressDialog loadingBar;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        rellay1 = findViewById(R.id.rellay1);
        rellay2 = findViewById(R.id.rellay2);
//        category = findViewById(R.id.category);


        //googleSignInButton = findViewById(R.id.google_signin_button);

        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash

        loguser = findViewById(R.id.logusermaneedit);
        logpass = findViewById(R.id.logpasswordedit);
//        shopname = findViewById(R.id.shopnameedit);

        loadingBar = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        //////////////////////////////////////////////
        //////////Check Internet Connection///////////
        //////////////////////////////////////////////
        checkConnection();
        if (connected == false) {
            //alertInternet();
            CheckInternet alert1 = new CheckInternet();
            alert1.showDialog(LoginActivity.this, "Warning");
        } else {
            Toast.makeText(LoginActivity.this, "Connected", Toast.LENGTH_LONG).show();
        }


        //////////////////////////////////////////////
        //////////Select Category/////////////////////
        //////////////////////////////////////////////

//        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(LoginActivity.this,
//                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.category));
//        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        category.setAdapter(provinceAdapter);
//
//        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (i == 1) {
//                    category_no = 0;
//                } else if (i == 2) {
//                    category_no = 1;
//                } else if (i == 3) {
//                    category_no = 2;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        waitingDialog = new SpotsDialog.Builder().setContext(this)
                .setMessage("Please wait...")
                .setCancelable(false)
                .build();

//
//        // Configure sign-in to request the user's ID, email address, and basic
//        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        mGoogleSignInClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
//                    {
//                        Toast.makeText(LoginActivity.this, "Connection to Google Sign in failed...", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//
//
//        googleSignInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                signIn();
//            }
//        });
//
//
//    }
//
//
//    private void signIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_SIGN_IN)
//        {
//            loadingBar.setTitle("google Sign In");
//            loadingBar.setMessage("Please wait, while we are allowing you to login using your Google Account...");
//            loadingBar.setCanceledOnTouchOutside(true);
//            loadingBar.show();
//
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//
//            if (result.isSuccess())
//            {
//                GoogleSignInAccount account = result.getSignInAccount();
//                firebaseAuthWithGoogle(account);
//                Toast.makeText(this, "Please wait, while we are getting your auth result...", Toast.LENGTH_SHORT).show();
//            }
//            else
//            {
//                Toast.makeText(this, "Can't get Auth result.", Toast.LENGTH_SHORT).show();
//                loadingBar.dismiss();
//            }
//        }
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        firebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful())
//                        {
//                            Log.d(TAG, "signInWithCredential:success");
//                            SendUserToHomeActivity();
//                            loadingBar.dismiss();
//                        }
//                        else
//                        {
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            String message = task.getException().toString();
//                            SendUserToLoginActivity();
//                            Toast.makeText(LoginActivity.this, "Not Authenticated : " + message, Toast.LENGTH_SHORT).show();
//                            loadingBar.dismiss();
//                        }
//                    }
//                });
//
    }
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if(currentUser != null)
        {
            SendUserToHomeActivity();
        }

    }

    private void SendUserToHomeActivity()
    {
        Intent mainIntent = new Intent(LoginActivity.this, Farmers_Home.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }



    private void SendUserToLoginActivity()
    {
        Intent mainIntent = new Intent(LoginActivity.this, LoginActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }



    /////////////////////////////////////////////
    //////////SignUp Activity/////////////////////
    /////////////////////////////////////////////
    public void signupbtn(View view) {
        Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    /////////////////////////////////////////////
    //////////Forgotten Activity/////////////////////
    /////////////////////////////////////////////
    public void forgotbtn(View view) {
        Intent i = new Intent(LoginActivity.this, ForgotActivity.class);
        startActivity(i);
    }

    /////////////////////////////////////////////
    //////////Open Home activity/////////////////
    /////////////////////////////////////////////


    public void loginbtn(View view) {

        final String email = loguser.getText().toString();
        final String password = logpass.getText().toString();


        if(password.length() == 0 && email.length() == 0){
            Toast.makeText(LoginActivity.this, "Fill All Fields", Toast.LENGTH_LONG).show();
        }else if(category_no == -1){
            Toast.makeText(this, "Please Select Your Category", Toast.LENGTH_SHORT).show();
        }
        else{


                try {
                        loadingBar.setTitle("Login Your Account");
                        loadingBar.setMessage("Please wait, while we are logging your Account...");
                        loadingBar.show();
                        loadingBar.setCanceledOnTouchOutside(true);
                        firebaseAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            String message = task.getException().getMessage();
                                            Toast.makeText(LoginActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();

                                        } else {
                                            loadingBar.dismiss();
                                            Toast.makeText(
                                                    LoginActivity.this,
                                                    "සාදරයෙන් පිලිගන්නවා!",
                                                    Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(LoginActivity.this, Farmers_Home.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            loadingBar.dismiss();
                                            finish();
                                        }

                                    }
                                });

                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "Error Occured: " + e, Toast.LENGTH_SHORT).show();
                }



        }


    }

    /////////////////////////////////////////////
    //////////Back Button////////////////////////
    /////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        ViewDialog alert = new ViewDialog();
        alert.showDialog(LoginActivity.this, "Warning");
    }



    /////////////////////////////////////////////
    //////////Check Internet/////////////////////
    /////////////////////////////////////////////
    private void checkConnection(){
        connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "" + connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }




}
