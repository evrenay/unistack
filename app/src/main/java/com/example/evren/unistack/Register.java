package com.example.evren.unistack;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by EVREN on 20.8.2017.
 */

public class Register extends Activity {
    //verileri al
    //parola email kontrolü yap
    //emaili parametre olarak postla ve doğrulama sayfasına geç
    //verileri sonraki activiye taşı
    //active olduğu zaman tüm verileri postla..
    Button Register;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;
    Intent intent;
    EditText Name, Surname, Email, Password, rePassword;
    String edt_Name, edt_Surname, edt_Email, edt_Password, edt_rePassword,message;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Name = findViewById(R.id.register_name);
        Surname = findViewById(R.id.register_surname);
        Email = findViewById(R.id.register_email);
        Password = findViewById(R.id.register_password);
        rePassword = findViewById(R.id.register_repassword);
        Register = findViewById(R.id.register);
        progressBarHolder = findViewById(R.id.progressBarHolder);

        setErrorMessage();
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                if (onClickRegister()==true && Email.getText().toString().contains("ogr") ){
                    inAnimation = new AlphaAnimation(0f, 1f);
                    inAnimation.setDuration(200);
                    progressBarHolder.setAnimation(inAnimation);
                    progressBarHolder.setVisibility(View.VISIBLE);
                    registerOgrenci();
                intent = new Intent(getApplicationContext(),Activate.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("keyName",edt_Name);
                    bundle.putString("keySurname",edt_Surname);
                    bundle.putString("keyEmail",edt_Email);
                    bundle.putString("keyPassword",edt_Password);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if (onClickRegister()==true){
                    inAnimation = new AlphaAnimation(0f, 1f);
                    inAnimation.setDuration(200);
                    progressBarHolder.setAnimation(inAnimation);
                    progressBarHolder.setVisibility(View.VISIBLE);
                    registerHoca();
                    intent = new Intent(getApplicationContext(),Activate.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("keyName",edt_Name);
                    bundle.putString("keySurname",edt_Surname);
                    bundle.putString("keyEmail",edt_Email);
                    bundle.putString("keyPassword",edt_Password);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Hata",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void setErrorMessage() {
        Name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    Name.setError("İsim Boş Geçilemez");
                }
            }
        });
        Surname.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    Surname.setError("Soyisim Boş Geçilemez!");
                }
            }
        });
        Email.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    Email.setError("Email Boş Geçilemez!");
                }
            }
        });
        Password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    Password.setError("Password Boş Geçilemez");
                }
            }
        });


    }

    private void getData() {
        edt_Name = Name.getText().toString();
        edt_Surname = Surname.getText().toString();
        edt_Email = Email.getText().toString();
        edt_Password = Password.getText().toString();
        edt_rePassword = rePassword.getText().toString();

    }

    private boolean checkPassword(EditText password, EditText rePassword) {
        if (password.getText().toString().equals(rePassword.getText().toString())) {
            return false;

        }
        return true;


    }

    private boolean isEmpty(EditText ad, EditText soyad, EditText email, EditText password, EditText rePassword) {
        if (ad.getText().toString().equals("")) {
            return false;
        } else if (soyad.getText().toString().equals("")) {
            return false;
        } else if (email.getText().toString().equals("")) {
            return false;
        } else if (password.getText().toString().equals("")) {
            return false;
        } else if (rePassword.getText().toString().equals("")) {
            return false;
        }

        return true;
    }


    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-zA-Z]).{8,12}";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

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

    private boolean onClickRegister() {

        if (isEmpty(Name, Surname, Email, Password, rePassword) == false) {
            Toast.makeText(getApplicationContext(), "Boş Alan Geçilemez", Toast.LENGTH_SHORT).show();
                return false;
        } else if (checkPassword(Password, rePassword) == true) {
            rePassword.setError("Uyumsuz Password!");
                return false;
        } else if (isEmailValid(Email.getText().toString()) == false) {
            Email.setError("Email formatı geçersiz!");
            return false;
        } else if (!isValidPassword(Password.getText().toString())) {
            Password.setError("Password 8 haneden fazla ve büyük küçük harf ile rakam kullanılmalıdır.");
            return false;
        }
        else if(isEmailUniversity(Email.getText().toString()) == false){

            Email.setError("Email alanına sadece üniversite mail adresi girilebilir. ");
            return false;
        }


        return true;
    }


    public void registerOgrenci() {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        okhttp3.RequestBody body = RequestBody.create(JSON, "");
        Request request = new Request.Builder()
                .url("https://hidden-anchorage-49895.herokuapp.com/api/v1/users/" + edt_Email + "/activation")
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
                        Toast.makeText(getApplicationContext(),"Mailinize Kod Gönderildi..", Toast.LENGTH_SHORT).show();
                            outAnimation = new AlphaAnimation(1f, 0f);
                            outAnimation.setDuration(200);
                            progressBarHolder.setAnimation(outAnimation);
                            progressBarHolder.setVisibility(View.GONE);}
                        else {
                            Toast.makeText(getApplicationContext(),"Bir Hata Oluştu.İntenet Bağlantınızı Kontrol Ediniz.!",Toast.LENGTH_SHORT).show();
                            outAnimation = new AlphaAnimation(1f, 0f);
                            outAnimation.setDuration(200);
                            progressBarHolder.setAnimation(outAnimation);
                            progressBarHolder.setVisibility(View.GONE);
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
                .url("https://hidden-anchorage-49895.herokuapp.com/api/v1/plebs/"+edt_Email+"/activation")
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
                            Toast.makeText(getApplicationContext(),"Mailinize Kod Gönderildi.!", Toast.LENGTH_SHORT).show();}
                        else {
                            Toast.makeText(getApplicationContext(),"Bir Hata Oluştu.İntenet Bağlantınızı Kontrol Ediniz.!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });


    }


}