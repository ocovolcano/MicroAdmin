package com.example.caro.microadmin;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Caro on 13/06/2017.
 */

public class EncargoFragment extends Fragment{

    private ArrayList<Cliente> listaClientes;
    private ArrayList<Producto> listaProductos;
    private ArrayList<Encargo> listaEncargos;
    private HashMap<String,ArrayList <String>> encabezado;
    private ExpandableListView expandableListView;
    private ExpansibleListViewAdapterEncargos expandableListViewAdapter;
    private EditText busqueda;


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
        listaEncargos = new ArrayList<>();
        listaProductos = new ArrayList<>();
        listaClientes = new ArrayList<>();
        expandableListViewAdapter =  new ExpansibleListViewAdapterEncargos(getContext());
        obtenerClientes();
        obtenerInventario();
        obtenerEncargado();
        fab = (FloatingActionButton) getView().findViewById(R.id.fabEncargos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AgregarEncargo.class);
                intent.putExtra("IDUsuario", getArguments().getInt("IDUsuario"));
                intent.putExtra("arrayClientes", listaClientes);
                intent.putExtra("arrayProductos", listaProductos);
                startActivityForResult(intent, 2);
            }
        });

        expandableListView = (ExpandableListView) getView().findViewById(R.id.listaEncargoselv);
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                long packedPosition = expandableListView.getExpandableListPosition(position);

                int itemType = ExpandableListView.getPackedPositionType(packedPosition);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                //int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);


        /*  if group item clicked */
                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    
                    parentLongClick(groupPosition);
                }
               
                return false;
            }
        });
        busqueda = (EditText) getView().findViewById(R.id.tf_buscarEncargos);
        busqueda.addTextChangedListener(new TextWatcher() {



            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = busqueda.getText().toString();
                expandableListViewAdapter.filterData(query);
                //expandAll();
            }
        });
    }

    private void parentLongClick(final int groupPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(("¿Está seguro que desea eliminar el encargo?")).setTitle("Eliminar Encargo");

        // Add the buttons
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                eliminarEncargo(listaEncargos.get(groupPosition));

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

    private void eliminarEncargo(Encargo encargo) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {

                    JSONObject JsonResponse = new JSONObject(response);
                    boolean eliminado = JsonResponse.getBoolean("success");

                    if (eliminado) {
                        obtenerEncargado();
                        expandableListViewAdapter.notifyDataSetChanged();
                        mostrarMensaje("Se ha eliminado correctamente");

                    } else {
                        mostrarMensaje("Ha ocurrido un error");

                    }





                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        EliminarEncargoRequest encargoRequest = new EliminarEncargoRequest(responseListener, encargo.getId());
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(encargoRequest);
    }

    private void mostrarMensaje(String mensaje){
        CharSequence text = mensaje;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getContext(), text, duration);
        toast.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == 2 ) {
            if(data.getBooleanExtra("nuevoEncargo", true)) {
                obtenerEncargado();
                expandableListViewAdapter.notifyDataSetChanged();
            }
        }
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

    public void obtenerEncargado(){
        listaEncargos = new ArrayList<>();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {

                    JSONArray jsonArray = new JSONArray(response);


                    for (int i=0; i< jsonArray.length(); i++) {
                        JSONObject jsonResponse = jsonArray.getJSONObject(i);

                        int idEncargo = jsonResponse.getInt("idEncargo");
                        String fechaActual = jsonResponse.getString("fecha");
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date newDate = format.parse(fechaActual);

                        format = new SimpleDateFormat("dd/MM/yyyy");
                        String date = format.format(newDate);

                        String nombre = jsonResponse.getString("nombreCliente")+" "+jsonResponse.getString("primerApellido")+" "+jsonResponse.getString("segundoApellido");
                        String telefono = jsonResponse.getString("telefono");
                        ArrayList<LineaEncargo> listaProductosEncargados = new ArrayList<>();
                        JSONArray productos = jsonResponse.getJSONArray("productos");
                        for (int j =  0 ; j<productos.length(); j++){
                            JSONObject  producto = productos.getJSONObject(j);
                            int cantidad = producto.getInt("cantidad");
                            String nombreProducto = producto.getString("nombreProducto");
                            LineaEncargo lineaVenta = new LineaEncargo(cantidad, nombreProducto);
                            listaProductosEncargados.add(lineaVenta);
                        }
                        Encargo producto = new Encargo(idEncargo, date, nombre,listaProductosEncargados,telefono);
                        listaEncargos.add(producto);
                        Collections.sort(listaEncargos);
                    }
                    crearArrayEncabezado();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        EncargoRequest encargoRequest = new EncargoRequest(responseListener, getArguments().getInt("IDUsuario", 0));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(encargoRequest);
    }

    private void crearArrayEncabezado(){
        encabezado = new HashMap<>();
        for(int i = 0 ; i<listaEncargos.size();i++){
            ArrayList<String> item = new ArrayList<>();

            item.add(listaEncargos.get(i).getFecha());
            item.add( (listaEncargos.get(i).getNombreCliente()));
            item.add(listaEncargos.get(i).getTelefono());
            encabezado.put(Integer.toString(i),item);
        }
        expandableListViewAdapter = new ExpansibleListViewAdapterEncargos(getContext(), listaEncargos,encabezado);

        expandableListView.setAdapter(expandableListViewAdapter);
    }
}
