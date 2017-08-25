package com.example.evren.unistack;


import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageView loginBackground;
    TextView forgotPassword,register1,register2;
    Button login;
    EditText edt_password,edt_email;
    String Password, Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginBackground = (ImageView)findViewById(R.id.login_background);
        forgotPassword= (TextView) findViewById(R.id.txt_forgotpassword);
        register1= (TextView) findViewById(R.id.txt_register1);
        register2= (TextView) findViewById(R.id.txt_register2);
        login= (Button) findViewById(R.id.btn_login);
        edt_password= (EditText) findViewById(R.id.edt_password);
        edt_email = (EditText) findViewById(R.id.edt_email);

        // Receive data
        getData();
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
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });
        register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });




    }


    private void getData(){
        Password = edt_password.getText().toString();
        Email = edt_email.getText().toString();


    }

}
