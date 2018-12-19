package com.site11.funwithultimate.trendingfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.site11.funwithultimate.trendingfood.Home.Farmer.Farmers_Home;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

public class SettingActivity extends AppCompatActivity {

    private Button UpdateAccountSettingButton;
    private CircleImageView userProfImage;
    private EditText userName, userProfName, userStatus, userGender, userRelation, userDOB,userCharacter,userProvince,userDistrict,userTown;

    private DatabaseReference SettinguserRef;
    private FirebaseAuth mAuth;
    private StorageReference UserProfileImageRef;

    final static int Gallery_Pick = 1;
    private ProgressDialog loadingBar;

    private String currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        SettinguserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        loadingBar = new ProgressDialog(this);

        //mToolbar = findViewById(R.id.settings_toolbar);
        //setSupportActionBar(mToolbar);
        //getSupportActionBar().setTitle("Account Setting");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userName = findViewById(R.id.settings_username);
        userProfName = findViewById(R.id.setting_profile_full_name);
        userStatus = findViewById(R.id.settings_status);
        userGender = findViewById(R.id.setting_gender);
        userRelation = findViewById(R.id.setting_relationship_status);
        userDOB = findViewById(R.id.setting_dob);
        userCharacter = findViewById(R.id.setting_character);
        userProvince = findViewById(R.id.setting_province);
        userDistrict = findViewById(R.id.setting_district);
        userTown = findViewById(R.id.setting_town);

        userProfImage = findViewById(R.id.settings_profile_image);
        UpdateAccountSettingButton = findViewById(R.id.update_account_setting_button);

        SettinguserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    String myProfileImage = dataSnapshot.child("profileimage").getValue().toString();
                    String myUserName = dataSnapshot.child("username").getValue().toString();
                    String myProfileName = dataSnapshot.child("fullname").getValue().toString();
                    String myProfileStatus = dataSnapshot.child("status").getValue().toString();
                    String myDOB = dataSnapshot.child("dob").getValue().toString();
                    String myGender = dataSnapshot.child("gender").getValue().toString();
                    String myRelationshipStatus = dataSnapshot.child("relationshipstatus").getValue().toString();
                    String myCharacter = dataSnapshot.child("character").getValue().toString();
                    String myProvince = dataSnapshot.child("userprovince").getValue().toString();
                    String myDistrict = dataSnapshot.child("userdistrict").getValue().toString();
                    String myTown = dataSnapshot.child("usertown").getValue().toString();

                    Picasso.get().load(myProfileImage).placeholder(R.drawable.profile).into(userProfImage);

                    userName.setText(myUserName);
                    userProfName.setText(myProfileName);
                    userStatus.setText(myProfileStatus);
                    userDOB.setText(myDOB);
                    userGender.setText(myGender);
                    userRelation.setText(myRelationshipStatus);
                    userCharacter.setText(myCharacter);
                    userProvince.setText(myProvince);
                    userDistrict.setText(myDistrict);
                    userTown.setText(myTown);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        UpdateAccountSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateAccountInfo();
            }
        });

        userProfImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        // crop button
        if(requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                loadingBar.setTitle("Profile Image");
                loadingBar.setMessage("Please wait, while we updating your profile image...");
                loadingBar.setCanceledOnTouchOutside(true);
                loadingBar.show();


                Uri resultUri = result.getUri();

                StorageReference filePath = UserProfileImageRef.child(currentUserId + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {

                            Toast.makeText(SettingActivity.this, "Profile Image stored successfully to Firebase storage...", Toast.LENGTH_SHORT).show();

                            Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();

                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUrl = uri.toString();

                                    SettinguserRef.child("profileimage").setValue(downloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Intent selfIntent = new Intent(SettingActivity.this, SettingActivity.class);
                                                        startActivity(selfIntent);

                                                        Toast.makeText(SettingActivity.this, "Profile Image stored to Firebase Database Successfully...", Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                    } else {
                                                        String message = task.getException().getMessage();
                                                        Toast.makeText(SettingActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                    }
                                                }
                                            });
                                }
                            });
                        }
                    }
                });
            }
            else {
                Toast.makeText(this, "Error Occured: Image can not be cropped. Try Again.", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }
    private void ValidateAccountInfo() {
        String username = userName.getText().toString();
        String profileName = userProfName.getText().toString();
        String status = userStatus.getText().toString();
        String dob = userDOB.getText().toString();
        String gender = userGender.getText().toString();
        String relation = userRelation.getText().toString();
        String character = userCharacter.getText().toString();
        String userprovince = userProvince.getText().toString();
        String userdistrict = userDistrict.getText().toString();
        String usertown = userTown.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please write your username", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(profileName)){
            Toast.makeText(this, "Please write your profileName", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(status)){
            Toast.makeText(this, "Please write your status", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(dob)){
            Toast.makeText(this, "Please write your dob", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(gender)){
            Toast.makeText(this, "Please write your gender", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(relation)){
            Toast.makeText(this, "Please write your relation", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(character)){
            Toast.makeText(this, "Please write your character", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(userprovince)){
            Toast.makeText(this, "Please write your userprovince", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(userdistrict)){
            Toast.makeText(this, "Please write your userdistrict", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(usertown)){
            Toast.makeText(this, "Please write your usertown", Toast.LENGTH_SHORT).show();
        }else {
            loadingBar.setTitle("Profile Image");
            loadingBar.setMessage("Please wait, while we updating your profile image...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            UpdateAccountInfo(username,profileName,status,dob,gender,relation,character,userprovince,userdistrict,usertown);
        }



    }

    private void UpdateAccountInfo(String username, String profileName, String status, String dob, String gender, String relation,String character,String userprovince,String userdistrict,String usertown) {

        HashMap userMap = new HashMap();

        userMap.put("username", username);
        userMap.put("fullname", profileName);
        userMap.put("status", status);
        userMap.put("dob", dob);
        userMap.put("gender", gender);
        userMap.put("relationshipstatus", relation);
        userMap.put("character", character);
        userMap.put("userprovince", userprovince);
        userMap.put("userdistrict", userdistrict);
        userMap.put("usertown", usertown);

        SettinguserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    SendUserToMainActivity();
                    Toast.makeText(SettingActivity.this, "Account Setting Update Successfully", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }else {
                    Toast.makeText(SettingActivity.this, "Error Occurd, while updating account setting info ", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });

    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(SettingActivity.this, Farmers_Home.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
