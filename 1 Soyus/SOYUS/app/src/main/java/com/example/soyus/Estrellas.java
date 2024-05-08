package com.example.soyus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Estrellas extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    LinearLayout Volver;
    TextView estrellasTxt;

    String UIDS, NOMBRES, ESTRELLAS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_estrellas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        estrellasTxt = findViewById(R.id.Estrellas);

        // Obtener la referencia del usuario actual
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Obtener el ID del usuario actual
            String uid = currentUser.getUid();
            // Obtener la referencia del usuario en la base de datos
            DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference("DATA BASE USUARIOS").child(uid);
            // Agregar un ValueEventListener para obtener el número de estrellas del usuario
            usuarioRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Obtener el valor actual de Estrellas
                    Integer estrellasActuales = dataSnapshot.child("Estrellas").getValue(Integer.class);
                    if (estrellasActuales != null) {
                        // Establecer el número de estrellas en el TextView
                        estrellasTxt.setText(String.valueOf(estrellasActuales));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Manejar el error si la lectura de la base de datos falla
                    Toast.makeText(Estrellas.this, "Error al obtener el número de estrellas", Toast.LENGTH_SHORT).show();
                }
            });
        }

        ImageView imageView = findViewById(R.id.imageView);
        // Cargar el GIF usando Glide
        Glide.with(this)
                .load(R.drawable.estrella)
                .into(imageView);

        LinearLayout VolverLayout = findViewById(R.id.Volver);
        VolverLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Estrellas.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
