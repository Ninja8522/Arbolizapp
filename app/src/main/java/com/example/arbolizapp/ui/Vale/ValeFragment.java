package com.example.arbolizapp.ui.Vale;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arbolizapp.R;

public class ValeFragment extends Fragment {

    private ValeVieweModel valeVieweModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        valeVieweModel =
                ViewModelProviders.of(this).get(ValeVieweModel.class);
        View root = inflater.inflate(R.layout.fragment_vale, container, false);
        final TextView textView = root.findViewById(R.id.text_vale);
        valeVieweModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
