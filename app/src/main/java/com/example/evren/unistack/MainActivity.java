package com.example.evren.unistack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.evren.unistack.chat.Chat;
import com.example.evren.unistack.register.Login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by EVREN on 21.11.2017.
 */

public class MainActivity extends AppCompatActivity {
    String token,id,name,surname,uniId,unitId,facId,depId,sinif,kullaniciAd,chatRoom;
    Bundle bundle;
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    private String url = "https://api.unistackapp.com/user/outoLogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString("token", "N");

        if (token.equals("N")){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else {
            loginCheck();

        }
    }

    private void loginCheck(){
        Request request = new Request.Builder()
                .url(url)
                .header("token","bearar "+token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                e.printStackTrace();
                finish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonString = response.body().string();
                Log.i("fsdf0",jsonString);
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    id = jsonObject.getString("id");
                    uniId = jsonObject.getString("uniID");
                    unitId = jsonObject.getString("unitID");
                    facId = jsonObject.getString("facID");
                    depId = jsonObject.getString("depID");
                    sinif = jsonObject.getString("sinif");
                    name = jsonObject.getString("name");
                    surname = jsonObject.getString("surname");
                    chatRoom=uniId+unitId+facId+depId;
                    kullaniciAd=name+" "+surname;
                    Log.i("ddasd",id);
                    if (id=="" || uniId==""){
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        bundle = new Bundle();
                        bundle.putString("kullaniciAd",kullaniciAd);
                        bundle.putString("chatRoom",chatRoom);
                        bundle.putString("sinif",sinif);
                        Intent intent = new Intent(getApplicationContext(), Chat.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    e.printStackTrace();
                    finish();
                }
            }
        });
    }
}
