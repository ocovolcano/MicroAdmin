package com.example.caro.microadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.example.caro.microadmin.R.id.bt_iniciar_sesion;
import static com.example.caro.microadmin.R.id.bt_registro;

public class Login_Registro_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__registro);
        abrirVentanaInicioSesion();
        abrirVentanaRegistro();
        }

    private void abrirVentanaRegistro() {
        Button boton_registro = (Button)findViewById(bt_registro);
        boton_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistroActivity.class));
            }
        });
    }

    private void abrirVentanaInicioSesion() {
        Button boton_iniciar_sesion = (Button) findViewById(bt_iniciar_sesion);
        boton_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }


}
