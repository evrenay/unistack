package com.example.evren.unistack.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.evren.unistack.R;
import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by EVREN on 22.10.2017.
 */

public class LoginActivity extends Activity {
    Button sinif1, sinif2, sinif3, sinif4;

    String mUsername,mRoomname, sinif, newRoomName;
    //Socket sınıfını kullanarak, sunucudaki chat yazılımı ile bağlantı kuruyoruz
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Bundle extras = this.getIntent().getExtras();
        mUsername= extras.getString("mUsername");
        mRoomname = extras.getString("chatRoom");
        sinif = extras.getString("sinif");
        Log.d("fffff",sinif);
        sinif1 = (Button) findViewById(R.id.sinif1);
        sinif2 = (Button) findViewById(R.id.sinif2);
        sinif3 = (Button) findViewById(R.id.sinif3);
        sinif4 = (Button) findViewById(R.id.sinif4);

        if(sinif.equals("1")){
                    newRoomName=mRoomname+"1";
                    Log.i("testrrrr",newRoomName);
                    attemptLogin();
                    attemptLogin2();
                    Intent intent = new Intent();
                    intent.putExtra("username", mUsername);
                    intent.putExtra("room", newRoomName);
                    setResult(RESULT_OK, intent);
                    finish();

        }
        else if(sinif=="2"){

                    newRoomName=mRoomname+"2";
                    Log.i("testrrrr",newRoomName);
                    attemptLogin();
                    attemptLogin2();
                    Intent intent = new Intent();
                    intent.putExtra("username", mUsername);
                    intent.putExtra("room", newRoomName);
                    setResult(RESULT_OK, intent);
        }
        else if(sinif=="3"){

                    newRoomName=mRoomname+"3";
                    Log.i("testrrrr",newRoomName);
                    attemptLogin();
                    attemptLogin2();
                    Intent intent = new Intent();
                    intent.putExtra("username", mUsername);
                    intent.putExtra("room", newRoomName);
                    setResult(RESULT_OK, intent);
        }
        else if(sinif=="4"){
                    newRoomName=mRoomname+"4";
                    Log.i("testrrrr",newRoomName);
                    attemptLogin();
                    attemptLogin2();
                    Intent intent = new Intent();
                    intent.putExtra("username", mUsername);
                    intent.putExtra("room", newRoomName);
                    setResult(RESULT_OK, intent);
        }
        else {
            sinif1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newRoomName=mRoomname+"1";
                    Log.i("testrrrr",newRoomName);
                    attemptLogin();
                    attemptLogin2();
                    Intent intent = new Intent();
                    intent.putExtra("username", mUsername);
                    intent.putExtra("room", newRoomName);
                    setResult(RESULT_OK, intent);
                    finish();

                }
            });
            sinif2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newRoomName=mRoomname+"2";
                    Log.i("testrrrr",newRoomName);
                    attemptLogin();
                    attemptLogin2();
                    Intent intent = new Intent();
                    intent.putExtra("username", mUsername);
                    intent.putExtra("room", newRoomName);
                    setResult(RESULT_OK, intent);
                    finish();

                }
            });
            sinif3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newRoomName=mRoomname+"3";
                    Log.i("testrrrr",newRoomName);
                    attemptLogin();
                    attemptLogin2();
                    Intent intent = new Intent();
                    intent.putExtra("username", mUsername);
                    intent.putExtra("room", newRoomName);
                    setResult(RESULT_OK, intent);
                    finish();

                }
            });
            sinif4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newRoomName=mRoomname+"4";
                    Log.i("testrrrr",newRoomName);
                    attemptLogin();
                    attemptLogin2();
                    Intent intent = new Intent();
                    intent.putExtra("username", mUsername);
                    intent.putExtra("room", newRoomName);
                    setResult(RESULT_OK, intent);
                    finish();

                }
            });
        }
    }

    private void attemptLogin() {
        // Kulanıcı adı kontrol ediliyor
        if (!mSocket.connected()){
            Toast.makeText(getApplicationContext(),"Bağlantı Hatası1",Toast.LENGTH_SHORT).show();
            return;}

        String room = newRoomName;
        Log.i("dfdsfsdf", room);
        //Kullanıcı adı, socket'e ekleniyor
        mSocket.emit("switchRoom", room);
    }
    private void attemptLogin2() {
        String username;

        if (!mSocket.connected()){
            Toast.makeText(getApplicationContext(),"Bağlantı Hatası2",Toast.LENGTH_SHORT).show();
            return;}
        // Kulanıcı adı kontrol ediliyor
        username = mUsername;

        //Kullanıcı adı, socket'e ekleniyor
        mSocket.emit("user", username);
    }


}

