package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    private EditText name,email,age,address,contact;
    private TextView displayName;
    private String bloodgroup;
    private FirebaseFirestore firebaseFirestore;
    private String user_id,downloadURL;
    private Uri filePath;
    private CircleImageView profile_Photo;
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
        btn_save = findViewById(R.id.Save);
        displayName = findViewById(R.id.user_profile_name);

        final Spinner spinner = findViewById(R.id.blood_group);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.blood_group, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try
                {
                    bloodgroup = spinner.getSelectedItem().toString();
                }
                catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(),"Error: "+ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        downloadURL = "";
        user_id = "";
        storageReference = FirebaseStorage.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        try
        {
            user_id = firebaseAuth.getCurrentUser().getUid();
        }
        catch (NullPointerException ex)
        {
            Toast.makeText(getApplicationContext(),"Error: "+ex.getMessage(),Toast.LENGTH_LONG);
        }
        firebaseFirestore = FirebaseFirestore.getInstance();


        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                displayName.setText(extras.getString("name"));
                name.setHint("Name");
                name.setText((extras.getString("name")));
                email.setHint("Email");
                email.setText((extras.getString("email")));
                age.setHint("Age");
                age.setText((extras.getString("age")));
                address.setHint("Address");
                address.setText((extras.getString("address")));
                contact.setHint("Contact");
                contact.setText((extras.getString("contact")));
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("STRING_I_NEED");
        }


        profile_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
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
            profile_Photo.setImageURI(filePath);
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
        else
        {
            Log.e("Error","Error");
        }
    }
    private void editProfile()
    {
        btn_save.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(EditProfile.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Editing Account...");
        progressDialog.show();

        final String names = name.getText().toString();
        final String emails = email.getText().toString();
        final String bloodGroup = bloodgroup;
        final String ages = age.getText().toString();
        final String addresses = address.getText().toString();
        final String contacts = contact.getText().toString();

        if(filePath != null)
        {
            final String user_id = firebaseAuth.getCurrentUser().getUid();
            StorageReference user_profile_pic = storageReference.child(user_id+".jpg");
            user_profile_pic.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        String downloadURL = task.getResult().getDownloadUrl().toString();
                        Map<String,Object> map = new HashMap();
                        map.put("Name",names);
                        map.put("Email", emails);
                        map.put("Age", ages);
                        map.put("Blood_Group",bloodGroup);
                        map.put("Address",addresses);
                        map.put("Contact",contacts);
                        map.put("image",downloadURL);
                        firebaseFirestore.collection("Users").document(user_id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Edited Successfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditProfile.this, Profile.class);
                                startActivity(intent);
                                //finish();
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Error: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else
        {
            Map<String,Object> map = new HashMap();
            map.put("Name",names);
            map.put("Email", emails);
            map.put("Age", ages);
            map.put("Blood_Group",bloodGroup);
            map.put("Address",addresses);
            map.put("Contact",contacts);
            firebaseFirestore.collection("Users").document(user_id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(),"Edited Successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProfile.this, Profile.class);
                    startActivity(intent);
                    //finish();
                }
            });
        }
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onEditSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 15000);
    }
    public void onEditSuccess() {
        btn_save.setEnabled(true);
        setResult(RESULT_OK, null);
        EditProfile.this.finish();
    }
    public boolean validate() {
        boolean valid = true;

        String names = name.getText().toString();
        String emails = email.getText().toString();
        String ages = age.getText().toString();

        if (names.isEmpty() || names.length() < 3) {
            name.setError("At least 3 characters");
            valid = false;
        } else {
            name.setError(null);
        }

        if (emails.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emails).matches()) {
            email.setError("Enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (ages.isEmpty() || Integer.parseInt(age.getText().toString()) < 0) {
            age.setError("Please enter a valid age.");
            valid = false;
        } else {
            age.setError(null);
        }

        return valid;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent intent = new Intent(EditProfile.this, Profile.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
