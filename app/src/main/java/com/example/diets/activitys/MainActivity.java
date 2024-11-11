package com.example.diets.activitys;

import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Verifica si el usuario ya está autenticado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d(TAG, "Usuario ya autenticado: " + currentUser.getUid());
            checkFirstTimeLogin(currentUser.getUid());
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (validateInputs(email, password)) {
                    Log.d(TAG, "Intentando iniciar sesión con: " + email);
                    loginUser(email, password);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Correo electrónico inválido");
            Log.d(TAG, "Correo electrónico inválido: " + email);
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            passwordEditText.setError("La contraseña debe tener al menos 6 caracteres");
            Log.d(TAG, "Contraseña inválida: " + password);
            return false;
        }
        return true;
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Log.d(TAG, "Inicio de sesión exitoso: " + user.getUid());
                            checkFirstTimeLogin(user.getUid());
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Error en el inicio de sesión";
                        Log.d(TAG, "Error en el inicio de sesión: " + errorMessage);
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkFirstTimeLogin(String userId) {
        Log.d(TAG, "Verificando primer inicio de sesión para el usuario: " + userId);
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange llamado");
                if (snapshot.exists()) {
                    Log.d(TAG, "Snapshot existe para el usuario: " + userId);
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        Log.d(TAG, "Datos del usuario obtenidos: " + user.toString());
                        if (user.isFirstTime()) {
                            Log.d(TAG, "Redirigiendo al formulario");
                            startActivity(new Intent(MainActivity.this, UserFormActivity.class));
                        } else {
                            Log.d(TAG, "Redirigiendo al dashboard");
                            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                        }
                        finish();
                    } else {
                        Log.d(TAG, "El usuario es nulo");
                        Toast.makeText(MainActivity.this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "El snapshot no existe para el usuario: " + userId);
                    Toast.makeText(MainActivity.this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error de base de datos: " + error.getMessage());
                Toast.makeText(MainActivity.this, "Error de base de datos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
