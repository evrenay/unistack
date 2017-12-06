package com.example.evren.unistack.register;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.evren.unistack.R;

import java.io.IOException;

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

public class SendCodeAgain extends DialogFragment {
    String Email;
    Button send;
    private final OkHttpClient client = new OkHttpClient();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sendcodeagain,container,false);
        Email = getArguments().getString("KeyEmail");
        send = v.findViewById(R.id.sendCode);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        setCancelable(false);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Email.contains("ogr")){
                    registerOgrenci();
                }
                else{
                    registerHoca();

                }
                sendData();

            }
        });
        return v;
    }


    public void registerOgrenci() {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        okhttp3.RequestBody body = RequestBody.create(JSON, "");
        Request request = new Request.Builder()
                .url("https://hidden-anchorage-49895.herokuapp.com/api/v1/users/"+Email+"/activation")
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

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if (response.isSuccessful()){
                            Toast.makeText(getActivity().getApplicationContext(),"Kod Başarı ile Gönderildi.", Toast.LENGTH_SHORT).show();
                        dismiss();
                        }
                        else {
                            Toast.makeText(getActivity().getApplicationContext(),"Bir Hata Oluştu.İntenet Bağlantınızı Kontrol Ediniz.!",Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }
                });


            }
        });


    }
    public void registerHoca() {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        okhttp3.RequestBody body = RequestBody.create(JSON, "");
        Request request = new Request.Builder()
                .url("https://hidden-anchorage-49895.herokuapp.com/api/v1/plebs/"+Email+"/activation")
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

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if (response.isSuccessful()){
                            Toast.makeText(getActivity().getApplicationContext(),"Mailinize Kod Gönderildi..!", Toast.LENGTH_SHORT).show();
                            dismiss();}
                        else {
                            Toast.makeText(getActivity().getApplicationContext(),"Bir Hata Oluştu.İntenet Bağlantınızı Kontrol Ediniz.!",Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }
                });


            }
        });


    }


    public void sendData(){
        timeListener listener = (timeListener) getActivity();
        listener.timeValue(2);

    }
}
