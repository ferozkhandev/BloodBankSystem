package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditProfile extends AppCompatActivity {
    private EditText name,email,bloodgroup,age,address,contact;
    private TextView displayName;
    private ProfileAttribs profileAttribs;
    private DatabaseReference databaseReference;
    private String user_id;
    private Uri filePath;
    private ImageButton profile_Photo;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
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

        storageReference = FirebaseStorage.getInstance().getReference("Users");
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
                //uploadFile();
                databaseReference.setValue(map);
                Toast.makeText(getApplicationContext(),"Edited Successfully",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    private void pickImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {
            filePath = data.getData();
            //Picasso.with
            profile_Photo.setImageURI(filePath);
            uploadFile();
        } else if (data == null)
        {
            Toast.makeText(getApplicationContext(),"Data is null",Toast.LENGTH_SHORT).show();
        }
        else if (data.getData() == null)
        {
            Toast.makeText(getApplicationContext(),"Data.data is null",Toast.LENGTH_SHORT).show();
        }
        else if (requestCode != PICK_IMAGE)
        {
            Toast.makeText(getApplicationContext(),"Request code is not equal to pick image",Toast.LENGTH_SHORT).show();
        }
        else if(requestCode != RESULT_OK)
        {
            Toast.makeText(getApplicationContext(),"Not result ok",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.e("Error","Error");
        }
    }
    private String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadFile()
    {
        if (filePath != null)
        {
            StorageReference storageReference1 = storageReference.child(System.currentTimeMillis()+"."+
            getFileExtension(filePath));
            storageReference1.putFile(filePath)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Upload_Image upload_image = new Upload_Image(taskSnapshot.getDownloadUrl().toString());
                    String uploadID = databaseReference.push().getKey();
                    databaseReference.child(uploadID).setValue(upload_image);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(),"Not File Selected",Toast.LENGTH_SHORT).show();
        }
    }
}
