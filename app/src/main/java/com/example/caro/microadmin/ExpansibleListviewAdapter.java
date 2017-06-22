package com.example.caro.microadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jean on 6/17/2017.
 */

class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Venta> parentDataSource;
    private HashMap<String,ArrayList <String>> encabezado;

    public ExpandableListViewAdapter(Context context, ArrayList<Venta> childParent, HashMap<String,ArrayList <String>> encabezado) {
        this.context = context;
        this.encabezado = encabezado;
        this.parentDataSource =childParent;
    }

    @Override
    public int getGroupCount() {
        return this.encabezado.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return this.parentDataSource.get(groupPosition).getListaProductos().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return encabezado.get(Integer.toString(groupPosition));
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

        ArrayList<String> parentHeader = (ArrayList<String>) getGroup(groupPosition);

        TextView parentItem1 = (TextView)view.findViewById(R.id.numeroVentatv);
        TextView parentItem2 = (TextView)view.findViewById(R.id.fechaVentatv);
        parentItem1.setText("Numero Venta :"+parentHeader.get(1));
        parentItem2.setText("Fecha :"+parentHeader.get(0));
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
        precio.setText(Double.toString(childName.getMonto()));

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
