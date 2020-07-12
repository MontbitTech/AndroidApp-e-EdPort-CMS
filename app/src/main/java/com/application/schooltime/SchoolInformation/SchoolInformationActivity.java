package com.application.schooltime.SchoolInformation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import android.app.ProgressDialog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.application.schooltime.R;
import com.application.schooltime.SchoolActivity;
import com.application.schooltime.Utilities.Constants;
import com.application.schooltime.Utilities.PrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SchoolInformationActivity extends AppCompatActivity {
    //********** THIS ACTIVITY WILL BE FIRED WHEN THE CHILD SELECTS HIS SCHOOL FOR THE FIRST TIME, OTHER THAN THAT IT WONT BE FIRED.....*********//
    PrefManager prefManager;
    Spinner schoolSpinner;
    int schoolId;
    Button btnSubmit;

    List<String> schoolNames;
    String[] schoolUrls;
    ArrayAdapter<String> schoolAdapter;
    //public static final String TAG = "response";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        prefManager = new PrefManager(this);
//*********************************ONE TIME CHECK
        if (prefManager.getSchoolUrl() != null) {
            launchSchoolActivity();
            finish();

        }


//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }

        // Set the activity launch screen here**********//
        setContentView(R.layout.activity_school_information);

        //#todo I need to correct the functionality of the progress bar also

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while we fetch the schools information");
        progressDialog.setTitle("Fetching Schools Data");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.show();

        schoolSpinner = findViewById(R.id.schools_spinner);


        //**************************INITIATING API REQUEST USING VOLLEY//
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.API_URL;


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            schoolNames = new ArrayList<>();
                            schoolUrls = new String[response.length()];
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject school = response.getJSONObject(i);

                                schoolNames.add(school.getString("schoolName"));
                                schoolUrls[i] = school.getString("schoolUrl");

                                int progress = 100 / (response.length()) * i;
                                progressDialog.incrementProgressBy(progress);

                            }

                            //***** setting up the adapter ************//

                            schoolAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.my_simple_spinner_dropdown, schoolNames);
                            schoolAdapter.setDropDownViewResource(R.layout.my_simple_spinner_item);
                            schoolSpinner.setAdapter(schoolAdapter);
                            progressDialog.dismiss();
                        } catch (Exception e) {
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


        btnSubmit = findViewById(R.id.btnSubmit);


        schoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                schoolId = position;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //indifaodianodidoaisjdoiasjodis
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = schoolUrls[schoolId];

                prefManager.setSchoolUrl(url);

                Snackbar.make(v, url, Snackbar.LENGTH_LONG).show();
                launchSchoolActivity();
                finish();
            }
        });

    }

    private void launchSchoolActivity() {

        startActivity(new Intent(SchoolInformationActivity.this, SchoolActivity.class));
    }
}
