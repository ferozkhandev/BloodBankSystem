package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class Requesters_List extends AppCompatActivity {

    private static final String TAG = "UserList" ;
    private FirebaseFirestore userlistFirestore,emailFirestore;
    private List<Donors> usernamelist;
    private List<String> uuidCollection;
    private ArrayAdapter arrayAdapter;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private RecyclerView donorsList;
    private ImageView emptyView;
    private RecyclerView.LayoutManager mLayoutManager;
    private String cBloodgroup,userID;
    private RecyclerViewCustomAdapter recyclerViewCustomAdapter;
    private boolean uuidExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requesters__list);
        userlistFirestore = FirebaseFirestore.getInstance();
        emailFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        donorsList = findViewById(R.id.donateblood_recycler_view);
        emptyView = findViewById(R.id.donateblood_empty_view);

        uuidExist = false;
        usernamelist = new ArrayList<Donors>();
        recyclerViewCustomAdapter = new RecyclerViewCustomAdapter(Requesters_List.this,usernamelist);

        uuidCollection = new ArrayList<String>();
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
        usernamelist.clear();
        userlistFirestore.collection("Users").document(userID).collection("Notifications").addSnapshotListener(Requesters_List.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges())
                {
                    if(doc.getType() == DocumentChange.Type.ADDED)
                    {
                        //recyclerViewCustomAdapter.notifyDataSetChanged();
                        Donors donors = doc.getDocument().toObject(Donors.class);
                        String uid = doc .getDocument().getData().get("from").toString();

                        if(uuidCollection.contains(uid))
                        {
                            continue;
                        }
                        else
                        {
                            listRequesters(uid);
                        }
                        uuidCollection.add(uid);
                    }
                }
            }
        });
        if (usernamelist.isEmpty())
        {
            donorsList.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),"No Donor available there",Toast.LENGTH_SHORT).show();
        }
        else
        {
            donorsList.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    public void listRequesters(final String uuid)
    {
        userlistFirestore.collection("Users").document(uuid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String bloodGroup = documentSnapshot.getString("Blood_Group");
                String email = documentSnapshot.getString("Email");
                String name = documentSnapshot.getString("Name");
                String image = null;
                try {
                    image = documentSnapshot.getString("image");
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
                Donors donors1;
                if (image != null) {
                    donors1 = new Donors(uuid, name, bloodGroup, email, image);
                } else {
                    donors1 = new Donors(uuid, name, bloodGroup, email);
                }
                //if (!firebaseAuth.getCurrentUser().getEmail().equals(email)) {
                    usernamelist.add(donors1);
                //}
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
