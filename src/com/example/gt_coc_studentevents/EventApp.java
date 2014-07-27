package com.example.gt_coc_studentevents;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

public class EventApp extends Application {
    public final static String PACKAGE_BASE = "com.example.gt_coc_studentevents";

    // Account Related Things
    public final static String ACCOUNT_EMAIL_KEY = PACKAGE_BASE + ".account.email";
    public final static String ACCOUNT_TOKEN = PACKAGE_BASE + ".account.token";
    public final static String ACCOUNT_AUTH_DATE = PACKAGE_BASE + ".account.authdate";

    public final static String ACCOUNT_PASSWORD_KEY = PACKAGE_BASE + "CY37vxshHRXYtalW";

    private String myPassword;


    /**
     * Calls setAccount(String email, String password) and then adds an auth date
     * and a token in the SharedPreferences of this account on the phone.
     *
     * @param email     A String that is the email associated with the account
     * @param password  A String that is the password to be used for the account
     * @param token     A String that is the token for the account
     */
    public void setAccount(String email, String password, String token) {
        setAccount(email, password);
        SharedPreferences sp = getSharedPreferences(PACKAGE_BASE, Context.MODE_PRIVATE);
        sp.edit().putLong(ACCOUNT_AUTH_DATE, new Date().getTime()).apply();
        sp.edit().putString(ACCOUNT_TOKEN, token).apply();
    }

    /**
     * Sets an account in the SharedPreferences on the phone using the email and password provided.
     *
     * @param email     A String that is the email associated with the account
     * @param password  A String that is the password to be used for the account
     */
    public void setAccount(String email, String password) {
        if (!email.isEmpty()) {
            SharedPreferences sp = getSharedPreferences(PACKAGE_BASE, Context.MODE_PRIVATE);
            sp.edit().putString(ACCOUNT_EMAIL_KEY, email).apply();
            sp.edit().putString(ACCOUNT_PASSWORD_KEY, password).apply();
            myPassword = password;
        }
    }

    /**
     * Gets the password of the account associated with this Application.
     *
     * @return  A String that is the password for the account or an empty string if one is not found
     */
    public String getPassword() {
        if(myPassword != null) {
            return myPassword;
        }

        SharedPreferences sp = getSharedPreferences(PACKAGE_BASE, Context.MODE_PRIVATE);
        return sp.getString(ACCOUNT_PASSWORD_KEY, "");
    }
}