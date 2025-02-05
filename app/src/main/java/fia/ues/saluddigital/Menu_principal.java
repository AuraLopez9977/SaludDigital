package fia.ues.saluddigital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fia.ues.saluddigital.RutinaEjercicios.EjercicioVideo;
import fia.ues.saluddigital.Usuario.RegistroUsuario;

public class Menu_principal extends AppCompatActivity {
    Button btn_registrarUsuario,btn_ejercicios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        btn_registrarUsuario = findViewById(R.id.btn_regUs);
        btn_registrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrarUs = new Intent(Menu_principal.this, RegistroUsuario.class);
                startActivity(registrarUs);
            }
        });
        
        btn_ejercicios = findViewById(R.id.btn_ejercicio);
        btn_ejercicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent video = new Intent(Menu_principal.this, EjercicioVideo.class);
                startActivity(video);
            }
        });
    }
}