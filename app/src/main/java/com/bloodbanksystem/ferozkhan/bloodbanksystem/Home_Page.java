package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Home_Page extends AppCompatActivity {
    private CardView logout,profile,blood_compatibilty, need_blood,donate_blood;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__page);

        //Initialized card views
        profile = findViewById(R.id.profile);
        logout = findViewById(R.id.logout);
        blood_compatibilty = findViewById(R.id.bloodcompatibilty);
        need_blood = findViewById(R.id.need_blood);
        donate_blood = findViewById(R.id.donate_blood);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Card View Listeners
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page.this, Profile.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout.setEnabled(false);
                Map<String,Object> tokenRemoveMap = new HashMap<>();
                tokenRemoveMap.put("Token_ID", FieldValue.delete());
                firebaseFirestore.collection("Users").document(auth.getUid()).update(tokenRemoveMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        setLogout();
                    }
                });
            }
        });
        blood_compatibilty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page.this, BloodCompatibility.class);
                startActivity(intent);
            }
        });
        need_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page.this, Need_Blood.class);
                startActivity(intent);
            }
        });

        donate_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Page.this, Donate_Blood.class);
                startActivity(intent);
            }
        });
    }

    //Logout Function
    private void setLogout()
    {
        auth.signOut();
        // this listener will be called when there is change in firebase user session
        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user == null) {

                    // user auth state is changed - user is null
                    // launch login activity
                    Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Home_Page.this, MainActivity.class);
                    Home_Page.this.startActivity(intent);
                    Home_Page.this.finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"can't logout",Toast.LENGTH_SHORT).show();
                }
            }
        };
        authListener.onAuthStateChanged(auth);
    }
}
