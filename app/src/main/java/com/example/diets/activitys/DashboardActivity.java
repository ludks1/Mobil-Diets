package com.example.diets.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.*;
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
import com.google.firebase.database.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {
    private static final String TAG = "DashboardActivity";

    // UI Elements
    private TextView caloriesNeededTextView, caloriesTodayTextView, proteinTextView, fatTextView, carbTextView;
    private TextView breakfastDetails, lunchDetails, dinnerDetails, snacksDetails;
    private EditText foodTextField;
    private ImageView adminProfileImageView;
    private User user;
    private FoodService foodService;
    private DatabaseReference mDatabase;
    private final Map<String, JsonArray> mealNutritionMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize UI and Firebase
        initializeUI();
        foodService = new FoodService();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        loadUserData(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }
    private void loadUserData(String userId) {
        Log.d(TAG, "Loading user data for userId: " + userId);
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d(TAG, "User data found for userId: " + userId);
                    user = snapshot.getValue(User.class); // Asegúrate de que la clase User esté configurada para Firebase
                    if (user != null) {
                        Log.d(TAG, "User data loaded successfully: " + user.toString());
                        updateNutritionalValues(user); // Actualiza los valores nutricionales en la interfaz
                    }
                } else {
                    Log.w(TAG, "No user data found for userId: " + userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error loading user data: " + error.getMessage(), error.toException());
            }
        });
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
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                // Navegar a la actividad "Home"
                startActivity(new Intent(this, DashboardActivity.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_dashboard) {
                // Navegar a la actividad "Dashboard"
                startActivity(new Intent(this, WeightActivity.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_video) {
                // Navegar a la actividad "Video"
                startActivity(new Intent(this, VideoActivity.class));
                return true;
            } else {
                return false;
            }
        });
        // Botones de Desayuno
        findViewById(R.id.addBreakfastButton).setOnClickListener(v -> fetchAndAddRecipe("Desayuno"));
        findViewById(R.id.deleteBreakfastButton).setOnClickListener(v -> deleteMeal("Desayuno"));
        findViewById(R.id.modifyBreakfastButton).setOnClickListener(v -> modifyMeal("Desayuno"));

        // Botones de Almuerzo
        findViewById(R.id.addLunchButton).setOnClickListener(v -> fetchAndAddRecipe("Almuerzo"));
        findViewById(R.id.deleteLunchButton).setOnClickListener(v -> deleteMeal("Almuerzo"));
        findViewById(R.id.modifyLunchButton).setOnClickListener(v -> modifyMeal("Almuerzo"));

        // Botones de Cena
        findViewById(R.id.addDinnerButton).setOnClickListener(v -> fetchAndAddRecipe("Cena"));
        findViewById(R.id.deleteDinnerButton).setOnClickListener(v -> deleteMeal("Cena"));
        findViewById(R.id.modifyDinnerButton).setOnClickListener(v -> modifyMeal("Cena"));

        // Botones de Snacks
        findViewById(R.id.addSnacksButton).setOnClickListener(v -> fetchAndAddRecipe("Snacks"));
        findViewById(R.id.deleteSnacksButton).setOnClickListener(v -> deleteMeal("Snacks"));
        findViewById(R.id.modifySnacksButton).setOnClickListener(v -> modifyMeal("Snacks"));
    }


    private void modifyMeal(String mealType) {
        String newFood = foodTextField.getText().toString().trim();
        Log.d(TAG, "Modifying meal for " + mealType + ", New food input: " + newFood);

        if (newFood.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa un alimento para modificar.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Eliminar los nutrientes actuales si existen
        JsonArray currentNutrients = mealNutritionMap.get(mealType);
        if (currentNutrients != null) {
            Log.d(TAG, "Removing current nutrients for " + mealType);
            adjustUserNutrition(currentNutrients, false);
            mealNutritionMap.remove(mealType);
        } else {
            Log.d(TAG, "No current nutrients found for " + mealType + ", Proceeding to add new food.");
        }

        // Agregar el nuevo alimento
        fetchAndAddRecipe(mealType);
    }

    private void fetchAndAddRecipe(String mealType) {
        String newFood = foodTextField.getText().toString().trim();
        Log.d(TAG, "Fetching recipe for mealType: " + mealType + ", food input: " + newFood);
        if (newFood.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa un alimento.", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                List<RecipeResponse.Result> recipes = foodService.getFoodRecipes(newFood);
                if (recipes != null && !recipes.isEmpty()) {
                    Log.d(TAG, "Recipes fetched successfully. Count: " + recipes.size());
                    runOnUiThread(() -> showRecipeSelectionDialog(recipes, mealType));
                } else {
                    Log.w(TAG, "No recipes found for food: " + newFood);
                    runOnUiThread(() -> Toast.makeText(this, "No se encontraron recetas.", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                Log.e(TAG, "Error al obtener recetas: " + e.getMessage(), e);
            }
        }).start();
    }

    private void showRecipeSelectionDialog(List<RecipeResponse.Result> recipes, String mealType) {
        String[] recipeTitles = recipes.stream().map(RecipeResponse.Result::title).toArray(String[]::new);
        Log.d(TAG, "Displaying recipe selection dialog. MealType: " + mealType);

        new AlertDialog.Builder(this)
                .setTitle("Selecciona una receta para " + mealType)
                .setItems(recipeTitles, (dialog, which) -> {
                    RecipeResponse.Result selectedRecipe = recipes.get(which);
                    Log.d(TAG, "Recipe selected: " + selectedRecipe.title() + " for " + mealType);
                    fetchAndAddNutritionData(selectedRecipe, mealType);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    Log.d(TAG, "Recipe selection cancelled for mealType: " + mealType);
                    dialog.dismiss();
                })
                .create()
                .show();
    }
    private void deleteMeal(String mealType) {
        // Recuperar los datos nutricionales almacenados para esta comida
        JsonArray nutrients = mealNutritionMap.get(mealType);

        if (nutrients != null) {
            adjustUserNutrition(nutrients, false); // Restar valores nutricionales
            mealNutritionMap.remove(mealType); // Limpia la referencia de los datos
            deleteMealDetails(mealType); // Actualizar detalles del UI
        } else {
            Log.w(TAG, "No stored nutrition data found for mealType: " + mealType);
            Toast.makeText(this, "No hay datos para eliminar.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchAndAddNutritionData(RecipeResponse.Result selectedRecipe, String mealType) {
        new Thread(() -> {
            try {
                JsonObject nutrition = foodService.getRecipeNutrition(selectedRecipe.id());
                if (nutrition != null) {
                    JsonArray nutrients = nutrition.getAsJsonArray("nutrients");

                    // Almacena los nutrientes para futuras eliminaciones
                    mealNutritionMap.put(mealType, nutrients);

                    adjustUserNutrition(nutrients, true); // Sumar valores nutricionales
                    updateMealDetails(mealType, selectedRecipe.title()); // Actualizar detalles de la comida
                }
            } catch (IOException e) {
                Log.e(TAG, "fetchAndAddNutritionData | Error al obtener la nutrición: " + e.getMessage(), e);
            }
        }).start();
    }

    private void adjustUserNutrition(JsonArray nutrients, boolean isAdding) {
        double caloriesChange = 0.0;
        double proteinChange = 0.0;
        double fatChange = 0.0;
        double carbChange = 0.0;

        for (int i = 0; i < nutrients.size(); i++) {
            JsonObject nutrient = nutrients.get(i).getAsJsonObject();
            String name = nutrient.get("name").getAsString();
            double amount = nutrient.get("amount").getAsDouble();

            Log.d(TAG, "Nutrient parsed -> Name: " + name + ", Amount: " + amount);

            switch (name.toLowerCase(Locale.ROOT)) {
                case "calories":
                    caloriesChange = amount;
                    break;
                case "protein":
                    proteinChange = amount;
                    break;
                case "fat":
                    fatChange = amount;
                    break;
                case "carbohydrates":
                    carbChange = amount;
                    break;
                default:
                    Log.d(TAG, "Unprocessed nutrient: " + name);
                    break;
            }
        }

        if (isAdding) {
            user.addCaloriesToday(caloriesChange);
            user.addProteinToday(proteinChange);
            user.addFatToday(fatChange);
            user.addCarbToday(carbChange);
        } else {
            user.subtractCaloriesToday(caloriesChange);
            user.subtractProteinToday(proteinChange);
            user.subtractFatToday(fatChange);
            user.subtractCarbToday(carbChange);
        }

        Log.d(TAG, "adjustUserNutrition | " + (isAdding ? "Added" : "Subtracted") +
                " Values -> Calories: " + caloriesChange +
                ", Protein: " + proteinChange +
                ", Fat: " + fatChange +
                ", Carbohydrates: " + carbChange);

        runOnUiThread(() -> updateNutritionalValues(user));
    }






    private void updateMealDetails(String mealType, String recipeTitle) {
        Map<String, TextView> mealDetailsMap = Map.of(
                "Desayuno", breakfastDetails,
                "Almuerzo", lunchDetails,
                "Cena", dinnerDetails,
                "Snacks", snacksDetails
        );

        TextView mealDetailView = mealDetailsMap.get(mealType);
        if (mealDetailView != null) {
            runOnUiThread(() -> {
                Log.d(TAG, "Updating meal details: " + mealType + " with recipe: " + recipeTitle);
                mealDetailView.setText(recipeTitle);
            });
        } else {
            Log.w(TAG, "MealType not recognized: " + mealType);
        }
    }
    private void deleteMealDetails(String mealType) {
        Map<String, TextView> mealDetailsMap = Map.of(
                "Desayuno", breakfastDetails,
                "Almuerzo", lunchDetails,
                "Cena", dinnerDetails,
                "Snacks", snacksDetails
        );

        TextView mealDetailView = mealDetailsMap.get(mealType);
        if (mealDetailView != null) {
            runOnUiThread(() -> {
                Log.d(TAG, "Deleting meal details for " + mealType);
                mealDetailView.setText("Sin detalles disponibles");
            });
        } else {
            Log.w(TAG, "MealType not recognized: " + mealType);
        }
    }
    private JsonObject retrieveStoredNutritionData(String mealDetails) {
        try {
            Log.d(TAG, "Retrieving stored nutrition data for meal: " + mealDetails);
            return foodService.getFoodNutrition(mealDetails);
        } catch (IOException e) {
            Log.e(TAG, "Error retrieving nutrition data for meal: " + mealDetails + " - " + e.getMessage(), e);
            return null; // Devuelve null en caso de error
        }
    }

    private JsonObject getFoodNutrition(String foodName) {
        JsonObject nutritionData = null;

        try {
            // Llama a la API o base de datos usando el servicio de alimentos
            Log.d(TAG, "Fetching nutrition data for food: " + foodName);
            nutritionData = foodService.getFoodNutrition(foodName); // Suponiendo que este método ya está implementado en FoodService

            // Validar que la respuesta no sea nula y contenga datos
            if (nutritionData != null && nutritionData.has("nutrients")) {
                Log.d(TAG, "Nutrition data fetched successfully for food: " + foodName);
            } else {
                Log.w(TAG, "No nutrition data found for food: " + foodName);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error fetching nutrition data for food: " + foodName + " - " + e.getMessage(), e);
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error in getFoodNutrition for food: " + foodName + " - " + e.getMessage(), e);
        }

        return nutritionData;
    }

    private void updateNutritionalValues(User user) {
        double caloriesNeeded = user.calculateCalories(
                user.getWeight(),
                user.getHeight(),
                user.getAge(),
                user.getGender(),
                user.getActivityLevel(),
                user.getGoal()
        );

        runOnUiThread(() -> {
            Log.d(TAG, "Updating nutritional values in UI");
            caloriesTodayTextView.setText(String.format(Locale.getDefault(), "Calorías de hoy: %.2f kcal", user.getCaloriesToday()));
            caloriesNeededTextView.setText(String.format(Locale.getDefault(), "Calorías necesarias: %.2f kcal", caloriesNeeded));
            proteinTextView.setText(String.format(Locale.getDefault(), "Proteínas: %.2f g", user.getProteinToday()));
            fatTextView.setText(String.format(Locale.getDefault(), "Grasas: %.2f g", user.getFatToday()));
            carbTextView.setText(String.format(Locale.getDefault(), "Carbohidratos: %.2f g", user.getCarbToday()));
        });
    }
    private String getCurrentMealDetails(String mealType) {
        Map<String, TextView> mealDetailsMap = Map.of(
                "Desayuno", breakfastDetails,
                "Almuerzo", lunchDetails,
                "Cena", dinnerDetails,
                "Snacks", snacksDetails
        );

        TextView mealDetailView = mealDetailsMap.get(mealType);
        if (mealDetailView != null) {
            String details = mealDetailView.getText().toString();
            Log.d(TAG, "Current meal details for " + mealType + ": " + details);
            return details;
        } else {
            Log.w(TAG, "MealType not recognized: " + mealType);
            return "Sin detalles disponibles";
        }
    }


}
