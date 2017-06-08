package com.example.caro.microadmin;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Caro on 23/05/2017.
 */

public class InventarioRequest extends StringRequest{
    private static final String URL_OBTENER_PRODUCTOS = "http://microadmin.000webhostapp.com/ObtenerProductos.php";
    private Map<String, String> parametros;

    public InventarioRequest( Response.Listener<String> listener, int IDUsuario) {
        super(Method.POST, URL_OBTENER_PRODUCTOS, listener, null);
        parametros = new HashMap<>();
        parametros.put("idUsuario", String.valueOf(IDUsuario));

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parametros;
    }


}
