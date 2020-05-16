package com.example.arbolizapp.ui.Vale;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ValeVieweModel extends ViewModel {


    private MutableLiveData<String> mText;

    public ValeVieweModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment de Vale");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
