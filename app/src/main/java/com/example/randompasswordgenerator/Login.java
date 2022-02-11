package com.example.randompasswordgenerator;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Login extends AppCompatActivity{

    private EditText username;
    private EditText password;
    private Button btnLogin;
    private TextView txtRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        InterfaceImplementation inter = new InterfaceImplementation();

        //----------------------------- Button Login ------------------------------
        username = findViewById(R.id.textUsername);
        password = findViewById(R.id.textPassword);
        btnLogin = findViewById(R.id.btnRegister);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory(), "./RandomPasswordGenerator/DataLogin.txt");

                //Read text from file
                StringBuilder text = new StringBuilder();

                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }
                    br.close();
                }
                catch (IOException e) {
                    //You'll need to add proper error handling here
                }

                DataLogin login = new Gson().fromJson(text.toString(), DataLogin.class);
                if(username.getText().toString().equals(login.getUsername()) && password.getText().toString().equals(login.getPassword())){
                    Toast.makeText(getApplicationContext(), "Logged!", Toast.LENGTH_SHORT).show();
                    inter.redirectActivity(Login.this, MainActivity.class); //chiamata funzione cambio pagina
                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        //----------------------------- Text Register ------------------------------
        txtRegister = findViewById(R.id.txtRegister);
        txtRegister.setPaintFlags(txtRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //per mettere sottolineatura sulla scritta
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inter.redirectActivity(Login.this, Register.class); //chiamata alla funzione campio pagina
            }
        });
    }



}