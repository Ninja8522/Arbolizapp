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

/**
 * Este es el código correspondiente a un fragment del vivero, en este caso es al de la sección de árboles por entregar
 */

public class PorEntregar extends Fragment {


    public PorEntregar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_por_entregar2,container,false);

        String[] porEntre = {"Nombre:" + "",
                "Folio:" + "",
                "Arbol:" + "",
                "Fecha" + ""};
        ListView listView=(ListView) view.findViewById(R.id.porentregar);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                porEntre
        );



        listView.setAdapter(listViewAdapter);
        // Inflate the layout for this fragment
        return view;
    }

}
