package com.example.arbolizapp.ui.generar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.arbolizapp.R;
import com.example.arbolizapp.interfaces.IComunicaFragment;

import java.util.zip.Inflater;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link GenerarSolicitudFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenerarSolicitudFragment extends Fragment {
    /*// TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GenerarSolicitudFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GenerarSolicitudFragment.

    // TODO: Rename and change types and number of parameters
    public static GenerarSolicitudFragment newInstance(String param1, String param2) {
        GenerarSolicitudFragment fragment = new GenerarSolicitudFragment();
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
    }*/
    private GenerarViewModel generarViewModel;
    Button seleccionarUbicacion;

    IComunicaFragment interfaceComunicaFragments;
    Activity actividad;
    View root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        generarViewModel = ViewModelProviders.of(this).get(GenerarViewModel.class);
        root = inflater.inflate(R.layout.fragment_generar_solicitud, container, false);

        seleccionarUbicacion = root.findViewById(R.id.seleccionar);

        seleccionarUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceComunicaFragments.seleccionarUbicacion();
            }
        });
        //final TextView textView = root.findViewById(R.id.text_generar);
        generarViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof Activity){
            actividad = (Activity) context;
            interfaceComunicaFragments= (IComunicaFragment) actividad;
        }
    }

}
