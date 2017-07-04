package com.example.caro.microadmin;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Caro on 03/07/2017.
 */

public class EliminarEncargoRequest extends StringRequest {

    private static final String URL_ELIMINAR_ENCARGO = "https://microadmin.000webhostapp.com/EliminarEncargo.php";
    private Map<String, String> parametros;
    private int IDEncargo;


    public EliminarEncargoRequest(Response.Listener<String> listener, int IDEncargo){
        super(Method.POST,URL_ELIMINAR_ENCARGO,listener,null);
        parametros = new HashMap<>();
        this.IDEncargo=IDEncargo;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError, JSONException {
        parametros.put("idEncargo", String.valueOf(IDEncargo));
        return parametros;
    }
}
