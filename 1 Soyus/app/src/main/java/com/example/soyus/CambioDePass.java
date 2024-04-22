package com.example.soyus;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CambioDePass extends AppCompatActivity {

    EditText ActualPass, NuevoPass;
    Button CambiarPass;
    DatabaseReference BASEDEDATOS;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cambio_de_pass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActualPass = findViewById(R.id.ActualPass);
        NuevoPass = findViewById(R.id.NuevoPass);
        CambiarPass = findViewById(R.id.CambiarPass);

        ImageView imageView = findViewById(R.id.imagegif);
        Glide.with(this)
                .load(R.drawable.mas)
                .into(imageView);

        BASEDEDATOS = FirebaseDatabase.getInstance().getReference("DATA BASE USUARIOS");
        firebaseAuth = FirebaseAuth.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();

        CambiarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ACTUAL = ActualPass.getText().toString().trim();
                String NUEVA = NuevoPass.getText().toString().trim();

                if (TextUtils.isEmpty(ACTUAL)){
                    Toast.makeText(CambioDePass.this, "Llenar campo actual contraseña", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(NUEVA)){
                    Toast.makeText(CambioDePass.this, "Llenar campo nueva contraseña", Toast.LENGTH_SHORT).show();
                }
                if (!TextUtils.isEmpty(ACTUAL)&& !TextUtils.isEmpty(NUEVA)&& ACTUAL.length()>= 7 && NUEVA.length()>= 7) {
                    CambioDePassUsuario(ACTUAL, NUEVA);
                    }
                }
        });
    }

    private void CambioDePassUsuario(String actual, String nueva) {

        AuthCredential authCredential = EmailAuthProvider.getCredential((user.getEmail()),actual);

        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updatePassword(nueva)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        String value = NuevoPass.getText().toString().trim();
                                        HashMap<String, Object> result = new HashMap<>();
                                        result.put("Password", value);
                                        BASEDEDATOS.child(user.getUid()).updateChildren(result)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(CambioDePass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        firebaseAuth.signOut();
                                        startActivity(new Intent(CambioDePass.this,Login.class));
                                        Toast.makeText(CambioDePass.this,"Cambio de contraseña exitoso", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(CambioDePass.this,"Vuelva a Iniciar Sesión", Toast.LENGTH_SHORT).show();

                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CambioDePass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CambioDePass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}