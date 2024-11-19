package com.example.diets.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.diets.R;
import com.example.diets.api.FoodService;
import com.example.diets.model.User;
import com.example.diets.record.RecipeResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";

    // UI elements
    private TextView caloriesNeededTextView, caloriesTodayTextView, proteinTextView, fatTextView, carbTextView;
    private TextView breakfastDetails, lunchDetails, dinnerDetails, snacksDetails;
    private Button addBreakfastButton, modifyBreakfastButton, deleteBreakfastButton;
    private Button addLunchButton, modifyLunchButton, deleteLunchButton;
    private Button addDinnerButton, modifyDinnerButton, deleteDinnerButton;
    private Button addSnacksButton, modifySnacksButton, deleteSnacksButton;
    private EditText foodTextField;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;

    // Firebase and services
    private FoodService foodService;
    private User user;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize Firebase and services
        foodService = new FoodService();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI
        navigationView = findViewById(R.id.navigation_view);
        initializeUI();

        // Setup navigation listener
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                logout();
                return true;
            }
            return false;
        });

        // Setup BottomNavigationView listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                // Ya estás en el Home
                Toast.makeText(this, "Ya estás en Home", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.navigation_dashboard) {
                // Redirige a la actividad de gestión del peso
                Intent weightIntent = new Intent(DashboardActivity.this, WeightActivity.class);
                startActivity(weightIntent);
                return true;
            } else if (item.getItemId() == R.id.navigation_video) {
                // Redirige a la actividad de video
                Intent videoIntent = new Intent(DashboardActivity.this, VideoActivity.class);
                startActivity(videoIntent);
                return true;
            }
            return false;
        });


        // Fetch current Firebase user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            loadUserData(currentUser.getUid());
        }
    }



    private void initializeUI() {
        foodTextField = findViewById(R.id.foodTextField);
        caloriesNeededTextView = findViewById(R.id.caloriesNeededTextView);
        caloriesTodayTextView = findViewById(R.id.caloriesTodayTextView);
        proteinTextView = findViewById(R.id.proteinTextView);
        fatTextView = findViewById(R.id.fatTextView);
        carbTextView = findViewById(R.id.carbTextView);

        breakfastDetails = findViewById(R.id.breakfastDetails);
        lunchDetails = findViewById(R.id.lunchDetails);
        dinnerDetails = findViewById(R.id.dinnerDetails);
        snacksDetails = findViewById(R.id.snacksDetails);

        addBreakfastButton = findViewById(R.id.addBreakfastButton);
        modifyBreakfastButton = findViewById(R.id.modifyBreakfastButton);
        deleteBreakfastButton = findViewById(R.id.deleteBreakfastButton);

        addLunchButton = findViewById(R.id.addLunchButton);
        modifyLunchButton = findViewById(R.id.modifyLunchButton);
        deleteLunchButton = findViewById(R.id.deleteLunchButton);

        addDinnerButton = findViewById(R.id.addDinnerButton);
        modifyDinnerButton = findViewById(R.id.modifyDinnerButton);
        deleteDinnerButton = findViewById(R.id.deleteDinnerButton);

        addSnacksButton = findViewById(R.id.addSnacksButton);
        modifySnacksButton = findViewById(R.id.modifySnacksButton);
        deleteSnacksButton = findViewById(R.id.deleteSnacksButton);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupFoodButtons();
    }

    private void setupFoodButtons() {
        // Add food functionality
        addBreakfastButton.setOnClickListener(v -> fetchAndAddRecipe("Desayuno"));
        addLunchButton.setOnClickListener(v -> fetchAndAddRecipe("Almuerzo"));
        addDinnerButton.setOnClickListener(v -> fetchAndAddRecipe("Cena"));
        addSnacksButton.setOnClickListener(v -> fetchAndAddRecipe("Snacks"));

        // Modify food functionality
        modifyBreakfastButton.setOnClickListener(v -> modifyMeal("Desayuno"));
        modifyLunchButton.setOnClickListener(v -> modifyMeal("Almuerzo"));
        modifyDinnerButton.setOnClickListener(v -> modifyMeal("Cena"));
        modifySnacksButton.setOnClickListener(v -> modifyMeal("Snacks"));

        // Delete food functionality
        deleteBreakfastButton.setOnClickListener(v -> deleteMeal("Desayuno"));
        deleteLunchButton.setOnClickListener(v -> deleteMeal("Almuerzo"));
        deleteDinnerButton.setOnClickListener(v -> deleteMeal("Cena"));
        deleteSnacksButton.setOnClickListener(v -> deleteMeal("Snacks"));
    }

    private void loadUserData(String userId) {
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (user != null) {
                    String greeting = String.format(Locale.getDefault(), "Hola, %s %s", user.getFirstName(), user.getLastName());
                    ((TextView) findViewById(R.id.dashboardGreeting)).setText(greeting);
                    updateNutritionalValues(user);
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

    private void modifyMeal(String mealType) {
        String currentMealDetails = getCurrentMealDetails(mealType);

        if (currentMealDetails.equals("Sin detalles disponibles") || currentMealDetails.isEmpty()) {
            Toast.makeText(this, "No hay detalles para modificar en " + mealType, Toast.LENGTH_SHORT).show();
            return;
        }

        String newMeal = foodTextField.getText().toString().trim();
        if (newMeal.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa un alimento.", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                // Obtener información nutricional del alimento actual
                JsonObject oldNutrition = foodService.getFoodNutrition(currentMealDetails);
                if (oldNutrition == null || !oldNutrition.has("nutrients")) {
                    runOnUiThread(() -> Toast.makeText(this, "No se encontró información nutricional del alimento actual.", Toast.LENGTH_SHORT).show());
                    return;
                }

                JsonArray oldNutrients = oldNutrition.getAsJsonArray("nutrients");
                adjustUserNutrition(oldNutrients, false); // Restar valores del alimento actual

                // Obtener información nutricional del nuevo alimento
                JsonObject newNutrition = foodService.getFoodNutrition(newMeal);
                if (newNutrition == null || !newNutrition.has("nutrients")) {
                    adjustUserNutrition(oldNutrients, true); // Restaurar valores originales
                    runOnUiThread(() -> Toast.makeText(this, "No se encontró información nutricional del nuevo alimento.", Toast.LENGTH_SHORT).show());
                    return;
                }

                JsonArray newNutrients = newNutrition.getAsJsonArray("nutrients");
                adjustUserNutrition(newNutrients, true); // Sumar valores del nuevo alimento

                runOnUiThread(() -> {
                    updateMealDetails(mealType, newMeal);
                    Toast.makeText(this, mealType + " modificado correctamente.", Toast.LENGTH_SHORT).show();
                    updateNutritionalValues(user);
                });
            } catch (Exception e) {
                Log.e(TAG, "Error al modificar el alimento: " + e.getMessage(), e);
                runOnUiThread(() -> Toast.makeText(this, "Error al modificar el alimento.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }


    private void deleteMeal(String mealType) {
        String currentMealDetails = getCurrentMealDetails(mealType);

        if (currentMealDetails.equals("Sin detalles disponibles") || currentMealDetails.isEmpty()) {
            Toast.makeText(this, "No hay detalles para eliminar en " + mealType, Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                JsonObject nutrition = foodService.getFoodNutrition(currentMealDetails);
                if (nutrition == null || !nutrition.has("nutrients")) {
                    runOnUiThread(() -> Toast.makeText(this, "No se pudo obtener información nutricional para " + mealType, Toast.LENGTH_SHORT).show());
                    return;
                }

                JsonArray nutrients = nutrition.getAsJsonArray("nutrients");
                adjustUserNutrition(nutrients, false); // Restar valores del alimento

                runOnUiThread(() -> {
                    updateMealDetails(mealType, "Sin detalles disponibles");
                    updateNutritionalValues(user);
                    Toast.makeText(this, mealType + " eliminado y valores actualizados.", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                Log.e(TAG, "Error al eliminar el alimento: " + e.getMessage(), e);
                runOnUiThread(() -> Toast.makeText(this, "Error al eliminar el alimento.", Toast.LENGTH_SHORT).show());
            }
        }).start();
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
                    runOnUiThread(() -> showRecipeSelectionDialog(recipes, mealType));
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "No se encontraron recetas.", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                Log.e(TAG, "Error al obtener las recetas.", e);
                runOnUiThread(() -> Toast.makeText(this, "Error al obtener las recetas.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void updateNutritionalValues(User user) {
        double caloriesNeeded = user.calculateCalories(user.getWeight(), user.getHeight(), user.getAge(),
                user.getGender(), user.getActivityLevel(), user.getGoal());

        Log.d(TAG, "Updating nutritional values: " +
                "Calories Needed=" + caloriesNeeded +
                ", Calories Today=" + user.getCaloriesToday() +
                ", Protein=" + user.getProteinToday() +
                ", Fat=" + user.getFatToday() +
                ", Carbs=" + user.getCarbToday());

        caloriesTodayTextView.setText(String.format(Locale.getDefault(), "Calorías de hoy: %.2f kcal", user.getCaloriesToday()));
        caloriesNeededTextView.setText(String.format(Locale.getDefault(), "Calorías necesarias: %.2f kcal", caloriesNeeded));
        proteinTextView.setText(String.format(Locale.getDefault(), "Proteínas: %.2f g", user.getProteinToday()));
        fatTextView.setText(String.format(Locale.getDefault(), "Grasas: %.2f g", user.getFatToday()));
        carbTextView.setText(String.format(Locale.getDefault(), "Carbohidratos: %.2f g", user.getCarbToday()));
    }

    private void updateMealDetails(String mealType, String recipeTitle) {
        Map<String, TextView> mealDetailsMap = Map.of(
                "Desayuno", breakfastDetails,
                "Almuerzo", lunchDetails,
                "Cena", dinnerDetails,
                "Snacks", snacksDetails
        );

        TextView mealDetailView = mealDetailsMap.getOrDefault(mealType, new TextView(this));
        mealDetailView.setText(recipeTitle);
    }


    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    private void showRecipeSelectionDialog(List<RecipeResponse.Result> recipes, String mealType) {
        // Crear un array de títulos de recetas
        String[] recipeTitles = recipes.stream()
                .map(RecipeResponse.Result::title)
                .toArray(String[]::new);

        // Crear y mostrar un diálogo para seleccionar una receta
        new AlertDialog.Builder(this)
                .setTitle("Selecciona una receta para " + mealType)
                .setItems(recipeTitles, (dialog, which) -> {
                    // Recuperar la receta seleccionada
                    RecipeResponse.Result selectedRecipe = recipes.get(which);
                    fetchAndAddNutritionData(selectedRecipe, mealType);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void fetchAndAddNutritionData(RecipeResponse.Result selectedRecipe, String mealType) {
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

                        // Actualizar valores nutricionales del usuario
                        user.addCaloriesToday(finalCalories);
                        user.addProteinToday(finalProtein);
                        user.addFatToday(finalFat);
                        user.addCarbToday(finalCarbohydrates);

                        updateNutritionalValues(user);
                    });
                }
            } catch (IOException e) {
                Log.e(TAG, "Error al obtener la información nutricional.", e);
                runOnUiThread(() -> Toast.makeText(this, "Error al obtener la información nutricional.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
    private String getCurrentMealDetails(String mealType) {
        Map<String, TextView> mealDetailsMap = Map.of(
                "Desayuno", breakfastDetails,
                "Almuerzo", lunchDetails,
                "Cena", dinnerDetails,
                "Snacks", snacksDetails
        );

        return mealDetailsMap.getOrDefault(mealType, new TextView(this)).getText().toString();
    }
    private void adjustUserNutrition(JsonArray nutrients, boolean isAdding) {
        double calories = 0.0, protein = 0.0, fat = 0.0, carbs = 0.0;

        for (var nutrient : nutrients) {
            String name = nutrient.getAsJsonObject().get("name").getAsString();
            double amount = nutrient.getAsJsonObject().get("amount").getAsDouble();

            Log.d(TAG, String.format("Nutrient Found: %s = %.2f", name, amount));

            switch (name) {
                case "Calories" -> calories = amount;
                case "Protein" -> protein = amount;
                case "Fat" -> fat = amount;
                case "Carbohydrates" -> carbs = amount;
                default -> Log.w(TAG, "Unexpected nutrient: " + name);
            }
        }

// Log nutrientes no encontrados
        if (fat == 0.0) Log.w(TAG, "No fat information found in JSON.");
        if (carbs == 0.0) Log.w(TAG, "No carbohydrates information found in JSON.");


        double factor = isAdding ? 1 : -1;

        Log.d(TAG, String.format("Adjusting nutrition: Calories=%.2f, Protein=%.2f, Fat=%.2f, Carbs=%.2f, Factor=%d",
                calories, protein, fat, carbs, isAdding ? 1 : -1));

        user.addCaloriesToday(factor * calories);
        user.addProteinToday(factor * protein);
        user.addFatToday(factor * fat);
        user.addCarbToday(factor * carbs);

        Log.d(TAG, String.format("Updated user nutrition: Calories=%.2f, Protein=%.2f, Fat=%.2f, Carbs=%.2f",
                user.getCaloriesToday(), user.getProteinToday(), user.getFatToday(), user.getCarbToday()));
    }





}
