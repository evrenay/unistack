package com.example.evren.unistack.register;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.evren.unistack.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by EVREN on 18.8.2017.
 */

public class ForgotPassword extends DialogFragment {

    Button send;
    EditText edt_email;
    String email;
    private String url = "https://api.unistackapp.com/user/forgotPassword";
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forgot_password,container,false);
        edt_email=v.findViewById(R.id.password_email);
        send=v.findViewById(R.id.btn_forget_password);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = edt_email.getText().toString();
                if(checkMail()==true){
                    sendChangePasswordMail();
                }



            }
        });
        return v;
    }

    private boolean checkMail(){
        if (edt_email.getText().toString().equals("")) {
            edt_email.setError("Email boş geçilemez.");
            return false;
        }
        else if(!(email.endsWith(".edu.tr"))){
            edt_email.setError("Üniversite maili giriniz.");
            return false; }
        else if(isEmailValid(edt_email.getText().toString())== false){
            edt_email.setError("Email formatı geçersiz!");
            return false;
        }
        return true;
    }
    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private void sendChangePasswordMail(){
        RequestBody formBody = new FormBody.Builder()
                .add("email",email)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String body = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                if (response.isSuccessful()){
                Log.i("dasd",body);
                    if (body.contains("error")){
                        Toast.makeText(getActivity().getApplicationContext(),body,Toast.LENGTH_SHORT).show();
                }
                else{Toast.makeText(getActivity().getApplicationContext(),"Mail gönderme başarılı",Toast.LENGTH_SHORT).show();
                        dismiss();}
            }
            else {
                    Toast.makeText(getActivity().getApplicationContext(),"HATA OLUŞTU...",Toast.LENGTH_SHORT).show();
                }}});
        }
        });

}}
