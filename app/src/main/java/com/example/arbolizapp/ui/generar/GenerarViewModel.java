package com.example.arbolizapp.ui.generar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GenerarViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public GenerarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment de generar solicitud");
    }

    public LiveData<String> getText() {
        return mText;
    }

}
