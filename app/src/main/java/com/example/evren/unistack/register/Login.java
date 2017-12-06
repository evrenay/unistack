package com.example.evren.unistack.register;


import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.evren.unistack.chat.Chat;
import com.example.evren.unistack.R;

import org.json.JSONException;
import org.json.JSONObject;

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

public class Login extends AppCompatActivity {
    ImageView loginBackground;
    TextView forgotPassword,register1,register2;
    Button login;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;
    RelativeLayout snackbarr;
    EditText edt_password,edt_email;
    String Password, Email ,bundleMail;
    FrameLayout progressBarHolder2;
    String token = "";
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    private String url = "https://api.unistackapp.com/user/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loginBackground = (ImageView)findViewById(R.id.login_background);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        forgotPassword= (TextView) findViewById(R.id.txt_forgotpassword);
        register1= (TextView) findViewById(R.id.txt_register1);
        snackbarr = (RelativeLayout) findViewById(R.id.main_snackbar);
        register2= (TextView) findViewById(R.id.txt_register2);
        login= (Button) findViewById(R.id.btn_login);
        edt_password= (EditText) findViewById(R.id.edt_password);
        edt_email = (EditText) findViewById(R.id.edt_email);
        Bundle gelen = getIntent().getExtras();
        if (gelen != null){
            boolean registerStatus = gelen.getBoolean("veri");
            if (registerStatus == true) {
                progressBarHolder2 = findViewById(R.id.progressBarHolder2);
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder2.setAnimation(inAnimation);
                progressBarHolder2.setVisibility(View.VISIBLE);
                final Snackbar snackbar = Snackbar.make(snackbarr, "Hesabınızı aktif etmek için mail adresinizi kontrol ediniz...", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Tamam", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            outAnimation = new AlphaAnimation(1f, 0f);
                            outAnimation.setDuration(200);
                            progressBarHolder2.setAnimation(outAnimation);
                            progressBarHolder2.setVisibility(View.GONE);
                            snackbar.dismiss();

                        }
                    });
                    snackbar.show();


            }
            else {
                Snackbar snackbar = Snackbar.make(snackbarr, "Hoşgeldiniz...", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }
        else {
            Snackbar snackbar = Snackbar.make(snackbarr, "Hoşgeldiniz!!!...", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        login.setOnClickListener(new View.OnClickListener() {
         @Override
            public void onClick(View view) {
        // Receive data
             getData();
             if (loginCheckItem()==true){
                 checkLogin();
                 inAnimation = new AlphaAnimation(0f, 1f);
                 inAnimation.setDuration(200);
                 progressBarHolder.setAnimation(inAnimation);
                 progressBarHolder.setVisibility(View.VISIBLE);
             }
             else {
                 Snackbar snackbar = Snackbar.make(snackbarr, "Bilgilerinizi Kontrol Ediniz!", Snackbar.LENGTH_LONG);
                 snackbar.show();
             }



            }
        });



        //go to forgot password screen
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                ForgotPassword dialogFragment = new ForgotPassword ();
                dialogFragment.show(fm,"hello");


            }
        });

        //go to register screen
        register1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
        register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });




    }


    private void checkLogin(){
        //check login item with api
        RequestBody formBody = new FormBody.Builder()
                .add("email",Email)
                .add("password",Password)
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
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String body = response.body().string();
                        try {
                            final JSONObject jsonObject  = new JSONObject(body);
                            final String code = jsonObject.getString("code");
                            final String id = jsonObject.getString("id");



                        if(code.equals("301") || code.equals("302")){
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {

                                   outAnimation = new AlphaAnimation(1f, 0f);
                                   outAnimation.setDuration(200);
                                   progressBarHolder.setAnimation(outAnimation);
                                   progressBarHolder.setVisibility(View.GONE);
                                   FragmentManager fm = getFragmentManager();
                                   Bundle fragmentBundle = new Bundle();
                                   fragmentBundle.putString("sendmailagain",Email);
                                   Activation fragobj = new Activation();
                                   fragobj.setArguments(fragmentBundle);
                                   fragobj.show(fm,"hello");
                               }
                           });

                        }
                        else if(code.equals("201") || code.equals("203")){
                            try {
                                token = jsonObject.getString("token");
                                Log.i("dasd0",token);
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("token",token);
                                editor.commit();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(Email.contains("ogr")){
                              bundleMail = Email.substring(Email.indexOf("ogr")+4,Email.indexOf(".edu.tr"));

                            }
                            else if(Email.contains("ogrenci")){
                                bundleMail = Email.substring(Email.indexOf("ogrenci")+8,Email.indexOf(".edu.tr"));

                            }
                            else if(!(Email.contains("ogr") || Email.contains("ogrenci"))){
                                bundleMail = Email.substring(Email.indexOf("@")+2,Email.indexOf(".edu.tr"));

                            }
                          runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  Bundle bundle = new Bundle();
                                  bundle.putString("email",bundleMail);
                                  bundle.putString("id",id);
                                  bundle.putString("token",token);
                                  Intent intent = new Intent(getApplicationContext(),RegisterForm.class);
                                  intent.putExtras(bundle);
                                  outAnimation = new AlphaAnimation(1f, 0f);
                                  outAnimation.setDuration(200);
                                  progressBarHolder.setAnimation(outAnimation);
                                  progressBarHolder.setVisibility(View.GONE);
                                  startActivity(intent);
                              }
                          });


                        }
                        else  if(code.equals("202") || code.equals("204")){

                            try {
                                token = jsonObject.getString("token");
                                Log.i("dasd0",token);
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("token",token);
                                editor.commit();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("token",token);
                                    Intent intent = new Intent(getApplicationContext(),Chat.class);
                                    intent.putExtras(bundle);
                                    outAnimation = new AlphaAnimation(1f, 0f);
                                    outAnimation.setDuration(200);
                                    progressBarHolder.setAnimation(outAnimation);
                                    progressBarHolder.setVisibility(View.GONE);
                                    startActivity(intent);
                                }
                            });
                        }
                        else if(code.equals("400")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    outAnimation = new AlphaAnimation(1f, 0f);
                                    outAnimation.setDuration(200);
                                    progressBarHolder.setAnimation(outAnimation);
                                    progressBarHolder.setVisibility(View.GONE);
                                    Snackbar snackbar = Snackbar.make(snackbarr, "Hatalı Kullanıcı Adı yada Şifre", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            outAnimation = new AlphaAnimation(1f, 0f);
                            outAnimation.setDuration(200);
                            progressBarHolder.setAnimation(outAnimation);
                            progressBarHolder.setVisibility(View.GONE);
                            Snackbar snackbar = Snackbar.make(snackbarr, "Bağlantınızı Kontrol Ediniz...", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });


                }
            }
        });
    }
    private  boolean loginCheckItem(){
        if(isEmailUniversity(Email)==false){
            edt_email.setError("Üniversite mail adresinizi giriniz.");
            return false;
        }
        else if(isEmailValid(Email)==false){
            edt_email.setError("Email geçersiz.");
            return false;
        }
        else if(isEmpty(edt_email,edt_password)== false){
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
    public boolean isEmailUniversity(String email){
        if (email.endsWith(".edu.tr")){
            return true; }
        return false;
    }
    private void getData(){
        Password = edt_password.getText().toString();
        Email = edt_email.getText().toString();

    }
    private boolean isEmpty(EditText email, EditText password) {
        if (email.getText().toString().equals("")) {
            email.setError("Email girip tekrar deneyiniz...");
            return false;
        } else if (password.getText().toString().equals("")) {
            email.setError("Password girip tekrar deneyiniz...");
            return false;
        }

        return true;
    }


}
