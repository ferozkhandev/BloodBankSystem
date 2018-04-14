package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home_Page extends AppCompatActivity {
    CardView logout,profile;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__page);

        //Initialized card views
        profile = findViewById(R.id.profile);
        logout = findViewById(R.id.logout);

        auth = FirebaseAuth.getInstance();

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
                setLogout();
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
