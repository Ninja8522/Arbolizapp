package com.example.arbolizapp.Controlador;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Este es el código correspondiente al controlador de los tabs del vivero, es el encargado de mover de un lado
 * a otro al usuario que este dentro de la interfaz de vivero.
 */

public class PagerController extends FragmentPagerAdapter {
    int numoftabs;

    public PagerController(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numoftabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Perfil();
            case 1:
                return new PorEntregar();
            case 2:
                return new Entregado();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numoftabs;
    }
}
