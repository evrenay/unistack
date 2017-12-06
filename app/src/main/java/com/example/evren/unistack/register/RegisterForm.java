package com.example.evren.unistack.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import android.widget.AdapterView.OnItemSelectedListener;

import com.example.evren.unistack.chat.Chat;
import com.example.evren.unistack.R;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by EVREN on 28.8.2017.
 */

public class RegisterForm extends Activity implements OnItemSelectedListener {
    Spinner academicUnit,faculty,department,clas;
    String selectedClas,selectedacademicUnit,selectedDepartment,selectedFaculty,uniCode,id,token;
    Button Devam;
    RelativeLayout snackbarLayout;
    int selectedAcademicUnitIndex,selectedFacultyIndex, selectedDepartmentIndex;
    String UnitIndex,FacultyIndex, DepartmentIndex;
    TextView facultyTxt;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;
    private List<String> categories = new ArrayList<>();
    private List<String> unitId = new ArrayList<>();
    private List<String> facultyArray = new ArrayList<>();
    private List<String> facultyArrayId = new ArrayList<>();
    private List<String> departmentArray = new ArrayList<>();
    private List<String> departmentArrayId = new ArrayList<>();
    private List<String> clasArray = new ArrayList<>();



    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_form);
        academicUnit = findViewById(R.id.academicUnit);
        faculty = findViewById(R.id.faculties);
        department = findViewById(R.id.departments);
        clas = findViewById(R.id.clas);
        snackbarLayout = findViewById(R.id.snackbar_layout);
        Devam = findViewById(R.id.devam);
        facultyTxt = findViewById(R.id.selected_unit);
        progressBarHolder = findViewById(R.id.progressBarHolder);
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);
        Bundle uniCodeBundle = getIntent().getExtras();
        uniCode = uniCodeBundle.getString("email");
        id = uniCodeBundle.getString("id");
        token = uniCodeBundle.getString("token");
        setAcademicUnit();
        academicUnit.setOnItemSelectedListener(this);
        faculty.setOnItemSelectedListener(this);
        department.setOnItemSelectedListener(this);
        clas.setOnItemSelectedListener(this);

        Devam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((selectedClas!="" && selectedDepartment!="" && selectedFaculty!="" && uniCode!="" && selectedacademicUnit!="") ){
                    sendUniversityInfo();
                }
                else{
                    Snackbar snackbar = Snackbar.make(snackbarLayout, "Verileri Kontrol Ediniz...", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });


    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        switch(parent.getId()){
            case R.id.academicUnit:
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder.setAnimation(inAnimation);
                progressBarHolder.setVisibility(View.VISIBLE);
                selectedacademicUnit = parent.getItemAtPosition(position).toString();
                selectedAcademicUnitIndex = (int) parent.getItemIdAtPosition((int) id);
                UnitIndex = unitId.get(selectedAcademicUnitIndex);
                facultyTxt.setText(selectedacademicUnit);
                facultyArray.clear();
                setFaculty();
                break;
            case R.id.faculties:
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder.setAnimation(inAnimation);
                progressBarHolder.setVisibility(View.VISIBLE);
                selectedFaculty = parent.getItemAtPosition(position).toString();
                selectedFacultyIndex = (int) parent.getItemIdAtPosition((int) id);
                FacultyIndex =  facultyArrayId.get(selectedFacultyIndex);
                departmentArray.clear();
                setDepartment();
                break;
            case  R.id.departments:
                selectedDepartment = parent.getItemAtPosition(position).toString();
                selectedDepartmentIndex = (int) parent.getItemIdAtPosition((int) id);
                DepartmentIndex = departmentArrayId.get(selectedDepartmentIndex);

                clasArray.clear();
                setClas();
                break;
            case R.id.clas:
                selectedClas = parent.getItemAtPosition(position).toString();
                break;
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    private void setAcademicUnit() {
        RequestBody formBody = new FormBody.Builder()
                .add("uniCode",uniCode)
                .build();
        Request request = new Request.Builder()
                .url("https://api.unistackapp.com/universityInfo/selectUnit")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                String body = response.body().string();
                try {
                    JSONObject json = new JSONObject(body);
                    JSONArray birimArray = json.getJSONArray("message");
                    Log.i("fsdf", String.valueOf(birimArray));
                    for (int i = 0; i < birimArray.length(); i++) {
                        JSONObject object = birimArray.getJSONObject(i);
                        categories.add(object.getString("unitName"));
                        unitId.add(object.getString("unitID"));

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, categories);
                            dataAdapter.setDropDownViewResource(R.layout.spinner_item);
                            academicUnit.setPrompt("Select your favorite Planet!");
                            academicUnit.setAdapter(dataAdapter);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                    Toast.makeText(getApplicationContext(),"Veriler yüklenirken bir hata oluştu.Sayfayı yenileyiniz.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setFaculty() {
      RequestBody formBody = new FormBody.Builder()
              .add("uniCode",uniCode)
              .add("unitID",UnitIndex)
              .build();
        Request request = new Request.Builder()
                .url("https://api.unistackapp.com/universityInfo/selectFaculty")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                String body = response.body().string();
                try {
                    JSONObject json = new JSONObject(body);
                    JSONArray birimArray = json.getJSONArray("message");

                    for (int i = 0; i < birimArray.length(); i++) {
                        JSONObject object = birimArray.getJSONObject(i);
                        facultyArray.add(object.getString("fakulte"));
                        facultyArrayId.add(object.getString("fid"));

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> facultyDataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, facultyArray);
                            facultyDataAdapter.setDropDownViewResource(R.layout.spinner_item);
                            faculty.setPrompt("Select your favorite Planet!");
                            faculty.setAdapter(facultyDataAdapter);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                    Toast.makeText(getApplicationContext(),"Veriler yüklenirken bir hata oluştu.Sayfayı yenileyiniz.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setDepartment(){
        RequestBody formBody = new FormBody.Builder()
                .add("uniCode",uniCode)
                .add("unitID",UnitIndex)
                .add("facID",FacultyIndex)
                .build();
        Request request = new Request.Builder()
                .url("https://api.unistackapp.com/universityInfo/selectDepartment")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                String body = response.body().string();
                try {
                    JSONObject json = new JSONObject(body);
                    JSONArray birimArray = json.getJSONArray("message");
                    Log.i("fsdf", String.valueOf(birimArray));
                    for (int i = 0; i < birimArray.length(); i++) {
                        JSONObject object = birimArray.getJSONObject(i);
                        departmentArray.add(object.getString("depName"));
                        departmentArrayId.add(object.getString("depID"));

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> departmentDataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, departmentArray);
                            departmentDataAdapter.setDropDownViewResource(R.layout.spinner_item);
                            department.setPrompt("Select your favorite Planet!");
                            department.setAdapter(departmentDataAdapter);
                            outAnimation = new AlphaAnimation(1f, 0f);
                            outAnimation.setDuration(200);
                            progressBarHolder.setAnimation(outAnimation);
                            progressBarHolder.setVisibility(View.GONE);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                    Toast.makeText(getApplicationContext(),"Veriler yüklenirken bir hata oluştu.Sayfayı yenileyiniz.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setClas(){
        clasArray.add("1");
        clasArray.add("2");
        clasArray.add("3");
        clasArray.add("4");
        ArrayAdapter<String> ClasDataAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, clasArray);
        ClasDataAdapter.setDropDownViewResource(R.layout.spinner_item);
        clas.setPrompt("Select your favorite Planet!");
        clas.setAdapter(ClasDataAdapter);

    }

    private void sendUniversityInfo(){
        Log.i("dsad",selectedDepartment);
        RequestBody formBody = new FormBody.Builder()
                .add("userID", id)
                .add("uniCode",uniCode)
                .add("unitID",UnitIndex)
                .add("facID",FacultyIndex)
                .add("depID",DepartmentIndex)
                .add("sinif",selectedClas)
                .build();
        Request request = new Request.Builder()
                .url("https://api.unistackapp.com/universityInfo/submit")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String body = response.body().string();
                    try {
                        JSONObject json = new JSONObject(body);
                        String code = json.getString("code");
                        Log.i("fsdfds",code);
                        if (code.equals("200")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Bundle bundleChat = new Bundle();
                                    bundleChat.putString("token",token);
                                    Snackbar snackbar = Snackbar.make(snackbarLayout, "Kayıt Başarılı", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                    Intent intent = new Intent(getApplicationContext(),Chat.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtras(bundleChat);
                                    startActivity(intent);
                                    finish();
                                }
                            });


                        }
                        else if(code.equals("400")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Snackbar snackbar = Snackbar.make(snackbarLayout, "Bağlantınızı Kontrol Ediniz...", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

}
