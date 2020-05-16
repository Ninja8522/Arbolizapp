package com.example.arbolizapp.ui.Catalago;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CatalagoViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public CatalagoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment de catalago");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
