package com.example.caro.microadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AgregarCliente extends AppCompatActivity {
    private EditText Nombre;
    private EditText PrimerApellido;
    private EditText SegundoApellido;
    private EditText Cedula;
    private EditText Telefono;
    private Button Guardar;
    private Intent intent;
    private ImageButton cerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cliente);
        intent = new Intent();
        Nombre = (EditText) findViewById(R.id.tf_nombre_cliente);
        PrimerApellido = (EditText) findViewById(R.id.tf_apellido_1);
        SegundoApellido = (EditText) findViewById(R.id.tf_apellido_2);
        Cedula = (EditText) findViewById(R.id.tf_cedula);
        Telefono = (EditText) findViewById(R.id.tf_telefono);
        Guardar = (Button) findViewById(R.id.bt_guardar_cliente);
        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cancel = false;
                View focusView = null;

                if (TextUtils.isEmpty(Nombre.getText().toString().trim()) ){
                    Nombre.setError("Este campo es requerido");
                    focusView = Nombre;
                    cancel = true;
                } if(TextUtils.isEmpty(PrimerApellido.getText().toString().trim())) {
                    PrimerApellido.setError("Este campo es requerido");
                    focusView = PrimerApellido;
                    cancel = true;
                } if(TextUtils.isEmpty(SegundoApellido.getText().toString().trim())) {
                    SegundoApellido.setError("Este campo es requerido");
                    focusView = SegundoApellido;
                    cancel = true;
                } if(TextUtils.isEmpty(Cedula.getText().toString().trim())) {
                    Cedula.setError("Este campo es requerido");
                    focusView = Cedula;
                    cancel = true;
                } if(TextUtils.isEmpty(Telefono.getText().toString().trim())) {
                    Telefono.setError("Este campo es requerido");
                    focusView = Telefono;
                    cancel = true;


                }else {
                    guardarCliente();
                }
            }
        });

        cerrar = (ImageButton) findViewById(R.id.bt_cerrar_cliente);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void guardarCliente(){
        final ProgressDialog loading = ProgressDialog.show(this,"Guardando...","Por favor espere...",false,false);


        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject JsonResponse = new JSONObject(response);
                    boolean registrado = JsonResponse.getBoolean("success");

                    if (registrado) {
                        loading.dismiss();
                        intent.putExtra("nuevaVenta",true);
                        mostrarMensaje("Se ha registrado correctamente");
                        Nombre.setText("");
                        PrimerApellido.setText("");
                        SegundoApellido.setText("");
                        Cedula.setText("");
                        Telefono.setText("");

                    } else {
                        mostrarMensaje("Ha ocurrido un error");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        AgregarClienteRequest registroRequest = new AgregarClienteRequest(String.valueOf(getIntent().getIntExtra("IDUsuario", 0)), Nombre.getText().toString().trim(), PrimerApellido.getText().toString().trim(),SegundoApellido.getText().toString().trim(),Cedula.getText().toString().trim(),Telefono.getText().toString().trim(), responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(AgregarCliente.this);
        requestQueue.add(registroRequest);
    }


    private void mostrarMensaje(String mensaje){
        CharSequence text = mensaje;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }
}
