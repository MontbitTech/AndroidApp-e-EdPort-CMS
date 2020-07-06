package com.application.schooltime.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.application.schooltime.Utilities.Constants;

public class PrefManager {

    //*************************THIS CLASS IS USED FOR STORING SHARED PREFERENCES **********************//

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public PrefManager(Context context){

        sharedPreferences= context.getSharedPreferences(Constants.FILE_NAME,Constants.PRIVATE_MODE);

        editor= sharedPreferences.edit();
    }
    public void setFirstTimeLaunch(boolean isFirstTime){

        editor.putBoolean(Constants.IS_FIRST_LAUNCH,isFirstTime);
        editor.commit();

    }

    public boolean isFirstTimeLaunch(){
        return sharedPreferences.getBoolean(Constants.IS_FIRST_LAUNCH,true);

    }



    public void setSchoolUrl(String url){
        editor.putString(Constants.SCHOOL_URL,url);
        editor.commit();
    }

    public String getSchoolUrl(){
        return sharedPreferences.getString(Constants.SCHOOL_URL,null);
    }

}
