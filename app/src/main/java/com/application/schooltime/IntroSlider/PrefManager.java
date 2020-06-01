package com.application.schooltime.IntroSlider;

import android.content.Context;
import android.content.SharedPreferences;

import com.application.schooltime.Utilities.Constants;

public class PrefManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public static final String IS_FIRST_LAUNCH= "isFirstLaunch";

    PrefManager(Context context){
        this.context= context;
        sharedPreferences= context.getSharedPreferences(Constants.FILE_NAME,Constants.PRIVATE_MODE);

        editor= sharedPreferences.edit();


    }

    public void setFirstTimeLaunch(boolean isFirstTime){

        editor.putBoolean(IS_FIRST_LAUNCH,isFirstTime);
        editor.commit();

    }

    public boolean isFirstTimeLaunch(){
        return sharedPreferences.getBoolean(IS_FIRST_LAUNCH,true);

    }

}
