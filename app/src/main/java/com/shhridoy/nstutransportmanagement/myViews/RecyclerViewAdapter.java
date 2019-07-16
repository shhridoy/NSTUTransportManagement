package com.shhridoy.nstutransportmanagement.myViews;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
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
import com.google.firebase.database.DatabaseReference;
import com.shhridoy.nstutransportmanagement.R;
import com.shhridoy.nstutransportmanagement.myObjects.BusSchedule;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.shhridoy.nstutransportmanagement.myUtilities.Constants.ADMIN_TAG;
import static com.shhridoy.nstutransportmanagement.myUtilities.Constants.SPINNER_ITEM_LIST_1;
import static com.shhridoy.nstutransportmanagement.myUtilities.Constants.SPINNER_ITEM_LIST_2;
import static com.shhridoy.nstutransportmanagement.myUtilities.Constants.SPINNER_ITEM_LIST_3;
import static com.shhridoy.nstutransportmanagement.myUtilities.Constants.USER_TAG;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<BusSchedule> busScheduleList = null;
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
    private Button saveBtn = null;

    public RecyclerViewAdapter(Context context, List<BusSchedule> busScheduleList,  String tag) {
        this.context = context;
        this.busScheduleList = busScheduleList;
        this.tag = tag;
    }

    public RecyclerViewAdapter(Context context, List<BusSchedule> busScheduleList, DatabaseReference databaseReference, String tag) {
        this.context = context;
        this.busScheduleList = busScheduleList;
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

        if (tag.equalsIgnoreCase(ADMIN_TAG)) {
            holder.editBtn.setVisibility(View.VISIBLE);
            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.voteBtn.setVisibility(View.GONE);
        } else {
            holder.editBtn.setVisibility(View.GONE);
            holder.deleteBtn.setVisibility(View.GONE);
            holder.voteBtn.setVisibility(View.VISIBLE);
        }

        final String key = busScheduleList.get(position).getKey();
        final String busTitle = busScheduleList.get(position).getBus_title();
        final String busType = busScheduleList.get(position).getBus_type();
        final String startTime = busScheduleList.get(position).getStart_time();
        final String startPoint = busScheduleList.get(position).getStart_point();
        final String endPoint = busScheduleList.get(position).getEnd_point();
        final String going = busScheduleList.get(position).getVote();

        //holder.textView.setText(busTitle+"\n"+busType+"\n"+startTime+"\n"+startPoint+"\n"+endPoint+"\n"+going);

        holder.busTitleTV.setText(busTitle);
        holder.busTypeTV.setText(busType);
        holder.startPointTV.setText(startPoint);
        holder.endPointTV.setText(endPoint);
        holder.timeTV.setText(startTime);
        holder.voteTV.setText(going);

        if (busTitle.contains("red") || busTitle.contains("Red")) {
            holder.busIconIMG.setImageResource(R.drawable.ic_icon_red_bus);
        } else if (busTitle.contains("white") || busTitle.contains("White")) {
            holder.busIconIMG.setImageResource(R.drawable.ic_icon_blue_bus);
        } else if (busTitle.contains("mini") || busTitle.contains("Mini") || busTitle.contains("micro") || busTitle.contains("Micro")) {
            holder.busIconIMG.setImageResource(R.drawable.ic_icon_yellow_bus);
        }

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, busTitle+": "+going+" will be going.", Toast.LENGTH_LONG).show();
            }
        });

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
            }
        });


    }

    @Override
    public int getItemCount() {
        return busScheduleList.size();
    }

    private void removeAt(int position) {
        busScheduleList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, busScheduleList.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener {

        TextView textView;
        ImageView busIconIMG;
        TextView busTitleTV, busTypeTV, startPointTV, endPointTV, timeTV, voteTV;
        ImageButton deleteBtn, editBtn, voteBtn;

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
            //itemView.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View v) {

        }*/
    }

    private void deleteSchedule(String Key) {
        databaseReference.child("BusSchedules").child(Key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Schedule removed.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Schedule can't remove.", Toast.LENGTH_SHORT).show();
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
                    startPointET.setVisibility(View.GONE);
                } else if (item.contains("Others")) {
                    startPointET.setVisibility(View.VISIBLE);
                } else {
                    arrayAdapter2 = new ArrayAdapter<String>(context, R.layout.item_spinner, R.id.itemSpinnerTV, SPINNER_ITEM_LIST_3);
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
                if (isInternetOn()) {
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

    private boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        //getBaseContext();
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

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