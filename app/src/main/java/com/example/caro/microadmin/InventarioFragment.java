package com.example.caro.microadmin;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

/**
 * Created by  on 28/05/2017.
 */

public class InventarioFragment extends Fragment {

    private FloatingActionButton fab;
    //private ListView listaInventario;
    private RecyclerView recycler;
    private RecyclerView.LayoutManager layoutManager;
    private  RecyclerView.Adapter adapter;
    private EditText busqueda;
    private ArrayList<Producto> listaProductos;
    //private InventarioAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ObtenerInventario();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.inventario_fragment, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Inventario");

        fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProductoActivity.class);
                intent.putExtra("IDUsuario", getArguments().getInt("IDUsuario"));
                startActivity(intent);
            }
        });

        recycler = (RecyclerView) getView().findViewById(R.id.recycler_view_inventario);
        layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        adapter = new InventarioAdapter(getContext(), listaProductos);
        recycler.setAdapter(adapter);



        //adapter = new InventarioAdapter(getContext(), listaProductos);
        //recycler.setAdapter(adapter);

        //listaInventario = (ListView) getView().findViewById(R.id.lista_inventario);
        //final String[] lista = {"item 1", "item 2", "item 3"};
        //ArrayList<String> llistarr = new ArrayList<>(Arrays.asList(lista));
        //final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, llistarr);

        //listaInventario.setAdapter(adapter);

        busqueda = (EditText) getView().findViewById(R.id.tf_buscar_inventario);
        busqueda.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {


            }
        });
    }

    private void ObtenerInventario() {
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