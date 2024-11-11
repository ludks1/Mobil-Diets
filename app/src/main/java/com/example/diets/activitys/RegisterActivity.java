package com.example.diets.activitys;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (validateInputs(firstName, lastName, email, password)) {
                    registerUser(firstName, lastName, email, password);
                }
            }
        });
    }

    private boolean validateInputs(String firstName, String lastName, String email, String password) {
        if (firstName.isEmpty()) {
            firstNameEditText.setError("Nombre requerido");
            return false;
        }
        if (lastName.isEmpty()) {
            lastNameEditText.setError("Apellido requerido");
            return false;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Correo electrónico inválido");
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            passwordEditText.setError("La contraseña debe tener al menos 6 caracteres");
            return false;
        }
        return true;
    }

    private void registerUser(String firstName, String lastName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Log.d("RegisterActivity", "Usuario registrado: " + user.getUid());
                            writeNewUser(user.getUid(), firstName, lastName, email);
                            Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            // Aquí puedes redirigir al usuario a otra actividad
                        }
                    } else {
                        Log.d("RegisterActivity", "Error en el registro: " + task.getException().getMessage());
                        Toast.makeText(RegisterActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void writeNewUser(String userId, String firstName, String lastName, String email) {
        User user = new User(firstName, lastName, email, "default", 0, 0.0, 0.0, "default", "default", "default");
        mDatabase.child("users").child(userId).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("RegisterActivity", "Usuario guardado en la base de datos");
                    } else {
                        Log.d("RegisterActivity", "Error al guardar el usuario en la base de datos: " + task.getException().getMessage());
                    }
                });
    }
}
