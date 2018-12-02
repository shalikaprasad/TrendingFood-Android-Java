package com.site11.funwithultimate.trendingfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

public class Profile_Activity extends AppCompatActivity {

    private EditText UserName, FullName;
    private Button SaveInformationbuttion;
    private CircleImageView ProfileImage;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private StorageReference UserProfileImageRef;

    String currentUserID;
    final static int Gallery_Pick = 1;

    private RadioGroup radiocatogerygroup;
    private RadioButton radiocatogerybtn;
    Spinner province,district,town;
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

        province =findViewById(R.id.province);
        district =findViewById(R.id.district);
        town = findViewById(R.id.town);
        radiocatogerygroup = findViewById(R.id.catogeryrg);

        prellay1 = (RelativeLayout) findViewById(R.id.prellay1);
        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");


        UserName = (EditText) findViewById(R.id.setup_username);
        FullName = (EditText) findViewById(R.id.setup_full_name);
        SaveInformationbuttion = (Button) findViewById(R.id.setup_information_button);
        ProfileImage = findViewById(R.id.setup_profile_image);
        loadingBar = new ProgressDialog(this);

        /////////////////////////////////////////////
        //////////Select Catogory////////////////////
        /////////////////////////////////////////////

        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(Profile_Activity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.province_array));
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province.setAdapter(provinceAdapter);

        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(Profile_Activity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.province_array));
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        district.setAdapter(provinceAdapter);

        ArrayAdapter<String> townAdapter = new ArrayAdapter<String>(Profile_Activity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.province_array));
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        town.setAdapter(provinceAdapter);


        SaveInformationbuttion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                    SaveAccountSetupInformation();
            }
        });


        //Open Gallery
        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });


        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("profileimage"))
                    {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(ProfileImage);
                        //Picasso.with(Profile_Activity.this).load(image).placeholder(R.drawable.profile).into(ProfileImage);
                    }
                    else
                    {
                        Toast.makeText(Profile_Activity.this, "Please select profile image first.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    //get Image and Crop Image

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
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                Uri resultUri = result.getUri();

                StorageReference filePath = UserProfileImageRef.child(currentUserID + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {

                            Toast.makeText(Profile_Activity.this, "Profile Image stored successfully to Firebase storage...", Toast.LENGTH_SHORT).show();

                            Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();

                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUrl = uri.toString();

                                    UsersRef.child("profileimage").setValue(downloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Intent selfIntent = new Intent(Profile_Activity.this, Profile_Activity.class);
                                                        startActivity(selfIntent);

                                                        Toast.makeText(Profile_Activity.this, "Profile Image stored to Firebase Database Successfully...", Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                    } else {
                                                        String message = task.getException().getMessage();
                                                        Toast.makeText(Profile_Activity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
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



    private void SaveAccountSetupInformation()
    {

        String username = UserName.getText().toString();
        String fullname = FullName.getText().toString();

        // get selected radio button from radioGroup
        int selectedId = radiocatogerygroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radiocatogerybtn = findViewById(selectedId);
        final String usercharactor = radiocatogerybtn.getText().toString();



        final String userprovince = province.getSelectedItem().toString();
        final String userdistrict = district.getSelectedItem().toString();
        final String usertown = province.getSelectedItem().toString();


        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please write your username...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(fullname))
        {
            Toast.makeText(this, "Please write your full name...", Toast.LENGTH_SHORT).show();
        }
        if(selectedId < 1)
        {
            Toast.makeText(this, "Please select your Character...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait, while we are creating your new Account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("username", username);
            userMap.put("fullname", fullname);
            userMap.put("character", usercharactor);
            userMap.put("userprovince", userprovince);
            userMap.put("userdistrict", userdistrict);
            userMap.put("usertown", usertown);
            userMap.put("status", "Hey there, i am using Poster Social Network, developed by Coding Cafe.");
            userMap.put("gender", "none");
            userMap.put("dob", "none");
            userMap.put("relationshipstatus", "none");

            UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        SendUserToMainActivity();
                        Toast.makeText(Profile_Activity.this, "your Account is created Successfully.", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message =  task.getException().getMessage();
                        Toast.makeText(Profile_Activity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }



    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(Profile_Activity.this, Farmers_Home.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
