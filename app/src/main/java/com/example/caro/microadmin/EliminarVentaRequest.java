package com.example.caro.microadmin;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Caro on 04/07/2017.
 */

public class EliminarVentaRequest extends StringRequest{

    private static final String URL_ELIMINAR_VENTA = "https://microadmin.000webhostapp.com/EliminarVenta.php";
    private Map<String, String> parametros;
    private int IDVenta;


    public EliminarVentaRequest(Response.Listener<String> listener, int IDVenta){
        super(Method.POST,URL_ELIMINAR_VENTA,listener,null);
        parametros = new HashMap<>();
        this.IDVenta=IDVenta;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError, JSONException {
        parametros.put("idVenta", String.valueOf(IDVenta));
        return parametros;
    }
}

