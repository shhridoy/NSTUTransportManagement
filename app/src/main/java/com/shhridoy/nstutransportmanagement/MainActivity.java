package com.shhridoy.nstutransportmanagement;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shhridoy.nstutransportmanagement.myObjects.BusSchedule;
import com.shhridoy.nstutransportmanagement.myUtilities.AppPreferences;
import com.shhridoy.nstutransportmanagement.myViews.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    // firebase objects
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    // views
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton addScheduleFab;
    private RecyclerView recyclerView;

    private RecyclerViewAdapter recyclerViewAdapter;
    private List<BusSchedule> busSchedules;

    // popup window views
    private PopupWindow mPopUpWindow;
    private ArrayAdapter<String> arrayAdapter1, arrayAdapter2;
    private RelativeLayout backDimRL = null;
    private RelativeLayout mainRL = null;
    private TextView popUpTitleTV = null;
    private EditText busTitleET = null, startPointET = null, endPointET = null, timeET = null, goingET = null;
    private Spinner startPointSpinner = null, endPointSpinner = null;
    private RelativeLayout rlPopup = null;
    private RadioGroup radioGroup = null;
    private RadioButton radioButton = null, teacherRb = null, studentRb = null, stuffRb = null;
    private Button saveBtn = null;
    private static final String[] spinnerItemList1 = {
            "University Campus", "Begumgonj Chourasta", "Maijdee Bazar", "Sudaram Thana", "Boro Mashjid Mor", "Town Hall", "Cinema Hall",
            "Boshur Haat", "Others"
    };
    private static final String[] spinnerItemList2 = {
            "Begumgonj Chourasta", "Maijdee Bazar", "Sudaram Thana", "Boro Mashjid Mor", "Town Hall", "Cinema Hall",
            "Boshur Haat", "Others"
    };
    private static final String[] spinnerItemList3 = {"University", "Others"};


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
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        busSchedules = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        getBusScheduleList();

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

    private void getBusScheduleList() {
        databaseReference.child("BusSchedules").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                busSchedules.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.child("key").getValue().toString();
                    String bus_name = ds.child("bus_title").getValue().toString();
                    String bus_type = ds.child("bus_type").getValue().toString();
                    String start_point = ds.child("start_point").getValue().toString();
                    String end_point = ds.child("end_point").getValue().toString();
                    String start_time = ds.child("start_time").getValue().toString();
                    String vote = ds.child("vote").getValue().toString();

                    busSchedules.add(new BusSchedule(key, bus_name, bus_type, start_point, end_point, start_time, vote));
                }

                recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, busSchedules, "Schedules");
                recyclerView.setAdapter(recyclerViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            saveBtn = layout.findViewById(R.id.busScheduleSaveBtn);

            radioGroup = layout.findViewById(R.id.busSchedulePopupBusTypeRG);
            teacherRb = layout.findViewById(R.id.radioBtnTeacher);
            studentRb = layout.findViewById(R.id.radioBtnStudent);
            stuffRb = layout.findViewById(R.id.radioBtnStuff);

            startPointSpinner = layout.findViewById(R.id.busSchedulePopupBusStartPointSpinner);
            endPointSpinner = layout.findViewById(R.id.busSchedulePopupBusEndPointSpinner);

            arrayAdapter1 = new ArrayAdapter<String>(this, R.layout.item_spinner, R.id.itemSpinnerTV, spinnerItemList1);
            startPointSpinner.setAdapter(arrayAdapter1);

            arrayAdapter2 = new ArrayAdapter<String>(this, R.layout.item_spinner, R.id.itemSpinnerTV, spinnerItemList1);
            endPointSpinner.setAdapter(arrayAdapter2);

        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( (int) (width*.95), (int) (height*.88) );

        assert mainRL != null;
        mainRL.setLayoutParams(params);

        assert goingET != null;
        goingET.setFocusable(false);

        assert startPointSpinner != null;
        startPointSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String item = adapterView.getItemAtPosition(i).toString();

                if (item.contains("University")) {
                    arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner, R.id.itemSpinnerTV, spinnerItemList2);
                    endPointSpinner.setAdapter(arrayAdapter2);
                    startPointET.setVisibility(View.GONE);
                } else if (item.contains("Others")) {
                    startPointET.setVisibility(View.VISIBLE);
                } else {
                    arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner, R.id.itemSpinnerTV, spinnerItemList3);
                    endPointSpinner.setAdapter(arrayAdapter2);
                    startPointET.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        assert endPointSpinner != null;
        endPointSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                if (item.contains("Others")) {
                    endPointET.setVisibility(View.VISIBLE);
                } else {
                    endPointET.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        assert timeET != null;
        timeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeET.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//No 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        assert saveBtn != null;
        final View finalLayout = layout;
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetOn()) {
                    addBusScheduleToDB(
                            finalLayout,
                            busTitleET,
                            radioGroup,
                            startPointSpinner,
                            endPointSpinner,
                            startPointET,
                            endPointET,
                            timeET,
                            goingET
                    );
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
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

    private void addBusScheduleToDB(
            View view,
            EditText bus_title,
            RadioGroup rg,
            Spinner spinner1, Spinner spinner2,
            EditText start_point_et,
            EditText end_point_et,
            EditText time_et,
            EditText going_et
    ) {

        String title = bus_title.getText().toString().trim();
        String time = time_et.getText().toString().trim();

        String type = "";
        if (rg.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "Please select Gender", Toast.LENGTH_SHORT).show();
        } else {
            // get selected radio button from radioGroup
            int selectedId = rg.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            radioButton = view.findViewById(selectedId);
            type = radioButton.getText().toString().trim();
        }

        String start = "";
        if (start_point_et.getVisibility() == View.VISIBLE) {
            start = start_point_et.getText().toString().trim();
        } else {
            start = spinner1.getSelectedItem().toString().trim();
        }

        String end = "";
        if (end_point_et.getVisibility() == View.VISIBLE) {
            end = start_point_et.getText().toString().trim();
        } else {
            end = spinner2.getSelectedItem().toString().trim();
        }

        //Log.d("POPUP", title+" : "+type+" : " + start+" : "+end+" : "+ time+" : ");

        if (title.length() <= 0 || type.length() <= 0 || start.length() <= 0 || end.length() <= 0 || time.length() <= 0) {
            Toast.makeText(getApplicationContext(), "No field should be empty!!",Toast.LENGTH_LONG).show();
        } else {

            final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Creating schedule....");
            progressDialog.setCancelable(false);
            progressDialog.show();

            DatabaseReference scheduleRef = databaseReference.child("BusSchedules");

            String busScheduleKey = scheduleRef.push().getKey();

            final BusSchedule busSchedule = new BusSchedule(busScheduleKey, title, type, start, end, time, "0");

            assert busScheduleKey != null;
            scheduleRef.child(busScheduleKey).setValue(busSchedule)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Schedule added.", Toast.LENGTH_SHORT).show();
                                mPopUpWindow.dismiss();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Schedule can't added!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }

    }

    private boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        getBaseContext();
        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        assert connec != null;
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            return false;
        }

        return false;
    }

}
