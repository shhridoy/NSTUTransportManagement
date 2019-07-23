package com.shhridoy.transportmanagementnstu;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shhridoy.transportmanagementnstu.myUtilities.AppPreferences;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.shhridoy.transportmanagementnstu.myUtilities.ExtraUtils.IS_INTERNET_ON;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2000;

    private boolean isInitialInternetOn = false;

    private ProgressBar progressBar;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        //TIMING();

        isInitialInternetOn = IS_INTERNET_ON(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                userAuthCheckAndGoToActivity();
            }
        }, SPLASH_TIME_OUT);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (isInitialInternetOn != IS_INTERNET_ON(this)) {
                userAuthCheckAndGoToActivity();
                isInitialInternetOn = IS_INTERNET_ON(this);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //userAuthCheckAndGoToActivity();
    }

    private void userAuthCheckAndGoToActivity() {

        if (isUserLoggedIn()) {
            progressBar.setVisibility(View.VISIBLE);
            if (IS_INTERNET_ON(this)) {
                userLogin();
            } else {
                Toast.makeText(getApplicationContext(), "Please check internet connection!!", Toast.LENGTH_LONG).show();
                isInitialInternetOn = false;
            }
        } else {
            Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        /*
        if (IS_INTERNET_ON()) {
            if (isUserLoggedIn()) {
                userLogin();
            } else {
                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please check internet connection!!", Toast.LENGTH_LONG).show();
            isInitialInternetOn = false;
        }*/
    }

    private boolean isUserLoggedIn() {
        if (AppPreferences.getPreferencePassword(this).equalsIgnoreCase(AppPreferences.DEFAULT)) {
            return false;
        } else {
            return true;
        }
    }

    private void userLogin() {
        firebaseAuth
                .signInWithEmailAndPassword(AppPreferences.getPreferenceEmail(this), AppPreferences.getPreferencePassword(this))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User logged in successfully.", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "User couldn't login successfully!!", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // nothing will happen if user click on back button
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}