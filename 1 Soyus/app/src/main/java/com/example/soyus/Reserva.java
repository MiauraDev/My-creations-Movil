package com.example.soyus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Reserva extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    LinearLayout Volver;

    String UIDS, NOMBRES, ESTRELLAS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reserva);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener una referencia al EditText y al botón de reserva
        EditText passPhone = findViewById(R.id.passPhone);
        Button ReservarBtn = findViewById(R.id.ReservarBtn);


        ReservarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el número de teléfono ingresado por el usuario
                String telefono = passPhone.getText().toString().trim();

                // Verificar si el número de teléfono tiene exactamente 9 dígitos
                if (telefono.length() == 9) {
                    // Limpiar la entrada del número de teléfono
                    passPhone.setText("");

                    // Obtener la referencia del usuario actual
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        // Sumar 5 puntos a Estrellas en la base de datos Firebase
                        sumarPuntosEstrellas(currentUser.getUid(), 5);
                    }
                } else {
                    passPhone.setError("Debe ser un número de celular válido (9 dígitos)");
                    passPhone.requestFocus();
                }
            }
        });


        ImageView imageView = findViewById(R.id.imageView);
        // Cargar el GIF usando Glide
        Glide.with(this)
                .load(R.drawable.estrella)
                .into(imageView);

        LinearLayout VolverLayout = findViewById(R.id.Volver);
        VolverLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reserva.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sumarPuntosEstrellas(String uid, int puntos) {
        DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference("DATA BASE USUARIOS").child(uid);
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Obtener el valor actual de Estrellas
                Integer estrellasActuales = dataSnapshot.child("Estrellas").getValue(Integer.class);
                if (estrellasActuales != null) {
                    // Sumar los puntos a Estrellas
                    int nuevasEstrellas = estrellasActuales + puntos;
                    // Actualizar el valor de Estrellas en la base de datos
                    usuarioRef.child("Estrellas").setValue(nuevasEstrellas);
                    Toast.makeText(Reserva.this, "Ganaste 5 Estrellas", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Reserva.this, "Enviado con éxito", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Reserva.this, "Error al sumar puntos a Estrellas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
