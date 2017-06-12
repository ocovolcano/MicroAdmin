package com.example.caro.microadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MostrarProducto extends AppCompatActivity {
    private Producto producto;
    public ImageView imagenProducto;
    public TextView codigoProducto;
    public TextView nombreProducto;
    public TextView precioUnitario;
    public TextView costoManufactura;
    public TextView cantidad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_producto);
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

    }
}
