package com.example.arbolizapp.Controlador;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.arbolizapp.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */

/**
 * Este es el código correspondiente a un fragment del vivero, en este caso es al de la sección de perfil la cual es
 * la primera en cargar al momento de hacer login como usuario de tipo vivero.
 */

public class Perfil extends Fragment {
    ImageView fotoVivero;
    /**
     * La URL de abajo corresponde a la imgen que teno por defecto del vivero.
     */
    String URL_FOTO = "http://192.168.56.1/android_register_login/uploads/vivero.png";


    public Perfil() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_perfil, container,false);

        final TextView nombreUsuario = view.findViewById(R.id.datoUsuarioVivero);
        final TextView correo = view.findViewById(R.id.datoCorreoVivero);
        final TextView telefono = view.findViewById(R.id.datoTelefonoVivero);
        fotoVivero = view.findViewById(R.id.fotoVivero);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Preferences", 0);
        String username = sharedPreferences.getString("username","");
        String email = sharedPreferences.getString("email", "");
        String phone = sharedPreferences.getString("phone", "");

        nombreUsuario.setText(username);
        correo.setText(email);
        telefono.setText(phone);
        Picasso.get().load(URL_FOTO).into(fotoVivero);

        // Inflate the layout for this fragment
        return view;
    }

}
