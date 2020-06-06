package com.example.arbolizapp.ui.Catalago;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arbolizapp.R;

/**
 * Este es el código correspondiente a la funcionalidad del fragment, de momento esta como se generan por defecto los fragments de un menú,
 * ya que aun no e diseñado la interfaz correspondiente a este fragment.
 */


public class fragment_catalago extends Fragment {

    private CatalagoViewModel catalagoViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        catalagoViewModel =
                ViewModelProviders.of(this).get(CatalagoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_catalago, container, false);
        /*final TextView textView = root.findViewById(R.id.text_catalago);
        catalagoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}
