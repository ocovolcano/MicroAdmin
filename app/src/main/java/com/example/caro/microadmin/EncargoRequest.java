package com.example.caro.microadmin;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jean on 6/28/2017.
 */

public class EncargoRequest extends StringRequest {
    private static final String URL_OBTENER_ENCARGOS = "http://microadmin.000webhostapp.com/ObtenerEncargos.php";
    private Map<String, String> parametros;
    private int IDUsuario;


    public EncargoRequest(Response.Listener<String> listener, int IDUsuario){
        super(Method.POST,URL_OBTENER_ENCARGOS,listener,null);
        parametros = new HashMap<>();
        this.IDUsuario=IDUsuario;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError, JSONException {
        parametros.put("IDUsuario", String.valueOf(IDUsuario));
        return parametros;
    }
}
