package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Donors_List extends AppCompatActivity {
    private static final String TAG = "UserList" ;
    private DatabaseReference userlistReference;
    private ValueEventListener mUserListListener;
    private ArrayList<String> usernamelist;
    private ArrayAdapter arrayAdapter;
    private FirebaseAuth firebaseAuth;
    private ListView userListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donors__list);
        userlistReference = FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        onStart();
        userListView = (ListView) findViewById(R.id.donors_listview);
    }
    @Override
    protected void onStart() {
        super.onStart();
        final ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usernamelist = new ArrayList<String>(collectEmails((Map<String,Object>)dataSnapshot.getValue()));
                usernamelist.remove(usernameOfCurrentUser());
                Log.i(TAG, "onDataChange: "+usernamelist.toString());
                arrayAdapter = new ArrayAdapter<String>(Donors_List.this,android.R.layout.simple_list_item_1,usernamelist);
                userListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled: ",databaseError.toException());
                Toast.makeText(Donors_List.this, "Failed to load User list.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        userlistReference.addListenerForSingleValueEvent(userListener);

        mUserListListener = userListener;
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
        if (mUserListListener != null) {
            userlistReference.removeEventListener(mUserListListener);
        }
    }
    private ArrayList<String> collectEmails(@org.jetbrains.annotations.NotNull Map<String,Object> donors) {

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
