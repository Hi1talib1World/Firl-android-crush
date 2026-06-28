package com.Denzo.firl.Model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

class MyHomeViewModel extends ViewModel {

    MutableLiveData<List<MatchPerson>> mLiveDataQrList;
    private MyQrListRepository repository;

    public MyHomeViewModel(MyQrListRepository repository){
        this.repository = repository;
        mLiveDataQrList = new MutableLiveData();

    }

    public void getDataQrListVM(){
        repository.getData(mLiveDataQrList);
    }
}
