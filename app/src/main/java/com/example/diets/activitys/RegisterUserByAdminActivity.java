package com.example.diets.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.diets.R;
import com.example.diets.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.io.ByteArrayOutputStream;

public class RegisterUserByAdminActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 102;
    private static final String TAG = "RegisterUserActivity";

    private EditText firstNameInput, lastNameInput, emailInput, passwordInput;
    private Spinner genderSpinner, ageSpinner, heightSpinner, activityLevelSpinner, goalSpinner;
    private CheckBox isAdminCheckBox;
    private Button registerButton, getLocationButton, takePhotoButton;
    private TextView locationTextView;
    private ImageView profileImageView;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private double currentLatitude = 0.0, currentLongitude = 0.0;
    private String profilePhotoBase64 = "";

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_by_admin);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        initializeUI();
        setupSpinners();
        setupLocation();

        takePhotoButton.setOnClickListener(v -> openCamera());
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
        getLocationButton = findViewById(R.id.getLocationButton);
        takePhotoButton = findViewById(R.id.takePhotoButton);
        locationTextView = findViewById(R.id.locationTextView);
        profileImageView = findViewById(R.id.profileImageView);
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

    private void setupLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Listener para recibir actualizaciones de ubicación
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                locationTextView.setText(String.format("Ubicación: Lat: %.6f, Lng: %.6f", currentLatitude, currentLongitude));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // Se puede implementar si se necesita manejar cambios de estado del proveedor
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                Toast.makeText(RegisterUserByAdminActivity.this, "Proveedor de ubicación habilitado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                Toast.makeText(RegisterUserByAdminActivity.this, "Proveedor de ubicación deshabilitado. Activa el GPS.", Toast.LENGTH_SHORT).show();
            }
        };

        // Configurar botón para obtener la ubicación
        getLocationButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Solicitar permisos
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 103);
            } else {
                // Solicitar actualizaciones de ubicación
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 103) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permisos concedidos, iniciar ubicación
                Toast.makeText(this, "Permiso de ubicación concedido", Toast.LENGTH_SHORT).show();
                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                } catch (SecurityException e) {
                    Log.e(TAG, "Error al solicitar actualizaciones de ubicación: " + e.getMessage());
                }
            } else {
                // Permisos denegados
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            profileImageView.setImageBitmap(photo); // Mostrar vista previa
            profilePhotoBase64 = encodeImageToBase64(photo);
        }
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void registerUser() {
        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem() != null ? genderSpinner.getSelectedItem().toString() : "";
        String ageText = ageSpinner.getSelectedItem() != null ? ageSpinner.getSelectedItem().toString() : "";
        String heightText = heightSpinner.getSelectedItem() != null ? heightSpinner.getSelectedItem().toString() : "";
        String activityLevel = activityLevelSpinner.getSelectedItem() != null ? activityLevelSpinner.getSelectedItem().toString() : "";
        String goal = goalSpinner.getSelectedItem() != null ? goalSpinner.getSelectedItem().toString() : "";
        boolean isAdmin = isAdminCheckBox.isChecked();

        // Validaciones de los campos
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || profilePhotoBase64.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos, incluida la foto de perfil.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!firstName.matches("[a-zA-Z]+")) {
            Toast.makeText(this, "El nombre no puede contener caracteres especiales ni números.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!lastName.matches("[a-zA-Z]+")) {
            Toast.makeText(this, "El apellido no puede contener caracteres especiales ni números.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            Toast.makeText(this, "El correo electrónico debe tener una estructura válida (ejemplo: usuario@dominio.com).", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mDatabase.push().getKey();

        if (userId != null) {
            User user = new User(firstName, lastName, email, password, Integer.parseInt(ageText), Double.parseDouble(heightText), 0.0, gender, activityLevel, goal);
            user.setAdmin(isAdmin);
            user.setId(userId);
            user.setLatitude(currentLatitude);
            user.setLongitude(currentLongitude);
            user.setProfilePhoto(profilePhotoBase64);

            saveUserToDatabase(user);
        }
    }

    private void saveUserToDatabase(User user) {
        mDatabase.child(user.getId()).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show();
                clearInputs();
            } else {
                Toast.makeText(this, "Error al registrar el usuario.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error al guardar el usuario en la base de datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void clearInputs() {
        firstNameInput.setText("");
        lastNameInput.setText("");
        emailInput.setText("");
        passwordInput.setText("");
        genderSpinner.setSelection(0);
        ageSpinner.setSelection(0);
        heightSpinner.setSelection(0);
        activityLevelSpinner.setSelection(0);
        goalSpinner.setSelection(0);
        isAdminCheckBox.setChecked(false);
        profileImageView.setImageResource(R.drawable.placeholder);
        profilePhotoBase64 = "";
        locationTextView.setText("Ubicación: No disponible");
    }
}
