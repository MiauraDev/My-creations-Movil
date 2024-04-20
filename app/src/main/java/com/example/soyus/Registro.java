package com.example.soyus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {
    //DECLARAR LAS VARIABLES
    EditText correoEt, passEt, nombreEt;
    TextView fechaTxt;
    Button Registrar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //CONEXIÓN CON LA VISTA
        correoEt = findViewById(R.id.correoEt);
        passEt = findViewById(R.id.passEt);
        nombreEt = findViewById(R.id.nombreEt);
        fechaTxt = findViewById(R.id.fechaTxt);
        Registrar = findViewById(R.id.Registrar);

        auth = FirebaseAuth.getInstance();

        Date date = new Date();
        SimpleDateFormat fecha= new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
        String Stringfecha = fecha.format(date);
        fechaTxt.setText(Stringfecha);

        Registrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String email = correoEt.getText().toString();
                String password = passEt.getText().toString();

                /*VALIDACIÓN PARA CORREO*/
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    correoEt.setError("Ingrese un correo válido");
                    correoEt.setFocusable(true);
                /*VALIDACIÓN PARA CONTRASEÑA*/
                } else if (password.length()<7) {
                    passEt.setError("Debe ser mayor a 7 digitos");
                    correoEt.setFocusable(true);
                }else {
                    RegistrarUsuario(email,password);
                }
            }


        });

    }
    /*METODO PARA REGISTRAR USUARIO*/
    private void RegistrarUsuario(String email, String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        /*SI EL USUARIO FUE REGISTRADO CORRECTAMENTE*/
                        if (task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();

                            int contador= 0;

                            assert user != null;
                            String uidString = user.getUid();
                            String correoString = correoEt.getText().toString();
                            String passString = passEt.getText().toString();
                            String nombreString = nombreEt.getText().toString();
                            String fechaString = fechaTxt.getText().toString();

                            HashMap<Object,Object>DatosUsuario = new HashMap<>();

                            DatosUsuario.put("Uid",uidString);
                            DatosUsuario.put("Email",correoString);
                            DatosUsuario.put("Contraseña",passString);
                            DatosUsuario.put("Nombre",nombreString);
                            DatosUsuario.put("Fecha",fechaString);
                            DatosUsuario.put("Estrellas",contador);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();//INSTANCIA
                            DatabaseReference reference = database.getReference("DATA BASE USUARIOS");/*NOMBRE BBDD*/
                            reference.child(uidString).setValue(DatosUsuario);
                            startActivity(new Intent(Registro.this,MenuActivity.class));
                            Toast.makeText(Registro.this,"USUARIO REGISTRADO EXITOSAMENTE", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(Registro.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
            /*SI FALLA EL REGISTRO*/
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registro.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });



    }
}