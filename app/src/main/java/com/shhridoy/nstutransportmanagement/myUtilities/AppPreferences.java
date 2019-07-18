package com.shhridoy.nstutransportmanagement.myUtilities;

import android.content.Context;
import android.preference.PreferenceManager;

public class AppPreferences {

    public static final String DEFAULT = "Default";

    private static final String EMAIL = "Email";
    private static final String PASSWORD = "Password";
    private static final String VOTED_EMAIL = "voted_email";
    private static final String VOTED_KEY1 = "voted_key1";
    private static final String VOTED_KEY2 = "voted_key2";

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

    public static void setPreferenceVoteEmail(Context context, String email) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(VOTED_EMAIL, email)
                .apply();
    }

    public static String getPreferenceVotedEmail(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(VOTED_EMAIL, DEFAULT);
    }


    public static void setPreferenceVotedKeyToCampus(Context context, String voted_key) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(VOTED_KEY1, voted_key)
                .apply();
    }

    public static String getPreferenceVotedKeyToCampus(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(VOTED_KEY1, DEFAULT);
    }

    public static void setPreferenceVotedKeyFromCampus(Context context, String voted_key) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(VOTED_KEY2, voted_key)
                .apply();
    }

    public static String getPreferenceVotedKeyFromCampus(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(VOTED_KEY2, DEFAULT);
    }

}
