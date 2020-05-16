package com.example.arbolizapp.Controlador;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.arbolizapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Entregado extends Fragment {


    public Entregado() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_entregado,container,false);

        String[] entregado = {"Folio" + "",
                                "Arbol:" + "",
                                "Precio" + ""};
        ListView listView=(ListView) view.findViewById(R.id.entregado);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                entregado
        );



        listView.setAdapter(listViewAdapter);
        // Inflate the layout for this fragment
        return view;
    }

}
