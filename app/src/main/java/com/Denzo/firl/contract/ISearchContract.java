package com.Denzo.firl.contract;


public interface ISettingsContract {

    interface View extends IBaseContract.View{

    }

    interface Presenter extends IBaseContract.Presenter<ISettingsContract.View>{

        void logout();

    }

}
