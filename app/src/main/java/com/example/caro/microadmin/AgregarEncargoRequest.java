package com.example.caro.microadmin;

import android.text.format.DateFormat;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Caro on 27/06/2017.
 */

public class AgregarEncargoRequest extends StringRequest{
    private static final String URL_INGRESAR_ENCARGO= "http://microadmin.000webhostapp.com/IngresarEncargo.php";
    private Map<String, String> parametros;
    private ArrayList<HashMap<String,String>> listaProductos;
    private int IDUsuario;
    private Date fecha;
    private int IDCliente;

    public AgregarEncargoRequest(int idUsuario, ArrayList<HashMap<String,String>> listaProductos, Date fecha, int IDCliente,Response.Listener<String> listener) {
        super(Method.POST, URL_INGRESAR_ENCARGO, listener, null);
        parametros = new HashMap<>();
        this.IDUsuario=idUsuario;
        this.listaProductos = listaProductos;
        this.fecha = fecha;
        this.IDCliente = IDCliente;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError, JSONException {
        parametros.put("IDUsuario",String.valueOf(IDUsuario));
        parametros.put("IDCliente",String.valueOf(IDCliente));

        CharSequence s  = DateFormat.format("yyyy-MM-dd ", fecha);
        parametros.put("fecha", s.toString());
        if(listaProductos!=null) {

            for (int i = 0; i < listaProductos.size(); i++) {
                parametros.put("listaProductos[" + i + "][idProducto]", String.valueOf(listaProductos.get(i).get("idProducto")));
                parametros.put("listaProductos[" + i + "][cantidad]", String.valueOf(listaProductos.get(i).get("cantidad")));

            }
        }
        return parametros;
    }
}
