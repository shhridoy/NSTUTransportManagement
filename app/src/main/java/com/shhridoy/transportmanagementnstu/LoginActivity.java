package com.shhridoy.transportmanagementnstu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.shhridoy.transportmanagementnstu.myUtilities.AppPreferences;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.shhridoy.transportmanagementnstu.myUtilities.ExtraUtils.IS_INTERNET_ON;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    ImageButton visibilityImgBtn;
    EditText emailET, passwordET;
    TextView signupTV;
    boolean isPasswordVisible = false;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        //databaseReference = FirebaseDatabase.getInstance().getReference();

        initViews();

        clickListeners();

    }

    private void initViews() {
        visibilityImgBtn = findViewById(R.id.visibilityImgBtn);
        emailET = findViewById(R.id.loginEmailET);
        passwordET = findViewById(R.id.passwordET);
        signupTV = findViewById(R.id.signupTV);
        signupTV.setText(Html.fromHtml("<u>Don't have an account? Sign up</u>"));
        loginBtn = findViewById(R.id.loginBtn);
    }

    private void clickListeners() {

        visibilityImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isPasswordVisible) {
                    visibilityImgBtn.setImageResource(R.drawable.ic_action_visibility_off);
                    // hide password
                    passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPasswordVisible = false;
                } else {
                    visibilityImgBtn.setImageResource(R.drawable.ic_action_visible);
                    // show password
                    passwordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isPasswordVisible = true;
                }

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IS_INTERNET_ON(LoginActivity.this)) {
                    loginUsingEmailAndPass();
                } else {
                    Toast.makeText(getApplicationContext(), "Please check internet connection!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        signupTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

    }

    private void loginUsingEmailAndPass() {
        final String email = emailET.getText().toString().trim();
        final String password = passwordET.getText().toString().trim();

        if (email.length() <= 0 && password.length() <= 0) {
            Toast.makeText(this, "Fields can't be empty!!", Toast.LENGTH_LONG).show();
        } else {

            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("User Login....");
            progressDialog.setCancelable(false);
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "User logged in successfully.", Toast.LENGTH_SHORT).show();
                                AppPreferences.setPreferenceEmail(getApplicationContext(), email);
                                AppPreferences.setPreferencePassword(getApplicationContext(), password);
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                emailET.setText("");
                                passwordET.setText("");
                            } else {
                                Toast.makeText(getApplicationContext(), "User couldn't login successfully!!", Toast.LENGTH_LONG).show();
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
