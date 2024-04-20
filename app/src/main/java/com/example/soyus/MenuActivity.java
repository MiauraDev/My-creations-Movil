package com.example.soyus;

import static com.denzcoskun.imageslider.ImageSlider.*;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.LinearLayout;

import android.view.Menu;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference USUARIOS;

    TextView MiEstrellasTxt, Estrellas, uid, correo, nombre;
    Button ReservaBtn, EstrellasBtn, AcerdaDeBtn, CambiarBtn;
    Dialog dialog;
    LinearLayout CerrarSesion; // Cambio de Button a LinearLayout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ImageSlider imageSlider = findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.img1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img4, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img5, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);


        ImageView imageView = findViewById(R.id.imagegif);
        Glide.with(this)
                .load(R.drawable.cargif)
                .into(imageView);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        USUARIOS = firebaseDatabase.getReference("DATA BASE USUARIOS");

        dialog = new Dialog(MenuActivity.this);

        MiEstrellasTxt = findViewById(R.id.Estrellas);
        nombre = findViewById(R.id.nombre);

        ReservaBtn = findViewById(R.id.ReservaBtn);
        EstrellasBtn = findViewById(R.id.EstrellasBtn);
        AcerdaDeBtn = findViewById(R.id.AcerdaDeBtn);
        CambiarBtn = findViewById(R.id.CambiarBtn);
        CerrarSesion = findViewById(R.id.CerrarSesion); // Asignación del LinearLayout

        /*RESERVA*/
        ReservaBtn.setOnClickListener((view) -> {
            // Crear la intención
            Intent intent = new Intent(MenuActivity.this, Reserva.class);

            // Iniciar la actividad
            startActivity(intent);
        });

;

        EstrellasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, Estrellas.class);
                startActivity(intent);
            }
        });

        AcerdaDeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcercaDe();
                //Toast.makeText(MenuActivity.this, "ACERCA DE", Toast.LENGTH_SHORT).show();

            }
        });

        CambiarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this,CambioDePass.class));
                //Toast.makeText(MenuActivity.this, "CAMBIAR", Toast.LENGTH_SHORT).show();
            }
        });

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CerrarSesion();
            }
        });

    }

    private void AcercaDe() {
        Button OK;

        dialog.setContentView(R.layout.acerca_de);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        OK = dialog.findViewById(R.id.OK);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void openLinkedInProfile(View v) {
        String linkedInUrl = "https://www.linkedin.com/in/laura-luque-582706257/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkedInUrl));
        startActivity(intent);
    }


    protected void onStart(){
        UsuarioLogueado();
        super.onStart();
    }

    //METODO COMPRUEBA SI EL USUARIO INICIO SESIÓN ANTES
    private void UsuarioLogueado(){
        if (user != null){
            Consulta();
            Toast.makeText(this, "Usuario en línea", Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(new Intent(MenuActivity.this, MainActivity.class));
            finish();
        }
    }


    //METODO CERRAR SESIÓN
    private void CerrarSesion(){
        auth.signOut();
        startActivity(new Intent(MenuActivity.this, MainActivity.class));
        Toast.makeText(this, "Cerró la sesión de su cuenta", Toast.LENGTH_SHORT).show();
    }

    private  void Consulta(){
        Query query = USUARIOS.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String estreString = ""+ds.child("Estrellas").getValue();
                    String nombreString = ""+ds.child("Nombre").getValue();

                    // Aquí también debes corregir los nombres
                    MiEstrellasTxt.setText(estreString);
                    nombre.setText(nombreString);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
