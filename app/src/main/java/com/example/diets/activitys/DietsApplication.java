package com.example.diets.activitys;



import android.app.Application;
import com.google.firebase.FirebaseApp;

public class DietsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}