package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bloodbanksystem.ferozkhan.bloodbanksystem.EditProfile.PICK_IMAGE;

public class Signup extends AppCompatActivity{
    TextView loginlink;
    Button btn_signup;
    private EditText username, email, password;
    private String bloodGroup;
    private CircleImageView proPic;
    private FirebaseAuth firebaseAuth;
    private Uri imageURI;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Firebase Initializations
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("images");
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Initialize Edit Texts
        username = findViewById(R.id.input_name);
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);

        //Initialize URI
        imageURI = null;

        //Initialize ImageButton
        proPic = findViewById(R.id.uploadimage);
        proPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });

        //Populate Spinner
        final Spinner spinner = findViewById(R.id.blood_dropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.blood_group, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try
                {
                    bloodGroup = spinner.getSelectedItem().toString();
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

        //Back Navigation to Login Page
        loginlink = findViewById(R.id.link_login);
        loginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Signup Event
        btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    signup();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {
            imageURI = data.getData();/*
            try
            {
                InputStream imageStream = getContentResolver().openInputStream(imageURI);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                selectedImage = ImageSizeReducer.getResizedBitmap(selectedImage,640,480);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), selectedImage, "Title", null);
                imageURI = Uri.parse(path);*/
                proPic.setImageURI(imageURI);/*
            }
            catch (IOException ex)
            {
                Toast.makeText(getApplicationContext(),"Error: "+ex.getMessage(),Toast.LENGTH_LONG).show();
            }*/
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

    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }

        btn_signup.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Signup.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String name = username.getText().toString();
        final String emails = email.getText().toString();
        final String passwords = password.getText().toString();

        // TODO: Implement your own signup logic here.
        firebaseAuth.createUserWithEmailAndPassword(emails,passwords)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            final String user_id = firebaseAuth.getCurrentUser().getUid();
                            StorageReference user_profile_pic = storageReference.child(user_id+".jpg");
                            user_profile_pic.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if(task.isSuccessful())
                                    {
                                        String downloadURL = task.getResult().getDownloadUrl().toString();
                                        Map<String,Object> map = new HashMap();
                                        map.put("Name",name);
                                        map.put("Email", emails);
                                        map.put("Password", passwords);
                                        map.put("Blood_Group",bloodGroup);
                                        map.put("image",downloadURL);
                                        firebaseFirestore.collection("Users").document(user_id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent intent = new Intent(Signup.this, Home_Page.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                        Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Error: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Could not register",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        btn_signup.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btn_signup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String names = username.getText().toString();
        String emails = email.getText().toString();
        String passwords = password.getText().toString();

        if (names.isEmpty() || names.length() < 3) {
            username.setError("at least 3 characters");
            valid = false;
        } else {
            username.setError(null);
        }

        if (emails.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emails).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (passwords.isEmpty() || passwords.length() < 4 || passwords.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
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
