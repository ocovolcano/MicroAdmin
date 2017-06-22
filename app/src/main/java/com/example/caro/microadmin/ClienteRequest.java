package com.example.caro.microadmin;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Caro on 21/06/2017.
 */

public class ClienteRequest extends StringRequest{
    private static final String URL_REGISTRO = "http://microadmin.000webhostapp.com/ObtenerClientes.php";
    private Map<String, String> parametros;

    public ClienteRequest(String idUsuario, Response.Listener<String> listener) {
        super(Method.POST, URL_REGISTRO, listener, null);
        parametros = new HashMap<>();
        parametros.put("IDUsuario", idUsuario);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parametros;
    }
}
