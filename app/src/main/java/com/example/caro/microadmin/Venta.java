package com.example.caro.microadmin;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Jean on 6/21/2017.
 */

public class Venta implements Serializable{
    private int idVenta;
    private String fecha;
    private ArrayList<lineaVenta> listaProductos;

    public Venta(){}

    public Venta(int idVenta, String fecha, ArrayList<lineaVenta> listaProductos){
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.listaProductos = listaProductos;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setListaProductos(ArrayList<lineaVenta> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public String getFecha(){
        return fecha;
    }

    public ArrayList<lineaVenta> getListaProductos(){
        return listaProductos;
    }

    public int getIdVenta(){
        return idVenta;
    }
}
