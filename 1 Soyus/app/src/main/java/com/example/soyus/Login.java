package com.example.soyus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    //DECLARAMOS VARIABLES
    EditText correoLogin, passLogin;
    Button BtnLogin;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //CONEXIÓN CON LA VISTA
        correoLogin = findViewById(R.id.correoLogin);
        passLogin = findViewById(R.id.passLogin);
        BtnLogin = findViewById(R.id.BtnLogin);
        auth = FirebaseAuth.getInstance();


        //EVENTO CLICK BOTON LOGIN
        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = correoLogin.getText().toString();
                String pass = passLogin.getText().toString();
                /*VALIDACIÓN PARA CORREO*/
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    correoLogin.setError("Ingrese un correo válido");
                    correoLogin.setFocusable(true);
                    /*VALIDACIÓN PARA CONTRASEÑA*/
                } else if (pass.length()<7) {
                    Toast.makeText(Login.this, "Ingrese una contraseña valida", Toast.LENGTH_SHORT).show();
                    correoLogin.setFocusable(true);
                }else {
                   LogeoDeUsuario(email,pass);
                }
            }
        });

    }

    /*METODO PARA LOGEO DE USUARIO*/
    private void LogeoDeUsuario(String email, String pass) {
        auth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            startActivity(new Intent(Login.this,MenuActivity.class));
                            assert user != null;
                            Toast.makeText(Login.this,"BIENVENIDO(A) "+ user.getEmail(),Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    //FALLO LOGEO
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }
}