package com.example.caro.microadmin;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jean on 6/28/2017.
 */

public class Encargo implements Serializable, Comparable<Encargo> {
    private int id;
    private String fecha;
    private String nombreCliente;
    private String telefono;
    private ArrayList<LineaEncargo> listaProductosEncargados;

    public Encargo(){}

    public Encargo(int id,String fecha,String nombreCliente, ArrayList<LineaEncargo> listaProductosEncargados, String telefono){
        this.fecha =fecha;
        this.id= id;
        this.nombreCliente = nombreCliente;
        this.listaProductosEncargados = listaProductosEncargados;
        this.telefono = telefono;
    }

    public void setTelefono(String telefono){
        this.telefono = telefono;
    }

    public String getTelefono(){
        return this.telefono;
    }
    public void setFecha(String fecha){
        this.fecha=fecha;
    }

    public void setNombreCliente(String nombreCliente){
        this.nombreCliente = nombreCliente;
    }

    public void setListaProductosEncargados(ArrayList<LineaEncargo> listaProductosEncargados){
        this.listaProductosEncargados = listaProductosEncargados;
    }

    public int getId(){
        return this.id;
    }

    public String getFecha(){
        return this.fecha;
    }

    public String getNombreCliente(){
        return this.nombreCliente;
    }

    public ArrayList<LineaEncargo> getListaProductosEncargados() {
        return listaProductosEncargados;
    }

    @Override
    public int compareTo(Encargo o) {
        return getFecha().compareTo(o.getFecha());
    }
}
