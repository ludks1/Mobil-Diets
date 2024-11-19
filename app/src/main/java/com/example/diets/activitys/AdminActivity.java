package com.example.diets.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diets.R;
import com.example.diets.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private static final String TAG = "AdminActivity";
    private TextView adminWelcomeTextView;
    private ListView userListView;
    private List<User> userList;
    private DatabaseReference mDatabase;
    private BottomNavigationView adminBottomNavigationView;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        adminWelcomeTextView = findViewById(R.id.adminWelcomeTextView);
        userListView = findViewById(R.id.userListView);
        adminBottomNavigationView = findViewById(R.id.adminBottomNavigationView);
        navigationView = findViewById(R.id.navigation_view);

        mAuth = FirebaseAuth.getInstance();

        userList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        // Cargar la lista de usuarios al iniciar
        fetchUsers();

        // Configurar navegación del menú inferior
        setupBottomNavigationView();

        // Configurar navegación del menú lateral
        setupNavigationDrawer();
    }

    private void fetchUsers() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                UserListAdapter adapter = new UserListAdapter(AdminActivity.this, userList);
                userListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminActivity.this, "Error al cargar usuarios.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error al cargar usuarios: " + error.getMessage());
            }
        });
    }

    private void setupBottomNavigationView() {
        adminBottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_admin_user_list) {
                // Acción para mostrar la lista de usuarios
                Toast.makeText(this, "Lista de usuarios cargada.", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_admin_profile) {
                // Acción para registrar un nuevo administrador
                startActivity(new Intent(AdminActivity.this, RegisterUserByAdminActivity.class));
                return true;
            } else {
                return false;
            }
        });
    }

    private void setupNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_logout) {
                mAuth.signOut();
                startActivity(new Intent(AdminActivity.this, MainActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

}
