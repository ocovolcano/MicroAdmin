package com.example.caro.microadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AgregarVenta extends AppCompatActivity {
    private ArrayList<Producto> listaProductos = new ArrayList<>();
    private ArrayList<String> listaAutoCompletar = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_venta);
        listaProductos = (ArrayList<Producto>)getIntent().getSerializableExtra("arrayProductos");
        CrearListaAutoCompletar();
        AutoCompleteTextView auto = (AutoCompleteTextView) findViewById(R.id.acProducto);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,listaAutoCompletar);
        auto.setAdapter(adapter);
    }

    public void CrearListaAutoCompletar(){
        listaAutoCompletar = new ArrayList<String>();
        for (Producto producto:listaProductos) {
            listaAutoCompletar.add(producto.getNombre());
        }

    }
}
