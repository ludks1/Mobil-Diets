package com.example.diets.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diets.R;
import com.example.diets.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserFormActivity extends AppCompatActivity {

    private EditText weightEditText;
    private EditText heightEditText;
    private EditText genderEditText;
    private EditText goalEditText;
    private Button submitButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);

        weightEditText = findViewById(R.id.weightEditText);
        heightEditText = findViewById(R.id.heightEditText);
        genderEditText = findViewById(R.id.genderEditText);
        goalEditText = findViewById(R.id.goalEditText);
        submitButton = findViewById(R.id.submitButton);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });
    }

    private void updateUserInfo() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            double weight = Double.parseDouble(weightEditText.getText().toString());
            double height = Double.parseDouble(heightEditText.getText().toString());
            String gender = genderEditText.getText().toString();
            String goal = goalEditText.getText().toString();

            mDatabase.child("users").child(userId).child("weight").setValue(weight);
            mDatabase.child("users").child(userId).child("height").setValue(height);
            mDatabase.child("users").child(userId).child("gender").setValue(gender);
            mDatabase.child("users").child(userId).child("goal").setValue(goal);
            mDatabase.child("users").child(userId).child("firstTime").setValue(false);

            Toast.makeText(UserFormActivity.this, "Información actualizada", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserFormActivity.this, DashboardActivity.class));
            finish();
        }
    }
}
