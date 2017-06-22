package com.example.caro.microadmin;

import java.io.Serializable;

/**
 * Created by Jean on 6/21/2017.
 */

public class lineaVenta implements Serializable{
    private String nombre;
    private double monto;

    public lineaVenta(String nombre, double monto){
        this.nombre = nombre;
        this.monto = monto;
    }
    public lineaVenta(){

    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public String getNombre(){
        return nombre;
    }
    public void SetMonto(double monto){
        this.monto = monto;
    }
    public double getMonto(){
        return monto;
    }
}
