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
 * Created by Caro on 21/06/2017.
 */

public class ClienteFragment extends Fragment {

    private FloatingActionButton fab;
    private ArrayList<Cliente> listaClientes;
    private ListView listViewClientes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.cliente_fragment, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Clientes");
        fab = (FloatingActionButton) getView().findViewById(R.id.fabCliente);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AgregarCliente.class);
                intent.putExtra("IDUsuario", getArguments().getInt("IDUsuario"));
                startActivity(intent);
            }
        });
        obtenerClientes();

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

                    listViewClientes = (ListView) getView().findViewById(R.id.lv_clientes);
                    ArrayList<String> listaNombres = new ArrayList<>();

                    for (Cliente cliente : listaClientes){
                        listaNombres.add(cliente.getNombre() + " " + cliente.getPrimerApellido() + " " + cliente.getSegundoApellido());
                    }

                    final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listaNombres);


                    listViewClientes.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ClienteRequest clienteRequest = new ClienteRequest(String.valueOf(getArguments().getInt("IDUsuario")), responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(clienteRequest);
    }

}
