package com.example.caro.microadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Caro on 13/06/2017.
 */

public class EncargoFragment extends Fragment{

    private ArrayList<Cliente> listaClientes;
    private ArrayList<Producto> listaProductos;

    private FloatingActionButton fab;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.encargos_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Encargos");
        obtenerClientes();
        obtenerInventario();

        fab = (FloatingActionButton) getView().findViewById(R.id.fabEncargos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AgregarEncargo.class);
                intent.putExtra("IDUsuario", getArguments().getInt("IDUsuario"));
                intent.putExtra("arrayClientes",listaClientes);
                intent.putExtra("arrayProductos",listaProductos);
                startActivity(intent);
            }
        });
    }

    private void obtenerClientes() {
        listaClientes = new ArrayList<Cliente>();
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {

                    JSONArray jsonArray = new JSONArray(response);


                    for (int i=0; i< jsonArray.length(); i++) {
                        JSONObject jsonResponse = jsonArray.getJSONObject(i);

                        int IDCliente = jsonResponse.getInt("idCliente");
                        String nombre = jsonResponse.getString("nombre");
                        String primerApellido = jsonResponse.getString("primerApellido");
                        String segundoApellido = jsonResponse.getString("segundoApellido");
                        String cedula = jsonResponse.getString("cedula");
                        String telefono = jsonResponse.getString("telefono");
                        Cliente cliente = new Cliente(IDCliente, nombre, primerApellido, segundoApellido, cedula, telefono);
                        listaClientes.add(cliente);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ClienteRequest clienteRequest = new ClienteRequest(String.valueOf(getArguments().getInt("IDUsuario")), responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(clienteRequest);
    }

    public void obtenerInventario() {
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
}
