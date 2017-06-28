package com.example.caro.microadmin;

import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AgregarVenta extends AppCompatActivity {
    ArrayList<Producto> listaProductos = new ArrayList<>();
    ArrayList<String> listaAutoCompletar = new ArrayList<>();
    ArrayList<HashMap<String,String>> agregar ;
    ArrayList<HashMap<String,String>> productosAgregados;
    ArrayList<String> datos ;
    ListViewAdapter adapter;
    ListView listView;
    AutoCompleteTextView auto;
    FloatingActionButton fbAgregarProducto;
    Button btnGuardar;
    EditText tvCantidad;
    TextView tvTotal;
    int cantidad;
    double total = 0 ;

    public static final String FIRST_COLUMN="First";
    public static final String SECOND_COLUMN="Second";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_venta);
        this.setTitle("Nueva Venta");
        cantidad = 0;
        listaProductos = (ArrayList<Producto>)getIntent().getSerializableExtra("arrayProductos");

        agregar = new ArrayList<>();
        datos = new ArrayList<>();
        productosAgregados = new ArrayList<>();
        CrearListaAutoCompletar();
        listView = (ListView) findViewById(R.id.lvVenta);
        //adapter = new ArrayAdapter<String> (this,android.R.layout.simple_list_item_1,datos);
        adapter = new ListViewAdapter(this,agregar);
        listView.setAdapter(adapter);

        fbAgregarProducto = (FloatingActionButton) findViewById(R.id.fabAgregarProdAVenta);
        fbAgregarProducto.hide();

        tvTotal = (TextView) findViewById(R.id.tvTotal);
        tvCantidad = (EditText) findViewById(R.id.etCantidad);
        btnGuardar = (Button) findViewById(R.id.btnGuardarVenta);
        btnGuardar.setEnabled(false);
        btnOnClick();
        btnGuardarOnClick();
        tvCantidad.addTextChangedListener(
                new TextWatcher() {


                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }


                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    public void afterTextChanged(Editable s) {
                        if(isNumeric(tvCantidad.getText().toString())) {
                            int valorCantidad = Integer.parseInt(tvCantidad.getText().toString());

                            if (valorCantidad < 0) {
                                Toast.makeText(getApplicationContext(), "Debe ingresar una cantidad mayor a 0", Toast.LENGTH_LONG).show();
                                tvCantidad.setText("");
                                fbAgregarProducto.hide();
                            } else if (valorCantidad > cantidad) {
                                Toast.makeText(getApplicationContext(), "Debe ingresar una cantidad menor a "+cantidad, Toast.LENGTH_LONG).show();
                                tvCantidad.setText("");
                                fbAgregarProducto.hide();
                            }else if(valorCantidad>0){
                                fbAgregarProducto.show();
                            }else{
                                fbAgregarProducto.hide();
                            }
                        }
                    }
                }
        );
        auto = (AutoCompleteTextView) findViewById(R.id.acProducto);
        auto.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String producto = auto.getText().toString();
                for (Producto prod:listaProductos
                        ) {
                    if(prod.getNombre().equals(producto)){
                        cantidad = prod.getCantidad();
                        Toast.makeText(getApplicationContext(),"Cantidad disponible "+cantidad,Toast.LENGTH_LONG).show();
                        fbAgregarProducto.hide();
                        tvCantidad.setText("");
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,listaAutoCompletar);
        auto.setAdapter(adapter2);
    }

    public void CrearListaAutoCompletar(){
        listaAutoCompletar = new ArrayList<String>();
        for (Producto producto:listaProductos) {
            listaAutoCompletar.add(producto.getNombre());
        }

    }

    private static boolean isNumeric(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }

    public void btnOnClick(){
        fbAgregarProducto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                HashMap<String,String> hashMap = new HashMap<String, String>();
                hashMap.put(FIRST_COLUMN,auto.getText().toString());
                hashMap.put(SECOND_COLUMN,tvCantidad.getText().toString());
                String producto = auto.getText().toString();

                for (Producto prod:listaProductos
                        ) {
                    if(prod.getNombre().equals(producto)){
                        btnGuardar.setEnabled(true);
                        cantidad = prod.getCantidad()- Integer.parseInt(tvCantidad.getText().toString());
                        prod.setCantidad(cantidad);
                        hashMap.put("idProducto",String.valueOf(prod.getIDProducto()));
                        hashMap.put("cantidad",tvCantidad.getText().toString());
                        productosAgregados.add(hashMap);
                        total +=(Integer.parseInt(tvCantidad.getText().toString())*prod.getPrecioUnidad());
                    }
                }
                auto.setText("");
                tvCantidad.setText("");
                tvTotal.setText(String.valueOf(total));
                fbAgregarProducto.hide();
                try {
                    agregar.add(hashMap);
                }catch (Error e){
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void btnGuardarOnClick(){
        btnGuardar.setOnClickListener(new View.OnClickListener(

        ) {
            @Override
            public void onClick(View v) {
                btnGuardar.setEnabled(false);
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject JsonResponse = new JSONObject(response);
                            boolean agregado = JsonResponse.getBoolean("success");

                            if (agregado) {
                                mostrarMensaje("Se ha guardado correctamente");

                            } else {
                                mostrarMensaje("Ha ocurrido un error");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                VentaRequest ventaRequest = new VentaRequest(responseListener, getIntent().getIntExtra("IDUsuario", 0), productosAgregados);
                RequestQueue requestQueue = Volley.newRequestQueue(AgregarVenta.this);
                requestQueue.add(ventaRequest);
            }
        });
    }

    private void mostrarMensaje(String mensaje){
        CharSequence text = mensaje;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }
}
