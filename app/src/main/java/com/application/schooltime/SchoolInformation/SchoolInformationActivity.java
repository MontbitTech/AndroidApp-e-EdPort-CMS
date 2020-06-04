package com.application.schooltime.SchoolInformation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.application.schooltime.R;
import com.application.schooltime.SchoolActivity;
import com.application.schooltime.Utilities.Constants;
import com.application.schooltime.Utilities.PrefManager;


public class SchoolInformationActivity extends AppCompatActivity {
//********** THIS ACTIVITY WILL BE FIRED WHEN THE CHILD SELECTS HIS SCHOOL FOR THE FIRST TIME, OTHER THAN THAT IT WONT BE FIRED.....*********//
    PrefManager prefManager;
    Spinner schoolSpinner;
    int schoolId;
    String schoolName;
    Button btnSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new PrefManager(this);

        if(prefManager.getSchool()!=null){
            launchSchoolAcitivty();
            finish();
        }

//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }



        setContentView(R.layout.activity_school_information);

        schoolSpinner= findViewById(R.id.schools_spinner);
        ArrayAdapter<CharSequence> schoolAdapter= ArrayAdapter.createFromResource(this,R.array.schools_array,R.layout.my_simple_spinner_dropdown);
        schoolAdapter.setDropDownViewResource(R.layout.my_simple_spinner_item);
        schoolSpinner.setAdapter(schoolAdapter);
        btnSubmit=findViewById(R.id.btnSubmit);
        schoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                schoolId=position;
                schoolName=(String) parent.getItemAtPosition(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(schoolId==0){

                }

                else if(schoolId==1){
                    prefManager.setSchoolFirstTime(schoolName);
                    prefManager.setSchoolUrl(Constants.DEMO_SCHOOL_URL);
                    startActivity(new Intent(SchoolInformationActivity.this,SchoolActivity.class));
                    finish();
                }
                else{
                    prefManager.setSchoolFirstTime(schoolName);
                    prefManager.setSchoolUrl(Constants.MAIN_SCHOOL_URL);
                    startActivity(new Intent(SchoolInformationActivity.this,SchoolActivity.class));
                    finish();
                }


            }
        });

    }

    private void launchSchoolAcitivty() {
        startActivity(new Intent(SchoolInformationActivity.this, SchoolActivity.class));
    }
}
