package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity{
    TextView loginlink;
    Button btn_signup;
    private EditText username, email, password;
    private String bloodGroup;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        //Initialize Edit Texts
        username = (EditText) findViewById(R.id.input_name);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);

        //Populate Spinner
        final Spinner spinner = (Spinner) findViewById(R.id.blood_dropdown);
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
                    Toast.makeText(getApplicationContext(),"Error: "+ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        //Back Navigation to Login Page
        loginlink = (TextView) findViewById(R.id.link_login);
        loginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Signup Event
        btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
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
                            String user_id = firebaseAuth.getCurrentUser().getUid();
                            DatabaseReference databaseRefrence = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                            Map map = new HashMap();
                            map.put("Name",name);
                            map.put("Email", emails);
                            map.put("Password", passwords);
                            map.put("Blood_Group",bloodGroup);
                            databaseRefrence.setValue(map);
                            Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_SHORT).show();
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
}
