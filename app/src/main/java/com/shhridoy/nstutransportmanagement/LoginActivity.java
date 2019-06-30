package com.shhridoy.nstutransportmanagement;

import android.content.Context;
import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    ImageButton visibilityImgBtn;
    EditText passwordET;
    TextView signupTV;
    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        clickListeners();

    }

    private void initViews() {
        visibilityImgBtn = findViewById(R.id.visibilityImgBtn);
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
                Toast.makeText(getApplicationContext(), "Login in...", Toast.LENGTH_LONG).show();
            }
        });


        signupTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
