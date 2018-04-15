package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    private EditText name,email,bloodgroup,age,address,contact;
    private TextView displayName;
    private ProfileAttribs profileAttribs;
    private DatabaseReference databaseReference;
    private String user_id;
    private ImageButton profile_Photo;
    private FirebaseAuth firebaseAuth;
    private Button btn_save;
    public static final int PICK_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profile_Photo = findViewById(R.id.user_profile_photo);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        age = findViewById(R.id.Age);
        address = findViewById(R.id.address);
        contact = findViewById(R.id.contact);
        bloodgroup = findViewById(R.id.blood_group);
        btn_save = findViewById(R.id.Save);
        displayName = findViewById(R.id.user_profile_name);

        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        profile_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map map = new HashMap();
                map.put("Name",name.getText().toString());
                map.put("Age", age.getText().toString());
                map.put("Email", email.getText().toString());
                map.put("Blood_Group",bloodgroup.getText().toString());
                map.put("Contact", contact.getText().toString());
                map.put("Address", address.getText().toString());
                databaseReference.setValue(map);
                Toast.makeText(getApplicationContext(),"Edited Successfully",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            //InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }
}
