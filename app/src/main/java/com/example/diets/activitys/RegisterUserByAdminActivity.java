package com.example.diets.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diets.R;
import com.example.diets.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUserByAdminActivity extends AppCompatActivity {

    private EditText firstNameInput, lastNameInput, emailInput, passwordInput;
    private Spinner genderSpinner, ageSpinner, heightSpinner, activityLevelSpinner, goalSpinner;
    private CheckBox isAdminCheckBox;
    private Button registerButton;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_by_admin);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        initializeUI();
        setupSpinners();

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void initializeUI() {
        firstNameInput = findViewById(R.id.registerFirstName);
        lastNameInput = findViewById(R.id.registerLastName);
        emailInput = findViewById(R.id.registerEmail);
        passwordInput = findViewById(R.id.registerPassword);
        genderSpinner = findViewById(R.id.registerGender);
        ageSpinner = findViewById(R.id.registerAge);
        heightSpinner = findViewById(R.id.registerHeight);
        activityLevelSpinner = findViewById(R.id.registerActivityLevel);
        goalSpinner = findViewById(R.id.registerGoal);
        isAdminCheckBox = findViewById(R.id.registerIsAdminCheckBox);
        registerButton = findViewById(R.id.registerButton);
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(this, R.array.age_options, android.R.layout.simple_spinner_item);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(ageAdapter);

        ArrayAdapter<CharSequence> heightAdapter = ArrayAdapter.createFromResource(this, R.array.height_options, android.R.layout.simple_spinner_item);
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heightSpinner.setAdapter(heightAdapter);

        ArrayAdapter<CharSequence> activityLevelAdapter = ArrayAdapter.createFromResource(this, R.array.activity_level_options, android.R.layout.simple_spinner_item);
        activityLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityLevelSpinner.setAdapter(activityLevelAdapter);

        ArrayAdapter<CharSequence> goalAdapter = ArrayAdapter.createFromResource(this, R.array.goal_options, android.R.layout.simple_spinner_item);
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalSpinner.setAdapter(goalAdapter);
    }

    private void registerUser() {
        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();
        String ageText = ageSpinner.getSelectedItem().toString();
        String heightText = heightSpinner.getSelectedItem().toString();
        String activityLevel = activityLevelSpinner.getSelectedItem().toString();
        String goal = goalSpinner.getSelectedItem().toString();
        boolean isAdmin = isAdminCheckBox.isChecked();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mDatabase.push().getKey();

        if (userId != null) {
            User user = new User(firstName, lastName, email, password, Integer.parseInt(ageText), Double.parseDouble(heightText), Double.parseDouble(heightText), gender, activityLevel, goal);
            user.setAdmin(isAdmin);
            user.setId(userId);

            mDatabase.child(userId).setValue(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error al registrar el usuario.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
