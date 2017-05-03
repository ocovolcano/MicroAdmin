package com.example.caro.microadmin;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Caro on 26/04/2017.
 */

public class RegistroRequest extends StringRequest{

    private static final String URL_REGISTRO = "http://microadmin.000webhostapp.com/Registro.php";
    private Map<String, String> parametros;

    public RegistroRequest(String nombre, String correo, String contrasena, Response.Listener<String> listener) {
        super(Method.POST, URL_REGISTRO, listener, null);
        parametros = new HashMap<>();
        parametros.put("nombre", nombre);
        parametros.put("correo", correo);
        parametros.put("contrasena", contrasena);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parametros;
    }
}
