package com.example.soyus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Duración de la animación en milisegundos
        int DURATION_SPLASH = 3000;

        // Iniciar la animación de la imagen
        ImageView imageView = findViewById(R.id.imageView);
        Animation animation = createAnimation();
        imageView.startAnimation(animation);

        // PostDelayed para iniciar la actividad principal después de la duración del splash
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, MenuActivity.class);
                startActivity(intent);
                finish(); // Finalizar la actividad actual
            }
        }, DURATION_SPLASH);

    }

    // Método para crear la animación de la imagen
    private Animation createAnimation() {
        // Crear la animación para ampliar la imagen
        Animation scaleUpAnimation = new ScaleAnimation(
                1.0f,
                1.2f,
                1.0f,
                1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleUpAnimation.setDuration(1500); // Duración de la animación

        // Crear la animación para volver a tamaño original
        Animation scaleDownAnimation = new ScaleAnimation(
                1.2f,
                1.0f,
                1.2f,
                1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleDownAnimation.setDuration(2000); // Duración de la animación
        scaleDownAnimation.setStartOffset(1500); // Iniciar después de 1500

        // Crear un conjunto de animaciones para ejecutarlas secuencialmente
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(scaleUpAnimation);
        animationSet.addAnimation(scaleDownAnimation);

        return animationSet;
    }
}
