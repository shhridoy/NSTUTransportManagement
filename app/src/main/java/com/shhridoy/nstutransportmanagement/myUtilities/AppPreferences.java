package com.shhridoy.nstutransportmanagement.myUtilities;

import android.content.Context;
import android.preference.PreferenceManager;

public class AppPreferences {

    public static final String DEFAULT = "Default";

    private static final String EMAIL = "Email";
    private static final String PASSWORD = "Password";

    public static void setPreferenceEmail(Context context, String email) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(EMAIL, email)
                .apply();
    }

    public static String getPreferenceEmail(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(EMAIL, DEFAULT);
    }

    public static void setPreferencePassword(Context context, String password) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PASSWORD, password)
                .apply();
    }

    public static String getPreferencePassword(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PASSWORD, DEFAULT);
    }

}
