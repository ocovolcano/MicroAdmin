package com.example.caro.microadmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.Hashtable;
import java.util.Map;

public class MostrarProducto extends AppCompatActivity {
    private Producto producto;
    public ImageView imagenProducto;
    public TextView codigoProducto;
    public TextView nombreProducto;
    public TextView precioUnitario;
    public TextView costoManufactura;
    public TextView cantidad;
    private Button eliminarProducto;
    private String DELETE_URL = "http://microadmin.000webhostapp.com/EliminarProducto.php";
    private ImageButton cerrar;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_producto);
        intent = new Intent();
        producto = (Producto) getIntent().getSerializableExtra("Producto");
        imagenProducto = (ImageView) this.findViewById(R.id.imagenProducto);
        nombreProducto = (TextView) this.findViewById(R.id.tvNombreProducto);
        precioUnitario = (TextView) this.findViewById(R.id.tvPrecioUnitario);
        cantidad = (TextView) this.findViewById(R.id.tvCantidad);
        codigoProducto = (TextView) this.findViewById(R.id.tvCodigo);
        costoManufactura = (TextView) this.findViewById(R.id.tvCostoManufactura);
        nombreProducto.setText(producto.getNombre());
        precioUnitario.setText(String.valueOf(producto.getPrecioUnidad()));
        cantidad.setText(String.valueOf(producto.getCantidad()));
        codigoProducto.setText(String.valueOf(producto.getCodigo()));
        costoManufactura.setText(String.valueOf(producto.getCostoManufactura()));
        Picasso.with(this).load(producto.getURL()).into(imagenProducto);
        eliminarProducto = (Button) this.findViewById(R.id.botonEliminar);
        eliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarAlerta();
            }
        });

        cerrar = (ImageButton) findViewById(R.id.bt_cerrar_mostrar);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void eliminarProducto(final int idProducto) {

        StringRequest request = new StringRequest(Request.Method.POST, DELETE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(MostrarProducto.this, "Se ha eliminado correctamente", Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        //Showing toast
                        Toast.makeText(MostrarProducto.this, "Ha ocurrido un error", Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String, String> params = new Hashtable<>();

                //Adding parameters

                params.put("idProducto", String.valueOf(idProducto));
                params.put("URL", producto.getURL());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MostrarProducto.this);
        requestQueue.add(request);
    }


    public void mostrarAlerta(){
        final Intent intent = new Intent(this, MainInventarioActivity.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(("¿Está seguro que desea eliminar el producto?")).setTitle("Eliminar Producto");

        // Add the buttons
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                eliminarProducto(producto.getIDProducto());
                InventarioFragment.listaProductos.remove(getIntent().getIntExtra("posicion",0));
                InventarioFragment.adapter.notifyItemRemoved(getIntent().getIntExtra("posicion",0));
                InventarioFragment.adapter.notifyDataSetChanged();

                finish();



            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });


// Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
