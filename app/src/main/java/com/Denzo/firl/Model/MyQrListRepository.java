package com.Denzo.firl.Model;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

class MyQrListRepository {

    private static final String TAG = "MyQrListRepository";
    private MediatorLiveData<MatchPerson> getInfo = new MediatorLiveData<>();


    MutableLiveData<Boolean> isLoadingGetList = new MutableLiveData<>();

    MutableLiveData<Boolean> isLoadingRepo = new MutableLiveData<>();

    MutableLiveData<List<MatchPerson>> liveData;

    public void getData(MutableLiveData<List<MatchPerson>> liveData) {
        isLoadingGetList.setValue(true);
        try {
            isLoadingGetList.setValue(false);
            List<MatchPerson> myQrItem = MyMatchesPersons.get().getData();
            liveData.postValue( myQrItem);
        }catch (Exception ex){
            isLoadingGetList.setValue(false);
        }


    }



}