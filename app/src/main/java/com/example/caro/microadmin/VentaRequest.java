package com.example.caro.microadmin;

import android.provider.Settings;
import android.text.format.DateFormat;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by Jean on 6/14/2017.
 */

public class VentaRequest extends StringRequest {
    private static final String URL_INGRESAR_VENTA= "http://microadmin.000webhostapp.com/AgregarVenta.php";
    private Map<String, String> parametros;
    private ArrayList<HashMap<String,String>> listaVenta;
    private int IDUsuario;

    public VentaRequest(Response.Listener<String> listener, int IDUsuario, ArrayList<HashMap<String,String>> listaVenta) {
        super(Method.POST, URL_INGRESAR_VENTA, listener, null);
        parametros = new HashMap<>();
        this.IDUsuario=IDUsuario;
        this.listaVenta = listaVenta;
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError, JSONException {
        Date d = new Date();
        CharSequence s  = DateFormat.format("yyyy-MM-dd ", d.getTime());

        parametros.put("fecha",s.toString());
        parametros.put("idUsuario",String.valueOf(IDUsuario));
        JSONArray jsonObjectArray = new JSONArray();
        for(int i=0;i<listaVenta.size();i++)
        {
            parametros.put("listaVentas["+i+"][idProducto]",String.valueOf(listaVenta.get(i).get("idProducto")));
            parametros.put("listaVentas["+i+"][cantidad]",String.valueOf(listaVenta.get(i).get("cantidad")));
            //JSONObject jsonObject=new JSONObject();
            //jsonObject.put("idProducto",String.valueOf(listaVenta.get(i).get("idProducto")));
            //jsonObject.put("cantidad",String.valueOf(listaVenta.get(i).get("cantidad")));
            //jsonObjectArray.put(jsonObject);
        }
        //System.out.print(jsonObjectArray.toString());
       // parametros.put("listaVentas",jsonObjectArray.toString());
        return parametros;
    }
}
