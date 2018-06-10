package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class Donors_List extends AppCompatActivity {
    private static final String TAG = "UserList" ;
    private FirebaseFirestore userlistFirestore,emailFirestore;
    private List<Donors> usernamelist;
    private ArrayAdapter arrayAdapter;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private RecyclerView donorsList;
    private ImageView emptyView;
    private RecyclerView.LayoutManager mLayoutManager;
    private String cBloodgroup,userID;
    private RecyclerViewCustomAdapter recyclerViewCustomAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donors__list);
        userlistFirestore = FirebaseFirestore.getInstance();
        emailFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        donorsList = findViewById(R.id.donors_recycler_view);
        emptyView = findViewById(R.id.empty_view);
        userID = firebaseAuth.getUid();

        usernamelist = new ArrayList<Donors>();
        recyclerViewCustomAdapter = new RecyclerViewCustomAdapter(Donors_List.this,usernamelist);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        donorsList.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        donorsList.setLayoutManager(mLayoutManager);
        donorsList.setAdapter(recyclerViewCustomAdapter);
        onStart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        userlistFirestore.collection("Users").addSnapshotListener(Donors_List.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                usernamelist.clear();
                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges())
                {
                    if(doc.getType() == DocumentChange.Type.ADDED)
                    {
                        recyclerViewCustomAdapter.notifyDataSetChanged();
                        //Donors donors = doc.getDocument().toObject(Donors.class);
                        String uuid = doc.getDocument().getId();
                        String name = doc.getDocument().getData().get("Name").toString();
                        String bloodGroup = doc.getDocument().getData().get("Blood_Group").toString();
                        String email = doc.getDocument().getData().get("Email").toString();
                        String image = null;
                        try
                        {
                            image = doc.getDocument().getData().get("image").toString();
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
                        }
                        Donors donors1;
                        if(image != null)
                        {
                            donors1 = new Donors(uuid,name,bloodGroup, email,image);
                        }
                        else
                        {
                            donors1 = new Donors(uuid,name, bloodGroup,email);
                        }
                        if(!firebaseAuth.getCurrentUser().getEmail().equals(email) && getIntent().getStringExtra("bloodgroup").equals(bloodGroup))
                        {
                            usernamelist.add(donors1);
                        }
                    }
                }
                if (usernamelist.isEmpty()) {
                    donorsList.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"No Donor available there",Toast.LENGTH_SHORT).show();
                }
                else {
                    donorsList.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
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
