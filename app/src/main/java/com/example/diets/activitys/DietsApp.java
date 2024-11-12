package com.example.diets.activitys;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class DietsApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Habilitar la persistencia de datos en Firebase Realtime Database
        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
    }
}
