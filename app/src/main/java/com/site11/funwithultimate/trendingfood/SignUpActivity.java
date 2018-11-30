package com.site11.funwithultimate.trendingfood;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.site11.funwithultimate.trendingfood.Helpers.AESCrypt;


public class SignUpActivity extends AppCompatActivity {

    RelativeLayout rellay1;
    Spinner province,district,town;
    //private static final int SELECT_PHOTO = 100;

    private EditText signusername,signuseremail,signuserpass,signuserpass2;
    private ProgressBar signloadingprogress;
    private RadioGroup radiocatogerygroup;
    private RadioButton radiocatogerybtn;
    Button signupbtn;

    ImageView userphoto;
    static int PReqCode =1;
    static int REQUEST_CODE =1;
    Uri pickeduri;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private boolean setimagesuccess = false;


    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        mAuth = FirebaseAuth.getInstance();

        rellay1 = findViewById(R.id.srellay1);
        province =findViewById(R.id.province);
        district =findViewById(R.id.district);
        town = findViewById(R.id.town);



        signusername = findViewById(R.id.signfullname);
        signuseremail = findViewById(R.id.signuseremail);
        signuserpass = findViewById(R.id.signuserpass);
        signuserpass2 = findViewById(R.id.signuserpass2);

        signloadingprogress = findViewById(R.id.signloading1);
        signupbtn = findViewById(R.id.createuserbtn);

        radiocatogerygroup = findViewById(R.id.catogeryrg);
        signloadingprogress.setVisibility(View.INVISIBLE);


        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash

        /////////////////////////////////////////////
        //////////Select Catogory////////////////////
        /////////////////////////////////////////////

        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(SignUpActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.province_array));
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province.setAdapter(provinceAdapter);

        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(SignUpActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.province_array));
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        district.setAdapter(provinceAdapter);

        ArrayAdapter<String> townAdapter = new ArrayAdapter<String>(SignUpActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.province_array));
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        town.setAdapter(provinceAdapter);



