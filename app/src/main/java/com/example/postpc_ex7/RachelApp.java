package com.example.postpc_ex7;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class RachelApp extends Application {
    private static RachelApp singleton;

    public static RachelApp getInstance() {
        if (singleton == null) {
            singleton = new RachelApp();
        }
        return singleton;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(RachelApp.getInstance());

    }
}