package com.example.diets.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.diets.R;
import com.example.diets.api.FoodService;
import com.example.diets.model.User;
import com.example.diets.record.RecipeResponse;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";

    // UI elements
    private TextView caloriesNeededTextView, caloriesTodayTextView, proteinTextView, fatTextView, carbTextView;
    private Button addBreakfastButton, addLunchButton, addDinnerButton, addSnacksButton;
    private EditText foodTextField;
    private NavigationView navigationView;

    // Firebase and services
    private FoodService foodService;
    private User user;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize Firebase and service
        foodService = new FoodService();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI
        navigationView = findViewById(R.id.navigation_view);
        initializeUI();
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_logout) { // Asegúrate de que el ID coincide con el del archivo XML del menú
                logout();
                return true;
            }
            return false;
        });

        // Fetch current Firebase user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user = snapshot.getValue(User.class);
                    if (user != null) {
                        Log.d(TAG, "Datos del usuario cargados: " + user);
                        TextView dashboardGreeting = findViewById(R.id.dashboardGreeting);
                        dashboardGreeting.setText("Hola, " + user.getFirstName() + " " + user.getLastName());
                        updateNutritionalValues(user.getCaloriesToday(), user.getProteinToday(), user.getFatToday(), user.getCarbToday(), user.calculateCalories(user.getWeight(), user.getHeight(), user.getAge(), user.getGender(), user.getActivityLevel(), user.getGoal()));
                    } else {
                        Log.e(TAG, "El usuario es nulo.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Error al obtener los datos del usuario", error.toException());
                }

            });
        }
    }

    private void initializeUI() {
        foodTextField = findViewById(R.id.foodTextField);
        caloriesNeededTextView = findViewById(R.id.caloriesNeededTextView);
        caloriesTodayTextView = findViewById(R.id.caloriesTodayTextView);
        proteinTextView = findViewById(R.id.proteinTextView);
        fatTextView = findViewById(R.id.fatTextView);
        carbTextView = findViewById(R.id.carbTextView);

        addBreakfastButton = findViewById(R.id.addBreakfastButton);
        addLunchButton = findViewById(R.id.addLunchButton);
        addDinnerButton = findViewById(R.id.addDinnerButton);
        addSnacksButton = findViewById(R.id.addSnacksButton);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupFoodButtons();
    }

    private void setupFoodButtons() {
        addBreakfastButton.setOnClickListener(v -> fetchAndAddRecipe("Desayuno"));
        addLunchButton.setOnClickListener(v -> fetchAndAddRecipe("Almuerzo"));
        addDinnerButton.setOnClickListener(v -> fetchAndAddRecipe("Cena"));
        addSnacksButton.setOnClickListener(v -> fetchAndAddRecipe("Snacks"));
    }

    private void fetchAndAddRecipe(String mealType) {
        String food = foodTextField.getText().toString().trim();
        if (food.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa un alimento.", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                List<RecipeResponse.Result> recipes = foodService.getFoodRecipes(food);
                if (recipes != null && !recipes.isEmpty()) {
                    Log.d(TAG, "Recetas obtenidas: " + recipes);
                    runOnUiThread(() -> showRecipeSelectionDialog(recipes, mealType));
                } else {
                    Log.w(TAG, "No se encontraron recetas.");
                    runOnUiThread(() -> Toast.makeText(this, "No se encontraron recetas.", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                Log.e(TAG, "Error al obtener las recetas.", e);
                runOnUiThread(() -> Toast.makeText(this, "Error al obtener las recetas.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void showRecipeSelectionDialog(List<RecipeResponse.Result> recipes, String mealType) {
        // Placeholder dialog for selecting a recipe
        RecipeSelectionDialog dialog = new RecipeSelectionDialog(recipes, selectedRecipe -> {
            new Thread(() -> {
                try {
                    JsonObject nutrition = foodService.getRecipeNutrition(selectedRecipe.id());
                    if (nutrition != null) {
                        double calories = 0.0, protein = 0.0, fat = 0.0, carbohydrates = 0.0;

                        for (var nutrient : nutrition.getAsJsonArray("nutrients")) {
                            String name = nutrient.getAsJsonObject().get("name").getAsString();
                            double amount = nutrient.getAsJsonObject().get("amount").getAsDouble();

                            switch (name) {
                                case "Calories":
                                    calories = amount;
                                    break;
                                case "Protein":
                                    protein = amount;
                                    break;
                                case "Fat":
                                    fat = amount;
                                    break;
                                case "Carbohydrates":
                                    carbohydrates = amount;
                                    break;
                            }
                        }

                        double finalCalories = calories;
                        double finalProtein = protein;
                        double finalFat = fat;
                        double finalCarbohydrates = carbohydrates;

                        runOnUiThread(() -> {
                            updateMealDetails(mealType, selectedRecipe.title());
                            Toast.makeText(this, "Receta agregada a " + mealType + ": " + selectedRecipe.title(), Toast.LENGTH_SHORT).show();

                            user.addCaloriesToday(finalCalories);
                            user.addProteinToday(finalProtein);
                            user.addFatToday(finalFat);
                            user.addCarbToday(finalCarbohydrates);

                            updateNutritionalValues(
                                    user.getCaloriesToday(),
                                    user.getProteinToday(),
                                    user.getFatToday(),
                                    user.getCarbToday(),
                                    user.calculateCalories(user.getWeight(), user.getHeight(), user.getAge(), user.getGender(), user.getActivityLevel(), user.getGoal())
                            );
                        });
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error al obtener la información nutricional.", e);
                }
            }).start();
        });
        dialog.show(getSupportFragmentManager(), "RecipeSelectionDialog");
    }
    private void logout() {
        FirebaseAuth.getInstance().signOut(); // Cierra la sesión del usuario actual
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
        finish(); // Finaliza la actividad actual
        startActivity(new Intent(DashboardActivity.this, MainActivity.class)); // Redirige al login
    }

    private void updateNutritionalValues(double caloriesToday, double protein, double fat, double carbs, double caloriesNeeded) {
        Log.d(TAG, "Actualizando valores nutricionales: " + caloriesToday);
        caloriesTodayTextView.setText("Calorías de hoy: " + String.format("%.2f", caloriesToday) + " kcal");
        caloriesNeededTextView.setText("Calorías necesarias: " + String.format("%.2f", caloriesNeeded) + " kcal");
        proteinTextView.setText("Proteínas: " + String.format("%.2f", protein) + "g");
        fatTextView.setText("Grasas: " + String.format("%.2f", fat) + "g");
        carbTextView.setText("Carbohidratos: " + String.format("%.2f", carbs) + "g");
    }

    private void updateMealDetails(String mealType, String recipeTitle) {
        switch (mealType) {
            case "Desayuno":
                ((TextView) findViewById(R.id.breakfastDetails)).setText(recipeTitle);
                break;
            case "Almuerzo":
                ((TextView) findViewById(R.id.lunchDetails)).setText(recipeTitle);
                break;
            case "Cena":
                ((TextView) findViewById(R.id.dinnerDetails)).setText(recipeTitle);
                break;
            case "Snacks":
                ((TextView) findViewById(R.id.snacksDetails)).setText(recipeTitle);
                break;
        }
    }
}
