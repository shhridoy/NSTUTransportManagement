package com.shhridoy.transportmanagementnstu.myViews;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.shhridoy.transportmanagementnstu.R;
import com.shhridoy.transportmanagementnstu.myObjects.BusSchedule;
import com.shhridoy.transportmanagementnstu.myObjects.Profile;
import com.shhridoy.transportmanagementnstu.myUtilities.AppPreferences;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;
import static com.shhridoy.transportmanagementnstu.myUtilities.AppPreferences.DEFAULT;
import static com.shhridoy.transportmanagementnstu.myUtilities.Constants.ADMIN_TAG;
import static com.shhridoy.transportmanagementnstu.myUtilities.Constants.SPINNER_ITEM_LIST_1;
import static com.shhridoy.transportmanagementnstu.myUtilities.Constants.SPINNER_ITEM_LIST_2;
import static com.shhridoy.transportmanagementnstu.myUtilities.Constants.SPINNER_ITEM_LIST_3;
import static com.shhridoy.transportmanagementnstu.myUtilities.Constants.USERS_LIST_TAG;
import static com.shhridoy.transportmanagementnstu.myUtilities.ExtraUtils.IS_INTERNET_ON;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<BusSchedule> busScheduleList = null;
    private List<Profile> profileList = null;
    private Context context;
    private DatabaseReference databaseReference;
    private String tag = null;
    private int previousPosition = -1;

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
    private Button saveBtn = null, resetBtn = null;

    public RecyclerViewAdapter(Context context, List<BusSchedule> busScheduleList, DatabaseReference databaseReference, String tag) {
        this.context = context;
        this.busScheduleList = busScheduleList;
        this.databaseReference = databaseReference;
        this.tag = tag;
    }

    public RecyclerViewAdapter(Context context, List<Profile> profileList, String tag, DatabaseReference databaseReference) {
        this.context = context;
        this.profileList = profileList;
        this.databaseReference = databaseReference;
        this.tag = tag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_schedule_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        /*if (position > previousPosition) { // scrolling down
            AnimationUtils.animateRecyclerView(holder.itemView, true);
        } else { // scrolling up
            AnimationUtils.animateRecyclerView(holder.itemView, false);
        }
        previousPosition = position;
        AnimationUtils.setFadeAnimation(holder.itemView);*/

        if (tag.equalsIgnoreCase(USERS_LIST_TAG)) {

            holder.voteBtn.setVisibility(View.GONE);
            holder.editBtn.setVisibility(View.GONE);
            holder.scheduleLL.setVisibility(View.GONE);
            holder.goingLL.setVisibility(View.GONE);
            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.textView.setVisibility(View.VISIBLE);

            String name = profileList.get(position).getName();
            String designation = profileList.get(position).getDesignation();
            String gender = profileList.get(position).getGender();
            String mobile = profileList.get(position).getMobile();
            final String email = profileList.get(position).getEmail();
            final String password = profileList.get(position).getPassword();
            final String userId = profileList.get(position).getUser_id();

            holder.textView.setText("Name: "+name+"\nDesignation: "+designation+"\nGender: "+gender+"\nMobile: "+mobile+"\nEmail: "+email+"\nPassword: "+password+"\nUser ID: "+userId);

            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    // builder.setCancelable(false);
                    builder.setTitle(Html.fromHtml("<font color='#D81B60'>Warning!!</font>"));
                    builder.setMessage(Html.fromHtml("<font color='#000000'>Do you want to delete this user?</font>"));
                    builder.setNegativeButton(Html.fromHtml("No"),new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setPositiveButton(Html.fromHtml("Yes"),new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteUser(userId, email, password);
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });

        } else {

            if (tag.equalsIgnoreCase(ADMIN_TAG)) {
                holder.editBtn.setVisibility(View.VISIBLE);
                holder.deleteBtn.setVisibility(View.VISIBLE);
                holder.voteBtn.setVisibility(GONE);
            } else {
                holder.editBtn.setVisibility(GONE);
                holder.deleteBtn.setVisibility(GONE);
                holder.voteBtn.setVisibility(View.VISIBLE);
            }

            final String key = busScheduleList.get(position).getKey();
            final String busTitle = busScheduleList.get(position).getBus_title();
            final String busType = busScheduleList.get(position).getBus_type();
            final String startTime = busScheduleList.get(position).getStart_time();
            final String startPoint = busScheduleList.get(position).getStart_point();
            final String endPoint = busScheduleList.get(position).getEnd_point();
            final String going = busScheduleList.get(position).getVote();

            holder.busTitleTV.setText(busTitle);
            holder.busTypeTV.setText(busType);
            holder.startPointTV.setText(startPoint);
            holder.endPointTV.setText(endPoint);
            holder.timeTV.setText(startTime);
            holder.voteTV.setText(going);



            if (endPoint.contains("University") || endPoint.contains("university") || endPoint.contains("campus") || endPoint.contains("Campus")) {
                if (AppPreferences.getPreferenceVotedKeyToCampus(context).equalsIgnoreCase(key)) {
                    holder.voteBtn.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                } else {
                    holder.voteBtn.setColorFilter(context.getResources().getColor(R.color.md_grey_600), PorterDuff.Mode.SRC_ATOP);
                }
            } else if (!endPoint.contains("University") || !endPoint.contains("university") || !endPoint.contains("campus") || !endPoint.contains("Campus")) {
                if (AppPreferences.getPreferenceVotedKeyFromCampus(context).equalsIgnoreCase(key)) {
                    holder.voteBtn.setColorFilter(context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                } else {
                    holder.voteBtn.setColorFilter(context.getResources().getColor(R.color.md_grey_600), PorterDuff.Mode.SRC_ATOP);
                }
            }

            if (busTitle.contains("red") || busTitle.contains("Red")) {
                holder.busIconIMG.setImageResource(R.drawable.ic_icon_red_bus);
            } else if (busTitle.contains("white") || busTitle.contains("White")) {
                holder.busIconIMG.setImageResource(R.drawable.ic_icon_blue_bus);
            } else if (busTitle.contains("mini") || busTitle.contains("Mini") || busTitle.contains("micro") || busTitle.contains("Micro")) {
                holder.busIconIMG.setImageResource(R.drawable.ic_icon_yellow_bus);
            }

            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    // builder.setCancelable(false);
                    builder.setTitle(Html.fromHtml("<font color='#D81B60'>Warning!!</font>"));
                    builder.setMessage(Html.fromHtml("<font color='#000000'>Do you want to delete this bus schedule?</font>"));
                    builder.setNegativeButton(Html.fromHtml("No"),new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setPositiveButton(Html.fromHtml("Yes"),new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteSchedule(busScheduleList.get(position).getKey());
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            holder.editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    busScheduleEditPopupWindow(
                            key, busTitle, busType, startTime, startPoint, endPoint, going
                    );
                }
            });


            holder.voteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (endPoint.contains("University") || endPoint.contains("university") || endPoint.contains("campus") || endPoint.contains("Campus")) {
                        if (IS_INTERNET_ON(context) && AppPreferences.getPreferenceVotedKeyToCampus(context).equalsIgnoreCase(DEFAULT)) {
                            AppPreferences.setPreferenceVotedKeyToCampus(context, key);
                            AppPreferences.setPreferenceVoteEmail(context, AppPreferences.getPreferenceEmail(context));
                            String vote = String.valueOf(Integer.parseInt(going)+1);
                            databaseReference.child("BusSchedules").child(key).child("vote").setValue(vote)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(context, "Voted!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "Voting failed!!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else if (IS_INTERNET_ON(context) && AppPreferences.getPreferenceVotedKeyToCampus(context).equalsIgnoreCase(key)) {
                            AppPreferences.setPreferenceVotedKeyToCampus(context, DEFAULT);
                            String vote = String.valueOf(Integer.parseInt(going)-1);
                            databaseReference.child("BusSchedules").child(key).child("vote").setValue(vote)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(context, "Vote Canceled!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "Failed to cancel vote!!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    } else if (!endPoint.contains("University") || !endPoint.contains("university") || !endPoint.contains("campus") || !endPoint.contains("Campus")) {
                        if (IS_INTERNET_ON(context) && AppPreferences.getPreferenceVotedKeyFromCampus(context).equalsIgnoreCase(DEFAULT)) {
                            AppPreferences.setPreferenceVotedKeyFromCampus(context, key);
                            AppPreferences.setPreferenceVoteEmail(context, AppPreferences.getPreferenceEmail(context));
                            String vote = String.valueOf(Integer.parseInt(going)+1);
                            databaseReference.child("BusSchedules").child(key).child("vote").setValue(vote)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(context, "Voted!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "Voting failed!!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else if (IS_INTERNET_ON(context) && AppPreferences.getPreferenceVotedKeyFromCampus(context).equalsIgnoreCase(key)) {
                            AppPreferences.setPreferenceVotedKeyFromCampus(context, DEFAULT);
                            String vote = String.valueOf(Integer.parseInt(going)-1);
                            databaseReference.child("BusSchedules").child(key).child("vote").setValue(vote)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(context, "Vote Canceled!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "Failed to cancel vote!!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        if (tag.equalsIgnoreCase(USERS_LIST_TAG)) {
            return profileList.size();
        } else {
            return busScheduleList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener {

        TextView textView;
        ImageView busIconIMG;
        TextView busTitleTV, busTypeTV, startPointTV, endPointTV, timeTV, voteTV;
        ImageButton deleteBtn, editBtn, voteBtn;
        LinearLayout scheduleLL, goingLL;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            editBtn = itemView.findViewById(R.id.editBtn);
            voteBtn = itemView.findViewById(R.id.voteBtn);
            busTitleTV = itemView.findViewById(R.id.busTitleTV);
            busTypeTV = itemView.findViewById(R.id.busTypeTV);
            startPointTV = itemView.findViewById(R.id.startPointTV);
            endPointTV = itemView.findViewById(R.id.endPointTV);
            timeTV = itemView.findViewById(R.id.timeTV);
            voteTV = itemView.findViewById(R.id.voteTV);
            busIconIMG = itemView.findViewById(R.id.busIconIMG);
            scheduleLL = itemView.findViewById(R.id.scheduleLL);
            goingLL = itemView.findViewById(R.id.goingLL);
            //itemView.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View v) {

        }*/
    }

    private void deleteSchedule(String Key) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Deleting Schedule....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        databaseReference.child("BusSchedules").child(Key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Schedule removed.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Schedule can't remove.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteUser(final String UserID, final String Email, final String Password) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Deleting User....");
        progressDialog.setCancelable(false);
        progressDialog.show();
       final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            AuthCredential credential = EmailAuthProvider.getCredential(Email, Password);
                            firebaseUser.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            firebaseUser.delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            progressDialog.dismiss();
                                                            if (task.isSuccessful()) {
                                                                databaseReference.child("Users").child(UserID).removeValue();
                                                                Toast.makeText(context, "User deleted!", Toast.LENGTH_SHORT).show();
                                                                firebaseAuth.signOut();
                                                                reAuthentication();
                                                            } else  {
                                                                Toast.makeText(context, "User can't deleted!", Toast.LENGTH_LONG).show();
                                                                firebaseAuth.signOut();
                                                                reAuthentication();
                                                            }
                                                        }
                                                    });

                                        }
                                    });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Failed!!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void reAuthentication() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(AppPreferences.getPreferenceEmail(context), AppPreferences.getPreferencePassword(context))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(getApplicationContext(), "User logged in successfully.", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(getApplicationContext(), "User couldn't login successfully!!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @SuppressLint("InflateParams")
    private void busScheduleEditPopupWindow(
            final String Key, String BusTitle, String BusType,
            String StartTime, String StartPoint, String EndPoint, String Going
    ) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = null;
        if (inflater != null) {
            layout = inflater.inflate(R.layout.bus_schedule_input_popup_window,null);
        }

        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;

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
            resetBtn = layout.findViewById(R.id.resetBtn);

            radioGroup = layout.findViewById(R.id.busSchedulePopupBusTypeRG);
            teacherRb = layout.findViewById(R.id.radioBtnTeacher);
            studentRb = layout.findViewById(R.id.radioBtnStudent);
            stuffRb = layout.findViewById(R.id.radioBtnStuff);

            startPointSpinner = layout.findViewById(R.id.busSchedulePopupBusStartPointSpinner);
            endPointSpinner = layout.findViewById(R.id.busSchedulePopupBusEndPointSpinner);

            arrayAdapter1 = new ArrayAdapter<String>(context, R.layout.item_spinner, R.id.itemSpinnerTV, SPINNER_ITEM_LIST_1);
            startPointSpinner.setAdapter(arrayAdapter1);

            arrayAdapter2 = new ArrayAdapter<String>(context, R.layout.item_spinner, R.id.itemSpinnerTV, SPINNER_ITEM_LIST_1);
            endPointSpinner.setAdapter(arrayAdapter2);

        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( (int) (width*.95), (int) (height*.88) );

        assert mainRL != null;
        mainRL.setLayoutParams(params);

        assert goingET != null;
        goingET.setFocusable(false);

        if (Key != null && BusTitle != null && BusType != null && StartTime != null && StartPoint != null && EndPoint != null) {
            busTitleET.setText(BusTitle);
            if (BusType.contains("Student")) {
                radioGroup.check(R.id.radioBtnStudent);
            } else if (BusType.contains("Teacher")) {
                radioGroup.check(R.id.radioBtnTeacher);
            } else {
                radioGroup.check(R.id.radioBtnStuff);
            }
            timeET.setText(StartTime);
            if (StartPoint.contains("Others")) {
                startPointET.setVisibility(View.VISIBLE);
                startPointET.setText(StartPoint);
            } else {
                startPointSpinner.setSelection(Arrays.asList(SPINNER_ITEM_LIST_1).indexOf(StartPoint));
            }
            if (EndPoint.contains("Others")) {
                endPointET.setVisibility(View.VISIBLE);
                endPointET.setText(EndPoint);
            } else {
                endPointSpinner.setSelection(Arrays.asList(SPINNER_ITEM_LIST_1).indexOf(EndPoint));
            }
            goingET.setText(Going);
        }

        assert startPointSpinner != null;
        startPointSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String item = adapterView.getItemAtPosition(i).toString();

                if (item.contains("University")) {
                    arrayAdapter2 = new ArrayAdapter<String>(context, R.layout.item_spinner, R.id.itemSpinnerTV, SPINNER_ITEM_LIST_2);
                    endPointSpinner.setAdapter(arrayAdapter2);
                    startPointET.setVisibility(GONE);
                } else if (item.contains("Others")) {
                    startPointET.setVisibility(View.VISIBLE);
                } else {
                    arrayAdapter2 = new ArrayAdapter<String>(context, R.layout.item_spinner, R.id.itemSpinnerTV, SPINNER_ITEM_LIST_3);
                    endPointSpinner.setAdapter(arrayAdapter2);
                    startPointET.setVisibility(GONE);
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
                    endPointET.setVisibility(GONE);
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
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //timeET.setText(selectedHour + ":" + selectedMinute);
                        String time = selectedHour+":"+selectedMinute;
                        try {
                            //String _24HourTime = "22:15";
                            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                            Date _24HourDt = _24HourSDF.parse(time);
                            //System.out.println(_24HourDt);
                            timeET.setText(_12HourSDF.format(_24HourDt));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                if (IS_INTERNET_ON(context)) {
                    updateBusSchedule(
                            finalLayout,
                            Key,
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
                    Toast.makeText(context, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });

        assert resetBtn != null;
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("BusSchedules").child(Key).child("vote").setValue("0")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    goingET.setText("0");
                                    Toast.makeText(context, "Reset!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Reset failed!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
        mPopUpWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.pop_up_bg));
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

    private void updateBusSchedule(
            View view,
            String key,
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
        String going = going_et.getText().toString().trim();

        String type = "";
        if (rg.getCheckedRadioButtonId() == -1) {
            Toast.makeText(context, "Please select Gender", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(context, "No field should be empty!!",Toast.LENGTH_LONG).show();
        } else {

            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Creating schedule....");
            progressDialog.setCancelable(false);
            progressDialog.show();

            DatabaseReference scheduleRef = databaseReference.child("BusSchedules");

            final BusSchedule busSchedule = new BusSchedule(key, title, type, start, end, time, going);

            scheduleRef.child(key).setValue(busSchedule)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Schedule updated.", Toast.LENGTH_SHORT).show();
                                mPopUpWindow.dismiss();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Schedule can't updated!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }

    }

}