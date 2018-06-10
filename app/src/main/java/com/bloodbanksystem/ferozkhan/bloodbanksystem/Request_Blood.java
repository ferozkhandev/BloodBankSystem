package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Request_Blood extends AppCompatActivity {
    private TextView name,email,bloodgroup,age,address,contact, displayName;
    private String sname,semail,sbloodgroup,sage,saddress,scontact,sUserImage, mCrrentID,targetTokenID;
    private ProfileAttribs profileAttribs;
    private FirebaseFirestore firebaseFirestore;
    private CircleImageView profilePic;
    private StorageReference storageReference;
    private String user_id,intentbloudgroup;
    private FirebaseAuth firebaseAuth;
    private Button btn_Request;
    private String longitude,latitude;
    private Location location = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request__blood);
        loading();
        name = findViewById(R.id.dname);
        email = findViewById(R.id.demail);
        age = findViewById(R.id.dAge);
        address = findViewById(R.id.daddress);
        contact = findViewById(R.id.dcontact);
        displayName = findViewById(R.id.donor_profile_name);
        bloodgroup = findViewById(R.id.d_blood_group);
        btn_Request = findViewById(R.id.requestBlood);
        profilePic = findViewById(R.id.donor_profile_photo);

        checkUserPermsions();
        if(location != null)
        {
            longitude = String.valueOf(location.getLongitude());
            latitude = String.valueOf(location.getLatitude());
        }
        /*MyLocationClass myLocationClass = new MyLocationClass();
        if(myLocationClass.location.getLongitude() != 0 && myLocationClass.location.getLatitude() != 0)
        {
            longitude = String.valueOf(myLocationClass.location.getLongitude());
            latitude = String.valueOf(myLocationClass.location.getLatitude());
        }
        else
        {
            myLocationClass.location.setLongitude(21);
            myLocationClass.location.setLatitude(21);
            longitude = String.valueOf(myLocationClass.location.getLongitude());
            latitude = String.valueOf(myLocationClass.location.getLatitude());
        }*/
        firebaseAuth = FirebaseAuth.getInstance();
        //User ID
        mCrrentID = FirebaseAuth.getInstance().getUid();
        //Target User ID
        user_id = getIntent().getStringExtra("UUID");

        intentbloudgroup = getIntent().getStringExtra("bloodgroup");
        storageReference = FirebaseStorage.getInstance().getReference().child("Users").child("profile.jpg");
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").document(user_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                sname = documentSnapshot.getString("Name");
                sUserImage = documentSnapshot.getString("image");
                semail = documentSnapshot.getString("Email");
                sage = documentSnapshot.getString("Age");
                saddress = documentSnapshot.getString("Address");
                scontact = documentSnapshot.getString("Contact");
                sbloodgroup = documentSnapshot.getString("Blood_Group");

                Glide.with(Request_Blood.this)
                        .load(sUserImage)
                        .into(profilePic);
                displayName.setText(sname);
                name.setText(("Name: "+sname));
                email.setText(("Email: "+semail));
                age.setText(("Age: "+sage));
                address.setText(("Address: "+saddress));
                contact.setText(("Contact: "+scontact));
                bloodgroup.setText(("Blood Group: "+sbloodgroup));
            }
        });

        btn_Request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = intentbloudgroup+" blood is required.";
                if(!TextUtils.isEmpty(message))
                {
                    Map<String, Object> notificationMessage = new HashMap<>();
                    notificationMessage.put("Message",message);
                    notificationMessage.put("from",mCrrentID);
                    notificationMessage.put("longitude", longitude);
                    notificationMessage.put("latitude", latitude);
                    firebaseFirestore.collection("Users/"+user_id+"/Notifications").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(),"Request Sent",Toast.LENGTH_SHORT).show();
                        }
                    });
                    /*notificationMessage.remove("Message");
                    notificationMessage.remove("from");
                    firebaseFirestore.collection("Users/"+user_id+"/Requests/"+mCrrentID).add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();
                        }
                    });*/
                }
            }
        });
    }

    private void loading()
    {
        final ProgressDialog progressDialog = new ProgressDialog(Request_Blood.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 4000);
    }

    //access to permsions
    void checkUserPermsions(){
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED  ){
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return ;
            }
        }

        getLocation();// init the contact list

    }
    //get acces to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();// init the contact list
                } else {
                    // Permission Denied
                    Toast.makeText( this,"Location Permission Denied" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void getLocation()
    {
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        try
        {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        catch (SecurityException ex)
        {
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
        /*MyLocationClass myLocationClass = new MyLocationClass(this);
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        try
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,10,myLocationClass);
        }
        catch (SecurityException ex)
        {
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
