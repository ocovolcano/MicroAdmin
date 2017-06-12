package com.example.caro.microadmin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.StringLoader;
import com.squareup.picasso.Picasso;

import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Caro on 31/05/2017.
 */

public class InventarioAdapter extends RecyclerView.Adapter<InventarioAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Producto> listaProductos;

    public InventarioAdapter(Context context, ArrayList<Producto> listaProductos) {
        this.context = context;
        this.listaProductos = listaProductos;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String nombre = listaProductos.get(position).getNombre();
        String precioUnitario = String.valueOf(listaProductos.get(position).getPrecioUnidad());
        String cantidad =  String.valueOf(listaProductos.get(position).getCantidad());
        holder.nombreProducto.setText(nombre);
        holder.idProducto = listaProductos.get(position).getIDProducto();
        holder.cantidad.setText(cantidad);
        holder.precioUnitario.setText(precioUnitario);
        Picasso.with(context).load(listaProductos.get(position).getURL()).into(holder.imagenProducto);


    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imagenProducto;
        public TextView nombreProducto;
        public TextView precioUnitario;
        public TextView cantidad;
        public int idProducto;

        public ViewHolder(View itemView) {
            super(itemView);
            imagenProducto = (ImageView) itemView.findViewById(R.id.img_producto);
            nombreProducto = (TextView) itemView.findViewById(R.id.tv_nombre_producto);
            precioUnitario = (TextView) itemView.findViewById(R.id.tv_precio_unit_inventario);
            cantidad = (TextView) itemView.findViewById(R.id.tv_cantidad);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posicion = getAdapterPosition();
                }
            });
        }


        @Override
        public void onClick(View v) {

        }
    }


    }

