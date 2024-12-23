
package com.example.diets.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
    private EditText ageEditText;
    private Spinner genderSpinner;
    private Spinner activityLevelSpinner;
    private Spinner goalSpinner;
    private Spinner weightSpinner;
    private Spinner heightSpinner;
    private Spinner ageSpinner;
    private Button submitButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);


        genderSpinner = findViewById(R.id.genderSpinner);
        activityLevelSpinner = findViewById(R.id.activityLevelSpinner);
        goalSpinner = findViewById(R.id.goalSpinner);
        weightSpinner = findViewById(R.id.weightSpinner);
        heightSpinner = findViewById(R.id.heightSpinner);
        ageSpinner = findViewById(R.id.ageSpinner);
        submitButton = findViewById(R.id.submitButton);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Configura el Spinner de género
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        // Configura el Spinner de nivel de actividad
        ArrayAdapter<CharSequence> activityLevelAdapter = ArrayAdapter.createFromResource(this,
                R.array.activity_level_options, android.R.layout.simple_spinner_item);
        activityLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityLevelSpinner.setAdapter(activityLevelAdapter);

        // Configura el Spinner de meta
        ArrayAdapter<CharSequence> goalAdapter = ArrayAdapter.createFromResource(this,
                R.array.goal_options, android.R.layout.simple_spinner_item);
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalSpinner.setAdapter(goalAdapter);

        // Configura el Spinner de peso
        ArrayAdapter<CharSequence> weightAdapter = ArrayAdapter.createFromResource(this,
                R.array.weight_options, android.R.layout.simple_spinner_item);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(weightAdapter);

        // Configura el Spinner de altura
        ArrayAdapter<CharSequence> heightAdapter = ArrayAdapter.createFromResource(this,
                R.array.height_options, android.R.layout.simple_spinner_item);
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heightSpinner.setAdapter(heightAdapter);

        // Configura el Spinner de edad
        ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(this,
                R.array.age_options, android.R.layout.simple_spinner_item);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(ageAdapter);

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

            // Obtener los valores de los spinners
            double weight = Double.parseDouble(weightSpinner.getSelectedItem().toString());
            double height = Double.parseDouble(heightSpinner.getSelectedItem().toString());
            int age = Integer.parseInt(ageSpinner.getSelectedItem().toString());
            String gender = genderSpinner.getSelectedItem().toString();
            String activityLevel = activityLevelSpinner.getSelectedItem().toString();
            String goal = goalSpinner.getSelectedItem().toString();

            // Actualiza la base de datos
            mDatabase.child("users").child(userId).child("weight").setValue(weight);
            mDatabase.child("users").child(userId).child("height").setValue(height);
            mDatabase.child("users").child(userId).child("age").setValue(age);
            mDatabase.child("users").child(userId).child("gender").setValue(gender);
            mDatabase.child("users").child(userId).child("activityLevel").setValue(activityLevel);
            mDatabase.child("users").child(userId).child("goal").setValue(goal);
            mDatabase.child("users").child(userId).child("firstTime").setValue(false)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("UserFormActivity", "Campo firstTime actualizado");
                            Toast.makeText(UserFormActivity.this, "Información actualizada", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UserFormActivity.this, DashboardActivity.class));
                            finish();
                        } else {
                            Log.d("UserFormActivity", "Error al actualizar el campo firstTime: " + task.getException().getMessage());
                            Toast.makeText(UserFormActivity.this, "Error al actualizar la información", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}

