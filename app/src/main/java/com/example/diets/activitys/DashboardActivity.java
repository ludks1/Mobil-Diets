package com.example.diets.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.diets.R;
import com.example.diets.api.FoodService;
import com.example.diets.model.User;
import com.example.diets.record.RecipeResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.diets.R;

import java.io.IOException;

public class DashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView caloriesTextView;
    private TextView proteinTextView;
    private TextView fatTextView;
    private TextView carbTextView;
    private TextView breakfastDetails;
    private TextView lunchDetails;
    private TextView dinnerDetails;
    private TextView snacksDetails;
    private Button addBreakfastButton;
    private Button addLunchButton;
    private Button addDinnerButton;
    private Button addSnacksButton;
    private User user;
    private FoodService foodService;
    private EditText foodTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.inflateHeaderView(R.layout.nav_header_main);

        caloriesTextView = findViewById(R.id.caloriesTextView);
        proteinTextView = findViewById(R.id.proteinTextView);
        fatTextView = findViewById(R.id.fatTextView);
        carbTextView = findViewById(R.id.carbTextView);
        breakfastDetails = findViewById(R.id.breakfastDetails);
        lunchDetails = findViewById(R.id.lunchDetails);
        dinnerDetails = findViewById(R.id.dinnerDetails);
        snacksDetails = findViewById(R.id.snacksDetails);
        addBreakfastButton = findViewById(R.id.addBreakfastButton);
        addLunchButton = findViewById(R.id.addLunchButton);
        addDinnerButton = findViewById(R.id.addDinnerButton);
        addSnacksButton = findViewById(R.id.addSnacksButton);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Obtener el usuario actual
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        if (navigationView != null) {
                            View headerView = navigationView.getHeaderView(0);
                            TextView navHeaderName = headerView.findViewById(R.id.nav_header_name);
                            TextView navHeaderEmail = headerView.findViewById(R.id.nav_header_email);
                            navHeaderName.setText(user.getFirstName() + " " + user.getLastName());
                            navHeaderEmail.setText(user.getEmail());
                        } else {
                            Log.e("DashboardActivity", "navigationView is null");
                        }

                        // Actualizar el saludo en el Dashboard
                        TextView dashboardGreeting = findViewById(R.id.dashboardGreeting);
                        dashboardGreeting.setText("Hola, " + user.getFirstName() + " " + user.getLastName() + ", este es el Dashboard");

                        // Calcular y mostrar las calorías necesarias
                        double calories =
                                user.calculateCalories(user.getWeight(), user.getHeight(), user.getAge(), user.getGender(), user.getActivityLevel(), user.getGoal());
                        caloriesTextView.setText(String.format("Calorías necesarias: %.2f", calories));

                        // Calcular y mostrar los macronutrientes
                        double[] macros =
                                user.calculateMacros(user.getWeight(), user.getGoal(), calories);
                        proteinTextView.setText(String.format("Proteínas: %.2f g", macros[0]));
                        fatTextView.setText(String.format("Grasas: %.2f g", macros[1]));
                        carbTextView.setText(String.format("Carbohidratos: %.2f g", macros[2]));

                        // Mostrar detalles de las comidas (ejemplo)
                        breakfastDetails.setText("Avena con frutas y nueces");
                        lunchDetails.setText("Pollo a la plancha con ensalada");
                        dinnerDetails.setText("Salmón al horno con vegetales");
                        snacksDetails.setText("Yogur griego con miel y almendras");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Manejar error
                }
            });
        }

    // Configurar los botones para mandar a una
        // nueva pantalla para ver los alimentos
        addBreakfastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String food = foodTextField.getText().toString();
                    foodService.getFoodRecipes(food);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        addLunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String food = foodTextField.getText().toString();
                    foodService.getFoodRecipes(food);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        addDinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String food = foodTextField.getText().toString();
                    foodService.getFoodRecipes(food);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        addSnacksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String food = foodTextField.getText().toString();
                    foodService.getFoodRecipes(food);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.navigation_home) {
                    Toast.makeText(DashboardActivity.this, "Home selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.navigation_dashboard) {
                    Toast.makeText(DashboardActivity.this, "Dashboard selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.navigation_notifications) {
                    Toast.makeText(DashboardActivity.this, "Notifications selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    return false;
                }
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_logout) {
                    showLogoutConfirmationDialog();
                    return true;
                }
                return false;
            }
        });
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
