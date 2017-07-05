package com.example.caro.microadmin;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Toast;

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

public class Ventas extends Fragment implements SearchView.OnCloseListener, SearchView.OnQueryTextListener{
    private FloatingActionButton fab;

    private ArrayList<Producto> listaProductos = new ArrayList<>();
    private ArrayList<Venta>listaVentas;
    private ExpandableListView expandableListView;

    private EditText busqueda;

    private ExpandableListViewAdapter expandableListViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaProductos = new ArrayList<>();
        listaVentas = new ArrayList<>();
        expandableListViewAdapter = new ExpandableListViewAdapter(getContext());
        ObtenerInventario();
        listaVentas = (ArrayList<Venta>) getArguments().getSerializable("listaVentas");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
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
                startActivityForResult(intent, 0);
            }
        });
        expandableListView = (ExpandableListView) getView().findViewById(R.id.listaVentaselv);
        busqueda = (EditText) getView().findViewById(R.id.tf_buscarVenta);
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
    }

    private void parentLongClick(final int groupPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(("¿Está seguro que desea eliminar la venta?")).setTitle("Eliminar Venta");

        // Add the buttons
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                eliminarVenta(listaVentas.get(groupPosition));

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

    private void eliminarVenta(Venta venta) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {

                    JSONObject JsonResponse = new JSONObject(response);
                    boolean eliminado = JsonResponse.getBoolean("success");

                    if (eliminado) {
                        obtenerVentas();
                        mostrarMensaje("Se ha eliminado correctamente");

                    } else {
                        mostrarMensaje("Ha ocurrido un error");

                    }





                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        EliminarVentaRequest ventaRequest = new EliminarVentaRequest(responseListener, venta.getIdVenta());
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(ventaRequest);
    }

    private void mostrarMensaje(String mensaje){
        CharSequence text = mensaje;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getContext(), text, duration);
        toast.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == 0 ) {
            if(data.getBooleanExtra("nuevaVenta", true)) {
                obtenerVentas();
                expandableListViewAdapter.notifyDataSetChanged();
            }
        }
    }

    public void ObtenerInventario() {
        listaProductos = new ArrayList<Producto>();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    String respuesta = response.toString();
                    if( !respuesta.equals("0 results")) {
                        JSONArray jsonArray = new JSONArray(response);


                        for (int i = 0; i < jsonArray.length(); i++) {
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
    private void expandAll(){
        int count= expandableListViewAdapter.getGroupCount();
        for (int i = 0; i< count; i++){
            expandableListView.expandGroup(i);
        }
    }

    private void obtenerVentas(){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    String respuesta = response.toString();
                    
                    listaVentas = new ArrayList<>();
                    if( !respuesta.equals("0 results")) {
                        JSONArray jsonArray = new JSONArray(response);


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonResponse = jsonArray.getJSONObject(i);

                            int idVenta = jsonResponse.getInt("idVenta");
                            String fechaActual = jsonResponse.getString("fecha");
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            java.util.Date newDate = format.parse(fechaActual);

                            format = new SimpleDateFormat("dd/MM/yyyy");
                            String date = format.format(newDate);
                            String idUsuario = jsonResponse.getString("idUsuario");
                            ArrayList<lineaVenta> productosVenta = new ArrayList<>();
                            JSONArray productos = jsonResponse.getJSONArray("productos");
                            double montoTotal = 0;
                            for (int j = 0; j < productos.length(); j++) {
                                JSONObject producto = productos.getJSONObject(j);
                                int idProducto = producto.getInt("idProducto");
                                int cantidad = producto.getInt("cantidad");
                                String nombre = producto.getString("nombre");
                                double precioUnidad = producto.getDouble("precioUnidad");
                                montoTotal += (cantidad * precioUnidad);
                                lineaVenta lineaVenta = new lineaVenta(nombre, cantidad * precioUnidad);
                                productosVenta.add(lineaVenta);
                            }
                            lineaVenta total = new lineaVenta("Total", montoTotal);
                            productosVenta.add(total);
                            Venta venta = new Venta(idVenta, date, productosVenta);
                            listaVentas.add(venta);
                        }
                    }
                    expandableListViewAdapter = new ExpandableListViewAdapter(getContext(), listaVentas);
                    expandableListView.setAdapter(expandableListViewAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        VentaRequest ventaRequest = new VentaRequest(responseListener, getArguments().getInt("IDUsuario", 0));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(ventaRequest);
    }



    @Override
    public boolean onClose() {
        expandableListViewAdapter.filterData("");
        expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        expandableListViewAdapter.filterData(query);
        expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        expandableListViewAdapter.filterData(newText);
        expandAll();
        return false;
    }
}
