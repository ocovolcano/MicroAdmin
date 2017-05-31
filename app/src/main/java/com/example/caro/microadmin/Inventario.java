package com.example.caro.microadmin;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by Caro on 28/05/2017.
 */

public class Inventario extends android.support.v4.app.Fragment{

    private FloatingActionButton fab;
    private ListView listaInventario;
    private EditText busqueda;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.inventario_fragment, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Inventario");
        fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProductoActivity.class));
            }
        });

        listaInventario = (ListView) getView().findViewById(R.id.lista_inventario);
        final String[] lista = {"item 1", "item 2", "item 3"};
        ArrayList<String> llistarr = new ArrayList<>(Arrays.asList(lista));
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, llistarr);

        listaInventario.setAdapter(adapter);

        busqueda = (EditText) getView().findViewById(R.id.tf_buscar_inventario);
        busqueda.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable arg0) {


            }
    });
}
}