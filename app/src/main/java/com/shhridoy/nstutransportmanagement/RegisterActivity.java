package com.shhridoy.nstutransportmanagement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shhridoy.nstutransportmanagement.myObjects.Profile;
import com.shhridoy.nstutransportmanagement.myUtilities.ExtraUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.shhridoy.nstutransportmanagement.myUtilities.ExtraUtils.IS_INTERNET_ON;

public class RegisterActivity extends AppCompatActivity {

    Button signupBtn;
    ImageButton visibilityImgBtn1, visibilityImgBtn2;
    EditText nameET, designationET, mobileET, emailET, passwordET1, passwordET2;
    TextView nameTV, designationTV, genderTV, mobileTV, emailTV, passwordTV1, passwordTV2;
    TextView signinTV;
    RadioGroup radioGroupGender, radioGroupDesignation;
    RadioButton radioButton, maleRB, femaleRB, othersRB;
    boolean isPassword1Visible = false;
    boolean isPassword2Visible = false;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        getRadioButton();

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        clickListeners();

    }

    private void initViews() {
        visibilityImgBtn1 = findViewById(R.id.visibilityImgBtn1);
        visibilityImgBtn2 = findViewById(R.id.visibilityImgBtn2);
        nameET = findViewById(R.id.registerNameET);
        designationET = findViewById(R.id.registerDesignationET);
        mobileET = findViewById(R.id.registerMobileNmbrET);
        emailET = findViewById(R.id.registerEmailET);
        passwordET1 = findViewById(R.id.passwordET1);
        passwordET2 = findViewById(R.id.passwordET2);

        nameTV = findViewById(R.id.registerNameTV);
        designationTV = findViewById(R.id.registerDesignationTV);
        genderTV = findViewById(R.id.registerGenderTV);
        mobileTV = findViewById(R.id.registerMobileNmbrTV);
        emailTV = findViewById(R.id.registerEmailTV);
        passwordTV1 = findViewById(R.id.registerPasswordTV1);
        passwordTV2 = findViewById(R.id.registerPasswordTV2);

        radioGroupGender = findViewById(R.id.registerGenderRG);
        radioGroupDesignation = findViewById(R.id.registerDesignationRG);

        signinTV = findViewById(R.id.signinTV);
        signinTV.setText(Html.fromHtml("<u>Already have an account? Sign in</u>"));
        signupBtn = findViewById(R.id.signupBtn);
    }

    public void checkButton(View view) {
        getRadioButton();
    }

    private void getRadioButton() {
        int buttonId = radioGroupGender.getCheckedRadioButtonId();
        radioButton = findViewById(buttonId);
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

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IS_INTERNET_ON(RegisterActivity.this)) {
                    signupAndSaveToDatabase();
                } else {
                    Toast.makeText(getApplicationContext(), "Please check internet connection!!", Toast.LENGTH_LONG).show();
                }
            }
        });


        signinTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void signupAndSaveToDatabase() {

        final String name = nameET.getText().toString().trim();
        //final String designation = designationET.getText().toString().trim();
        final String gender = radioButton.getText().toString().trim();
        final String mobile = mobileET.getText().toString().trim();
        final String email = emailET.getText().toString().trim();
        final String password1 = passwordET1.getText().toString().trim();
        String password2 = passwordET2.getText().toString().trim();
        String desig = "";
        if (radioGroupDesignation.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "Please select Gender", Toast.LENGTH_SHORT).show();
        } else {
            // get selected radio button from radioGroup
            int selectedId = radioGroupDesignation.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            RadioButton radioButtonDesignation = findViewById(selectedId);
            desig = radioButtonDesignation.getText().toString().trim();
        }


        if (name.length()<=0 || desig.length()<=0 || gender.length()<=0 || email.length()<=0 || password1.length()<=0 || password2.length()<=0) {

            Toast.makeText(getApplicationContext(), "(*) Asterick field can't be empty!!", Toast.LENGTH_LONG).show();

        } else if (!password1.equals(password2)) {

            Toast.makeText(getApplicationContext(), "Password doesn't match!", Toast.LENGTH_LONG).show();

        } else {

            final DatabaseReference dbRef = databaseReference.child("Users");

            final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("Creating profile....");
            progressDialog.setCancelable(false);
            progressDialog.show();

            final String finalDesig = desig;
            firebaseAuth.createUserWithEmailAndPassword(email, password1)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                firebaseUser = firebaseAuth.getCurrentUser();

                                final Profile profile = new Profile(name, finalDesig, gender, mobile, email, password1, firebaseUser.getUid());

                                dbRef.child(firebaseUser.getUid()).setValue(profile)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), "Profile created.", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                    //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Profile can't be created!!", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                            }
                                        });

                            } else {
                                Toast.makeText(getApplicationContext(), "User couldn't create successfully!!", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
