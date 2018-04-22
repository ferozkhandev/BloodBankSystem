package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Donors_List extends AppCompatActivity {
    private static final String TAG = "UserList" ;
    private DatabaseReference userlistReference,emailRefrence;
    private ArrayList<Donors> usernamelist;
    private ArrayAdapter arrayAdapter;
    private FirebaseAuth firebaseAuth;
    private RecyclerView donorsList;
    private RecyclerView.LayoutManager mLayoutManager;
    private String name,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donors__list);
        userlistReference = FirebaseDatabase.getInstance().getReference().child("Users");
        emailRefrence = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        donorsList = findViewById(R.id.donors_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        donorsList.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        donorsList.setLayoutManager(mLayoutManager);

        onStart();
    }
    @Override
    protected void onStart() {
        super.onStart();
        Query donors = userlistReference.orderByChild("Email");
        donors.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren() != null)
                {
                    usernamelist = new ArrayList<Donors>();
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        if (snapshot.getValue() != null)
                        {
                            if (snapshot.child("Name").getValue() != null)
                            {
                                name = snapshot.child("Name").getValue().toString();
                            }
                            else
                            {
                                name = "";
                            }
                            if (snapshot.child("Email").getValue()  != null)
                            {
                                email = snapshot.child("Email").getValue().toString();
                            }
                            else
                            {
                                email = "";
                            }
                            Donors donors = new Donors(name,email);
                            usernamelist.add(donors);
                        }
                        usernamelist.remove(usernameOfCurrentUser());
                        Log.i(TAG, "onDataChange: "+usernamelist.toString());

                        // specify an adapter (see also next example)
                        donorsList.setAdapter(new RecyclerViewCustomAdapter(usernamelist));
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Null",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public String usernameOfCurrentUser()
    {
        String email = firebaseAuth.getCurrentUser().getEmail();
        if (email.contains("@")) {
            return email.split("@")[0];
        }
        else
        {
            return email;
        }
    }
    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
//        if (mUserListListener != null) {
//            userlistReference.removeEventListener(mUserListListener);
//        }
    }
    private ArrayList<String> collectEmails(Map<String,Object> donors) {

    ArrayList<String> emails = new ArrayList<>();

    //iterate through each user, ignoring their UID
    for (Map.Entry<String, Object> entry : donors.entrySet()){

        //Get user map
        Map singleUser = (Map) entry.getValue();
        //Get phone field and append to list
        emails.add((String) singleUser.get("Email"));
    }

    return emails;
}
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.action_logout:
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(this, MainActivity.class));
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
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
