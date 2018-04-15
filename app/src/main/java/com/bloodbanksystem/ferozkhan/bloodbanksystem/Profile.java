package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    private TextView name,email,bloodgroup,age,address,contact, displayName;
    private ProfileAttribs profileAttribs;
    private DatabaseReference databaseReference;
    private String user_id;
    private FirebaseAuth firebaseAuth;
    private Button btn_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        age = findViewById(R.id.Age);
        address = findViewById(R.id.address);
        contact = findViewById(R.id.contact);
        displayName = findViewById(R.id.user_profile_name);
        bloodgroup = findViewById(R.id.blood_group);
        btn_edit = findViewById(R.id.Edit);

        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                displayName.setText((String) dataSnapshot.child("Users").child(user_id).child("Name").getValue());
                name.setText(("Name: "+dataSnapshot.child("Users").child(user_id).child("Name").getValue()));
                email.setText(("Email: "+dataSnapshot.child("Users").child(user_id).child("Email").getValue()));
                age.setText(("Age: "+dataSnapshot.child("Users").child(user_id).child("Age").getValue()));
                address.setText(("Address: "+dataSnapshot.child("Users").child(user_id).child("Address").getValue()));
                contact.setText(("Contact: "+dataSnapshot.child("Users").child(user_id).child("Contact").getValue()));
                bloodgroup.setText(("Blood Group: "+dataSnapshot.child("Users").child(user_id).child("Blood_Group").getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, EditProfile.class);
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
