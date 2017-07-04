package com.example.caro.microadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Jean on 6/28/2017.
 */

public class ExpansibleListViewAdapterEncargos extends BaseExpandableListAdapter {
    private ArrayList<Encargo> listaEncargos;
    private ArrayList<Encargo> listaEncargosFiltrada;
    private Context context;

    public ExpansibleListViewAdapterEncargos(Context context, ArrayList<Encargo> listaEncargo, HashMap<String,ArrayList<String>> encabezado){
        this.context = context;
        this.listaEncargos = listaEncargo;
        this.listaEncargosFiltrada = listaEncargo;
    }
    @Override
    public int getGroupCount() {
        return this.listaEncargosFiltrada.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listaEncargosFiltrada.get(groupPosition).getListaProductosEncargados().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return  listaEncargosFiltrada.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listaEncargosFiltrada.get(groupPosition).getListaProductosEncargados().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.parent_layout2, parent, false);
        }

       Encargo parentHeader = (Encargo) getGroup(groupPosition);


        TextView parentItem1 = (TextView)view.findViewById(R.id.nombreClienteEncargotv);
        TextView parentItem2 = (TextView)view.findViewById(R.id.telefonoEncargotv);
        TextView parentItem3 = (TextView)view.findViewById(R.id.fechaEncargotv);
        parentItem1.setText("Cliente :"+parentHeader.getNombreCliente());
        parentItem3.setText("Fecha :"+ parentHeader.getFecha());
        parentItem2.setText("Teléfono :"+parentHeader.getTelefono());
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.child_layout, parent, false);
        }

        LineaEncargo childName = (LineaEncargo) getChild(groupPosition, childPosition);

        TextView producto = (TextView)view.findViewById(R.id.productotv);
        TextView precio = (TextView)view.findViewById(R.id.precioProductotv);

        producto.setText(childName.getNombre());
        precio.setText("Cantidad: "+Integer.toString(childName.getCantidad()));

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void filterData(String query){
        query = query.toLowerCase();
        if(query.isEmpty()){
            listaEncargosFiltrada.clear();
            listaEncargosFiltrada.addAll(listaEncargos);
        }else{
            ArrayList<Encargo> nuevosHijos = new ArrayList<>();
            for (int i = 0;i<listaEncargos.size();i++){

                Encargo encargo =listaEncargos.get(i);
                String fecha = encargo.getFecha();
                String Nombre = encargo.getNombreCliente();

                if(Nombre.toLowerCase().contains(query) ||
                        fecha.toLowerCase().contains(query)){
                    nuevosHijos.add(listaEncargos.get(i));
                }


            }
            if (nuevosHijos.size()>0 && listaEncargosFiltrada.size() !=nuevosHijos.size() ){
                listaEncargosFiltrada = nuevosHijos;
            }
        }
        try {
            notifyDataSetChanged();
        }catch (Exception e){

        }

    }
}
