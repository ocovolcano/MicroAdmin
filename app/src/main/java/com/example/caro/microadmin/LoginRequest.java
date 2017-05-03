package com.example.caro.microadmin;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Caro on 24/04/2017.
 */

public class LoginRequest extends StringRequest{

    private static final String URL_INICIAR_SESION = "http://microadmin.000webhostapp.com/IniciarSesion.php";
    private Map<String, String> parametros;

    public LoginRequest(String correo, String contrasena, Response.Listener<String> listener) {
        super(Method.POST, URL_INICIAR_SESION, listener, null);
        parametros = new HashMap<>();
        parametros.put("correo", correo);
        parametros.put("contrasena", contrasena);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parametros;
    }
}
