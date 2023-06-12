package fia.ues.saluddigital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fia.ues.saluddigital.Usuario.RegistroUsuario;

import fia.ues.saluddigital.GestionPeso.GestionPesoMenu;

public class MainActivity extends AppCompatActivity {

    Button btn_reg_us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_reg_us =findViewById(R.id.btn_registro);
        btn_reg_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regUx = new Intent(MainActivity.this, RegistroUsuario.class);
                startActivity(regUx);
            }
        });

        try {
            Intent intent = new Intent(MainActivity.this, GestionPesoMenu.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();

    }
}

}