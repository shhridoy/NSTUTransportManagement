package com.shhridoy.transportmanagementnstu;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shhridoy.transportmanagementnstu.myObjects.Profile;
import com.shhridoy.transportmanagementnstu.myViews.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.shhridoy.transportmanagementnstu.myUtilities.Constants.USERS_LIST_TAG;

public class UsersActivity extends AppCompatActivity {

    // firebase objects
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    // views
    private RecyclerView recyclerView;

    private List<Profile> profileList;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        init();

    }

    private void init() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        profileList = new ArrayList<>();

        getUserLists();

    }

    private void getUserLists() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Users....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                profileList.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue().toString();
                    String designation = ds.child("designation").getValue().toString();
                    String gender = ds.child("gender").getValue().toString();
                    String mobile = ds.child("mobile").getValue().toString();
                    String email = ds.child("email").getValue().toString();
                    String password = ds.child("password").getValue().toString();
                    String userId = ds.child("user_id").getValue().toString();

                    profileList.add(new Profile(name, designation, gender, mobile, email, password, userId));
                }

                recyclerViewAdapter = new RecyclerViewAdapter(UsersActivity.this, profileList, USERS_LIST_TAG, databaseReference);
                recyclerView.setAdapter(recyclerViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
