package com.example.diets.activitys;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.diets.R;
import com.example.diets.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserListAdapter extends android.widget.ArrayAdapter<User> {

    public UserListAdapter(@NonNull Context context, @NonNull List<User> objects) {
        super(context, R.layout.item_user, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }

        User user = getItem(position);
        if (user != null) {
            TextView userNameTextView = convertView.findViewById(R.id.userNameTextView);
            Button userDetailsButton = convertView.findViewById(R.id.userDetailsButton);

            userNameTextView.setText(user.getFirstName() + " " + user.getLastName());

            userDetailsButton.setOnClickListener(v -> {
                // Obtener el ID del usuario desde Firebase
                FirebaseDatabase.getInstance().getReference("users")
                        .orderByChild("email")
                        .equalTo(user.getEmail()) // Filtra por correo electrónico o cualquier campo único del usuario
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String userId = snapshot.getKey(); // Obtiene el ID del nodo
                                        if (userId != null && !userId.isEmpty()) {
                                            Intent intent = new Intent(getContext(), UserDetailsActivity.class);
                                            intent.putExtra("userId", userId);
                                            getContext().startActivity(intent);
                                            return;
                                        }
                                    }
                                }
                                Toast.makeText(getContext(), "Usuario no encontrado en la base de datos", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getContext(), "Error al obtener el ID del usuario: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            });
        }
        return convertView;
    }
}
