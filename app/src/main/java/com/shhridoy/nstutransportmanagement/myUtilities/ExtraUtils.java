package com.shhridoy.nstutransportmanagement.myUtilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExtraUtils {

    public static boolean IS_TIME_EXCEEDED(String time) {
        try {
            //String _24HourTime = "22:15";
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            Date _12HourDt = _12HourSDF.parse(time);
            String currentTime = _24HourSDF.format(Calendar.getInstance().getTime());
            assert _12HourDt != null;
            String busStartingTime = _24HourSDF.format(_12HourDt);
            Date date1 = _24HourSDF.parse(busStartingTime);
            Date dateCurrent = _24HourSDF.parse(currentTime);

            //Log.d("GIVEN_TIME", date1.toString());
            //Log.d("CURRENT_TIME", dateCurrent.toString());

            assert date1 != null;
            if(date1.before(dateCurrent)) {
                return false;
            } else {
                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void TIMING() {

        try {

            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());
            String currDt = sdf.format(today);
            Log.d("CURRENT_DATE_TIME", currDt);

            Date date = new Date();
            date.setDate(20);
            date.setHours(18);
            date.setMinutes(0);
            date.setSeconds(0);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());
            String setDate = dateFormat.format(date);
            Log.d("CURRENT_DATE_TIME", setDate);


            if (today.before(date)) {
                Log.d("CURRENT_DATE_TIME", "Before");
            }

            if (today.after(date)) {
                Log.d("CURRENT_DATE_TIME", "after");
            }


            //Calendar calendar = Calendar.getInstance();
            //calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            //calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
            //calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            //calendar.set(Calendar.HOUR_OF_DAY, 6);
            //calendar.set(Calendar.MINUTE, 0);
            //calendar.set(Calendar.SECOND, 0);
            //calendar.set(Calendar.AM_PM, 1);
            //Date date1 = dateFormat.parse(String.valueOf(calendar.getTime()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean IS_INTERNET_ON(Context context) {

        // get Connectivity Manager object to check connection
        //(Activity)context.getBaseContext();
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
