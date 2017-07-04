package com.example.caro.microadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Jean on 6/17/2017.
 */

class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Venta> parentDataSource;
    private ArrayList<Venta> hijosOriginal;

    public ExpandableListViewAdapter(Context context, ArrayList<Venta> childParent) {
        this.context = context;
        this.parentDataSource =childParent;
        this.hijosOriginal = childParent;
    }

    public ExpandableListViewAdapter(Context context){
        this.context = context;
    }

    public void ActulizarLista(ArrayList<Venta> listaVentas){
        parentDataSource = listaVentas;
        hijosOriginal = listaVentas;
    }

    @Override
    public int getGroupCount() {
        return this.parentDataSource.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return this.parentDataSource.get(groupPosition).getListaProductos().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.parentDataSource.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.parentDataSource.get(groupPosition).getListaProductos().get(childPosition);

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
            view = inflater.inflate(R.layout.parent_layout, parent, false);
        }

        Venta parentHeader = (Venta) getGroup(groupPosition);

        TextView parentItem1 = (TextView)view.findViewById(R.id.nombreClienteEncargotv);
        TextView parentItem2 = (TextView)view.findViewById(R.id.telefonoEncargotv);
        parentItem1.setText("Numero Venta :"+parentHeader.getIdVenta());
        parentItem2.setText("Fecha :"+parentHeader.getFecha());
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           view = inflater.inflate(R.layout.child_layout, parent, false);
        }

        lineaVenta childName = (lineaVenta) getChild(groupPosition, childPosition);

        TextView producto = (TextView)view.findViewById(R.id.productotv);
        TextView precio = (TextView)view.findViewById(R.id.precioProductotv);

        producto.setText(childName.getNombre());
        if(childPosition  != getChildrenCount(groupPosition)-1) {
            precio.setText("Monto: " + Double.toString(childName.getMonto()));
        }else{
            precio.setText( Double.toString(childName.getMonto()));
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void filterData(String query){
        query = query.toLowerCase();
        if(query.isEmpty()){
            parentDataSource.clear();
            parentDataSource.addAll(hijosOriginal);
        }else{
            HashMap<String,ArrayList<String>> encabezadoNuevo = new HashMap();
            ArrayList<Venta> nuevosHijos = new ArrayList<>();
            for (int i = 0;i<hijosOriginal.size();i++){

                Venta venta =hijosOriginal.get(i);
                String fecha = venta.getFecha();

                if(String.valueOf( venta.getIdVenta()).toLowerCase().contains(query) ||
                        fecha.toLowerCase().contains(query)){
                        nuevosHijos.add(hijosOriginal.get(i));
                }


            }
            if (nuevosHijos.size()>0 && parentDataSource.size() !=nuevosHijos.size() ){
                parentDataSource = nuevosHijos;
            }
        }
        try {
            notifyDataSetChanged();
        }catch (Exception e){

        }

    }

}
