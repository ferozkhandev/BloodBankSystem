package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity{
    private TextView name,email,bloodgroup,age,address,contact, displayName;
    private String sname,semail,sbloodgroup,sage,saddress,scontact, sdisplayName;
    private ProfileAttribs profileAttribs;
    private DatabaseReference databaseReference;
    private CircleImageView profilePic;
    private StorageReference storageReference;
    private String user_id;
    private FirebaseAuth firebaseAuth;
    private Button btn_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        age = findViewById(R.id.Age);
        address = findViewById(R.id.address);
        contact = findViewById(R.id.contact);
        displayName = findViewById(R.id.user_profile_name);
        bloodgroup = findViewById(R.id.blood_group);
        btn_edit = findViewById(R.id.Edit);
        profilePic = findViewById(R.id.user_profile_photo);

        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference().child("Users").child("profile.jpg");
        Glide.with(Profile.this)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(profilePic);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sname = (String) dataSnapshot.child("Users").child(user_id).child("Name").getValue();
                semail = (String) dataSnapshot.child("Users").child(user_id).child("Email").getValue();
                sage = (String) dataSnapshot.child("Users").child(user_id).child("Age").getValue();
                saddress = (String) dataSnapshot.child("Users").child(user_id).child("Address").getValue();
                scontact = (String) dataSnapshot.child("Users").child(user_id).child("Contact").getValue();
                sbloodgroup = (String) dataSnapshot.child("Users").child(user_id).child("Blood_Group").getValue();

                displayName.setText(sname);
                name.setText(("Name: "+sname));
                email.setText(("Email: "+semail));
                age.setText(("Age: "+sage));
                address.setText(("Address: "+saddress));
                contact.setText(("Contact: "+scontact));
                bloodgroup.setText(("Blood Group: "+sbloodgroup));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, EditProfile.class);
                intent.putExtra("name",sname);
                intent.putExtra("email",semail);
                intent.putExtra("age",sage);
                intent.putExtra("address",saddress);
                intent.putExtra("contact",scontact);
                intent.putExtra("blood_Group",sbloodgroup);
                startActivity(intent);
            }
        });
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
