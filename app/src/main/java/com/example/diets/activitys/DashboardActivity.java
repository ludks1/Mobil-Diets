package com.example.diets.activitys;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.IOException;
import java.util.List;
import com.google.gson.JsonObject;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";

    private TextView caloriesNeededTextView, caloriesTodayTextView, proteinTextView, fatTextView, carbTextView;
    private Button addBreakfastButton, addLunchButton, addDinnerButton, addSnacksButton;
    private EditText foodTextField;
    private FoodService foodService;
    private User user;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        foodService = new FoodService();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Obtener el usuario actual de Firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user = snapshot.getValue(User.class);
                    if (user != null) {
                        Log.d(TAG, "Datos del usuario cargados: " + user);
                        initializeUI();
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
        // Inicialización de vistas
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
        RecipeSelectionDialog dialog = new RecipeSelectionDialog(recipes, selectedRecipe -> {
            new Thread(() -> {
                try {
                    // Obtener información nutricional de la receta seleccionada
                    JsonObject nutrition = foodService.getRecipeNutrition(selectedRecipe.id());

                    if (nutrition != null) {
                        double calories = 0.0;
                        double protein = 0.0;
                        double fat = 0.0;
                        double carbohydrates = 0.0;

                        // Extraer los datos nutricionales de la respuesta JSON
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
                            // Actualizar detalles del desayuno, almuerzo, cena, etc.
                            updateMealDetails(mealType, selectedRecipe.title());

                            // Mensaje Toast para mostrar la receta agregada
                            Toast.makeText(this, "Receta agregada a " + mealType + ": " + selectedRecipe.title(), Toast.LENGTH_SHORT).show();

                            // Registrar información nutricional en el Logcat
                            Log.d(TAG, "Receta seleccionada: " + selectedRecipe.getRecipeInfo());

                            // Sumar los valores al usuario
                            user.addCaloriesToday(finalCalories);
                            user.addProteinToday(finalProtein);
                            user.addFatToday(finalFat);
                            user.addCarbToday(finalCarbohydrates);

                            // Registrar la nueva información del usuario en el Logcat
                            Log.d(TAG, "Valores nutricionales después de agregar el alimento: " +
                                    "Calorías hoy: " + user.getCaloriesToday() +
                                    ", Proteínas hoy: " + user.getProteinToday() +
                                    ", Grasas hoy: " + user.getFatToday() +
                                    ", Carbohidratos hoy: " + user.getCarbToday());

                            // Actualizar la vista con los nuevos valores nutricionales
                            updateNutritionalValues(user.getCaloriesToday(), user.getProteinToday(), user.getFatToday(), user.getCarbToday(), user.calculateCalories(user.getWeight(), user.getHeight(), user.getAge(), user.getGender(), user.getActivityLevel(), user.getGoal()));
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "Error al obtener la información nutricional.", Toast.LENGTH_SHORT).show());
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error al obtener la información nutricional.", e);
                    runOnUiThread(() -> Toast.makeText(this, "Error al obtener la información nutricional.", Toast.LENGTH_SHORT).show());
                }
            }).start();
        });
        dialog.show(getSupportFragmentManager(), "RecipeSelectionDialog");
    }


    private void updateNutritionalValues(double caloriesToday, double protein, double fat, double carbs, double caloriesNeeded) {
        Log.d(TAG, "Actualizando valores nutricionales en la UI. Calorías hoy: " + caloriesToday +
                ", Proteínas: " + protein + ", Grasas: " + fat + ", Carbohidratos: " + carbs);
        caloriesTodayTextView.setText("Calorías de hoy: " + String.format("%.2f", caloriesToday) + " kcal");
        caloriesNeededTextView.setText("Calorías necesarias: " + String.format("%.2f", caloriesNeeded) + " kcal");
        proteinTextView.setText("Proteínas: " + String.format("%.2f", protein) + "g");
        fatTextView.setText("Grasas: " + String.format("%.2f", fat) + "g");
        carbTextView.setText("Carbohidratos: " + String.format("%.2f", carbs) + "g");
    }

    private void updateMealDetails(String mealType, String recipeTitle) {
        switch (mealType) {
            case "Desayuno":
                TextView breakfastDetails = findViewById(R.id.breakfastDetails);
                breakfastDetails.setText(recipeTitle);
                break;
            case "Almuerzo":
                TextView lunchDetails = findViewById(R.id.lunchDetails);
                lunchDetails.setText(recipeTitle);
                break;
            case "Cena":
                TextView dinnerDetails = findViewById(R.id.dinnerDetails);
                dinnerDetails.setText(recipeTitle);
                break;
            case "Snacks":
                TextView snacksDetails = findViewById(R.id.snacksDetails);
                snacksDetails.setText(recipeTitle);
                break;
        }
    }
}