//        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (i == 1) {
//                    //open
//                } else if (i == 2) {
//                    //open
//                }else if (i == 3) {
//                    //open
//                }else if (i == 4) {
//                    //open
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        /////////////////////////////////////////////
        //////////sign up btn/////////////////////////
        /////////////////////////////////////////////

        signupbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signupbtn.setVisibility(View.INVISIBLE);
                signloadingprogress.setVisibility(View.VISIBLE);
                // get selected radio button from radioGroup
                int selectedId = radiocatogerygroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radiocatogerybtn = findViewById(selectedId);
                final String usercharactor = radiocatogerybtn.getText().toString();



                final String userprovince = province.getSelectedItem().toString();
                final String userdistrict = district.getSelectedItem().toString();
                final String usertown = province.getSelectedItem().toString();


                final String setname = signusername.getText().toString();
                final String setemail = signuseremail.getText().toString();
                final String setpass = signuserpass.getText().toString();
                final String setpass2 = signuserpass2.getText().toString();

                if(setemail.isEmpty() || setname.isEmpty() || setpass.isEmpty() || usercharactor.isEmpty() ){
                    showMessage("Please Verify all fields");
                    signupbtn.setVisibility(View.VISIBLE);
                    signloadingprogress.setVisibility(View.INVISIBLE);
                }
                else if(!setpass.equals(setpass2)){
                    Toast.makeText(SignUpActivity.this, "Your Password do not match with your Confirm Password...", Toast.LENGTH_SHORT).show();
                    signupbtn.setVisibility(View.VISIBLE);
                    signloadingprogress.setVisibility(View.INVISIBLE);
                }else if(setpass.length() < 6){
                    Toast.makeText(SignUpActivity.this, "Your Password should be more than 5 Characters ", Toast.LENGTH_SHORT).show();
                    signupbtn.setVisibility(View.VISIBLE);
                    signloadingprogress.setVisibility(View.INVISIBLE);
                }else if (setimagesuccess == false){
                    Toast.makeText(SignUpActivity.this, "Please Check Your Profile Photo", Toast.LENGTH_SHORT).show();
                    signupbtn.setVisibility(View.VISIBLE);
                    signloadingprogress.setVisibility(View.INVISIBLE);
                }
                else
                {
                    try {
                        CreateUserAccount(setemail,setname,setpass,usercharactor,userprovince,userdistrict,usertown);
                    } catch (Exception e) {
                        Toast.makeText(
                                SignUpActivity.this,
                                "Create Account is Failed",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

        });
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        userphoto = findViewById(R.id.display_image);
        userphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setimagesuccess = true;
                if(Build.VERSION.SDK_INT >= 22){

                    checkpermission();
                }else {
                    opengallery();
                }
            }
        });
    }

    /////////////////////////////////////////////
    //////////Create User Account////////////////
    /////////////////////////////////////////////

    private void CreateUserAccount(final String setemail, final String setname, final String setpass, final String character1, final String provice1,final String district1, final String town1) throws Exception {

        AESCrypt aesCryptclass = new AESCrypt();
        final String encriptpass = aesCryptclass.encrypt(setpass);
        mAuth.createUserWithEmailAndPassword(setemail,setpass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            database = FirebaseDatabase.getInstance();
                            myRef = database.getReference("Users").child(setname);
                            myRef.child("Names").setValue(setname);
                            myRef.child("Character").setValue(character1);
                            myRef.child("District").setValue(district1);
                            myRef.child("Province").setValue(provice1);
                            myRef.child("Town").setValue(town1);
                            myRef.child("Email").setValue(setemail);
                            myRef.child("Password").setValue(encriptpass);

                            showMessage("Account Created");
                            updateUserInfo(setname,pickeduri,mAuth.getCurrentUser());
                        }else {
                            showMessage("Account Created Failed" + task.getException().getMessage());
                            signupbtn.setVisibility(View.VISIBLE);
                            signloadingprogress.setVisibility(View.INVISIBLE);

                        }
                    }
                });
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////
    //////////update User Info///////////////////
    /////////////////////////////////////////////

    private void updateUserInfo(final String setname, Uri pickeduri, final FirebaseUser currentUser) {

        try {
            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("user_photos");
            final StorageReference imageFilePath = mStorage.child(pickeduri.getLastPathSegment());
            imageFilePath.putFile(pickeduri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //image upload successfull
                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            UserProfileChangeRequest profileupdate =  new UserProfileChangeRequest.Builder()
                                    .setDisplayName(setname)
                                    .setPhotoUri(uri)
                                    .build();

                            currentUser.updateProfile(profileupdate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                showMessage("Register Complete");
                                                updateUI();
                                            }
                                        }
                                    });
                        }
                    });
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Please Check Your Profile Photo", Toast.LENGTH_SHORT).show();
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void updateUI() {

        Intent homeActivities = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(homeActivities);
        finish();
    }




    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }



/*
    public void open_photo(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivity(intent);
    }
*/

    /////////////////////////////////////////////
    //////////pick Image/////////////////////////
    /////////////////////////////////////////////
//    public void pickAImage(View view) {
//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//        photoPickerIntent.setType("image/*");
//        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//        Uri selectedImage;
//        switch (requestCode) {
//            case SELECT_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    selectedImage = imageReturnedIntent.getData();
//                    SignUpActivity s = new SignUpActivity();
//                    s.pickeduri = selectedImage;
//                    InputStream imageStream = null;
//                    try {
//                        imageStream = getContentResolver().openInputStream(selectedImage);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
//                    image.setImageURI(selectedImage);// To display selected image in image view
//                }
//        }
//
//    }


    private void opengallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUEST_CODE);
    }

    private void checkpermission() {
        if(ContextCompat.checkSelfPermission(SignUpActivity.this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(SignUpActivity.this,"Please accept for requaired permission",Toast.LENGTH_LONG).show();
            }else {
                ActivityCompat.requestPermissions(SignUpActivity.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }else {
            opengallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null){
            pickeduri = data.getData();
            userphoto.setImageURI(pickeduri);
        }else{
            Toast.makeText(
                    SignUpActivity.this,
                    "Set Image Failed",
                    Toast.LENGTH_LONG).show();
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
