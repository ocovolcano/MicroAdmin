package com.example.caro.microadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.example.caro.microadmin.R.id.bt_iniciar_sesion;

public class Login_Registro_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__registro);
        abrirVentanaInicioSesion();
    }

    private void abrirVentanaInicioSesion() {
        Button boton_iniciar_sesion = (Button) findViewById(bt_iniciar_sesion);
        boton_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }


}
