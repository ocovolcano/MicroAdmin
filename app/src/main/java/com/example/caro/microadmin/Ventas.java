package com.example.caro.microadmin;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Caro on 28/05/2017.
 */

public class Ventas extends Fragment{
    private FloatingActionButton fab;

    private ArrayList<Producto> listaProductos = new ArrayList<>();
    private ArrayList<Venta>listaVentas;
    private ExpandableListView expandableListView;
    private HashMap<String,ArrayList <String>> encabezado;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ObtenerInventario();
        listaVentas = (ArrayList<Venta>) getArguments().getSerializable("listaVentas");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        HashMap<String, ArrayList<String>> allChildItems = new HashMap<>();
        ArrayList<String> venta= new ArrayList<>();
        venta.add("Jabon");
        venta.add("Shampoo");
        venta.add("Arroz");
        allChildItems.put("Venta",venta);



        return inflater.inflate(R.layout.ventas_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        obtenerVentas();

        getActivity().setTitle("Ventas");
        fab = (FloatingActionButton) getView().findViewById(R.id.fabVentas);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AgregarVenta.class);
                intent.putExtra("IDUsuario", getArguments().getInt("IDUsuario"));
                intent.putExtra("arrayProductos",listaProductos);
                startActivity(intent);
            }
        });
        expandableListView = (ExpandableListView) getView().findViewById(R.id.listaVentaselv);

    }
    public void ObtenerInventario() {
        listaProductos = new ArrayList<Producto>();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {

                    JSONArray jsonArray = new JSONArray(response);


                    for (int i=0; i< jsonArray.length(); i++) {
                        JSONObject jsonResponse = jsonArray.getJSONObject(i);

                        int IDProducto = jsonResponse.getInt("idProducto");
                        String codigo = jsonResponse.getString("codigo");
                        String nombre = jsonResponse.getString("nombre");
                        String URL = jsonResponse.getString("urlImagen");
                        double precioUnidad = jsonResponse.getDouble("precioUnidad");
                        double costoManufactura = jsonResponse.getDouble("costoManufactura");
                        int cantidad = jsonResponse.getInt("cantidad");
                        Producto producto = new Producto(IDProducto, codigo, nombre, URL, precioUnidad, costoManufactura, cantidad);
                        listaProductos.add(producto);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        InventarioRequest inventarioRequest = new InventarioRequest(responseListener,  getArguments().getInt("IDUsuario"));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(inventarioRequest);
    }

    private void obtenerVentas(){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    listaVentas = new ArrayList<>();
                    for (int i=0; i< jsonArray.length(); i++) {
                        JSONObject jsonResponse = jsonArray.getJSONObject(i);

                        int idVenta = jsonResponse.getInt("idVenta");
                        String fecha = jsonResponse.getString("fecha");
                        String idUsuario = jsonResponse.getString("idUsuario");
                        ArrayList<lineaVenta> productosVenta = new ArrayList<>();
                        JSONArray productos = jsonResponse.getJSONArray("productos");
                        double montoTotal = 0;
                        for (int j =  0 ; j<productos.length(); j++){
                            JSONObject  producto = productos.getJSONObject(j);
                            int idProducto = producto.getInt("idProducto");
                            int cantidad = producto.getInt("cantidad");
                            String nombre = producto.getString("nombre");
                            double precioUnidad = producto.getDouble("precioUnidad");
                            montoTotal+=(cantidad*precioUnidad);
                            lineaVenta lineaVenta = new lineaVenta(nombre,cantidad*precioUnidad);
                            productosVenta.add(lineaVenta);
                        }
                        lineaVenta total = new lineaVenta("Total", montoTotal);
                        productosVenta.add(total);
                        Venta venta = new Venta(idVenta,fecha,productosVenta);
                        listaVentas.add(venta);
                    }
                    crearArrayEncabezado();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        VentaRequest ventaRequest = new VentaRequest(responseListener, getArguments().getInt("IDUsuario", 0));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(ventaRequest);
    }


    private void crearArrayEncabezado(){
        encabezado = new HashMap<>();
        for(int i = 0 ; i<listaVentas.size();i++){
            ArrayList<String> item = new ArrayList<>();
            item.add(listaVentas.get(i).getFecha());
            item.add( Integer.toString((listaVentas.get(i).getIdVenta())));
            encabezado.put(Integer.toString(i),item);
        }
        ExpandableListViewAdapter expandableListViewAdapter = new ExpandableListViewAdapter(getContext(), listaVentas,encabezado);

        expandableListView.setAdapter(expandableListViewAdapter);
    }

}
