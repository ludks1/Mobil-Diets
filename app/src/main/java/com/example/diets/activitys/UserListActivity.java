package com.example.diets.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diets.R;
import com.example.diets.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private static final String TAG = "UserListActivity";
    private ListView userListView;
    private List<User> userList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        // Inicializar vistas y base de datos
        userListView = findViewById(R.id.userListView);
        userList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        fetchUsers();

        userListView.setOnItemClickListener((parent, view, position, id) -> {
            User selectedUser = userList.get(position);

            // Validar que el usuario tiene un ID
            if (selectedUser != null && selectedUser.getId() != null && !selectedUser.getId().isEmpty()) {
                Intent intent = new Intent(UserListActivity.this, UserDetailsActivity.class);
                intent.putExtra("userId", selectedUser.getId());
                Log.d(TAG, "Seleccionado User ID: " + selectedUser.getId());
                startActivity(intent);
            } else {
                // Mostrar mensaje de error si el usuario no tiene un ID válido
                Log.e(TAG, "El usuario seleccionado no tiene un ID válido.");
                Toast.makeText(UserListActivity.this, "Error: El usuario seleccionado no tiene un ID válido.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUsers() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);

                    // Validar que el usuario tiene un ID antes de agregarlo a la lista
                    if (user != null && user.getId() != null && !user.getId().isEmpty()) {
                        userList.add(user);
                    } else {
                        Log.w(TAG, "Se encontró un usuario sin ID válido en la base de datos.");
                    }
                }

                // Configurar el adaptador para mostrar la lista de usuarios
                UserListAdapter adapter = new UserListAdapter(UserListActivity.this, userList);
                userListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserListActivity.this, "Error al cargar usuarios", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error al cargar usuarios: " + error.getMessage());
            }
        });
    }
}
