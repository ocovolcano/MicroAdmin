package com.example.caro.microadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InventarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);
        final TextView tvNombre= (TextView)findViewById(R.id.tv_nombre);
        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nombre");
        tvNombre.setText(nombre);
    }
}
