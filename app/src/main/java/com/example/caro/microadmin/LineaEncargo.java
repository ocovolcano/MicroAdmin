package com.example.caro.microadmin;

import java.io.Serializable;

/**
 * Created by Jean on 6/28/2017.
 */

public class LineaEncargo implements Serializable {
    private String nombre;
    private int cantidad;

    public LineaEncargo(int cantidad, String nombre){
        this.cantidad= cantidad;
        this.nombre=nombre;
    }

    public LineaEncargo(){}

    public int getCantidad(){
        return cantidad;
    }
    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public void setCantidad(int cantidad){
        this.cantidad = cantidad;
    }
}
