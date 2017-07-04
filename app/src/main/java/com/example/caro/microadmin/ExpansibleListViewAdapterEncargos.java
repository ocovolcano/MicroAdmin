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
    private Context context;
    private HashMap<String,ArrayList <String>> encabezado;

    public ExpansibleListViewAdapterEncargos(Context context, ArrayList<Encargo> listaEncargo, HashMap<String,ArrayList<String>> encabezado){
        this.context = context;
        this.listaEncargos = listaEncargo;
        this.encabezado = encabezado;
    }
    @Override
    public int getGroupCount() {
        return this.encabezado.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listaEncargos.get(groupPosition).getListaProductosEncargados().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return  encabezado.get(Integer.toString(groupPosition));
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listaEncargos.get(groupPosition).getListaProductosEncargados().get(childPosition);
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

        ArrayList<String> parentHeader = (ArrayList<String>) getGroup(groupPosition);


        TextView parentItem1 = (TextView)view.findViewById(R.id.nombreClienteEncargotv);
        TextView parentItem2 = (TextView)view.findViewById(R.id.telefonoEncargotv);
        TextView parentItem3 = (TextView)view.findViewById(R.id.fechaEncargotv);
        parentItem1.setText("Cliente :"+parentHeader.get(1));
        parentItem3.setText("Fecha :"+ parentHeader.get(0));
        parentItem2.setText("Teléfono :"+parentHeader.get(2));
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
}
