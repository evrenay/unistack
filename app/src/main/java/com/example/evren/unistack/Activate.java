package com.example.evren.unistack;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by EVREN on 22.8.2017.
 */

public class Activate extends Activity implements timeListener {
    TextView txtTimer,txtGetCode;
    private static final String FORMAT = "%02d:%02d";
    String Name, Surname, Email, Password, Code;
    Bundle bundle1,bundle2;
    private SendCodeAgain dialogFragment = new SendCodeAgain ();
    private final OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activate);
        txtTimer = findViewById(R.id.timer);
        txtGetCode = findViewById(R.id.getCode);
        Code = txtGetCode.getText().toString();
        geriSayici();
        getBundle();
    }

    private void getBundle(){
        bundle1 = getIntent().getExtras();
        Name = bundle1.getString("keyName");
        Surname = bundle1.getString("keySurname");
        Email = bundle1.getString("keyEmail");
        Password = bundle1.getString("Password");



    }

    private void setBundle(){
            bundle2 = new Bundle();
            bundle2.putString("KeyEmail",Email);
            dialogFragment.setArguments(bundle2);
    }

    private void geriSayici(){
        new CountDownTimer(30000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                txtTimer.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                txtTimer.setText("Süre Bitti..!");
                setBundle();
                FragmentManager fm = getFragmentManager();
                dialogFragment.show(fm,"hello");
            }
        }.start();


    }

    public void activateStudent() {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        okhttp3.RequestBody body = RequestBody.create(JSON, "");
        Request request = new Request.Builder()
                .url("https://hidden-anchorage-49895.herokuapp.com/api/v1/users/"+Code+"/activation")
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    public void run() {
                        if (response.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Kod Başarı ile Gönderildi.", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Bir Hata Oluştu.İntenet Bağlantınızı Kontrol Ediniz.!",Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        });


    }



    @Override
    public void timeValue(int basla) {
        if(basla==2){
            geriSayici();

        }
        else {
            Toast.makeText(getApplicationContext(),"Bir Hata Oluştu!",Toast.LENGTH_SHORT).show();

        }


    }
}
