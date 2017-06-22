package com.example.caro.microadmin;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Caro on 21/06/2017.
 */

public class AgregarClienteRequest extends StringRequest{

    private static final String URL_REGISTRO = "https://microadmin.000webhostapp.com/IngresarCliente.php";
    private Map<String, String> parametros;

    public AgregarClienteRequest(String idUsuario, String nombre, String apellido1, String apellido2, String cedula, String telefono, Response.Listener<String> listener) {
        super(Method.POST, URL_REGISTRO, listener, null);
        parametros = new HashMap<>();
        parametros.put("IDUsuario", idUsuario);
        parametros.put("nombre", nombre);
        parametros.put("primerApellido", apellido1);
        parametros.put("segundoApellido", apellido2);
        parametros.put("cedula", cedula);
        parametros.put("telefono", telefono);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parametros;
    }

}
