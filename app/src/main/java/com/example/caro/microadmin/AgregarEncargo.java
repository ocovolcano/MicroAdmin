package com.example.caro.microadmin;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AgregarEncargo extends AppCompatActivity{

    private EditText tfFecha;
    private ArrayList<Cliente> listaClientes;
    private ArrayList<String> listaAutoCompletarClientes;
    private AutoCompleteTextView autoCompletarClientes;
    private FloatingActionButton fabAgregarProducto;
    private ArrayList<Producto> listaProductos;
    private ArrayList<String> listaAutoCompletarProductos;
    private AutoCompleteTextView autoCompletarProductos;
    private EditText tfCantidad;
    private int cantidad = 0;
    private Button btnGuardar;
    public static final String FIRST_COLUMN="First";
    public static final String SECOND_COLUMN="Second";
    private ArrayList<HashMap<String,String>> listaHashProductos;
    private ArrayList<HashMap<String,String>> productosAgregados;
    private ListView listView;
    private ListViewAdapter adapter;
    private int idCliente = -1;
    private  HashMap<Integer,Integer> hashPosicionesCliente;
    private Calendar myCalendar;
    private Intent intent;
    private ImageButton cerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_encargo);
        intent = new Intent();
        fabAgregarProducto = (FloatingActionButton) findViewById(R.id.fab_agregarProductoEncargo);
        fabAgregarProducto.hide();
        listaClientes = (ArrayList<Cliente>)getIntent().getSerializableExtra("arrayClientes");
        listaProductos = (ArrayList<Producto>)getIntent().getSerializableExtra("arrayProductos");
        productosAgregados = new ArrayList<>();
        listaHashProductos = new ArrayList<>();
        hashPosicionesCliente = new HashMap<>();

        crearListaAutoCompletarClientes();
        crearListaAutoCompletarProductos();

        listView = (ListView) findViewById(R.id.listView_ProductosEncargo);

        adapter = new ListViewAdapter(this,listaHashProductos);
        listView.setAdapter(adapter);

        btnGuardar = (Button) findViewById(R.id.bt_guardarEncargo);
        btnGuardar.setEnabled(false);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                guardarEncargo();
            }
        });

        fabAgregarProductoOnClick();
        System.out.println(System.currentTimeMillis() - 1000);
        myCalendar = Calendar.getInstance();
        tfFecha = (EditText) findViewById(R.id.tf_fechaEncargo);
        tfFecha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog picker = new DatePickerDialog(AgregarEncargo.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                picker.show();
            }
        });


        autoCompletarClientes = (AutoCompleteTextView) findViewById(R.id.autoComp_NombreCliente);
        final ArrayAdapter<String> adapterAutoCompletarClientes = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,listaAutoCompletarClientes);
        autoCompletarClientes.setAdapter(adapterAutoCompletarClientes);
        autoCompletarClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                idCliente = hashPosicionesCliente.get(listaAutoCompletarClientes.indexOf(parent.getItemAtPosition(position)));

            }
        });



        autoCompletarProductos = (AutoCompleteTextView) findViewById(R.id.autoComp_Producto);
        ArrayAdapter<String> adapterAutoCompletarProductos = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,listaAutoCompletarProductos);
        autoCompletarProductos.setAdapter(adapterAutoCompletarProductos);
        autoCompletarProductos.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String producto = autoCompletarProductos.getText().toString();
                for (Producto prod: listaProductos) {
                    if(prod.getNombre().equals(producto)){
                        cantidad = prod.getCantidad();
                        Toast.makeText(getApplicationContext(), "Cantidad disponible " + cantidad, Toast.LENGTH_LONG).show();
                        fabAgregarProducto.hide();
                        tfCantidad.setText("");
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });


        tfCantidad = (EditText) findViewById(R.id.tf_cantidadEncargo);
        tfCantidad.addTextChangedListener(
                new TextWatcher() {


                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }


                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    public void afterTextChanged(Editable s) {
                        if(isNumeric(tfCantidad.getText().toString())) {
                            int valorCantidad = Integer.parseInt(tfCantidad.getText().toString());

                            if (valorCantidad < 0) {
                                Toast.makeText(getApplicationContext(), "Debe ingresar una cantidad mayor a 0", Toast.LENGTH_LONG).show();
                                tfCantidad.setText("");
                                fabAgregarProducto.hide();
                            } else if (valorCantidad > cantidad) {
                                Toast.makeText(getApplicationContext(), "Debe ingresar una cantidad menor a "+cantidad, Toast.LENGTH_LONG).show();
                                tfCantidad.setText("");
                                fabAgregarProducto.hide();
                            }else if(valorCantidad>0){
                                fabAgregarProducto.show();
                            }else{
                                fabAgregarProducto.hide();
                            }
                        }
                    }
                }
        );

        cerrar = (ImageButton)findViewById(R.id.bt_cerrar_encargo);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }


    private void guardarEncargo() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject JsonResponse = new JSONObject(response);
                    boolean agregado = JsonResponse.getBoolean("success");

                    if (agregado) {
                        intent.putExtra("nuevoEncargo", true);
                        autoCompletarClientes.setText("");
                        tfFecha.setText("");
                        listaHashProductos.clear();
                        productosAgregados.clear();
                        adapter.notifyDataSetChanged();
                        mostrarMensaje("Se ha guardado correctamente");

                    } else {
                        mostrarMensaje("Ha ocurrido un error");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
            //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            if(validacionesCorrectas()) {


                AgregarEncargoRequest encargoRequest = new AgregarEncargoRequest(getIntent().getIntExtra("IDUsuario", 0), productosAgregados, myCalendar.getTime(), idCliente, responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(AgregarEncargo.this);
                requestQueue.add(encargoRequest);

            }
    }

    private boolean validacionesCorrectas() {
        AutoCompleteTextView cliente = (AutoCompleteTextView) findViewById(R.id.autoComp_NombreCliente);
        EditText fecha = (EditText) findViewById(R.id.tf_fechaEncargo);
        AutoCompleteTextView prod = (AutoCompleteTextView) findViewById(R.id.autoComp_Producto);
        EditText cantidad = (EditText) findViewById(R.id.tf_cantidadEncargo);
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(cliente.getText().toString().trim()) ){
            cliente.setError("Este campo es requerido");
            focusView = cliente;
            cancel = true;
        }else{
            if (idCliente == -1){
                cliente.setError("Este cliente no existe, debe ingresarlo primero");
                focusView = cliente;
                cancel = true;
            }
        }


        if(TextUtils.isEmpty(fecha.getText().toString().trim())) {
            fecha.setError("Este campo es requerido");
            focusView = fecha;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if(focusView != null){
                focusView.requestFocus();
            }
            return false;
        }
            return true;

    }

    private void mostrarMensaje(String mensaje){
        CharSequence text = mensaje;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }



    private void fabAgregarProductoOnClick() {

        fabAgregarProducto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                HashMap<String,String> hashMap = new HashMap<String, String>();
                hashMap.put(FIRST_COLUMN,autoCompletarProductos.getText().toString());
                hashMap.put(SECOND_COLUMN,tfCantidad.getText().toString());
                String producto = autoCompletarProductos.getText().toString();

                for (Producto prod:listaProductos
                        ) {
                    if(prod.getNombre().equals(producto)){
                        btnGuardar.setEnabled(true);
                        cantidad = prod.getCantidad()- Integer.parseInt(tfCantidad.getText().toString());
                        prod.setCantidad(cantidad);
                        hashMap.put("idProducto",String.valueOf(prod.getIDProducto()));
                        hashMap.put("cantidad",tfCantidad.getText().toString());
                        productosAgregados.add(hashMap);
                    }
                }
                autoCompletarProductos.setText("");
                tfCantidad.setText("");
                fabAgregarProducto.hide();
                try {
                    listaHashProductos.add(hashMap);
                }catch (Error e){
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private static boolean isNumeric(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }


    public void crearListaAutoCompletarClientes(){
        listaAutoCompletarClientes = new ArrayList<String>();
        int i = 0;
        for (Cliente cliente:listaClientes) {
            listaAutoCompletarClientes.add(cliente.getNombre() + " " + cliente.getPrimerApellido() + " " + cliente.getSegundoApellido());
            hashPosicionesCliente.put(i, cliente.getIDCliente());
            i++;
        }

    }

    public void crearListaAutoCompletarProductos(){
        listaAutoCompletarProductos = new ArrayList<String>();
        for (Producto producto:listaProductos) {
            listaAutoCompletarProductos.add(producto.getNombre());
        }
    }



    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            System.out.println("DIA " + dayOfMonth);

            updateLabel(myCalendar.getTime());
        }

    };


    private void updateLabel(Date fecha) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        tfFecha.setText(DateFormat.format(myFormat, fecha));
    }




}
