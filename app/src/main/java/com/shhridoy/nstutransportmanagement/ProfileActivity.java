package com.shhridoy.nstutransportmanagement;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shhridoy.nstutransportmanagement.myObjects.Profile;
import com.shhridoy.nstutransportmanagement.myUtilities.AppPreferences;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileActivity extends AppCompatActivity {

    // firebase objects
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    // views
    private EditText nameET, designationET, phoneET, emailET, passwordET1, passwordET2;
    private ImageButton visibilityImgBtn1, visibilityImgBtn2;
    private RadioGroup radioGroupGender, radioGroupDesignation;
    private RadioButton maleRB, femaleRB, othersRB;
    private Button saveBtn;
    boolean isPassword1Visible = false;
    boolean isPassword2Visible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        getCurrentUserData();

    }

    private void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeViews();
        disableViews(true);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        clickListeners();
    }

    private void initializeViews() {
        nameET = findViewById(R.id.prfileNameET);
        designationET = findViewById(R.id.profileDesignationET);
        phoneET = findViewById(R.id.profileMobileNmbrET);
        emailET = findViewById(R.id.profileEmailET);
        passwordET1 = findViewById(R.id.profilePasswordET1);
        passwordET2 = findViewById(R.id.profilePasswordET2);
        visibilityImgBtn1 = findViewById(R.id.visibilityImgBtn1);
        visibilityImgBtn2 = findViewById(R.id.visibilityImgBtn2);
        radioGroupGender = findViewById(R.id.profileGenderRG);
        radioGroupDesignation = findViewById(R.id.profileDesignationRG);
        maleRB = findViewById(R.id.radioBtnMale);
        femaleRB = findViewById(R.id.radioBtnFemale);
        othersRB = findViewById(R.id.radioBtnOthers);
        saveBtn = findViewById(R.id.profileSaveBtn);
    }

    private void clickListeners() {
        visibilityImgBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isPassword1Visible) {
                    visibilityImgBtn1.setImageResource(R.drawable.ic_action_visibility_off);
                    // hide password
                    passwordET1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPassword1Visible = false;
                } else {
                    visibilityImgBtn1.setImageResource(R.drawable.ic_action_visible);
                    // show password
                    passwordET1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isPassword1Visible = true;
                }

            }
        });

        visibilityImgBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isPassword2Visible) {
                    visibilityImgBtn2.setImageResource(R.drawable.ic_action_visibility_off);
                    // hide password
                    passwordET2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPassword2Visible = false;
                } else {
                    visibilityImgBtn2.setImageResource(R.drawable.ic_action_visible);
                    // show password
                    passwordET2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isPassword2Visible = true;
                }

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserProfile();
            }
        });
    }

    private void disableViews(boolean b) {
        if (b) {
            // disable every thing
            nameET.setFocusable(false);
            nameET.setFocusableInTouchMode(false);
            nameET.setEnabled(false);
            designationET.setFocusable(false);
            designationET.setFocusableInTouchMode(false);
            designationET.setEnabled(false);
            phoneET.setFocusable(false);
            phoneET.setFocusableInTouchMode(false);
            phoneET.setEnabled(false);
            emailET.setFocusable(false);
            emailET.setFocusableInTouchMode(false);
            emailET.setEnabled(false);
            passwordET1.setFocusable(false);
            passwordET1.setFocusableInTouchMode(false);
            passwordET1.setEnabled(false);
            passwordET2.setFocusable(false);
            passwordET2.setFocusableInTouchMode(false);
            passwordET2.setEnabled(false);
            for(int i = 0; i < radioGroupDesignation.getChildCount(); i++){
                radioGroupDesignation.getChildAt(i).setEnabled(false);
            }
            for(int i = 0; i < radioGroupGender.getChildCount(); i++){
                radioGroupGender.getChildAt(i).setEnabled(false);
            }
            saveBtn.setVisibility(View.GONE);
        } else {
            // enable every thing
            nameET.setFocusable(true);
            nameET.setFocusableInTouchMode(true);
            nameET.setEnabled(true);
            designationET.setFocusable(true);
            designationET.setFocusableInTouchMode(true);
            designationET.setEnabled(true);
            phoneET.setFocusable(true);
            phoneET.setFocusableInTouchMode(true);
            phoneET.setEnabled(true);
            passwordET1.setFocusable(true);
            passwordET1.setFocusableInTouchMode(true);
            passwordET1.setEnabled(true);
            passwordET2.setFocusable(true);
            passwordET2.setFocusableInTouchMode(true);
            passwordET2.setEnabled(true);
            for(int i = 0; i < radioGroupDesignation.getChildCount(); i++){
                radioGroupDesignation.getChildAt(i).setEnabled(true);
            }
            for(int i = 0; i < radioGroupGender.getChildCount(); i++){
                radioGroupGender.getChildAt(i).setEnabled(true);
            }
            saveBtn.setVisibility(View.VISIBLE);
        }
    }

    private void getCurrentUserData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting user data....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        databaseReference.child("Users").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                String name = dataSnapshot.child("name").getValue().toString();
                String pass = dataSnapshot.child("password").getValue().toString();
                String gender = dataSnapshot.child("gender").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String mobile = dataSnapshot.child("mobile").getValue().toString();
                String designation = dataSnapshot.child("designation").getValue().toString();
                setCurrentUserData(name, pass, gender, email, mobile, designation);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    private void setCurrentUserData(String name, String pass, String gender, String email, String mobile, String designation) {
        nameET.setText(name);
        designationET.setText(designation);
        phoneET.setText(mobile);
        emailET.setText(email);
        passwordET1.setText(pass);
        if (gender.contains("Male")) {
            radioGroupGender.check(R.id.radioBtnMale);
        } else if (gender.contains("Female")) {
            radioGroupGender.check(R.id.radioBtnFemale);
        } else {
            radioGroupGender.check(R.id.radioBtnOthers);
        }
        if (designation.contains("Student")) {
            radioGroupDesignation.check(R.id.radioBtnStudent);
        } else if (designation.contains("Teacher")) {
            radioGroupDesignation.check(R.id.radioBtnTeacher);
        } else {
            radioGroupDesignation.check(R.id.radioBtnStuff);
        }
    }

    private void updateUserProfile() {

        final String name = nameET.getText().toString().trim();
        String designation = "";//designationET.getText().toString().trim();
        if (radioGroupDesignation.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "Please select Designation", Toast.LENGTH_SHORT).show();
        } else {
            // get selected radio button from radioGroup
            int selectedId = radioGroupDesignation.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            RadioButton radioButtonDesignation = findViewById(selectedId);
            designation = radioButtonDesignation.getText().toString().trim();
        }
        String gender = "";
        if (radioGroupGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "Please select Gender", Toast.LENGTH_SHORT).show();
        } else {
            // get selected radio button from radioGroup
            int selectedId = radioGroupGender.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            RadioButton radioButtonGender = findViewById(selectedId);
            gender = radioButtonGender.getText().toString().trim();
        }
        final String mobile = phoneET.getText().toString().trim();
        final String email = emailET.getText().toString().trim();
        final String password1 = passwordET1.getText().toString().trim();
        //String password2 = passwordET2.getText().toString().trim();

        if (name.length()<=0 || designation.length()<=0 || gender.length()<=0 || email.length()<=0 || password1.length()<=0) {

            Toast.makeText(getApplicationContext(), "No Field should be empty!!", Toast.LENGTH_LONG).show();

        } else {

            final DatabaseReference dbRef = databaseReference.child("Users");

            final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
            progressDialog.setMessage("Updating profile....");
            progressDialog.setCancelable(false);
            progressDialog.show();

            final Profile profile = new Profile(name, designation, gender, mobile, email, password1, firebaseUser.getUid());

            if (AppPreferences.getPreferencePassword(this).equals(password1)) {
                dbRef.child(firebaseUser.getUid()).setValue(profile)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Profile Updated.", Toast.LENGTH_SHORT).show();
                                    disableViews(true);
                                    //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), "Profile can't be Updated!!", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
            } else {
                AuthCredential credential = EmailAuthProvider
                        .getCredential(AppPreferences.getPreferenceEmail(this), AppPreferences.getPreferencePassword(this));
                firebaseUser.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    firebaseUser.updatePassword(password1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                dbRef.child(firebaseUser.getUid()).setValue(profile)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(getApplicationContext(), "Profile Updated.", Toast.LENGTH_SHORT).show();
                                                                    disableViews(true);
                                                                    //AppPreferences.setPreferenceEmail(getApplicationContext(), email);
                                                                    AppPreferences.setPreferencePassword(getApplicationContext(), password1);
                                                                    //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "Profile can't be Updated!!", Toast.LENGTH_SHORT).show();
                                                                    progressDialog.dismiss();
                                                                }
                                                            }
                                                        });
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Password can't update!!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "User auth failed!!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            disableViews(false);
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
