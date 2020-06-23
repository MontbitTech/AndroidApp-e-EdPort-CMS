package com.application.schooltime.SchoolInformation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.schooltime.R;
import com.application.schooltime.SchoolActivity;
import com.application.schooltime.Utilities.Constants;
import com.application.schooltime.Utilities.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


public class SchoolInformationActivity extends AppCompatActivity {
//********** THIS ACTIVITY WILL BE FIRED WHEN THE CHILD SELECTS HIS SCHOOL FOR THE FIRST TIME, OTHER THAN THAT IT WONT BE FIRED.....*********//
    PrefManager prefManager;
    Spinner schoolSpinner;
    int schoolId;
    String schoolName;
    Button btnSubmit;
   List<String> schoolNames;
    String []schoolUrls;
    ArrayAdapter<String> schoolAdapter;
    public static final String TAG="response";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new PrefManager(this);
//*********************************ONE TIME CHECK ////////////////////////////
//        if(prefManager.getSchool()!=null){
//            launchSchoolAcitivty();
//            finish();
//        }

//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }






        setContentView(R.layout.activity_school_information);

        schoolSpinner= findViewById(R.id.schools_spinner);


        //**************************INITIATING API REQUEST USING VOLLEY//
        RequestQueue queue= Volley.newRequestQueue(this);
        String url = "https://raw.githubusercontent.com/MontbitTech/e-EdPort-cms-app/master/cms_list.json";


        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            schoolNames= new ArrayList<>();
                            schoolUrls = new String[response.length()];
                            for(int i=0;i<response.length();i++){

                                JSONObject school = response.getJSONObject(i);

                                schoolNames.add(school.getString("schoolName"));
                                schoolUrls[i]= school.getString("schoolUrl");

                            }
                            schoolAdapter=  new ArrayAdapter<String>(getApplicationContext(),R.layout.my_simple_spinner_dropdown, schoolNames);
                            schoolAdapter.setDropDownViewResource(R.layout.my_simple_spinner_item);
                            schoolSpinner.setAdapter(schoolAdapter);

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        queue.add(jsonArrayRequest);










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

//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(schoolId==0){
//                    // do nothing
//                    // #todo
//                }
//
//                else if(schoolId==1){
//                    prefManager.setSchoolFirstTime(schoolName);
//                    prefManager.setSchoolUrl(Constants.DEMO_SCHOOL_URL);
//                    startActivity(new Intent(SchoolInformationActivity.this,SchoolActivity.class));
//                    finish();
//                }
//                else{
//                    prefManager.setSchoolFirstTime(schoolName);
//                    prefManager.setSchoolUrl(Constants.MAIN_SCHOOL_URL);
//                    startActivity(new Intent(SchoolInformationActivity.this,SchoolActivity.class));
//                    finish();
//                }
//
//
//            }
//        });

    }

    private void launchSchoolAcitivty() {
        startActivity(new Intent(SchoolInformationActivity.this, SchoolActivity.class));
    }
}
