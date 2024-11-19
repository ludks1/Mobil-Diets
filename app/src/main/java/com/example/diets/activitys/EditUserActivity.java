package com.example.diets.activitys;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diets.R;
import com.example.diets.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditUserActivity extends AppCompatActivity {

    private EditText editUserFirstName, editUserLastName, editUserEmail, editUserAge, editUserWeight, editUserHeight;
    private Spinner editUserGender, editUserActivityLevel, editUserGoal;
    private Button saveUserButton;

    private DatabaseReference userRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // Inicializa la UI
        initializeUI();

        // Obtén el ID del usuario desde el Intent
        userId = getIntent().getStringExtra("userId");

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "ID de usuario no encontrado.", Toast.LENGTH_SHORT).show();
            Log.e("EditUserActivity", "ID de usuario es nulo o vacío.");
            finish();
            return;
        }

        // Referencia a Firebase para el usuario
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        // Configura las opciones de los spinners
        setupSpinners();

        // Carga los datos actuales del usuario
        loadUserDetails();

        // Configura el botón de guardar cambios
        saveUserButton.setOnClickListener(v -> saveUserChanges());
    }

    private void initializeUI() {
        editUserFirstName = findViewById(R.id.editUserFirstName);
        editUserLastName = findViewById(R.id.editUserLastName);
        editUserEmail = findViewById(R.id.editUserEmail);
        editUserAge = findViewById(R.id.editUserAge);
        editUserWeight = findViewById(R.id.editUserWeight);
        editUserHeight = findViewById(R.id.editUserHeight);
        editUserGender = findViewById(R.id.editUserGender);
        editUserActivityLevel = findViewById(R.id.editUserActivityLevel);
        editUserGoal = findViewById(R.id.editUserGoal);
        saveUserButton = findViewById(R.id.saveUserButton);
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editUserGender.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> activityLevelAdapter = ArrayAdapter.createFromResource(this,
                R.array.activity_level_options, android.R.layout.simple_spinner_item);
        activityLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editUserActivityLevel.setAdapter(activityLevelAdapter);

        ArrayAdapter<CharSequence> goalAdapter = ArrayAdapter.createFromResource(this,
                R.array.goal_options, android.R.layout.simple_spinner_item);
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editUserGoal.setAdapter(goalAdapter);
    }

    private void loadUserDetails() {
        userRef.get().addOnSuccessListener(snapshot -> {
            User user = snapshot.getValue(User.class);
            if (user != null) {
                // Llena los campos con los datos actuales del usuario
                editUserFirstName.setText(user.getFirstName());
                editUserLastName.setText(user.getLastName());
                editUserEmail.setText(user.getEmail());
                editUserAge.setText(String.valueOf(user.getAge()));
                editUserWeight.setText(String.valueOf(user.getWeight()));
                editUserHeight.setText(String.valueOf(user.getHeight()));

                // Selecciona las opciones correctas en los spinners
                setSpinnerSelection(editUserGender, user.getGender());
                setSpinnerSelection(editUserActivityLevel, user.getActivityLevel());
                setSpinnerSelection(editUserGoal, user.getGoal());
            } else {
                Toast.makeText(this, "No se pudieron cargar los datos del usuario.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error al cargar los datos del usuario.", Toast.LENGTH_SHORT).show();
            Log.e("EditUserActivity", "Error al cargar los datos: " + e.getMessage());
            finish();
        });
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void saveUserChanges() {
        String firstName = editUserFirstName.getText().toString().trim();
        String lastName = editUserLastName.getText().toString().trim();
        String email = editUserEmail.getText().toString().trim();
        int age = Integer.parseInt(editUserAge.getText().toString().trim());
        double weight = Double.parseDouble(editUserWeight.getText().toString().trim());
        double height = Double.parseDouble(editUserHeight.getText().toString().trim());
        String gender = editUserGender.getSelectedItem().toString();
        String activityLevel = editUserActivityLevel.getSelectedItem().toString();
        String goal = editUserGoal.getSelectedItem().toString();

        // Crea un mapa con los datos actualizados
        User updatedUser = new User(firstName, lastName, email, "", age, weight, height, gender, activityLevel, goal);

        userRef.setValue(updatedUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Usuario actualizado correctamente.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar el usuario.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

