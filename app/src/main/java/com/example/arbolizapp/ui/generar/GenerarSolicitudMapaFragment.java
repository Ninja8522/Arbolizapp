package com.example.arbolizapp.ui.generar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arbolizapp.R;
/**
 * Este es el codigo de seleccionar la ubicacion de la solucitud, manda llamar su propio diseño.
 */
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenerarSolicitudMapaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenerarSolicitudMapaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GenerarSolicitudMapaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GenerarSolicitudMapaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GenerarSolicitudMapaFragment newInstance(String param1, String param2) {
        GenerarSolicitudMapaFragment fragment = new GenerarSolicitudMapaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_generar_solicitud_mapa, container, false);
    }
}
