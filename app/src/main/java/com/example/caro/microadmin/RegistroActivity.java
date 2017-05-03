package com.example.caro.microadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistroActivity extends AppCompatActivity {

    //UI Referencias
    private TextView linkIniciarSesion;
    private Button btRegistro;
    private EditText tfNombre;
    private EditText tfCorreo;
    private EditText tfContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        linkIniciarSesion =(TextView) findViewById(R.id.link_iniciarSesion);
        btRegistro = (Button)findViewById(R.id.bt_registrarme);
        tfNombre = (EditText)findViewById(R.id.tf_nombre);
        tfCorreo = (EditText)findViewById(R.id.tf_correo);
        tfContrasena = (EditText)findViewById(R.id.tf_contrasena);
        enviarAInicioDeSesion();
        registrar();

    }

    private void registrar() {


        btRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nombre = tfNombre.getText().toString();
                final String correo = tfCorreo.getText().toString();
                final String contrasena = tfContrasena.getText().toString();
                boolean cancelar = false;
                View focusView = null;

                // Check for a valid password, if the user entered one.
                if (TextUtils.isEmpty(contrasena)){
                    tfContrasena.setError(getString(R.string.error_field_required));
                    focusView = tfContrasena;
                    cancelar = true;
                } else if(!isPasswordValid(contrasena)) {
                    tfContrasena.setError(getString(R.string.error_invalid_password));
                    focusView = tfContrasena;
                    cancelar = true;
                }

                // Check for a valid email address.
                if (TextUtils.isEmpty(correo)) {
                    tfCorreo.setError(getString(R.string.error_field_required));
                    focusView = tfCorreo;
                    cancelar = true;
                } else if (!isEmailValid(correo)) {
                    tfCorreo.setError(getString(R.string.error_invalid_email));
                    focusView = tfCorreo;
                    cancelar = true;
                }

                //Validar nombre
                if (TextUtils.isEmpty(nombre)) {
                    tfNombre.setError(getString(R.string.error_field_required));
                    focusView = tfNombre;
                    cancelar = true;
                }

                if(cancelar) {
                    focusView.requestFocus();

                }else{
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject JsonResponse = new JSONObject(response);
                            boolean registrado = JsonResponse.getBoolean("success");

                            if (registrado) {
                                mostrarMensaje("Se ha registrado correctamente");
                                RegistroActivity.this.startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            } else {
                                mostrarMensaje("El correo que intenta registrar, ya existe");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegistroRequest registroRequest = new RegistroRequest(nombre, correo, contrasena, responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(RegistroActivity.this);
                requestQueue.add(registroRequest);
            }//else
            }
            });

    }


    private void mostrarMensaje(String mensaje){
        CharSequence text = mensaje;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }

    private void enviarAInicioDeSesion() {
        linkIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


}
