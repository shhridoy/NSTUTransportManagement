package com.shhridoy.nstutransportmanagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shhridoy.nstutransportmanagement.myUtilities.AppPreferences;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    // firebase objects
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    // views
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton addScheduleFab;

    // popup window
    private PopupWindow mPopUpWindow;
    private RadioGroup radioGroup = null;
    private RadioButton radioButton = null;
    ArrayAdapter<String> arrayAdapter1, arrayAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        clickListeners();

    }

    private void init () {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        addScheduleFab = findViewById(R.id.fab);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();



    }

    private void clickListeners() {

        addScheduleFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
                busScheduleInputPopupWindow();
            }
        });

    }

    private void getUserData() {
        databaseReference.child("Users").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String pass = dataSnapshot.child("password").getValue().toString();
                String gender = dataSnapshot.child("gender").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String mobile = dataSnapshot.child("mobile").getValue().toString();
                String designation = dataSnapshot.child("designation").getValue().toString();
                //detailTV.setText("Name: "+name+"\nDesignation: "+designation+"\nPhone: "+phone+"\nE-mail: "+email+"\nPassword: "+pass);
                Toast.makeText(getApplicationContext(), name+"\n"+gender+"\n"+designation+"\n"+mobile+"\n"+email+"\n"+pass, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
            return true;
        } else if (id == R.id.action_profile) {
            getUserData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        firebaseAuth.signOut();
    }

    private void logout() {
        firebaseAuth.signOut();
        AppPreferences.setPreferenceEmail(this, AppPreferences.DEFAULT);
        AppPreferences.setPreferencePassword(this, AppPreferences.DEFAULT);
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    boolean backButtonPressedOnce = false;
    @Override
    public void onBackPressed() {
        if(backButtonPressedOnce){
            super.onBackPressed();
            return;
        }

        this.backButtonPressedOnce = true;
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please press back again to exit!!", Snackbar.LENGTH_SHORT);
        snackbar.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backButtonPressedOnce = false;
            }
        }, 2000);
    }

    private void getRadioButton() {
        int buttonId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(buttonId);
    }

    public void checkButton(View view) {
        getRadioButton();
    }

    @SuppressLint("InflateParams")
    private void busScheduleInputPopupWindow() {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = null;
        if (inflater != null) {
            layout = inflater.inflate(R.layout.bus_schedule_input_popup_window,null);
        }

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        mPopUpWindow = new PopupWindow(layout, width, height, true);

        // popup window views
        RelativeLayout backDimRL = null;
        RelativeLayout mainRL = null;
        TextView popUpTitleTV = null;
        EditText busTitleET = null, startPointET = null, endPointET = null, timeET = null, goingET = null;
        Spinner startPointSpinner = null, endPointSpinner = null;
        RelativeLayout rlPopup = null;
        Button saveBtn = null;
        final String[] spinnerItemList1 = {
                "University Campus", "Begumgonj Chourasta", "Maijdee Bazar", "Sudaram Thana", "Boro Mashjid Mor", "Town Hall", "Cinema Hall",
                "Boshur Haat", "Others"
        };
        final String[] spinnerItemList2 = {
                "Begumgonj Chourasta", "Maijdee Bazar", "Sudaram Thana", "Boro Mashjid Mor", "Town Hall", "Cinema Hall",
                "Boshur Haat", "Others"
        };
        final String[] spinnerItemList3 = {"University", "Others"};

        if (layout != null) {
            backDimRL = layout.findViewById(R.id.dimRL);
            mainRL = layout.findViewById(R.id.main_popup);
            popUpTitleTV = layout.findViewById(R.id.popUpTitleTV);
            rlPopup = layout.findViewById(R.id.popUpRL);
            busTitleET = layout.findViewById(R.id.busSchedulePopupBusTitleET);
            startPointET = layout.findViewById(R.id.busSchedulePopupBusStartPointET);
            endPointET = layout.findViewById(R.id.busSchedulePopupBusEndPointET);
            timeET = layout.findViewById(R.id.busSchedulePopupTimeET);
            goingET = layout.findViewById(R.id.busScheduleGoingET);
            radioGroup = layout.findViewById(R.id.busSchedulePopupBusTypeRG);
            getRadioButton();
            startPointSpinner = layout.findViewById(R.id.busSchedulePopupBusStartPointSpinner);
            endPointSpinner = layout.findViewById(R.id.busSchedulePopupBusEndPointSpinner);

            arrayAdapter1 = new ArrayAdapter<String>(this, R.layout.item_spinner, R.id.itemSpinnerTV, spinnerItemList1);
            startPointSpinner.setAdapter(arrayAdapter1);

            if (startPointSpinner.getSelectedItem().toString().contains("University")) {

            }
            arrayAdapter2 = new ArrayAdapter<String>(this, R.layout.item_spinner, R.id.itemSpinnerTV, spinnerItemList1);
            endPointSpinner.setAdapter(arrayAdapter2);

        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( (int) (width*.95), (int) (height*.88) );

        assert mainRL != null;
        mainRL.setLayoutParams(params);


        final Spinner finalEndPointSpinner = endPointSpinner;
        final EditText finalStartPointET = startPointET;
        startPointSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerItemList1[i].contains("University")) {
                    arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner, R.id.itemSpinnerTV, spinnerItemList2);
                    finalEndPointSpinner.setAdapter(arrayAdapter2);
                } else if (spinnerItemList1[i].contains("Others")) {
                    finalStartPointET.setVisibility(View.VISIBLE);
                } else {
                    arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner, R.id.itemSpinnerTV, spinnerItemList3);
                    finalEndPointSpinner.setAdapter(arrayAdapter2);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final EditText finalEndPointET = endPointET;
        endPointSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                if (item.contains("Others")) {
                    finalEndPointET.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        assert backDimRL != null;
        backDimRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopUpWindow.dismiss();
            }
        });

        assert rlPopup != null;
        rlPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nothing to do
            }
        });

        assert popUpTitleTV != null;
        popUpTitleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nothing to do
            }
        });

        //Set up touch closing outside of pop-up
        mPopUpWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_up_bg));
        mPopUpWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopUpWindow.setTouchInterceptor(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopUpWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        mPopUpWindow.setOutsideTouchable(true);

        mPopUpWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopUpWindow.setContentView(layout);
        mPopUpWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

    private void addBusScheduleToDB(EditText title, Spinner spinner) {

    }

}
