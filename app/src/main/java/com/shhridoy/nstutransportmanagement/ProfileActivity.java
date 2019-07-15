package com.shhridoy.nstutransportmanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ProfileActivity extends AppCompatActivity {

    // views
    private EditText nameET, designationET, phoneET, emailET, passwordET1, passwordET2;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

    }

    private void init() {
        initializeViews();

    }

    private void initializeViews() {
        nameET = findViewById(R.id.prfileNameET);
        designationET = findViewById(R.id.profileDesignationET);
        phoneET = findViewById(R.id.profileMobileNmbrET);
        emailET = findViewById(R.id.profileEmailET);
        passwordET1 = findViewById(R.id.profilePasswordET1);
        passwordET2 = findViewById(R.id.profilePasswordET2);
        radioGroup = findViewById(R.id.profileGenderRG);
        saveBtn = findViewById(R.id.profileSaveBtn);
    }



}
