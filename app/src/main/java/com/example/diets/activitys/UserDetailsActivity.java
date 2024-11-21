package com.example.diets.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diets.R;
import com.example.diets.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetailsActivity extends AppCompatActivity {

    private TextView userDetailsName, userDetailsEmail, userDetailsAge, userDetailsWeight, userDetailsHeight,
            userDetailsGender, userDetailsActivityLevel, userDetailsGoal, userDetailsCaloriesToday,
            userDetailsProteinToday, userDetailsFatToday, userDetailsCarbToday, userDetailsCaloriesNeeded,
            userDetailsIsAdmin, userDetailsLocation;
    private ImageView userDetailsProfileImage;
    private Button editUserButton, deleteUserButton;
    private DatabaseReference userRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // Inicializa los TextViews, ImageView y botones
        initializeUI();

        // Obtén el Intent y el userId
        userId = getIntent().getStringExtra("userId");

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "ID de usuario no encontrado.", Toast.LENGTH_SHORT).show();
            Log.e("UserDetailsActivity", "ID de usuario es nulo o vacío.");
            finish();
            return;
        }

        Log.d("UserDetailsActivity", "ID de usuario recibido: " + userId);

        // Inicializa Firebase Database Reference
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        // Carga los detalles del usuario
        loadUserDetails();

        // Configura botones
        setupButtons();
    }

    private void initializeUI() {
        userDetailsName = findViewById(R.id.userDetailsName);
        userDetailsEmail = findViewById(R.id.userDetailsEmail);
        userDetailsAge = findViewById(R.id.userDetailsAge);
        userDetailsWeight = findViewById(R.id.userDetailsWeight);
        userDetailsHeight = findViewById(R.id.userDetailsHeight);
        userDetailsGender = findViewById(R.id.userDetailsGender);
        userDetailsActivityLevel = findViewById(R.id.userDetailsActivityLevel);
        userDetailsGoal = findViewById(R.id.userDetailsGoal);
        userDetailsCaloriesToday = findViewById(R.id.userDetailsCaloriesToday);
        userDetailsProteinToday = findViewById(R.id.userDetailsProteinToday);
        userDetailsFatToday = findViewById(R.id.userDetailsFatToday);
        userDetailsCarbToday = findViewById(R.id.userDetailsCarbToday);
        userDetailsCaloriesNeeded = findViewById(R.id.userDetailsCaloriesNeeded);
        userDetailsIsAdmin = findViewById(R.id.userDetailsIsAdmin);
        userDetailsLocation = findViewById(R.id.userDetailsLocation);
        userDetailsProfileImage = findViewById(R.id.userDetailsProfileImage);
        editUserButton = findViewById(R.id.editUserButton);
        deleteUserButton = findViewById(R.id.deleteUserButton);
    }

    private void loadUserDetails() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        double caloriesNeeded = user.calculateCalories(user.getWeight(), user.getHeight(), user.getAge(),
                                user.getGender(), user.getActivityLevel(), user.getGoal());

                        userDetailsName.setText("Nombre: " + user.getFirstName() + " " + user.getLastName());
                        userDetailsEmail.setText("Email: " + user.getEmail());
                        userDetailsAge.setText("Edad: " + user.getAge());
                        userDetailsWeight.setText("Peso: " + user.getWeight() + " kg");
                        userDetailsHeight.setText("Altura: " + user.getHeight() + " cm");
                        userDetailsGender.setText("Género: " + user.getGender());
                        userDetailsActivityLevel.setText("Nivel de actividad: " + user.getActivityLevel());
                        userDetailsGoal.setText("Meta: " + user.getGoal());
                        userDetailsCaloriesToday.setText("Calorías consumidas hoy: " + user.getCaloriesToday());
                        userDetailsProteinToday.setText("Proteínas consumidas hoy: " + user.getProteinToday() + " g");
                        userDetailsFatToday.setText("Grasas consumidas hoy: " + user.getFatToday() + " g");
                        userDetailsCarbToday.setText("Carbohidratos consumidos hoy: " + user.getCarbToday() + " g");
                        userDetailsCaloriesNeeded.setText("Calorías necesarias: " + String.format("%.2f", caloriesNeeded) + " kcal");
                        userDetailsIsAdmin.setText("Administrador: " + (user.isAdmin() ? "Sí" : "No"));

                        // Muestra la ubicación
                        userDetailsLocation.setText(String.format("Ubicación: Lat %.6f, Lng %.6f", user.getLatitude(), user.getLongitude()));

                        // Decodifica y muestra la imagen de perfil
                        loadProfileImage(user.getProfilePhoto());
                    } else {
                        Toast.makeText(UserDetailsActivity.this, "No se pudo obtener la información del usuario.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(UserDetailsActivity.this, "Usuario no encontrado.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserDetailsActivity.this, "Error al cargar los datos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProfileImage(String profilePhotoBase64) {
        if (profilePhotoBase64 == null || profilePhotoBase64.isEmpty()) {
            userDetailsProfileImage.setImageResource(R.drawable.placeholder);
            return;
        }

        try {
            byte[] decodedBytes = Base64.decode(profilePhotoBase64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            userDetailsProfileImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e("UserDetailsActivity", "Error al decodificar la imagen: " + e.getMessage());
            userDetailsProfileImage.setImageResource(R.drawable.placeholder);
        }
    }

    private void setupButtons() {
        editUserButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserDetailsActivity.this, EditUserActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        deleteUserButton.setOnClickListener(v -> new AlertDialog.Builder(UserDetailsActivity.this)
                .setTitle("Eliminar Usuario")
                .setMessage("¿Estás seguro de eliminar este usuario?")
                .setPositiveButton("Eliminar", (DialogInterface dialog, int which) -> userRef.removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(UserDetailsActivity.this, "Usuario eliminado correctamente.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UserDetailsActivity.this, "Error al eliminar el usuario.", Toast.LENGTH_SHORT).show();
                    }
                }))
                .setNegativeButton("Cancelar", null)
                .show());
    }
}
