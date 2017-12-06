package com.example.evren.unistack.register;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.evren.unistack.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by EVREN on 5.10.2017.
 */

public class Activation extends DialogFragment {
    String Email;
    Button SendMail;
    LinearLayout Snackbarr;
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activate,container,false);
        SendMail = v.findViewById(R.id.sendactivate);
        Snackbarr = v.findViewById(R.id.snack);
        Email = getArguments().getString("sendmailagain");
        SendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                againMailSend();
            }
        });

        return v;
    }

    private void againMailSend(){
        Log.i("FSDFSD",Email);
        Request request = new Request.Builder()
                .url("https://api.unistackapp.com/security/accountVerifyAgain?email="+Email)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    String code = jsonObject.getString("code");
                    Log.i("fdsfsd",code);
                    if(code.equals("200")){
                        final Snackbar snackbar = Snackbar.make(Snackbarr, "Mail hesabınıza aktif linki yollandı.", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("Tamam", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.dismiss();
                                // Geri Al Butonu Tıklandığında yapılacak işlemler
                            }
                        });
                        snackbar.show();
                    }
                    else if(code.equals("400")) {
                        final Snackbar snackbar = Snackbar.make(Snackbarr, "5 dakika sonra tekrar deneyiniz.", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("Tamam", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.dismiss();
                                // Geri Al Butonu Tıklandığında yapılacak işlemler
                            }
                        });
                        snackbar.show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
