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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.diets.R;
import com.example.diets.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class EditUserActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 102;

    private EditText editUserFirstName, editUserLastName, editUserEmail, editUserLatitude, editUserLongitude;
    private Spinner editUserAgeSpinner, editUserWeightSpinner, editUserHeightSpinner, editUserGender, editUserActivityLevel, editUserGoal;
    private CheckBox editUserAdminCheckBox;
    private Button saveUserButton, editUserPhotoButton;
    private ImageView editUserProfileImage;

    private DatabaseReference userRef;
    private String userId;
    private String profilePhotoBase64 = "";

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
        saveUserButton.setOnClickListener(v -> {
            if (validateInputs()) {
                saveUserChanges();
            }
        });

        // Configura el botón de editar foto
        editUserPhotoButton.setOnClickListener(v -> openCamera());
    }

    private void initializeUI() {
        editUserFirstName = findViewById(R.id.editUserFirstName);
        editUserLastName = findViewById(R.id.editUserLastName);
        editUserEmail = findViewById(R.id.editUserEmail);
        editUserLatitude = findViewById(R.id.editUserLatitude);
        editUserLongitude = findViewById(R.id.editUserLongitude);
        editUserAgeSpinner = findViewById(R.id.editUserAgeSpinner);
        editUserWeightSpinner = findViewById(R.id.editUserWeightSpinner);
        editUserHeightSpinner = findViewById(R.id.editUserHeightSpinner);
        editUserGender = findViewById(R.id.editUserGender);
        editUserActivityLevel = findViewById(R.id.editUserActivityLevel);
        editUserGoal = findViewById(R.id.editUserGoal);
        editUserAdminCheckBox = findViewById(R.id.editUserAdminCheckBox);
        saveUserButton = findViewById(R.id.saveUserButton);
        editUserPhotoButton = findViewById(R.id.editUserPhotoButton);
        editUserProfileImage = findViewById(R.id.editUserProfileImage);
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(this,
                R.array.age_options, android.R.layout.simple_spinner_item);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editUserAgeSpinner.setAdapter(ageAdapter);

        ArrayAdapter<CharSequence> weightAdapter = ArrayAdapter.createFromResource(this,
                R.array.weight_options, android.R.layout.simple_spinner_item);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editUserWeightSpinner.setAdapter(weightAdapter);

        ArrayAdapter<CharSequence> heightAdapter = ArrayAdapter.createFromResource(this,
                R.array.height_options, android.R.layout.simple_spinner_item);
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editUserHeightSpinner.setAdapter(heightAdapter);

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

    private boolean validateInputs() {
        String firstName = editUserFirstName.getText().toString().trim();
        if (firstName.isEmpty()) {
            Toast.makeText(this, "El nombre no puede estar vacío.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!firstName.matches("[a-zA-Z]+")) {
            Toast.makeText(this, "El nombre no puede contener caracteres especiales ni números.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validación del apellido
        String lastName = editUserLastName.getText().toString().trim();
        if (lastName.isEmpty()) {
            Toast.makeText(this, "El apellido no puede estar vacío.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!lastName.matches("[a-zA-Z]+")) {
            Toast.makeText(this, "El apellido no puede contener caracteres especiales ni números.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validación del correo electrónico
        String email = editUserEmail.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "El correo electrónico no puede estar vacío.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!email.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            Toast.makeText(this, "El correo electrónico debe tener una estructura válida (ejemplo: usuario@dominio.com).", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (editUserLatitude.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "La latitud no puede estar vacía.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (editUserLongitude.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "La longitud no puede estar vacía.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void loadUserDetails() {
        userRef.get().addOnSuccessListener(snapshot -> {
            User user = snapshot.getValue(User.class);
            if (user != null) {
                editUserFirstName.setText(user.getFirstName());
                editUserLastName.setText(user.getLastName());
                editUserEmail.setText(user.getEmail());
                editUserLatitude.setText(String.valueOf(user.getLatitude()));
                editUserLongitude.setText(String.valueOf(user.getLongitude()));
                editUserAdminCheckBox.setChecked(user.isAdmin());

                setSpinnerSelection(editUserAgeSpinner, String.valueOf(user.getAge()));
                setSpinnerSelection(editUserWeightSpinner, String.valueOf(user.getWeight()));
                setSpinnerSelection(editUserHeightSpinner, String.valueOf(user.getHeight()));
                setSpinnerSelection(editUserGender, user.getGender());
                setSpinnerSelection(editUserActivityLevel, user.getActivityLevel());
                setSpinnerSelection(editUserGoal, user.getGoal());

                loadProfileImage(user.getProfilePhoto());
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
            if (adapter.getItem(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
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
            editUserProfileImage.setImageBitmap(photo);
            profilePhotoBase64 = encodeImageToBase64(photo);
        }
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void loadProfileImage(String profilePhotoBase64) {
        if (profilePhotoBase64 == null || profilePhotoBase64.isEmpty()) {
            editUserProfileImage.setImageResource(R.drawable.placeholder);
            return;
        }

        try {
            byte[] decodedBytes = Base64.decode(profilePhotoBase64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            editUserProfileImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e("EditUserActivity", "Error al decodificar la imagen: " + e.getMessage());
            editUserProfileImage.setImageResource(R.drawable.placeholder);
        }
    }

    private void saveUserChanges() {
        userRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                User currentUser = snapshot.getValue(User.class);

                if (currentUser != null) {
                    // Mantener la contraseña actual
                    String password = currentUser.getPassword();

                    // Si no se cambió la foto, mantener la foto actual
                    String profilePhoto = profilePhotoBase64.isEmpty() ? currentUser.getProfilePhoto() : profilePhotoBase64;

                    // Actualizar solo los campos necesarios
                    String firstName = editUserFirstName.getText().toString().trim();
                    String lastName = editUserLastName.getText().toString().trim();
                    String email = editUserEmail.getText().toString().trim();
                    double latitude = Double.parseDouble(editUserLatitude.getText().toString().trim());
                    double longitude = Double.parseDouble(editUserLongitude.getText().toString().trim());
                    int age = Integer.parseInt(editUserAgeSpinner.getSelectedItem().toString());
                    double weight = Double.parseDouble(editUserWeightSpinner.getSelectedItem().toString());
                    double height = Double.parseDouble(editUserHeightSpinner.getSelectedItem().toString());
                    String gender = editUserGender.getSelectedItem().toString();
                    String activityLevel = editUserActivityLevel.getSelectedItem().toString();
                    String goal = editUserGoal.getSelectedItem().toString();
                    boolean isAdmin = editUserAdminCheckBox.isChecked();

                    // Crear el objeto actualizado
                    User updatedUser = new User(firstName, lastName, email, password, age, weight, height, gender, activityLevel, goal);
                    updatedUser.setLatitude(latitude);
                    updatedUser.setLongitude(longitude);
                    updatedUser.setProfilePhoto(profilePhoto);
                    updatedUser.setAdmin(isAdmin);

                    // Guardar cambios en Firebase
                    userRef.setValue(updatedUser).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Usuario actualizado correctamente.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Error al actualizar el usuario.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "Usuario no encontrado.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error al acceder a los datos del usuario.", Toast.LENGTH_SHORT).show();
            Log.e("EditUserActivity", "Error al acceder a los datos del usuario: " + e.getMessage());
        });
    }

}
