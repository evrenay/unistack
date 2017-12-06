package com.example.evren.unistack.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.evren.unistack.R;
import com.example.evren.unistack.register.Login;

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
 * Created by EVREN on 6.10.2017.
 */

public class Chat extends AppCompatActivity {
    Bundle gelenBundle,gidenBundle;
    String kullaniciAd,sinif,chatRoom,token="",id,uniId,unitId,facId,depId,name,surname;
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    private String url = "https://api.unistackapp.com/user/outoLogin";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_main);
        gelenBundle = getIntent().getExtras();
        if(!(gelenBundle.containsKey("token"))){

            kullaniciAd = gelenBundle.getString("kullaniciAd");
            sinif = gelenBundle.getString("sinif");
            chatRoom = gelenBundle.getString("chatRoom");
            gidenBundle = new Bundle();
            gidenBundle.putString("kullaniciAd",kullaniciAd);
            gidenBundle.putString("chatRoom",chatRoom);
            gidenBundle.putString("sinif",sinif);
            MainFragment mainFragment=new MainFragment();
            mainFragment.setArguments(gidenBundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.realtabcontent, mainFragment);
            transaction.commit();
        }
        else {

            token=gelenBundle.getString("token");
            tokenDesifre();
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    private void tokenDesifre(){

        Request request = new Request.Builder()
                .url(url)
                .header("token","bearar "+token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(),"Bir Hata Oluştu.Lütfen Tekrar giriş yapınız.",Toast.LENGTH_LONG).show();
                e.printStackTrace();

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
                    Log.i("ddasd",kullaniciAd);
                    if (id=="" || uniId==""){
                        Toast.makeText(getApplicationContext(),"Bir Hata Oluştu.Lütfen Tekrar giriş yapınız.",Toast.LENGTH_LONG).show();
                    }
                    else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gidenBundle = new Bundle();
                                gidenBundle.putString("kullaniciAd", kullaniciAd);
                                gidenBundle.putString("chatRoom", chatRoom);
                                gidenBundle.putString("sinif", sinif);
                                MainFragment mainFragment = new MainFragment();
                                mainFragment.setArguments(gidenBundle);
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.realtabcontent, mainFragment);
                                transaction.commit();
                            }});
                    }



                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"Bir Hata Oluştu.Lütfen Tekrar giriş yapınız.",Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }
        });
    }

}
